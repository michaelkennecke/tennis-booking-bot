package com.example.blauhofbot.Utils;

import com.example.blauhofbot.Model.Booking;

import java.time.LocalDate;
import java.util.HashMap;

public class Database {
    public static HashMap<LocalDate, Booking> bookings = new HashMap<>();
}
