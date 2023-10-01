package com.miguel.webflux.service;

import com.miguel.webflux.model.Invoice;
import reactor.core.publisher.Mono;

public interface IInvoiceService extends ICRUD<Invoice, String> {

    Mono<byte[]> generateReport(String idInvoice);
}
