package com.hamedTech.service.impl;

import com.hamedTech.mapper.CityMapper;
import com.hamedTech.model.City;
import com.hamedTech.payload.request.CityRequest;
import com.hamedTech.payload.response.CityResponse;
import com.hamedTech.repository.CityRepository;
import com.hamedTech.service.CityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {


    private final CityRepository cityRepository;

    @Override
    public CityResponse createCity(CityRequest cityRequest) {

        boolean existingCity = cityRepository.existsByCityCode(cityRequest.getCityCode());

        if(existingCity){
            throw new RuntimeException("City with given city code already exists:: " + cityRequest.getCityCode());
        }

        City city = CityMapper.toCity(cityRequest);
        cityRepository.save(city);

        CityResponse response = CityMapper.toCityResponse(city);

        return response;

    }

    @Override
    public CityResponse getCityById(Long id) {

        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City with given id does not exist:: " + id));

        CityResponse response = CityMapper.toCityResponse(city);

        return response;
    }

    @Override
    @Transactional
    public CityResponse updateCity(Long id, CityRequest cityRequest) {

        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found"));

        CityMapper.updateCity(city, cityRequest);
        City updatedCity = cityRepository.save(city);

        CityResponse response = CityMapper.toCityResponse(updatedCity);

        return response;
    }

    @Override
    public void deleteCity(Long id) {

        if(!cityRepository.existsById(id)){
            throw new RuntimeException("City not found");
        }

        cityRepository.deleteById(id);

    }

    @Override
    public Page<CityResponse> getAllCities(Pageable pageable) {

        Page<City> cities = cityRepository.findAll(pageable);

        Page<CityResponse> responses = cities.map(CityMapper::toCityResponse);
        return responses;
    }

    @Override
    public Page<CityResponse> searchCities(String keyword, Pageable pageable) {

        Page<City> cities = cityRepository.searchByKeyWord(keyword, pageable);

        Page<CityResponse> responses = cities.map(CityMapper :: toCityResponse);

        return responses;
    }

    @Override
    public Page<CityResponse> getCitiesByCountryCode(String countryCode, Pageable pageable) {

        Page<City> cities = cityRepository.findByCountryCodeIgnoreCase(countryCode, pageable);

        Page<CityResponse> responses = cities.map(CityMapper :: toCityResponse);

        return responses;
    }

    @Override
    public boolean cityExists(String cityCode) {

        boolean cityExists = cityRepository.existsByCityCode(cityCode);
        return cityExists;
    }

}
