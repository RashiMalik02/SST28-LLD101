package com.booking.api;

import com.booking.enums.SeatStatus;
import com.booking.model.*;

import java.util.List;
import java.util.Map;

/**
 * Data Transfer Objects used as API responses.
 * Keeps API contracts stable even if domain models change (OCP / DIP).
 */
public class ApiResponse {

    // ─── showMovies response ──────────────────────────────────────────────────

    public record MovieInCity(
            String movieId,
            String title,
            String language,
            String genre,
            int durationMinutes,
            List<ShowSummary> shows
    ) {}

    // ─── showTheaters response ────────────────────────────────────────────────

    public record TheaterWithShows(
            String theaterId,
            String theaterName,
            String address,
            List<MovieWithShows> movies
    ) {}

    public record MovieWithShows(
            String movieId,
            String title,
            List<ShowSummary> shows
    ) {}

    // ─── Shared show summary ──────────────────────────────────────────────────

    public record ShowSummary(
            String showId,
            String theaterId,
            String theaterName,
            String startTime,
            int availableSeats,
            int totalSeats
    ) {}

    // ─── Seat map response ────────────────────────────────────────────────────

    public record SeatMapResponse(
            String showId,
            String hallId,
            String hallName,
            List<SeatInfo> seats
    ) {}

    public record SeatInfo(
            String seatId,
            String rowLabel,
            int seatNumber,
            String seatType,   // SILVER / GOLD / PLATINUM
            SeatStatus status,
            double price
    ) {}

    // ─── bookTickets response ─────────────────────────────────────────────────

    public record TicketResponse(
            String bookingId,
            String movieTitle,
            String theaterName,
            String hallName,
            String showTime,
            List<String> seatIds,
            double totalAmount,
            String bookingStatus,
            String paymentMethod,
            String paymentId
    ) {}

    // ─── cancelTicket response ────────────────────────────────────────────────

    public record CancellationResponse(
            String bookingId,
            double refundAmount,
            String refundMethod,
            String status
    ) {}
}
