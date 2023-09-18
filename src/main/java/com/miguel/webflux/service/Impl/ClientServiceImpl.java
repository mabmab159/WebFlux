package com.miguel.webflux.service.Impl;

import com.miguel.webflux.model.Client;
import com.miguel.webflux.model.Dish;
import com.miguel.webflux.repository.IClientRepo;
import com.miguel.webflux.repository.IDishRepo;
import com.miguel.webflux.repository.IGenericRepo;
import com.miguel.webflux.service.IClientService;
import com.miguel.webflux.service.IDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor // usar cuando se añadira un atributo que no viene de un repositorio | Evita el constructor
public class ClientServiceImpl extends CRUDImpl<Client, String> implements IClientService {

    private final IClientRepo repo;

    @Override
    protected IGenericRepo<Client, String> getRepo() {
        return repo;
    }

}
