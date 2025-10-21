package com.arturfrimu.lifemanager.shared.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "openapi")
public class OpenApiProperties {

    private Info info = new Info();
    private Server server = new Server();

    @Data
    public static class Info {
        private String title;
        private String version;
        private String description;
        private Contact contact = new Contact();
        private License license = new License();

        @Data
        public static class Contact {
            private String name;
            private String email;
        }

        @Data
        public static class License {
            private String name;
            private String url;
        }
    }

    @Data
    public static class Server {
        private String url;
        private String description;
    }
}

