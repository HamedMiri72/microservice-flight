package com.hamedTech.payload.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CityRequest {

    @NotBlank(message = "City name is required")
    @Size(min = 3, max = 100, message = "City name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "City code is required")
    @Size(min = 3, max = 10, message = "City code must be between 3 and 10 characters")
    private String cityCode;

    @NotBlank(message = "Country code is required")
    @Size(min = 2, max = 5, message = "Country code must be 2 characters")
    private String countryCode;

    @NotBlank(message = "Country name is required")
    @Size(min = 3, max = 100, message = "Country name must be between 3 and 100 characters")
    private String countryName;

    @Size(max = 10, message = "Region code must be between 3 and 10 characters")
    private String regionCode;

    @Size(max = 10, message = "Time zone offset must be between 3 and 10 characters")
    private String timeZoneOffset;
}
