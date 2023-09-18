package com.miguel.webflux.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishDTO {
    private String id;
    private String nameDish;
    private Double priceDish;
    private Boolean statusDish;
}
