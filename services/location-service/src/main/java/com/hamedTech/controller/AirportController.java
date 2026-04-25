package com.hamedTech.controller;


import com.hamedTech.payload.request.AirportRequest;
import com.hamedTech.payload.response.AirportResponse;
import com.hamedTech.payload.response.ApiResponse;
import com.hamedTech.service.AirportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;


    @PostMapping
    public ResponseEntity<AirportResponse> createAirport(@Valid @RequestBody AirportRequest airportRequest){

        AirportResponse airportResponse = airportService.createAirport(airportRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(airportResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AirportResponse> updateAirport(@PathVariable Long id,
                                                         @RequestBody AirportRequest airportRequest){
        AirportResponse airportResponse = airportService.updateAirport(id, airportRequest);
        return ResponseEntity.status(HttpStatus.OK).body(airportResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAirport(@PathVariable Long id){
        airportService.deleteAirport(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Airport deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportResponse> getAirportById(@PathVariable Long id){
        AirportResponse airportResponse = airportService.getAirportById(id);
        return ResponseEntity.status(HttpStatus.OK).body(airportResponse);
    }

    @GetMapping
    public ResponseEntity<List<AirportResponse>> getAllAirports(){
        List<AirportResponse> airportResponses = airportService.getAllAirports();
        return ResponseEntity.status(HttpStatus.OK).body(airportResponses);
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<AirportResponse>> getAllAirportsByCityId(@PathVariable Long cityId){
        List<AirportResponse> airportResponses = airportService.getAirportsByCityId(cityId);
        return ResponseEntity.status(HttpStatus.OK).body(airportResponses);
    }

}
