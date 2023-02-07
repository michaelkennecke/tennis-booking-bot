package com.example.blauhofbot.Controller;

import com.example.blauhofbot.Service.BookingService;
import com.example.blauhofbot.Utils.Database;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

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
    public ResponseEntity<String> createBooking(@PathVariable("dateTimeOfEvent") String userInputDateOfEvent,
                                                @PathVariable("dateTimeOfBookingStart") String userInputDateTimeOfBookingStart,
                                                @PathVariable("dateTimeOfBookingEnd") String userInputDateTimeOfBookingEnd,
                                                @RequestBody String[] userInputPreferences) {

        System.out.println(userInputPreferences.length);

        LocalDate localDateOfEvent = LocalDate.parse(userInputDateOfEvent);
        LocalDateTime localDateTimeOfBookingStart = userInputDateTimeOfBookingStart.equals("now") ?
                LocalDateTime.now() : LocalDateTime.parse(userInputDateTimeOfBookingStart);
        LocalDateTime localDateTimeOfBookingEnd = userInputDateTimeOfBookingEnd.equals("now") ?
                LocalDateTime.now().plusSeconds(10) : LocalDateTime.parse(userInputDateTimeOfBookingEnd);

        List<LocalTime> preferences = Arrays.stream(userInputPreferences).map(pref -> LocalTime.parse(pref)).collect(Collectors.toList());

        // LocalTime[] preferences = (LocalTime[]) Arrays.stream(userInputPreferences).map(preference -> LocalTime.parse(preference)).toArray();

        this.bookingService.createBooking(localDateOfEvent, localDateTimeOfBookingStart, localDateTimeOfBookingEnd, preferences);
        return ResponseEntity.ok()
                .body("Created booking task (" +
                        "Date of event: " + localDateOfEvent + ", " +
                        "Start booking: " + localDateTimeOfBookingStart + ", " +
                        "End booking: " + localDateTimeOfBookingEnd +
                        ")"
                );
    }

    @DeleteMapping("/booking/{dateTimeOfEvent}")
    public ResponseEntity<String> cancelBooking(@PathVariable("dateTimeOfEvent") String userInputDateOfEvent) {
        LocalDate localDateOfEvent = LocalDate.parse(userInputDateOfEvent);
        this.bookingService.deleteBooking(localDateOfEvent);
        return ResponseEntity.ok()
                .body("Booking task for " + localDateOfEvent + " deleted!");
    }
}
