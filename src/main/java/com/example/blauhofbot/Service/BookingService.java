package com.example.blauhofbot.Service;

import java.time.LocalDateTime;
import java.util.Timer;

public interface BookingService {
    Timer timer = new Timer("BookingTimer"); //todo: make naming dynamic

    void createBooking(LocalDateTime dateOfEvent);

    // todo: deleteBooking
}
