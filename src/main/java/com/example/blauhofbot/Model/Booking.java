package com.example.blauhofbot.Model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.TimerTask;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Booking {
    private LocalDateTime localDateTimeOfEvent;
    private LocalDateTime localDateTimeOfBookingStart;
    private LocalDateTime localDateTimeOfBookingEnd;
    private BookingStatus bookingStatus;
    private TimerTask timerTask;
    private int bookingAttempts;

    @Builder
    public Booking(LocalDateTime localDateTimeOfEvent, LocalDateTime localDateTimeOfBookingStart, LocalDateTime localDateTimeOfBookingEnd, BookingStatus bookingStatus, TimerTask timerTask) {
        this.localDateTimeOfEvent = localDateTimeOfEvent;
        this.localDateTimeOfBookingStart = localDateTimeOfBookingStart;
        this.localDateTimeOfBookingEnd = localDateTimeOfBookingEnd;
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
