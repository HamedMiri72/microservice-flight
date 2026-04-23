package com.hamedTech.embedable;


import jakarta.persistence.Embeddable;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class GeoCode {

    private String latitude;
    private String longitude;
}
