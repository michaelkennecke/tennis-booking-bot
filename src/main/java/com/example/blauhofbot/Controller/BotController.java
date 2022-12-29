package com.example.blauhofbot.Controller;

import com.example.blauhofbot.Service.BotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/tennis")
public class BotController {

    private BotService botService;
    private HashMap<String, TimerTask> courtBookingTasks;
    private LocalTime BOOKING_START_TIME = LocalTime.of(8, 00, 00);
    private LocalTime BOOKING_END_TIME = LocalTime.of(9, 30, 00);

    BotController(BotService botService) {
        this.botService = botService;
        this.courtBookingTasks = new HashMap<>();
    }

    @GetMapping("/courtBookings")
    public ResponseEntity<HashMap> getAllCourtBookings() {
        return ResponseEntity.ok()
                .body(this.courtBookingTasks);
    }

    @PostMapping("/newCourtBooking/{playingDate}/{startTime}")
    public ResponseEntity<String> addBooking(@PathVariable("playingDate") String playingDate, @PathVariable("startTime") int startTime) {
        LocalDate transformedPlayingDate = LocalDate.parse(playingDate);
        LocalDate bookingDate = transformedPlayingDate.minusDays(7);
        LocalDateTime bookingDateTime = bookingDate.atTime(BOOKING_START_TIME);
        Date transformedBookingDate = Date.from(bookingDateTime.atZone(ZoneId.systemDefault()).toInstant());
        TimerTask courtBookingTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (LocalTime.now().isAfter(BOOKING_END_TIME)) {
                        cancel();
                    }
                    boolean isBooked = botService.bookCourt(startTime, transformedPlayingDate);
                    if (isBooked) {
                        System.out.println("Booking successful!");
                        cancel();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        this.courtBookingTasks.put(playingDate, courtBookingTask);
        Timer timer = new Timer("BookingTimer");
        LocalDate today = LocalDate.now();
        LocalDateTime todayTime = today.atTime(BOOKING_START_TIME);
        var timeBetween = Duration.between(today.atStartOfDay(), transformedPlayingDate.atStartOfDay()).toDays();
        if (timeBetween < 7) {
            timer.scheduleAtFixedRate(courtBookingTask, Date.from(todayTime.atZone(ZoneId.systemDefault()).toInstant()), 300000);
        } else {
           timer.scheduleAtFixedRate(courtBookingTask, transformedBookingDate, 60000);
        }
        return ResponseEntity.ok()
                .body("Created booking task (" +
                        "PlayingDate: " + playingDate + ", " +
                        "BookingDate: " + transformedBookingDate + ", " +
                        "Earliest Start time: " + startTime + ")!"
                );
    }

    @PostMapping("/cancelCourtBooking/{playingDate}")
    public ResponseEntity<String> cancelBooking(@PathVariable("playingDate") String playingDate) {
        this.courtBookingTasks.get(playingDate).cancel();
        this.courtBookingTasks.remove(playingDate);
        return ResponseEntity.ok()
                .body("Booking for " + playingDate + " canceled!");
    }

    @PostMapping("/updateBookingTimeWindow/{startTime}/{endTime}")
    public ResponseEntity<String> updateBookingTimeWindow(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        System.out.println("Start: " + LocalTime.parse(startTime));
        this.BOOKING_START_TIME = LocalTime.parse(startTime);
        this.BOOKING_END_TIME = LocalTime.parse(endTime);
        return ResponseEntity.ok()
                .body("new start time: " + this.BOOKING_START_TIME + ", new end time: " + this.BOOKING_END_TIME);
    }

}
