package com.example.blauhofbot.timerTask;
import com.example.blauhofbot.bot.RothofBookingBot;
import com.example.blauhofbot.model.Booking;
import com.example.blauhofbot.utils.Database;

import java.time.LocalDateTime;
import java.util.TimerTask;

public class RothofBookingTimerTask extends TimerTask {
    private RothofBookingBot rothofBookingBot;
    private Booking booking;

    public RothofBookingTimerTask(Booking booking, RothofBookingBot rothofBookingBot) {
        this.booking = booking;
        this.rothofBookingBot = rothofBookingBot;
    }

    @Override
    public void run() {
        if (LocalDateTime.now().isAfter(this.booking.getLocalDateTimeOfBookingEnd())) {
            Database.getBooking(this.booking.getLocalDateOfEvent()).setBookingStatus(Booking.BookingStatus.NOT_SUCCESSFUL);
            cancel();
            return;
        }
        boolean isBooked = this.rothofBookingBot.book(this.booking);
        if (isBooked) {
            Database.getBooking(this.booking.getLocalDateOfEvent()).setBookingStatus(Booking.BookingStatus.SUCCESSFUL);
            cancel();
        }
    }
}
