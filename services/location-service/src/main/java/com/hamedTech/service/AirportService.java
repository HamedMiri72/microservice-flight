package com.hamedTech.service;

import com.hamedTech.payload.request.AirportRequest;
import com.hamedTech.payload.response.AirportResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AirportService {

    AirportResponse createAirport(AirportRequest airportRequest);

    AirportResponse updateAirport(Long id, AirportRequest airportRequest);

    void deleteAirport(Long id);

    AirportResponse getAirportById(Long id);

    List<AirportResponse> getAllAirports();

    List<AirportResponse> getAirportsByCityId(Long cityId);
}
