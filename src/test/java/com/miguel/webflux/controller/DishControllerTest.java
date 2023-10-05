package com.miguel.webflux.controller;

import com.miguel.webflux.dto.DishDTO;
import com.miguel.webflux.model.Dish;
import com.miguel.webflux.service.IDishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = DishController.class)
public class DishControllerTest {
    @Autowired
    private WebTestClient client;

    @MockBean
    private IDishService service;

    @MockBean
    @Qualifier("defaultMapper")
    private ModelMapper mapper;

    @MockBean
    private WebProperties.Resources resources;

    private Dish dish1;
    private Dish dish2;
    private List<Dish> list;

    private DishDTO dish1DTO;
    private DishDTO dish2DTO;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        dish1 = new Dish();
        dish1.setId("1");
        dish1.setName("Soda");
        dish1.setPrice(29.9);
        dish1.setStatus(true);

        dish2 = new Dish();
        dish2.setId("2");
        dish2.setName("Pizza");
        dish2.setPrice(49.9);
        dish2.setStatus(true);

        list = new ArrayList<>();
        list.add(dish1);
        list.add(dish2);

        dish1DTO = new DishDTO();
        dish1DTO.setId("1");
        dish1DTO.setNameDish("Soda");
        dish1DTO.setPriceDish(29.9);
        dish1DTO.setStatusDish(true);

        dish2DTO = new DishDTO();
        dish2DTO.setId("2");
        dish2DTO.setNameDish("Pizza");
        dish2DTO.setPriceDish(49.9);
        dish2DTO.setStatusDish(true);

        Mockito.when(service.findAll()).thenReturn(Flux.fromIterable(list));
        Mockito.when(mapper.map(dish1, DishDTO.class)).thenReturn(dish1DTO);
        Mockito.when(mapper.map(dish2, DishDTO.class)).thenReturn(dish2DTO);
        Mockito.when(service.save(any())).thenReturn(Mono.just(dish1));
        Mockito.when(service.update(any(), any())).thenReturn(Mono.just(dish1));
    }

    @Test
    public void findAllTest() {
        client.get()
                .uri("/dishes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void createTest() {
        client.post()
                .uri("/dishes")
                .body(Mono.just(dish1DTO), DishDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.nameDish").isNotEmpty()
                .jsonPath("$.priceDish").isNumber()
                .jsonPath("$.statusDish").isBoolean();
    }

    @Test
    public void updateTest() {
        client.put()
                .uri("/dishes/" + dish1.getId())
                .body(Mono.just(dish1DTO), DishDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.nameDish").isNotEmpty()
                .jsonPath("$.priceDish").isNumber()
                .jsonPath("$.statusDish").isBoolean();
    }

    @Test
    public void deleteTest() {
        Mockito.when(service.delete(any())).thenReturn(Mono.just(true));
        client.delete()
                .uri("/dishes/" + dish1.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void deleteTestError() {
        Mockito.when(service.delete(any())).thenReturn(Mono.just(false));
        client.delete()
                .uri("/dishes/" + dish1.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}
