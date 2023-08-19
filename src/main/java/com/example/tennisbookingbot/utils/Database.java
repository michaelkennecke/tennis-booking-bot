package com.example.tennisbookingbot.utils;

import com.example.tennisbookingbot.exception.NotFoundException;
import com.example.tennisbookingbot.model.Booking;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class Database {
    private static HashMap<LocalDate, Booking> bookings = new HashMap<>();

    public static List<Booking> getBookings() {
        return bookings.values().stream().toList();
    }

    public static Booking getBooking(LocalDate localDateOfEvent) throws NotFoundException {
        Booking booking = bookings.get(localDateOfEvent);
        if (booking == null) {
            throw new NotFoundException("No booking found for " + localDateOfEvent + "!");
        }
        return booking;
    }

    public static void insertBooking(Booking booking) {
        bookings.put(booking.getLocalDateOfEvent(), booking);
    }

    public static void deleteBooking(LocalDate localDateOfEvent) {
        Booking booking = bookings.remove(localDateOfEvent);
        if (booking == null) {
            throw new NotFoundException("Cannot delete booking! Reason: No booking found for " + localDateOfEvent + "!");
        }
    }
}
