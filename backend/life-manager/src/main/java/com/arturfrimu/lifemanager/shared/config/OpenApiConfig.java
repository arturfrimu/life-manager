package com.arturfrimu.lifemanager.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    private final OpenApiProperties properties;

    @Bean
    public OpenAPI lifeManagerOpenAPI() {
        Server server = new Server();
        server.setUrl(properties.getServer().getUrl());
        server.setDescription(properties.getServer().getDescription());

        Contact contact = new Contact();
        contact.setName(properties.getInfo().getContact().getName());
        contact.setEmail(properties.getInfo().getContact().getEmail());

        License license = new License()
                .name(properties.getInfo().getLicense().getName())
                .url(properties.getInfo().getLicense().getUrl());

        Info info = new Info()
                .title(properties.getInfo().getTitle())
                .version(properties.getInfo().getVersion())
                .description(properties.getInfo().getDescription())
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}