package com.example.blauhofbot.Service;

import java.time.LocalDateTime;

public interface BookingService {

    void createBooking(LocalDateTime localDateTimeOfEvent,
                       LocalDateTime localDateTimeOfBookingStart,
                       LocalDateTime localDateTimeOfBookingEnd
    );

    boolean deleteBooking(LocalDateTime localDateTimeOfEvent);
}
