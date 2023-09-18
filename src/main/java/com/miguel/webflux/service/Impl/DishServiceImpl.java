package com.miguel.webflux.service.Impl;

import com.miguel.webflux.model.Dish;
import com.miguel.webflux.repository.IDishRepo;
import com.miguel.webflux.repository.IGenericRepo;
import com.miguel.webflux.service.IDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor // usar cuando se añadira un atributo que no viene de un repositorio | Evita el constructor
public class DishServiceImpl extends CRUDImpl<Dish, String> implements IDishService {

    private final IDishRepo repo;

    @Override
    protected IGenericRepo<Dish, String> getRepo() {
        return repo;
    }
}
