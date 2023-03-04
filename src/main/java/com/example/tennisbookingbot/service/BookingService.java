package com.example.tennisbookingbot.service;

import com.example.tennisbookingbot.model.Booking;
import com.example.tennisbookingbot.utils.Database;

import java.time.LocalDate;
import java.util.HashMap;

public abstract class BookingService {

    public abstract void createBooking(Booking booking);

    public HashMap<LocalDate, Booking> getBookings() {
        return Database.getBookings();
    }

    public Booking getBooking(LocalDate localDateOfEvent) {
        return Database.getBooking(localDateOfEvent);
    }

    public void deleteBooking(LocalDate localDateOfEvent) {
        Database.getBooking(localDateOfEvent).getTimerTask().cancel();
        Database.deleteBooking(localDateOfEvent);
    }
}
