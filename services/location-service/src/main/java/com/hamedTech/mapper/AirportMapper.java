package com.hamedTech.mapper;


import com.hamedTech.model.Airport;
import com.hamedTech.payload.request.AirportRequest;
import com.hamedTech.payload.response.AirportResponse;

import java.time.ZoneId;

public class AirportMapper {

    public static Airport mapToAirport(AirportRequest airportRequest) {

        if(airportRequest == null) {
            return null;
        }
        return Airport.builder()
                .name(airportRequest.getName())
                .iataCode(airportRequest.getIataCode())
                .address(airportRequest.getAddress())
                .geoCode(airportRequest.getGeoCode())
                .timeZone(airportRequest.getTimeZone() != null ?
                        airportRequest.getTimeZone().toZoneId().getId() : null)
                .build();
    }

    public static AirportResponse mapToAirportResponse(Airport airport) {

        if(airport == null) {
            return null;
        }
        return AirportResponse.builder()
                .id(airport.getId())
                .name(airport.getName())
                .iataCode(airport.getIataCode())
                .detailedName(airport.getDetailedName())
                .timeZone(airport.getTimeZone() != null ?
                        ZoneId.of(airport.getTimeZone()) : null)
                .geoCode(airport.getGeoCode())
                .address(airport.getAddress())
                .city(CityMapper.toCityResponse(airport.getCity()))
                .build();
    }

    public static Airport updateAirport(Airport airport, AirportRequest airportRequest) {

        if(airportRequest == null) return null;


        if(airportRequest.getName() != null){
            airport.setName(airportRequest.getName());
        }


        if(airportRequest.getGeoCode() != null){
            airport.setGeoCode(airportRequest.getGeoCode());
        }

        if(airportRequest.getAddress() != null){
            airport.setAddress(airportRequest.getAddress());
        }

//        if(airportRequest.getTimeZone() != null){
//            airport.setTimeZone(airportRequest.getTimeZone().toZoneId().getId());
//        }

        return airport;
    }
}
