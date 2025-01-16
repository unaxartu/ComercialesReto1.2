package com.example.comercialesreto12;

public class Event {
    private String title;
    private String location;
    private String time;

    public Event(String title, String location, String time) {
        this.title = title;
        this.location = location;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }
}
