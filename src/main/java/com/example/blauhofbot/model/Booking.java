package com.example.blauhofbot.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.TimerTask;

@Getter
@Setter
@ToString
public class Booking {
    private LocalDate localDateOfEvent;
    private Optional<LocalDateTime> localDateTimeOfBookingStart;
    private LocalDateTime localDateTimeOfBookingEnd;
    private List<LocalTime> preferences;
    private BookingStatus bookingStatus;
    private TimerTask timerTask;
    private int bookingAttempts;

    public Booking(LocalDate localDateOfEvent,
                   Optional<LocalDateTime> localDateTimeOfBookingStart,
                   LocalDateTime localDateTimeOfBookingEnd,
                   List<LocalTime> preferences) {

        this.localDateOfEvent = localDateOfEvent;
        if (localDateTimeOfBookingStart.isEmpty()) {
            this.localDateTimeOfBookingStart = Optional.of(LocalDateTime.now());
        } else {
            this.localDateTimeOfBookingStart = localDateTimeOfBookingStart;
        }
        this.localDateTimeOfBookingEnd = localDateTimeOfBookingEnd;
        this.preferences = preferences;
        this.bookingStatus = BookingStatus.CREATED;
        this.bookingAttempts = 0;
    }

    public enum BookingStatus {
        CREATED,
        SUCCESSFUL,
        NOT_SUCCESSFUL
    }
}
