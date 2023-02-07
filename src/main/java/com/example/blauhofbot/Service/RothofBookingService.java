package com.example.blauhofbot.Service;

import com.example.blauhofbot.Bot.RothofBookingBot;
import com.example.blauhofbot.Model.Booking;
import com.example.blauhofbot.TimerTask.RothofBookingTimerTask;
import com.example.blauhofbot.Utils.Database;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Timer;

@Service
public class RothofBookingService implements BookingService {
    private final RothofBookingBot rothofBookingBot;

    RothofBookingService(RothofBookingBot rothofBookingBot) {
        this.rothofBookingBot = rothofBookingBot;
    }

    @Override
    public void createBooking(LocalDate localDateOfEvent,
                              LocalDateTime localDateTimeOfBookingStart,
                              LocalDateTime localDateTimeOfBookingEnd,
                              List<LocalTime> preferences) {
        Booking booking = Booking.builder()
                .localDateOfEvent(localDateOfEvent)
                .localDateTimeOfBookingStart(localDateTimeOfBookingStart)
                .localDateTimeOfBookingEnd(localDateTimeOfBookingEnd)
                .preferences(preferences)
                .bookingStatus(Booking.BookingStatus.CREATED)
                .build();
        RothofBookingTimerTask rothofBookingTimerTask = new RothofBookingTimerTask(booking, this.rothofBookingBot);
        booking.setTimerTask(rothofBookingTimerTask);
        Database.bookings.put(booking.getLocalDateOfEvent(), booking);
        Timer timer = new Timer("RothofBookingTimer");
        timer.scheduleAtFixedRate(
                rothofBookingTimerTask,
                Date.from(localDateTimeOfBookingStart.atZone(ZoneId.systemDefault()).toInstant()),
                30000
        );
    }

    @Override
    public boolean deleteBooking(LocalDate localDateOfEvent) {
        try {
            Database.bookings.get(localDateOfEvent).getTimerTask().cancel();
            Database.bookings.remove(localDateOfEvent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
