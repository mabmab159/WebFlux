package com.miguel.webflux.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.miguel.webflux.dto.ClientDTO;
import com.miguel.webflux.model.Client;
import com.miguel.webflux.service.IClientService;
import lombok.RequiredArgsConstructor;
import org.cloudinary.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.Map;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final IClientService service;
    @Qualifier("clientMapper")
    private final ModelMapper mapper;
    private final Cloudinary cloudinary;

    @GetMapping
    public Mono<ResponseEntity<Flux<ClientDTO>>> findAll() {
        Flux<ClientDTO> fx = service.findAll().map(this::convertToDto);
        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fx))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ClientDTO>> findById(@PathVariable("id") String id) {
        return service.findById(id).map(this::convertToDto)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> save(@RequestBody ClientDTO dto, final ServerHttpRequest request) {
        return service.save(convertToEntity(dto))
                .map(e -> ResponseEntity.created(
                                URI.create(
                                        request.getURI()
                                                .toString()
                                                .concat("/")
                                                .concat(e.getId())
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ClientDTO>> update(@RequestBody ClientDTO dto, @PathVariable("id") String id) {
        return Mono.just(dto)
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(e -> service.update(convertToEntity(e), id))
                .map(this::convertToDto)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable("id") String id) {
        return service.delete(id)
                .flatMap(e -> e ? Mono.just(ResponseEntity.noContent().build()) : Mono.just(ResponseEntity.notFound().build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/v1/upload/{id}")
    public Mono<ResponseEntity<ClientDTO>> uploadV1(@PathVariable("id") String id, @RequestPart("file") FilePart filePart) throws Exception {
        File f = Files.createTempFile("temp", filePart.filename()).toFile();
        return filePart.transferTo(f).then(service.findById(id).flatMap(c -> {
            Map response;
            try {
                response = cloudinary.uploader().upload(f, ObjectUtils.asMap("resource_type", "auto"));
                JSONObject json = new JSONObject(response);
                String url = json.getString("url");
                c.setUrlPhoto(url);
                return service.update(c, id)
                        .map(this::convertToDto)
                        .map(e -> ResponseEntity.ok().body(e));
            } catch (IOException e) {
                return Mono.error(new RuntimeException(e));
            }
        })).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/v2/upload/{id}")
    public Mono<ResponseEntity<ClientDTO>> uploadV2(@PathVariable("id") String id, @RequestPart("file") FilePart filePart) throws Exception {
        return service.findById(id).flatMap(
                c -> {
                    try {
                        File f = Files.createTempFile("temp", filePart.filename()).toFile();
                        filePart.transferTo(f).block();
                        Map response = cloudinary.uploader().upload(f, ObjectUtils.asMap("resource_type", "auto"));
                        JSONObject json = new JSONObject(response);
                        String url = json.getString("url");
                        c.setUrlPhoto(url);
                        return service.update(c, id)
                                .map(this::convertToDto)
                                .map(e -> ResponseEntity.ok().body(e));
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                });
    }

    private ClientDTO convertToDto(Client model) {
        return mapper.map(model, ClientDTO.class);
    }

    private Client convertToEntity(ClientDTO dto) {
        return mapper.map(dto, Client.class);
    }
}
