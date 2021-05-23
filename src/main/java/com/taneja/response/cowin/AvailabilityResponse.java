package com.taneja.response.cowin;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
public class AvailabilityResponse {

    public List<Sessions> sessions;

}
