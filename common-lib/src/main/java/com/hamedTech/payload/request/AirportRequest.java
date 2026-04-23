package com.hamedTech.payload.request;

import com.hamedTech.embedable.Address;
import com.hamedTech.embedable.GeoCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.TimeZone;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AirportRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "IATA code is required")
    @Size(min = 3, max = 3, message = "IATA code must be 3 characters long")
    private String iataCode;


    @Valid
    private Address address;

    @NotNull(message = "City ID is required")
    private Long cityId;

    @Valid
    private GeoCode geoCode;

    private TimeZone timeZone;
}
