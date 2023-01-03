package com.example.blauhofbot.Controller;

import com.example.blauhofbot.Service.BookingService;
import com.example.blauhofbot.Utils.Database;
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
    public ResponseEntity<HashMap> getAllCourtBookings() {
        return ResponseEntity.ok()
                .body(Database.bookings);
    }

    @PostMapping("/booking/{dateTimeOfEvent}")
    public ResponseEntity<String> createBooking(@PathVariable("dateTimeOfEvent") String userInputDateTimeOfEvent) {
        LocalDateTime localDateTimeOfEvent = LocalDateTime.parse(userInputDateTimeOfEvent);
        this.bookingService.createBooking(localDateTimeOfEvent);
        return ResponseEntity.ok()
                .body("Created booking task (" +
                        "Date of event: " + localDateTimeOfEvent + ", " + ")"
                );
    }

    @DeleteMapping("/booking/{dateTimeOfEvent}")
    public ResponseEntity<String> cancelBooking(@PathVariable("dateTimeOfEvent") String userInputDateTimeOfEvent) {
        LocalDateTime localDateTimeOfEvent = LocalDateTime.parse(userInputDateTimeOfEvent);
        Database.bookings.get(localDateTimeOfEvent).getTimerTask().cancel();
        Database.bookings.remove(localDateTimeOfEvent);
        return ResponseEntity.ok()
                .body("Booking for " + localDateTimeOfEvent + " deleted!");
    }

    @PostMapping("/updateBookingTimeWindow/{startTime}/{endTime}")
    public ResponseEntity<String> updateBookingTimeWindow(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        Database.bookingStartTime = LocalTime.parse(startTime);
        Database.bookingEndTime = LocalTime.parse(endTime);
        return ResponseEntity.ok()
                .body("new start time: " + Database.bookingStartTime + ", new end time: " + Database.bookingEndTime);
    }

}
