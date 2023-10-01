package com.miguel.webflux.service.Impl;

import com.miguel.webflux.model.Invoice;
import com.miguel.webflux.repository.IInvoiceRepo;
import com.miguel.webflux.repository.IGenericRepo;
import com.miguel.webflux.service.IInvoiceService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor // usar cuando se añadira un atributo que no viene de un repositorio | Evita el constructor
public class InvoiceServiceImpl extends CRUDImpl<Invoice, String> implements IInvoiceService {

    private final IInvoiceRepo iInvoiceRepo;

    @Override
    protected IGenericRepo<Invoice, String> getRepo() {
        return iInvoiceRepo;
    }

    @Override
    public Mono<byte[]> generateReport(String idInvoice) {
        return iInvoiceRepo.findById(idInvoice)
                .map(inv -> {
                    try {
                        Map<String, Object> parameters = new HashMap<>();
                        parameters.put("txt_client", inv.getClient().getFirstName());
                        InputStream stream = getClass().getResourceAsStream("/Facturas.jrxml");
                        JasperReport report = JasperCompileManager.compileReport(stream);
                        JasperPrint print = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(inv.getItems()));
                        return JasperExportManager.exportReportToPdf(print);
                    } catch (Exception e) {
                        Mono.just(new byte[0]);
                    }
                    return new byte[0];
                });

    }
}
