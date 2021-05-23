package com.taneja.response.cowin.calendar;

import lombok.Data;

import java.util.List;

@Data
public class CalendarSession {

    public String session_id;
    public String date;
    public int available_capacity;
    public int min_age_limit;
    public String vaccine;
    public List<String> slots;
    public int available_capacity_dose1;
    public int available_capacity_dose2;

}
