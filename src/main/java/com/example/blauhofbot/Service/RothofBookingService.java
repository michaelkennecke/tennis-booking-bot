package com.example.blauhofbot.Service;

import com.example.blauhofbot.Bot.RothofBookingBot;
import com.example.blauhofbot.Model.Booking;
import com.example.blauhofbot.TimerTask.RothofBookingTimerTask;
import com.example.blauhofbot.Utils.Database;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;
import java.util.Timer;

@Service
public class RothofBookingService implements BookingService {
    private final RothofBookingBot rothofBookingBot;

    RothofBookingService(RothofBookingBot rothofBookingBot) {
        this.rothofBookingBot = rothofBookingBot;
    }

    @Override
    public void createBooking(LocalDateTime localDateTimeOfEvent,
                              LocalDateTime localDateTimeOfBookingStart,
                              LocalDateTime localDateTimeOfBookingEnd) {
        Booking booking = Booking.builder()
                .localDateTimeOfEvent(localDateTimeOfEvent)
                .localDateTimeOfBookingStart(localDateTimeOfBookingStart)
                .localDateTimeOfBookingEnd(localDateTimeOfBookingEnd)
                .bookingStatus(Booking.BookingStatus.CREATED)
                .build();
        RothofBookingTimerTask rothofBookingTimerTask = new RothofBookingTimerTask(booking, this.rothofBookingBot);
        booking.setTimerTask(rothofBookingTimerTask);
        Database.bookings.put(booking.getLocalDateTimeOfEvent(), booking);
        Timer timer = new Timer("RothofBookingTimer");
        timer.scheduleAtFixedRate(
                rothofBookingTimerTask,
                Date.from(localDateTimeOfBookingStart.atZone(ZoneId.systemDefault()).toInstant()),
                300000
        );
    }

    @Override
    public boolean deleteBooking(LocalDateTime localDateTimeOfEvent) {
        try {
            Database.bookings.get(localDateTimeOfEvent).getTimerTask().cancel();
            Database.bookings.remove(localDateTimeOfEvent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
