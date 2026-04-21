package com.hamedTech;


import com.hamedTech.payload.request.CityRequest;
import com.hamedTech.payload.response.ApiResponse;
import com.hamedTech.payload.response.CityResponse;
import com.hamedTech.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;


    @PostMapping
    public ResponseEntity<CityResponse> createCity(@Valid @RequestBody CityRequest cityRequest){
        CityResponse response = cityService.createCity(cityRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> getCityById(@PathVariable Long id){
        CityResponse response = cityService.getCityById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResponse> updateCity(@PathVariable Long id, @Valid @RequestBody CityRequest cityRequest){
        CityResponse response = cityService.updateCity(id, cityRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCity(@PathVariable Long id){
        cityService.deleteCity(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("City deleted successfully"));
    }

    @GetMapping
    public ResponseEntity<Page<CityResponse>> getAllCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.status(HttpStatus.OK).body(cityService.getAllCities(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CityResponse>> searchCities(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(cityService.searchCities(keyword, pageable));
    }

    @GetMapping("/country/{countryCode}")
    public ResponseEntity<Page<CityResponse>> getCitiesByCountryCode(
            @PathVariable String countryCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(cityService.getCitiesByCountryCode(countryCode, pageable));
    }

    @GetMapping("/exists/{cityCode}")
    public ResponseEntity<ApiResponse> cityExists(@PathVariable String cityCode) {
        boolean exists = cityService.cityExists(cityCode);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("City exists: " + exists));
    }

}
