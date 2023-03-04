package com.example.blauhofbot.utils;

import com.example.blauhofbot.exception.NotFoundException;
import com.example.blauhofbot.model.Booking;

import java.time.LocalDate;
import java.util.HashMap;

public class Database {
    private static HashMap<LocalDate, Booking> bookings = new HashMap<>();

    public static HashMap<LocalDate, Booking> getBookings() {
        return bookings;
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
