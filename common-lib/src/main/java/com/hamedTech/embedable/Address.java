package com.hamedTech.embedable;


import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class Address {


    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;
}