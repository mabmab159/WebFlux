package com.miguel.webflux.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DishDTO {
    private String id;
    @NotNull //No nulo
    @NotEmpty //No vacio
    @NotBlank //No caracteres en blanco
    @Size(min = 3)
    private String nameDish;
    @Min(value = 1)
    @Max(value = 999)
    private Double priceDish;
    @NotNull
    private Boolean statusDish;
}
