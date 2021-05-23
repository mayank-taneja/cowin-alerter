package com.taneja.response.cowin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sessions {

    Integer centerId;
    String name;
    String address;
    String stateName;
    String districtName;
    String blockName;
    Integer pincode;
    String from;
    String to;
    BigDecimal lat;
    String feeType;
    String sessionId;
    String date;
    Integer availableCapacityDose1;
    Integer availableCapacityDose2;
    Integer availableCapacity;
    String fee;
    Integer minAgeLimit;
    String vaccine;

}
