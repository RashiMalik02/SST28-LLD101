package com.booking.service;

import com.booking.exception.NotFoundException;
import com.booking.model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SRP: manages movie catalog only.
 */
public class MovieService {

    private static volatile MovieService instance;
    private final Map<String, Movie> movies = new ConcurrentHashMap<>();

    private MovieService() {}

    public static MovieService getInstance() {
        if (instance == null) {
            synchronized (MovieService.class) {
                if (instance == null) instance = new MovieService();
            }
        }
        return instance;
    }

    public void addMovie(Movie movie) {
        movies.put(movie.getId(), movie);
    }

    public Movie getMovie(String movieId) {
        Movie m = movies.get(movieId);
        if (m == null) throw new NotFoundException("Movie not found: " + movieId);
        return m;
    }

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies.values());
    }
}
