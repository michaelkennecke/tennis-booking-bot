package com.example.blauhofbot.Model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.TimerTask;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Booking {
    private LocalDate localDateOfEvent;
    private LocalDateTime localDateTimeOfBookingStart;
    private LocalDateTime localDateTimeOfBookingEnd;
    private List<LocalTime> preferences;
    private BookingStatus bookingStatus;
    private TimerTask timerTask;
    private int bookingAttempts;

    @Builder
    public Booking(LocalDate localDateOfEvent, LocalDateTime localDateTimeOfBookingStart, LocalDateTime localDateTimeOfBookingEnd, List<LocalTime>  preferences, BookingStatus bookingStatus, TimerTask timerTask) {
        this.localDateOfEvent = localDateOfEvent;
        this.localDateTimeOfBookingStart = localDateTimeOfBookingStart;
        this.localDateTimeOfBookingEnd = localDateTimeOfBookingEnd;
        this.preferences = preferences;

        this.bookingStatus = bookingStatus;
        this.timerTask = timerTask;
        this.bookingAttempts = 0;
    }

    public enum BookingStatus {
        CREATED,
        SUCCESSFUL,
        CANCELED
    }
}
