package com.syduck.hitchcommons.domain.bo;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorldMapBO {

    private String key;
    private String name;
    private Location location;
    private int ratio;

    public WorldMapBO(GeoBO geoBO, String key) {

        this.location = new Location(geoBO.getLat(), geoBO.getLng());
        this.key = key;
        this.name = key;
    }
}

