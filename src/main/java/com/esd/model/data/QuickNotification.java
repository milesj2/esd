package com.esd.model.data;

import java.util.ArrayList;
import java.util.List;

public class QuickNotification {

    private String title;
    private List<String> lines;

    public QuickNotification(String title, List<String> lines) {
        this.title = title;
        this.lines = lines;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLines() {
        return lines;
    }
}
