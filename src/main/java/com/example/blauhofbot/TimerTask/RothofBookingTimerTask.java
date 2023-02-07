package com.example.blauhofbot.TimerTask;
import com.example.blauhofbot.Bot.RothofBookingBot;
import com.example.blauhofbot.Model.Booking;
import com.example.blauhofbot.Utils.Database;

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
            System.out.println("Booking not successful!");
            Database.bookings.get(this.booking.getLocalDateOfEvent()).setBookingStatus(Booking.BookingStatus.CANCELED);
            cancel();
            return;
        }
        boolean isBooked = this.rothofBookingBot.book(this.booking);
        if (isBooked) {
            System.out.println("Booking successful!");
            Database.bookings.get(this.booking.getLocalDateOfEvent()).setBookingStatus(Booking.BookingStatus.SUCCESSFUL);
            cancel();
        }
    }
}
