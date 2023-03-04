package com.example.tennisbookingbot.controller;

import com.example.tennisbookingbot.model.Booking;
import com.example.tennisbookingbot.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/tennis")
public class BookingController {
    private final BookingService bookingService;

    BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/bookings")
    public ResponseEntity<HashMap> getAllBookings() {
        return ResponseEntity.ok()
                .body(this.bookingService.getBookings());
    }

    @GetMapping("/booking/{dateOfEvent}")
    public ResponseEntity<Booking> getBooking(@PathVariable("dateOfEvent") LocalDate dateOfEvent) {
        return ResponseEntity.ok()
                .body(this.bookingService.getBooking(dateOfEvent));
    }

    @PostMapping("/booking")
    public ResponseEntity<String> createBooking(@RequestBody Booking booking) {
        this.bookingService.createBooking(booking);
        return ResponseEntity.ok()
                .body("Booking task for " + booking.getLocalDateOfEvent() + " created!");
    }

    @DeleteMapping("/booking/{dateOfEvent}")
    public ResponseEntity<String> cancelBooking(@PathVariable("dateOfEvent") LocalDate dateOfEvent) {
        this.bookingService.deleteBooking(dateOfEvent);
        return ResponseEntity.ok()
                .body("Booking task for " + dateOfEvent + " deleted!");
    }
}
