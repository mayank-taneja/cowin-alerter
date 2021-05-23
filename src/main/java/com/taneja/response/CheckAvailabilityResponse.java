package com.taneja.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CheckAvailabilityResponse {

    Boolean availableAnywhere;
    Integer totalAvailability;
    List<AvailableDetails> detailsList = new ArrayList<>();

}
