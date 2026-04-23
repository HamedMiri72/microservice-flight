package com.hamedTech.payload.response;


import com.hamedTech.embedable.Address;
import com.hamedTech.embedable.GeoCode;
import lombok.*;

import java.time.ZoneId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AirportResponse {


    private Long id;
    private String iataCode;
    private String name;
    private String detailedName;
    private Address address;
    private GeoCode geoCode;
    private ZoneId timeZone;
    private CityResponse city;
}
