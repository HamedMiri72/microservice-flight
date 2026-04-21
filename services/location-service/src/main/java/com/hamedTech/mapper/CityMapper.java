package com.hamedTech.mapper;

import com.hamedTech.model.City;
import com.hamedTech.payload.request.CityRequest;
import com.hamedTech.payload.response.CityResponse;

public class CityMapper {


    public static City toCity(CityRequest cityRequest){

        if(cityRequest == null) return null;

        return City.builder()
                .name(cityRequest.getName())
                .cityCode(cityRequest.getCityCode())
                .countryCode(cityRequest.getCountryCode())
                .countryName(cityRequest.getCountryName())
                .regionCode(cityRequest.getRegionCode())
                .timeZoneId(cityRequest.getTimeZoneOffset())
                .build();
    }


    public static CityResponse toCityResponse(City city){
        if(city == null) return null;

        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .cityCode(city.getCityCode())
                .countryCode(city.getCountryCode())
                .countryName(city.getCountryName())
                .regionCode(city.getRegionCode())
                .timeZoneOffset(city.getTimeZoneId())
                .build();
    }

    public static City updateCity(City city, CityRequest cityRequest){

        if(city.getName() != cityRequest.getName()){
            city.setName(cityRequest.getName().trim());
        }

        if(city.getCityCode() != city.getCityCode()){
            city.setCityCode(cityRequest.getCityCode().trim());
        }

        if (city.getCountryName() != cityRequest.getCountryName()){
            city.setCountryName(cityRequest.getCountryName().trim());
        }

        if(city.getRegionCode() != cityRequest.getCityCode()){
            city.setRegionCode(cityRequest.getRegionCode().trim());
        }

       if(city.getCountryCode() != cityRequest.getCountryCode()){
           city.setCountryCode(cityRequest.getCountryCode().trim());
       }


        return city;
    }
}
