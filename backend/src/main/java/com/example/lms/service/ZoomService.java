package com.example.lms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import us.zoom.sdk.ZoomApi;
import us.zoom.sdk.ZoomApiException;
import us.zoom.sdk.ZoomApiRequest;
import us.zoom.sdk.ZoomApiResponse;

@Service
public class ZoomService {

    @Value("${zoom.api.key}")
    private String apiKey;

    @Value("${zoom.api.secret}")
    private String apiSecret;

    private ZoomApi zoomApi;

    public ZoomService() {
        this.zoomApi = new ZoomApi(apiKey, apiSecret);
    }

    public String createMeeting(String topic, String startTime, int duration) {
        ZoomApiRequest request = new ZoomApiRequest();
        request.setTopic(topic);
        request.setStartTime(startTime);
        request.setDuration(duration);

        try {
            ZoomApiResponse response = zoomApi.createMeeting(request);
            return response.getJoinUrl();
        } catch (ZoomApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteMeeting(String meetingId) {
        try {
            zoomApi.deleteMeeting(meetingId);
            return true;
        } catch (ZoomApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ZoomApiResponse getMeetingDetails(String meetingId) {
        try {
            return zoomApi.getMeetingDetails(meetingId);
        } catch (ZoomApiException e) {
            e.printStackTrace();
            return null;
        }
    }
}
