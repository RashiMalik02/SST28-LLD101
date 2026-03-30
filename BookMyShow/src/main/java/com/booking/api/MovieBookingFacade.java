package com.booking.api;

import com.booking.admin.AdminService;
import com.booking.concurrency.SeatLockManager;
import com.booking.enums.PaymentMethod;
import com.booking.enums.SeatStatus;
import com.booking.model.*;
import com.booking.notification.EmailNotificationObserver;
import com.booking.notification.SmsNotificationObserver;
import com.booking.pricing.PricingEngine;
import com.booking.service.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Facade Pattern — single entry point for all client-facing and admin APIs.
 *
 * Hides internal service complexity behind clean, intention-revealing method names.
 * Clients only ever interact with this class.
 *
 * ISP: methods are grouped by concern; clients only call what they need.
 */
public class MovieBookingFacade {

    private static volatile MovieBookingFacade instance;

    private final CityService cityService       = CityService.getInstance();
    private final TheaterService theaterService = TheaterService.getInstance();
    private final MovieService movieService     = MovieService.getInstance();
    private final ShowService showService       = ShowService.getInstance();
    private final BookingService bookingService = BookingService.getInstance();
    private final AdminService adminService     = AdminService.getInstance();
    private final PricingEngine pricingEngine   = PricingEngine.getInstance();

    private MovieBookingFacade() {
        bookingService.registerObserver(new EmailNotificationObserver());
        bookingService.registerObserver(new SmsNotificationObserver());
    }

    public static MovieBookingFacade getInstance() {
        if (instance == null) {
            synchronized (MovieBookingFacade.class) {
                if (instance == null) instance = new MovieBookingFacade();
            }
        }
        return instance;
    }

    // ════════════════════════════════════════════════════════════════════════
    // USER APIs
    // ════════════════════════════════════════════════════════════════════════

    /**
     * API: showMovies(cityId)
     * Returns all movies playing in the city, each with their available shows.
     */
    public List<ApiResponse.MovieInCity> showMovies(String cityId) {
        List<String> movieIds = showService.getMovieIdsInCity(cityId);

        return movieIds.stream().map(movieId -> {
            Movie movie = movieService.getMovie(movieId);
            List<Show> shows = showService.getShowsForMovieInCity(movieId, cityId);

            List<ApiResponse.ShowSummary> showSummaries = shows.stream()
                    .map(show -> toShowSummary(show))
                    .collect(Collectors.toList());

            return new ApiResponse.MovieInCity(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getLanguage(),
                    movie.getGenre(),
                    movie.getDurationMinutes(),
                    showSummaries
            );
        }).collect(Collectors.toList());
    }

    /**
     * API: showTheaters(cityId)
     * Returns all theaters in the city, each with movies and their shows.
     */
    public List<ApiResponse.TheaterWithShows> showTheaters(String cityId) {
        List<Theater> theaters = cityService.getTheatersInCity(cityId);

        return theaters.stream().map(theater -> {
            List<Show> shows = showService.getShowsForTheater(theater.getId());

            // Group shows by movie
            Map<String, List<Show>> showsByMovie = shows.stream()
                    .collect(Collectors.groupingBy(Show::getMovieId));

            List<ApiResponse.MovieWithShows> moviesWithShows = showsByMovie.entrySet().stream()
                    .map(entry -> {
                        Movie movie = movieService.getMovie(entry.getKey());
                        List<ApiResponse.ShowSummary> summaries = entry.getValue().stream()
                                .map(this::toShowSummary)
                                .collect(Collectors.toList());
                        return new ApiResponse.MovieWithShows(movie.getId(), movie.getTitle(), summaries);
                    })
                    .collect(Collectors.toList());

            return new ApiResponse.TheaterWithShows(
                    theater.getId(),
                    theater.getName(),
                    theater.getAddress(),
                    moviesWithShows
            );
        }).collect(Collectors.toList());
    }

    /**
     * API: getSeatMap(showId)
     * Returns the full hall seat map with each seat's current status and price.
     * Called after the user selects a show.
     */
    public ApiResponse.SeatMapResponse getSeatMap(String showId) {
        Show show = showService.getShow(showId);
        Theater theater = theaterService.getTheater(show.getTheaterId());
        Hall hall = theaterService.getHall(show.getHallId());
        Map<String, SeatStatus> statusMap = bookingService.getSeatMap(showId);

        List<ApiResponse.SeatInfo> seatInfos = hall.getSeats().stream().map(seat -> {
            SeatStatus status = statusMap.getOrDefault(seat.getId(), SeatStatus.AVAILABLE);
            double price = pricingEngine.getPrice(theater, show, seat.getType());
            return new ApiResponse.SeatInfo(
                    seat.getId(),
                    seat.getRowLabel(),
                    seat.getSeatNumber(),
                    seat.getType().name(),
                    status,
                    price
            );
        }).collect(Collectors.toList());

        return new ApiResponse.SeatMapResponse(showId, hall.getId(), hall.getName(), seatInfos);
    }

    /**
     * API: bookTickets(userId, showId, seatIds, paymentMethod)
     *
     * Full booking flow in one call:
     *   1. Atomically lock seats (throws if already held by another user)
     *   2. Process payment
     *   3. Confirm booking
     *   4. Notify observers
     *
     * If payment fails, locks are automatically released.
     */
    public ApiResponse.TicketResponse bookTickets(String userId,
                                                   String showId,
                                                   List<String> seatIds,
                                                   PaymentMethod paymentMethod) {
        // Step 1 & 2: initiate (lock seats) then confirm (pay)
        Booking booking = bookingService.initiateBooking(userId, showId, seatIds);
        booking = bookingService.confirmBooking(booking.getId(), paymentMethod);

        // Build response
        Show show = showService.getShow(showId);
        Movie movie = movieService.getMovie(show.getMovieId());
        Theater theater = theaterService.getTheater(show.getTheaterId());
        Hall hall = theaterService.getHall(show.getHallId());

        return new ApiResponse.TicketResponse(
                booking.getId(),
                movie.getTitle(),
                theater.getName(),
                hall.getName(),
                show.getStartTime().toString(),
                booking.getSeatIds(),
                booking.getTotalAmount(),
                booking.getStatus().name(),
                booking.getPaymentMethod().name(),
                booking.getPaymentId()
        );
    }

    /**
     * API: cancelTicket(bookingId, requestingUserId)
     * Cancels a confirmed booking and refunds via the original payment method.
     */
    public ApiResponse.CancellationResponse cancelTicket(String bookingId,
                                                          String requestingUserId,
                                                          boolean isAdmin) {
        Booking booking = bookingService.cancelBooking(bookingId, requestingUserId, isAdmin);

        return new ApiResponse.CancellationResponse(
                booking.getId(),
                booking.getTotalAmount(),
                booking.getPaymentMethod() != null ? booking.getPaymentMethod().name() : "N/A",
                booking.getStatus().name()
        );
    }

    // ════════════════════════════════════════════════════════════════════════
    // ADMIN APIs
    // ════════════════════════════════════════════════════════════════════════

    public City addCity(User admin, City city) {
        return adminService.addCity(admin, city);
    }

    public Theater addTheater(User admin, Theater theater) {
        return adminService.addTheater(admin, theater);
    }

    public Hall addHall(User admin, String theaterId, Hall hall) {
        return adminService.addHall(admin, theaterId, hall);
    }

    public Movie addMovie(User admin, Movie movie) {
        return adminService.addMovie(admin, movie);
    }

    public Show addShow(User admin, Show show) {
        return adminService.addShow(admin, show);
    }

    // ════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ════════════════════════════════════════════════════════════════════════

    private ApiResponse.ShowSummary toShowSummary(Show show) {
        Theater theater = theaterService.getTheater(show.getTheaterId());
        int available = SeatLockManager.getInstance().getAvailableSeatCount(show.getId());
        return new ApiResponse.ShowSummary(
                show.getId(),
                theater.getId(),
                theater.getName(),
                show.getStartTime().toString(),
                available,
                show.getTotalSeats()
        );
    }
}
