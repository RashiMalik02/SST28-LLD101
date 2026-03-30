package com.booking.admin;

import com.booking.exception.UnauthorizedException;
import com.booking.model.*;
import com.booking.service.*;

/**
 * AdminService enforces that only ADMIN users can mutate the catalog.
 *
 * SRP: purely about admin-gated write operations.
 * DIP: delegates to existing services — no direct data mutation here.
 */
public class AdminService {

    private static volatile AdminService instance;

    private final CityService cityService       = CityService.getInstance();
    private final TheaterService theaterService = TheaterService.getInstance();
    private final MovieService movieService     = MovieService.getInstance();
    private final ShowService showService       = ShowService.getInstance();

    private AdminService() {}

    public static AdminService getInstance() {
        if (instance == null) {
            synchronized (AdminService.class) {
                if (instance == null) instance = new AdminService();
            }
        }
        return instance;
    }

    // ─── Authorization guard ──────────────────────────────────────────────────

    private void requireAdmin(User actor) {
        if (!actor.isAdmin()) {
            throw new UnauthorizedException("Only admins can perform this action.");
        }
    }

    // ─── Theater management ───────────────────────────────────────────────────

    /**
     * Adds a new theater to the system and registers it under its city.
     */
    public Theater addTheater(User admin, Theater theater) {
        requireAdmin(admin);
        theaterService.addTheater(theater);

        // Register with city
        City city = cityService.getCity(theater.getCityId());
        city.addTheater(theater);

        System.out.println("[ADMIN] Theater added: " + theater.getName()
                + " in city " + theater.getCityId());
        return theater;
    }

    /**
     * Adds a new hall to an existing theater.
     */
    public Hall addHall(User admin, String theaterId, Hall hall) {
        requireAdmin(admin);
        theaterService.addHallToTheater(theaterId, hall);
        System.out.println("[ADMIN] Hall added: " + hall.getName() + " to theater: " + theaterId);
        return hall;
    }

    // ─── Movie management ─────────────────────────────────────────────────────

    /**
     * Adds a new movie to the catalog.
     */
    public Movie addMovie(User admin, Movie movie) {
        requireAdmin(admin);
        movieService.addMovie(movie);
        System.out.println("[ADMIN] Movie added: " + movie.getTitle());
        return movie;
    }

    // ─── Show management ─────────────────────────────────────────────────────

    /**
     * Adds a new show (a screening of a movie in a hall at a specific time).
     * Automatically initializes all seats as AVAILABLE in the SeatLockManager.
     */
    public Show addShow(User admin, Show show) {
        requireAdmin(admin);

        // Validate movie and theater exist
        movieService.getMovie(show.getMovieId());       // throws if not found
        theaterService.getTheater(show.getTheaterId()); // throws if not found
        theaterService.getHall(show.getHallId());       // throws if not found

        showService.addShow(show);
        System.out.println("[ADMIN] Show added: " + show.getId()
                + " | Movie: " + show.getMovieId()
                + " | Hall: " + show.getHallId()
                + " | Time: " + show.getStartTime());
        return show;
    }

    // ─── City management ──────────────────────────────────────────────────────

    public City addCity(User admin, City city) {
        requireAdmin(admin);
        cityService.addCity(city);
        System.out.println("[ADMIN] City added: " + city.getName());
        return city;
    }
}
