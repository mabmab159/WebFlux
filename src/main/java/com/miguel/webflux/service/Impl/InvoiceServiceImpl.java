package com.miguel.webflux.service.Impl;

import com.miguel.webflux.model.Invoice;
import com.miguel.webflux.model.InvoiceDetail;
import com.miguel.webflux.repository.IClientRepo;
import com.miguel.webflux.repository.IDishRepo;
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
import java.util.List;
import java.util.Map;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor // usar cuando se añadira un atributo que no viene de un repositorio | Evita el constructor
public class InvoiceServiceImpl extends CRUDImpl<Invoice, String> implements IInvoiceService {

    private final IInvoiceRepo iInvoiceRepo;

    private final IClientRepo iClientRepo;

    private final IDishRepo iDishRepo;

    @Override
    protected IGenericRepo<Invoice, String> getRepo() {
        return iInvoiceRepo;
    }

    private Mono<Invoice> populateClient(Invoice invoice) {
        return iClientRepo.findById(invoice.getClient().getId())
                .map(client -> {
                    invoice.setClient(client);
                    return invoice;
                });
    }

    private Mono<Invoice> populateItems(Invoice invoice) {
        List<Mono<InvoiceDetail>> list = invoice.getItems().stream()
                .map(item -> iDishRepo.findById(item.getDish().getId())
                        .map(dish -> {
                            item.setDish(dish);
                            return item;
                        })
                ).toList();
        return Mono.when(list).then(Mono.just(invoice));
    }

    public byte[] generatePdfReport(Invoice invoice) {
        try (InputStream stream = getClass().getResourceAsStream("/Facturas.jrxml")) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("txt_client", invoice.getClient().getFirstName());
            JasperReport report = JasperCompileManager.compileReport(stream);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(invoice.getItems()));
            return JasperExportManager.exportReportToPdf(print);
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    @Override
    public Mono<byte[]> generateReport(String idInvoice) {
        return iInvoiceRepo.findById(idInvoice)
                .flatMap(this::populateClient)
                .flatMap(this::populateItems)
                .map(this::generatePdfReport)
                .onErrorResume(e -> Mono.empty());
    }

    /*
    @Override
    public Mono<byte[]> generateReport(String idInvoice) {
        return iInvoiceRepo.findById(idInvoice)
                //Obtener el cliente
                .flatMap(inv -> Mono.just(inv)
                        .zipWith(iClientRepo.findById(inv.getClient().getId()), (in, cl) -> {
                            in.setClient(cl);
                            return in;
                        }))
                //Obteniendo los dish
                .flatMap(inv -> Flux.fromIterable(inv.getItems())
                        .flatMap(item -> iDishRepo.findById(item.getDish().getId())
                                .map(d -> {
                                    item.setDish(d);
                                    return item;
                                }))
                        .collectList().flatMap(list -> {
                            inv.setItems(list);
                            return Mono.just(inv);
                        }))
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
    */
}
