package com.miguel.webflux.config;

import com.miguel.webflux.dto.ClientDTO;
import com.miguel.webflux.model.Client;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class MapperConfig {

    @Bean("defaultMapper")
    public ModelMapper defaultMapper() {
        return new ModelMapper();
    }

    @Bean("clientMapper")
    public ModelMapper clientMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        //Lectura
        TypeMap<Client, ClientDTO> typeMap2 = mapper.createTypeMap(Client.class, ClientDTO.class);
        typeMap2.addMapping(Client::getFirstName, (destination, value) -> destination.setNameClient((String) value));
        typeMap2.addMapping(Client::getLastName, (destination, value) -> destination.setSurnameClient((String) value));
        typeMap2.addMapping(Client::getBirthDate, (destination, value) -> destination.setBirthDateClient((LocalDate) value));
        typeMap2.addMapping(Client::getUrlPhoto, (destination, value) -> destination.setUrlPhotoClient((String) value));

        //Escritura
        TypeMap<ClientDTO, Client> typeMap1 = mapper.createTypeMap(ClientDTO.class, Client.class);
        typeMap1.addMapping(ClientDTO::getNameClient, ((destination, value) -> destination.setFirstName((String) value)));
        typeMap1.addMapping(ClientDTO::getSurnameClient, ((destination, value) -> destination.setLastName((String) value)));
        typeMap1.addMapping(ClientDTO::getUrlPhotoClient, ((destination, value) -> destination.setUrlPhoto((String) value)));

        return mapper;
    }
}
