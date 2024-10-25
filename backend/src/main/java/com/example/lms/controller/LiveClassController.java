package com.example.lms.controller;

import com.example.lms.service.NotificationService;
import com.example.lms.service.ZoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/live-class")
public class LiveClassController {

    @Autowired
    private ZoomService zoomService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/schedule")
    public String scheduleLiveClass(@RequestParam String topic, @RequestParam String startTime, @RequestParam int duration) {
        return zoomService.createMeeting(topic, startTime, duration);
    }

    @PostMapping("/invite")
    public void inviteStudents(@RequestParam String meetingUrl, @RequestParam String[] studentEmails) {
        for (String email : studentEmails) {
            notificationService.sendEmailNotification(email, "Live Class Invitation", "You are invited to a live class. Join here: " + meetingUrl);
        }
    }

    @PostMapping("/notify")
    public void sendClassNotification(@RequestParam String token, @RequestParam String title, @RequestParam String body) {
        notificationService.sendPushNotification(token, title, body);
    }

    @PostMapping("/record")
    public void recordLiveClass(@RequestParam String meetingId) {
        // Implement recording logic here
    }

    @PostMapping("/upload")
    public void uploadRecordedClass(@RequestParam String filePath) {
        // Implement upload logic here
    }
}
