package models.entitities;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import models.enums.SeatCategory;

public class Theater {
    private String id;
    private String name;
    private String city;
    private String address;
    private List<String> hallIds;
    private Map<SeatCategory, BigDecimal> basePricing;

    public Theater(String id, String name, String city, String address,
                   List<String> hallIds, Map<SeatCategory, BigDecimal> basePricing) {
                    this.id = id;
                    this.name = name;
                    this.city = city;
                    this.address = address;
                    this.hallIds = hallIds;
                    this.basePricing = basePricing; 
                   }
 
    public static Theater create(String idString name, String city, String address,
                                  Map<SeatCategory, BigDecimal> basePricing) {
        return Theater.builder()
                .id(id)
                .name(name)
                .city(city)
                .address(address)
                .basePricing(basePricing)
                .build();
    }
}
