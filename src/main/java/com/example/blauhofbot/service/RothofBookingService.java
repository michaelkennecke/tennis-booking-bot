package com.example.blauhofbot.service;

import com.example.blauhofbot.bot.RothofBookingBot;
import com.example.blauhofbot.model.Booking;
import com.example.blauhofbot.timerTask.RothofBookingTimerTask;
import com.example.blauhofbot.utils.Database;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;
import java.util.Timer;

@Service
public class RothofBookingService extends BookingService {
    private final RothofBookingBot rothofBookingBot;
    private final long BOOKING_ATTEMPTS_RATE_IN_MS = 3000;

    RothofBookingService(RothofBookingBot rothofBookingBot) {
        this.rothofBookingBot = rothofBookingBot;
    }

    @Override
    public void createBooking(Booking booking) {
        RothofBookingTimerTask rothofBookingTimerTask = new RothofBookingTimerTask(booking, this.rothofBookingBot);
        booking.setTimerTask(rothofBookingTimerTask);
        Database.insertBooking(booking);
        Timer timer = new Timer("RothofBookingTimer");
        timer.scheduleAtFixedRate(
                rothofBookingTimerTask,
                Date.from(booking.getLocalDateTimeOfBookingStart().get().atZone(ZoneId.systemDefault()).toInstant()),
                BOOKING_ATTEMPTS_RATE_IN_MS
        );
    }
}
