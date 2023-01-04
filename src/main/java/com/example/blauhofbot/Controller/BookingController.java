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

    @PostMapping("/booking/{dateTimeOfEvent}/{dateTimeOfBookingStart}/{dateTimeOfBookingEnd}")
    public ResponseEntity<String> createBooking(@PathVariable("dateTimeOfEvent") String userInputDateTimeOfEvent,
                                                @PathVariable("dateTimeOfBookingStart") String userInputDateTimeOfBookingStart,
                                                @PathVariable("dateTimeOfBookingEnd") String userInputDateTimeOfBookingEnd) {
        LocalDateTime localDateTimeOfEvent = LocalDateTime.parse(userInputDateTimeOfEvent);
        LocalDateTime localDateTimeOfBookingStart = userInputDateTimeOfBookingStart.equals("now") ?
                LocalDateTime.now() : LocalDateTime.parse(userInputDateTimeOfBookingStart);
        LocalDateTime localDateTimeOfBookingEnd = userInputDateTimeOfBookingEnd.equals("now") ?
                LocalDateTime.now().plusSeconds(10) : LocalDateTime.parse(userInputDateTimeOfBookingEnd);
        this.bookingService.createBooking(localDateTimeOfEvent, localDateTimeOfBookingStart, localDateTimeOfBookingEnd);
        return ResponseEntity.ok()
                .body("Created booking task (" +
                        "Date of event: " + localDateTimeOfEvent + ", " +
                        "Start booking: " + localDateTimeOfBookingStart + ", " +
                        "End booking: " + localDateTimeOfBookingEnd +
                        ")"
                );
    }

    @DeleteMapping("/booking/{dateTimeOfEvent}")
    public ResponseEntity<String> cancelBooking(@PathVariable("dateTimeOfEvent") String userInputDateTimeOfEvent) {
        LocalDateTime localDateTimeOfEvent = LocalDateTime.parse(userInputDateTimeOfEvent);
        this.bookingService.deleteBooking(localDateTimeOfEvent);
        return ResponseEntity.ok()
                .body("Booking task for " + localDateTimeOfEvent + " deleted!");
    }
}
