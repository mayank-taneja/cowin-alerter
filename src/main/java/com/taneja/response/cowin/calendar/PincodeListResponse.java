package com.taneja.response.cowin.calendar;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PincodeListResponse {

    List<Integer> pincode_list = new ArrayList<>();

}
