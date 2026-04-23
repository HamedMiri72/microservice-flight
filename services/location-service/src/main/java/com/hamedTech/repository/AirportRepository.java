package com.hamedTech.repository;

import com.hamedTech.model.Airport;
import com.hamedTech.payload.response.AirportResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long> {

    Optional<Airport> findByIataCode(String iataCode);

    List<Airport> findByCityId(Long cityId);

    boolean existsByIataCode(String iataCode);
}
