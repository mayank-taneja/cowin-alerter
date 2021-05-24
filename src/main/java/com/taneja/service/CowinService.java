package com.taneja.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taneja.response.AvailableDetails;
import com.taneja.response.CheckAvailabilityResponse;
import com.taneja.response.cowin.AvailabilityResponse;
import com.taneja.response.cowin.Sessions;
import com.taneja.response.cowin.calendar.*;
import com.taneja.sound.PlaySound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Service
public class CowinService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PlaySound playSound;

    @Value("${second.alert.min}")
    Integer minAlert;

    @Value("${first.district.id}")
    Integer firstDistrictId;

    @Value("${last.district.id}")
    Integer lastDistrictId;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }

    public CheckAvailabilityResponse checkAvailability(String minAge) throws JsonProcessingException {

        final String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict";
        LocalDate today = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        AvailabilityResponse availabilityResponse = new AvailabilityResponse();
        Integer total = 0;
        List<AvailableDetails> availableDetailsList = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.USER_AGENT, "Mozilla/5.0 ");
        headers.set(HttpHeaders.ORIGIN, "https://www.cowin.gov.in");
        headers.set(HttpHeaders.REFERER, "https://www.cowin.gov.in");


        for(int districtId = firstDistrictId; districtId <= lastDistrictId; districtId++) {

            for(int i = 0; i <= 9; i++) {

                LocalDate dateToCheck = today.plusDays(i);
                String formattedDate = dateToCheck.format(format);

                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                        .queryParam("district_id", districtId)
                        .queryParam("date", formattedDate);

                HttpEntity<?> entity = new HttpEntity<>(headers);

                HttpEntity<String> response = restTemplate.exchange(
                        builder.toUriString(),
                        HttpMethod.GET,
                        entity,
                        String.class);

                System.out.println("Date = " + formattedDate + "\nDistrict_Id = " + districtId + "\nResponse = " + response);
                System.out.println("======================================================================================");

                ObjectMapper mapper = new ObjectMapper();

                availabilityResponse = mapper.readValue(response.getBody(), AvailabilityResponse.class);

                List<Sessions> available_sessions = availabilityResponse.getSessions();

                for(Sessions sessions : available_sessions) {

                    if(sessions.getMinAgeLimit() == Integer.parseInt(minAge) && sessions.getAvailableCapacityDose1() > 0) {
                        total++;
                        AvailableDetails availableDetails = new AvailableDetails();
                        availableDetails.setAvailableDate(sessions.getDate());
                        availableDetails.setCentreName(sessions.getName());
                        availableDetails.setDistrictName(sessions.getDistrictName());
                        availableDetails.setVaccine(sessions.getVaccine());
                        availableDetails.setAddress(sessions.getAddress());
                        availableDetails.setAvailableCapacityDose1(sessions.getAvailableCapacityDose1());
                        availableDetails.setFee(sessions.getFee());
                        availableDetails.setPincode(sessions.getPincode());
                        availableDetailsList.add(availableDetails);
                    }
                }

            }

        }

        CheckAvailabilityResponse checkAvailabilityResponse = new CheckAvailabilityResponse();
        if(total > 0) {
            checkAvailabilityResponse.setAvailableAnywhere(true);
            checkAvailabilityResponse.setDetailsList(availableDetailsList);
        } else {
            checkAvailabilityResponse.setAvailableAnywhere(false);
        }
        checkAvailabilityResponse.setTotalAvailability(total);

        return checkAvailabilityResponse;
    }


    public PincodeListResponse calendarByDistrict(int fromToday, int minAge) throws JsonProcessingException {

        final String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict";
        LocalDate today = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        CalendarByDistrictResponse calendarByDistrictResponse = new CalendarByDistrictResponse();
        HashMap<Integer, Integer> pinMap = new HashMap<>();
        int capacity = 0;

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.USER_AGENT, "Mozilla/5.0 ");
        headers.set(HttpHeaders.ORIGIN, "https://www.cowin.gov.in");
        headers.set(HttpHeaders.REFERER, "https://www.cowin.gov.in");
        headers.set("authority", "cdn-api.co-vin.in");
        headers.set("scheme", "https");

        for(int districtId = firstDistrictId; districtId <= lastDistrictId; districtId++) {

            LocalDate dateToCheck = today.plusDays(fromToday);
            String formattedDate = dateToCheck.format(format);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("district_id", districtId)
                    .queryParam("date", formattedDate);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            HttpEntity<String> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class);

            //System.out.println("District_Id = " + districtId + "\nResponse = " + response);
            //System.out.println("======================================================================================");


            ObjectMapper mapper = new ObjectMapper();

            calendarByDistrictResponse = mapper.readValue(response.getBody(), CalendarByDistrictResponse.class);

            List<Centers> available_centers = calendarByDistrictResponse.getCenters();

            for(Centers centers : available_centers) {

                for(CalendarSession calendarSession : centers.getSessions()) {

                    if(calendarSession.getMin_age_limit() == minAge && calendarSession.getAvailable_capacity_dose1() > 0) {
                        if(pinMap.containsKey(centers.getPincode())) {
                            capacity = pinMap.get(centers.getPincode());
                            capacity += calendarSession.getAvailable_capacity_dose1();
                            pinMap.put(centers.getPincode(), capacity);
                        } else {
                            capacity = calendarSession.getAvailable_capacity_dose1();
                            pinMap.put(centers.getPincode(), capacity);
                        }
                    }

                }

            }

        }

        List<PincodeResponse> resultList = new ArrayList<>();

        System.out.println("=================PRINTING NOW===============");
        for(Map.Entry<Integer, Integer> entry : pinMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());
            Integer pinCode = entry.getKey();
            Integer value = entry.getValue();
            //pinMap.remove(pinCode);
            pinCode -= 110000;
            //pinMap.put(pinCode, value);
            PincodeResponse response = new PincodeResponse();
            response.setCapacity(value);
            response.setPincode(pinCode);
            resultList.add(response);
        }

        PincodeListResponse listResponse = new PincodeListResponse();
        List<Integer> pincodelist = new ArrayList<>();

        Collections.sort(resultList, Collections.reverseOrder());
        for(int i = 0; i < resultList.size(); i++) {
            PincodeResponse new_response = resultList.get(i);
            int pin = new_response.getPincode();
            pincodelist.add(pin);
        }

        if(!resultList.isEmpty() && resultList.get(0).getCapacity() >= minAlert) {
            playSound.playBeep();
        }

        listResponse.setPincode_list(pincodelist);

        System.out.println("");
        System.out.println("*************************************************************");
        System.out.println(listResponse.getPincode_list());
        System.out.println("*************************************************************");

        return listResponse;

    }

}
