package com.syduck.hitchcommons.domain.bo;

import com.syduck.hitchcommons.utils.CommonsUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoBO implements Serializable {

    private String targetId;
    private float distance;
    private String lng;
    private String lat;


    @Override
    public String toString() {
        return "GeoBO{" +
                "targetId='" + targetId + '\'' +
                '}';
    }

    public Float toKilometre() {
        return Float.parseFloat(CommonsUtils.floatToStr(distance));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoBO geoBO = (GeoBO) o;
        return targetId.equals(geoBO.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetId);
    }
}
