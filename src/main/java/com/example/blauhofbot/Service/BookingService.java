package com.example.blauhofbot.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface BookingService {

    void createBooking(LocalDate localDateOfEvent,
                       LocalDateTime localDateTimeOfBookingStart,
                       LocalDateTime localDateTimeOfBookingEnd,
                       List<LocalTime> preferences
    );

    boolean deleteBooking(LocalDate localDateOfEvent);
}
