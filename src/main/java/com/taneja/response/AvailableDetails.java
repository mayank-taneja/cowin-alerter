package com.taneja.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AvailableDetails {

    String availableDate;
    String districtName;
    String centreName;
    String address;
    String vaccine;
    Integer pincode;
    Integer availableCapacityDose1;
    String fee;
}
