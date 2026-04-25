package com.hamedTech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "City name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Size(min = 3, max = 10)
    @Column(nullable = false, unique = true)
    private String cityCode;

    @NotBlank
    @Column(nullable = false)
    private String countryCode;

    @NotBlank
    @Column(nullable = false)
    private String countryName;

    @Column(nullable = false)
    @Size(max = 10)
    private String regionCode;

    @Column(name = "time_zone_id", length = 50)
    private String timeZoneId;
}