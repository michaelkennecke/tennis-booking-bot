package com.example.blauhofbot.Controller;

import com.example.blauhofbot.Service.BotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/api/v1/tennis")
public class BotController {

    private BotService botService;
    private HashMap<String, TimerTask> courtBookingTasks;

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
        LocalDate bookingDate = LocalDate.parse(playingDate).minusDays(7);
        LocalDateTime bookingDateTime = bookingDate.atTime(00, 00, 00);
        Date transformedBookingDate = Date.from(bookingDateTime.atZone(ZoneId.systemDefault()).toInstant());
        TimerTask courtBookingTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    botService.bookCourt(startTime, bookingDate);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        this.courtBookingTasks.put(playingDate, courtBookingTask);
        Timer timer = new Timer("BookingTimer");
        timer.schedule(courtBookingTask, transformedBookingDate);
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

}
