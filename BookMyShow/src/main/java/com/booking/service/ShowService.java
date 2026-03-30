package com.booking.service;

import com.booking.concurrency.SeatLockManager;
import com.booking.exception.NotFoundException;
import com.booking.model.Hall;
import com.booking.model.Movie;
import com.booking.model.Seat;
import com.booking.model.Show;
import com.booking.model.Theater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * SRP: manages shows and the show→seat-map relationship.
 * Also answers the "shows for a movie in a city" and "shows for a theater" queries.
 */
public class ShowService {

    private static volatile ShowService instance;

    // showId → Show
    private final Map<String, Show> shows = new ConcurrentHashMap<>();

    // movieId → List<Show>
    private final Map<String, List<Show>> showsByMovie = new ConcurrentHashMap<>();

    // theaterId → List<Show>
    private final Map<String, List<Show>> showsByTheater = new ConcurrentHashMap<>();

    private final TheaterService theaterService = TheaterService.getInstance();
    private final SeatLockManager seatLockManager = SeatLockManager.getInstance();

    private ShowService() {}

    public static ShowService getInstance() {
        if (instance == null) {
            synchronized (ShowService.class) {
                if (instance == null) instance = new ShowService();
            }
        }
        return instance;
    }

    /**
     * Adds a show and registers all seats in SeatLockManager as AVAILABLE.
     */
    public void addShow(Show show) {
        shows.put(show.getId(), show);
        showsByMovie.computeIfAbsent(show.getMovieId(), k -> new ArrayList<>()).add(show);
        showsByTheater.computeIfAbsent(show.getTheaterId(), k -> new ArrayList<>()).add(show);

        // Initialize seat statuses in the lock manager
        Hall hall = theaterService.getHall(show.getHallId());
        List<String> seatIds = hall.getSeats().stream()
                .map(Seat::getId)
                .collect(Collectors.toList());
        seatLockManager.initializeShow(show.getId(), seatIds);
    }

    public Show getShow(String showId) {
        Show s = shows.get(showId);
        if (s == null) throw new NotFoundException("Show not found: " + showId);
        return s;
    }

    /** showMovies path: returns all shows of a movie across all theaters in a city. */
    public List<Show> getShowsForMovieInCity(String movieId, String cityId) {
        List<Show> movieShows = showsByMovie.getOrDefault(movieId, List.of());
        return movieShows.stream()
                .filter(show -> {
                    Theater theater = theaterService.getTheater(show.getTheaterId());
                    return theater.getCityId().equals(cityId);
                })
                .collect(Collectors.toList());
    }

    /** showTheaters path: returns all shows for a theater. */
    public List<Show> getShowsForTheater(String theaterId) {
        return new ArrayList<>(showsByTheater.getOrDefault(theaterId, List.of()));
    }

    /** Returns movies playing in a city (distinct), used by showMovies API. */
    public List<String> getMovieIdsInCity(String cityId) {
        return shows.values().stream()
                .filter(show -> {
                    Theater theater = theaterService.getTheater(show.getTheaterId());
                    return theater.getCityId().equals(cityId);
                })
                .map(Show::getMovieId)
                .distinct()
                .collect(Collectors.toList());
    }
}
