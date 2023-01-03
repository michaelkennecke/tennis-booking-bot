package com.example.blauhofbot.Utils;

import com.example.blauhofbot.Model.Booking;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

public class Database {
    public static HashMap<LocalDateTime, Booking> bookings = new HashMap<>();
    public static LocalTime bookingStartTime = LocalTime.of(7, 30, 00);
    public static LocalTime bookingEndTime = LocalTime.of(10, 00, 00);
}
