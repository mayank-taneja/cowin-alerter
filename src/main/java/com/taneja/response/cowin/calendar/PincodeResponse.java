package com.taneja.response.cowin.calendar;

import lombok.Data;

import java.util.Comparator;

@Data
public class PincodeResponse implements Comparable<PincodeResponse> {

    Integer pincode;
    Integer capacity;


    @Override
    public int compareTo(PincodeResponse o) {
        return this.getCapacity().compareTo(o.getCapacity());
    }

}
