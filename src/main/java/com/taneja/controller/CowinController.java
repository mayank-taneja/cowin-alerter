package com.taneja.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.taneja.response.CheckAvailabilityResponse;
import com.taneja.response.cowin.calendar.PincodeListResponse;
import com.taneja.response.cowin.calendar.PincodeResponse;
import com.taneja.service.CowinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cowin")
public class CowinController {

    @Autowired
    CowinService cowinService;

    @RequestMapping(method = RequestMethod.GET, path = "/checkAvailability/{minage}")
    public CheckAvailabilityResponse checkAvailability(@PathVariable(value = "minage")String minAge) throws JsonProcessingException {

        CheckAvailabilityResponse response = cowinService.checkAvailability(minAge);
        System.out.println("Response = " + response);
        return response;

    }

    @RequestMapping(method = RequestMethod.GET, path = "/calendarByDistrict/{fromToday}/{minage}")
    public PincodeListResponse calendarByDistrict(@PathVariable(value = "fromToday")String fromToday, @PathVariable(value = "minage")String minAge) throws JsonProcessingException {

        System.out.println("FROM TODAY = " + Integer.parseInt(fromToday));
        PincodeListResponse pincodeListResponse = cowinService.calendarByDistrict(Integer.parseInt(fromToday), Integer.parseInt(minAge));
        System.out.println("DONE!!!!!");
        return pincodeListResponse;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/test")
    public String testApi() {
        return "OK-200";
    }

}
