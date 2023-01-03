package com.example.blauhofbot.TimerTask;
import com.example.blauhofbot.Bot.RothofBookingBot;
import com.example.blauhofbot.Model.Booking;
import com.example.blauhofbot.Utils.Database;

import java.time.LocalTime;
import java.util.TimerTask;

public class RothofBookingTimerTask extends TimerTask {

    private RothofBookingBot rothofBookingBot;
    private Booking booking;

    public RothofBookingTimerTask(RothofBookingBot rothofBookingBot) {
        this.rothofBookingBot = rothofBookingBot;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    @Override
    public void run() {
        if (LocalTime.now().isAfter(Database.bookingEndTime)) {
            Database.bookings.get(this.booking.getLocalDateTimeOfEvent()).setBookingStatus(Booking.BookingStatus.CANCELED);
            cancel();
        }
        boolean isBooked = this.rothofBookingBot.book(this.booking);
        if (isBooked) {
            System.out.println("Booking successful!");
            Database.bookings.get(this.booking.getLocalDateTimeOfEvent()).setBookingStatus(Booking.BookingStatus.SUCCESSFUL);
            cancel();
        }
    }
}
