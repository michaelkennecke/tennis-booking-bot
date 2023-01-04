package com.example.blauhofbot.Utils;

import com.example.blauhofbot.Model.Booking;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Database {
    public static HashMap<LocalDateTime, Booking> bookings = new HashMap<>();
}
