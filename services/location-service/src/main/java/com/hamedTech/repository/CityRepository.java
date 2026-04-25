package com.hamedTech.repository;

import com.hamedTech.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    boolean existsByCityCode(String cityCode);

    Optional<City> findById(Long id);

    boolean existsByCityCodeAndIdNot(String cityCode, Long id);

    Page<City> findByCountryCodeIgnoreCase(String countryCode, Pageable pageable);

    @Query("SELECT c FROM City c WHERE " +
            "lower(c.name) LIKE lower(concat('%', :keyword, '%')) OR " +
            "lower(c.cityCode) LIKE lower(concat('%', :keyword, '%')) OR " +
            "lower(c.countryName) LIKE lower(concat('%', :keyword, '%'))")
    Page<City> searchByKeyWord(String keyword, Pageable pageable);


    Page<City> findAllByCountryCode(String countryCode, Pageable pageable);


}
