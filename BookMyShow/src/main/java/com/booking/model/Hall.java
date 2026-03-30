package com.booking.model;

import com.booking.enums.SeatType;

import java.util.ArrayList;
import java.util.List;

// Hall belongs to a Theater and has a grid of seats
public class Hall {
    private final String id;
    private final String theaterId;
    private final String name;
    private final List<Seat> seats;

    public Hall(String id, String theaterId, String name) {
        this.id = id;
        this.theaterId = theaterId;
        this.name = name;
        this.seats = new ArrayList<>();
    }

    /**
     * Factory method: builds a hall's seat grid.
     * rowConfigs: each entry is {row label, seat count, SeatType}
     */
    public static Hall create(String id, String theaterId, String name,
                              List<RowConfig> rowConfigs) {
        Hall hall = new Hall(id, theaterId, name);
        for (RowConfig rc : rowConfigs) {
            for (int i = 1; i <= rc.count(); i++) {
                String seatId = rc.rowLabel() + i;
                hall.seats.add(new Seat(seatId, id, rc.rowLabel(), i, rc.type()));
            }
        }
        return hall;
    }

    public List<Seat> getSeats() { return new ArrayList<>(seats); }
    public String getId() { return id; }
    public String getTheaterId() { return theaterId; }
    public String getName() { return name; }

    public record RowConfig(String rowLabel, int count, SeatType type) {}
}
