package com.hamedTech.service;

import com.hamedTech.payload.request.CityRequest;
import com.hamedTech.payload.response.CityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CityService {

    CityResponse createCity(CityRequest cityRequest);
    CityResponse getCityById(Long id);

    CityResponse updateCity(Long id, CityRequest cityRequest);
    void deleteCity(Long id);
    Page<CityResponse> getAllCities(Pageable pageable);

    Page<CityResponse> searchCities(String keyword, Pageable pageable);
    Page<CityResponse> getCitiesByCountryCode(String countryCode, Pageable pageable);

    boolean cityExists(String cityCode);
}
