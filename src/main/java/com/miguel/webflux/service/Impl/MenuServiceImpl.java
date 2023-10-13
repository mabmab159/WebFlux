package com.miguel.webflux.service.Impl;

import com.miguel.webflux.model.Menu;
import com.miguel.webflux.repository.IGenericRepo;
import com.miguel.webflux.repository.IMenuRepo;
import com.miguel.webflux.service.IMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor

public class MenuServiceImpl extends CRUDImpl<Menu, String> implements IMenuService {

    private final IMenuRepo menuRepo;

    @Override
    public Flux<Menu> getMenus(String[] roles) {
        return menuRepo.getMenus(roles);
    }

    @Override
    protected IGenericRepo<Menu, String> getRepo() {
        return menuRepo;
    }
}
