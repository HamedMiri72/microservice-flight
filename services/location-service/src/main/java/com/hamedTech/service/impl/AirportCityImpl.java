package com.hamedTech.service.impl;

import com.hamedTech.mapper.AirportMapper;
import com.hamedTech.model.Airport;
import com.hamedTech.model.City;
import com.hamedTech.payload.request.AirportRequest;
import com.hamedTech.payload.response.AirportResponse;
import com.hamedTech.repository.AirportRepository;
import com.hamedTech.repository.CityRepository;
import com.hamedTech.service.AirportService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirportCityImpl implements AirportService{

    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;

    @Override
    @Transactional
    public AirportResponse createAirport(AirportRequest airportRequest) {

        String iataCode = airportRequest.getIataCode().toUpperCase().trim();


        if(airportRepository.existsByIataCode(iataCode)){
            throw new RuntimeException("Airport with IATA code already exists: " + iataCode);
        }


        City city = cityRepository.findById(airportRequest.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found with ID: " + airportRequest.getCityId()));


        Airport airportToSave = AirportMapper.mapToAirport(airportRequest);
        airportToSave.setCity(city);


        Airport savedAirport = airportRepository.save(airportToSave);


        return AirportMapper.mapToAirportResponse(savedAirport);
    }

    @Override
    @Transactional
    public AirportResponse updateAirport(Long id, AirportRequest airportRequest) {

        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Airport not found with ID: " + id));


        if (airportRequest.getIataCode() != null) {
            String cleanIata = airportRequest.getIataCode().toUpperCase().trim();
            if (!airport.getIataCode().equals(cleanIata)) {
                if (airportRepository.existsByIataCode(cleanIata)) {
                    throw new RuntimeException("IATA already exists: " + cleanIata);
                }
                airport.setIataCode(cleanIata);
            }
        }

        if(airportRequest.getCityId() != null){
            City city = cityRepository.findById(airportRequest.getCityId())
                    .orElseThrow(() -> new RuntimeException("City not found with ID: " + airportRequest.getCityId()));
            airport.setCity(city);
        }

        Airport updatedAirport = AirportMapper.updateAirport(airport, airportRequest);

        airportRepository.save(updatedAirport);

        return AirportMapper.mapToAirportResponse(updatedAirport);

    }

    @Override
    public void deleteAirport(Long id) {

        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Airport not found with ID: " + id));

        airportRepository.delete(airport);

    }

    @Override
    public AirportResponse getAirportById(Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Airport not found with ID: " + id));
        return AirportMapper.mapToAirportResponse(airport);
    }

    @Override
    public List<AirportResponse> getAllAirports() {

        List<Airport> airports = airportRepository.findAll();

        return airports.stream()
                .map(AirportMapper::mapToAirportResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AirportResponse> getAirportsByCityId(Long cityId) {

        List<Airport> airports = airportRepository.findByCityId(cityId);
        return airports.stream()
                .map(AirportMapper::mapToAirportResponse)
                .collect(Collectors.toList());
    }
}
