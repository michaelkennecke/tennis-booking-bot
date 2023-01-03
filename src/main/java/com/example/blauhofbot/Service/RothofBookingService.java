package com.example.blauhofbot.Service;

import com.example.blauhofbot.Bot.RothofBookingBot;
import com.example.blauhofbot.Model.Booking;
import com.example.blauhofbot.TimerTask.RothofBookingTimerTask;
import com.example.blauhofbot.Utils.Database;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;

@Service
public class RothofBookingService implements BookingService {

    private RothofBookingBot rothofBookingBot;

    public RothofBookingService(RothofBookingBot rothofBookingBot) {
        this.rothofBookingBot = rothofBookingBot;
    }

    @Override
    public void createBooking(LocalDateTime localDateTimeOfEvent) {
        RothofBookingTimerTask rothofBookingTimerTask = new RothofBookingTimerTask(this.rothofBookingBot);
        if (this.canBookImmediately(localDateTimeOfEvent)) {
            LocalDateTime localDateTimeOfBooking = LocalDate.now().atTime(Database.bookingStartTime);
            Booking booking = Booking.builder()
                    .localDateTimeOfEvent(localDateTimeOfEvent)
                    .localDateTimeOfBooking(localDateTimeOfBooking)
                    .bookingStatus(Booking.BookingStatus.CREATED)
                    .timerTask(rothofBookingTimerTask)
                    .build();
            Database.bookings.put(booking.getLocalDateTimeOfEvent(), booking);
            rothofBookingTimerTask.setBooking(booking);
            timer.schedule(
                    rothofBookingTimerTask,
                    Date.from(localDateTimeOfBooking.atZone(ZoneId.systemDefault()).toInstant())
            );
        } else {
            LocalDateTime localDateTimeOfBooking = localDateTimeOfEvent.toLocalDate().minusDays(7).atTime(Database.bookingStartTime);
            Booking booking = Booking.builder()
                    .localDateTimeOfEvent(localDateTimeOfEvent)
                    .localDateTimeOfBooking(localDateTimeOfBooking)
                    .bookingStatus(Booking.BookingStatus.CREATED)
                    .timerTask(rothofBookingTimerTask)
                    .build();
            Database.bookings.put(booking.getLocalDateTimeOfEvent(), booking);
            rothofBookingTimerTask.setBooking(booking);
            timer.scheduleAtFixedRate(
                    rothofBookingTimerTask,
                    Date.from(localDateTimeOfBooking.atZone(ZoneId.systemDefault()).toInstant()),
                    60000
            );
        }
    }

    private boolean canBookImmediately(LocalDateTime localDateTimeOfEvent) {
        long daysBetweenTodayAndEvent = Duration.between(
                LocalDate.now().atStartOfDay(),
                localDateTimeOfEvent.toLocalDate().atStartOfDay()
        ).toDays();
        return daysBetweenTodayAndEvent < 7;
    }
}
