package com.miguel.webflux.service.Impl;

import com.miguel.webflux.model.Invoice;
import com.miguel.webflux.repository.IInvoiceRepo;
import com.miguel.webflux.repository.IGenericRepo;
import com.miguel.webflux.service.IInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor // usar cuando se añadira un atributo que no viene de un repositorio | Evita el constructor
public class InvoiceServiceImpl extends CRUDImpl<Invoice, String> implements IInvoiceService {

    private final IInvoiceRepo repo;

    @Override
    protected IGenericRepo<Invoice, String> getRepo() {
        return repo;
    }

}
