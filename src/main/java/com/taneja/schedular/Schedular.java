package com.taneja.schedular;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.taneja.response.cowin.calendar.PincodeListResponse;
import com.taneja.service.CowinService;
import com.taneja.sound.PlaySound;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class Schedular {

    @Autowired
    CowinService cowinService;

    @Autowired
    PlaySound playSound;

    @Scheduled(fixedDelay = 30000L)
    public void scheduledHit() throws JsonProcessingException {

        PincodeListResponse pincodeListResponse = cowinService.calendarByDistrict(0, 18);

        if(!pincodeListResponse.getPincode_list().isEmpty()) {
            playSound.playBeep2();
        }

    }

}
