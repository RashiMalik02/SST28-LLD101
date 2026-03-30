package com.booking;

import com.booking.api.ApiResponse;
import com.booking.api.MovieBookingFacade;
import com.booking.enums.PaymentMethod;
import com.booking.enums.SeatType;
import com.booking.enums.UserRole;
import com.booking.model.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * End-to-end demo of the movie booking system.
 *
 * Flow demonstrated:
 *  1. Admin seeds data (city, theater, hall, movie, show)
 *  2. User browses movies in city → picks a show → views seat map
 *  3. User books tickets (seat lock + payment + confirm)
 *  4. User cancels booking (refund issued)
 *  5. Concurrency demo: two users racing for the same seats
 */
public class Main {

    public static void main(String[] args) {
        MovieBookingFacade facade = MovieBookingFacade.getInstance();

        System.out.println("\n═══════════════ ADMIN: Seeding data ═══════════════");

        User admin = new User("admin-1", "Super Admin", "admin@app.com", "9999999999", UserRole.ADMIN);
        User alice  = new User("user-alice", "Alice", "alice@mail.com", "9111111111", UserRole.USER);
        User bob    = new User("user-bob",   "Bob",   "bob@mail.com",   "9222222222", UserRole.USER);

        // City
        City bangalore = new City("city-blr", "Bangalore");
        facade.addCity(admin, bangalore);

        // Theater with base pricing
        Map<SeatType, Double> basePricing = Map.of(
                SeatType.SILVER,   150.0,
                SeatType.GOLD,     200.0,
                SeatType.PLATINUM, 300.0
        );
        Theater pvr = new Theater("theater-pvr", "PVR Orion", "city-blr", "Rajajinagar, Bangalore", basePricing);
        facade.addTheater(admin, pvr);

        // Hall with seat rows
        Hall hall1 = Hall.create("hall-1", "theater-pvr", "Audi 1", List.of(
                new Hall.RowConfig("A", 10, SeatType.SILVER),
                new Hall.RowConfig("B", 10, SeatType.SILVER),
                new Hall.RowConfig("C", 8,  SeatType.GOLD),
                new Hall.RowConfig("D", 8,  SeatType.GOLD),
                new Hall.RowConfig("E", 6,  SeatType.PLATINUM)
        ));
        facade.addHall(admin, "theater-pvr", hall1);

        // Movie
        Movie movie = new Movie("movie-1", "Interstellar", "English", "Sci-Fi", 169);
        facade.addMovie(admin, movie);

        // Show — Saturday evening (triggers weekend + demand pricing)
        LocalDateTime showTime = LocalDateTime.of(2026, 4, 4, 19, 30); // Saturday
        Show show = new Show("show-1", "movie-1", "hall-1", "theater-pvr", showTime, 42);
        facade.addShow(admin, show);

        System.out.println("\n═══════════════ USER: showMovies(Bangalore) ════════════════");
        List<ApiResponse.MovieInCity> moviesInCity = facade.showMovies("city-blr");
        moviesInCity.forEach(m -> {
            System.out.printf("  Movie: %s [%s] — %d show(s)%n", m.title(), m.language(), m.shows().size());
            m.shows().forEach(s -> System.out.printf(
                    "    Show %s | Theater: %s | Time: %s | Available: %d/%d%n",
                    s.showId(), s.theaterName(), s.startTime(), s.availableSeats(), s.totalSeats()));
        });

        System.out.println("\n═══════════════ USER: showTheaters(Bangalore) ══════════════");
        List<ApiResponse.TheaterWithShows> theatersInCity = facade.showTheaters("city-blr");
        theatersInCity.forEach(t -> {
            System.out.printf("  Theater: %s (%s)%n", t.theaterName(), t.address());
            t.movies().forEach(m -> {
                System.out.printf("    Movie: %s — %d show(s)%n", m.title(), m.shows().size());
            });
        });

        System.out.println("\n═══════════════ USER: getSeatMap(show-1) ═══════════════════");
        ApiResponse.SeatMapResponse seatMap = facade.getSeatMap("show-1");
        System.out.printf("  Hall: %s | Total seats: %d%n", seatMap.hallName(), seatMap.seats().size());

        // Print first 6 seats as a sample
        seatMap.seats().stream().limit(6).forEach(s ->
            System.out.printf("    Seat %-4s | %-9s | %-10s | ₹%.2f%n",
                    s.seatId(), s.seatType(), s.status(), s.price())
        );
        System.out.println("    ... (remaining seats omitted for brevity)");

        System.out.println("\n═══════════════ USER (Alice): bookTickets ══════════════════");
        List<String> aliceSeats = List.of("C1", "C2");
        ApiResponse.TicketResponse aliceTicket = facade.bookTickets(
                alice.getId(), "show-1", aliceSeats, PaymentMethod.UPI);

        System.out.printf("  Booking ID   : %s%n", aliceTicket.bookingId());
        System.out.printf("  Movie        : %s%n", aliceTicket.movieTitle());
        System.out.printf("  Theater      : %s | Hall: %s%n", aliceTicket.theaterName(), aliceTicket.hallName());
        System.out.printf("  Show time    : %s%n", aliceTicket.showTime());
        System.out.printf("  Seats        : %s%n", aliceTicket.seatIds());
        System.out.printf("  Total amount : ₹%.2f%n", aliceTicket.totalAmount());
        System.out.printf("  Status       : %s | Payment: %s%n", aliceTicket.bookingStatus(), aliceTicket.paymentMethod());

        
        System.out.println("\n═══════════════ USER (Bob): bookTickets (same seats) ═══════");
        try {
            facade.bookTickets(bob.getId(), "show-1", aliceSeats, PaymentMethod.CREDIT_CARD);
            System.out.println("  ERROR: Bob should NOT have been able to book Alice's seats!");
        } catch (Exception e) {
            System.out.println("  Correctly rejected: " + e.getMessage());
        }

        System.out.println("\n═══════════════ USER (Bob): bookTickets (different seats) ══");
        List<String> bobSeats = List.of("E1", "E2");
        ApiResponse.TicketResponse bobTicket = facade.bookTickets(
                bob.getId(), "show-1", bobSeats, PaymentMethod.NET_BANKING);
        System.out.printf("  Bob booked   : %s | Amount: ₹%.2f | Status: %s%n",
                bobTicket.seatIds(), bobTicket.totalAmount(), bobTicket.bookingStatus());

        

        System.out.println("\n═══════════════ USER (Alice): cancelTicket ═════════════════");
        ApiResponse.CancellationResponse cancellation = facade.cancelTicket(
                aliceTicket.bookingId(), alice.getId(), false);
        System.out.printf("  Booking %s cancelled%n", cancellation.bookingId());
        System.out.printf("  Refund: ₹%.2f via %s | Status: %s%n",
                cancellation.refundAmount(), cancellation.refundMethod(), cancellation.status());


        System.out.println("\n═══════════════ Seat map after Alice's cancellation ════════");
        ApiResponse.SeatMapResponse updatedMap = facade.getSeatMap("show-1");
        updatedMap.seats().stream()
                .filter(s -> aliceSeats.contains(s.seatId()) || bobSeats.contains(s.seatId()))
                .forEach(s -> System.out.printf("    Seat %-4s | %-9s | %-10s%n",
                        s.seatId(), s.seatType(), s.status()));

        System.out.println("\n═══════════════ USER (Alice): tries to cancel Bob's ticket ═");
        try {
            facade.cancelTicket(bobTicket.bookingId(), alice.getId(), false);
            System.out.println("  ERROR: Alice should NOT be able to cancel Bob's booking!");
        } catch (Exception e) {
            System.out.println("  Correctly rejected: " + e.getMessage());
        }

        System.out.println("\n═══════════════ Demo complete ══════════════════════════════\n");
    }
}
