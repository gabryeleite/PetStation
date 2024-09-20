package com.ecommerce.petstation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Permitir origens específicas ou todas ()
        corsConfiguration.addAllowedOrigin("");

        // Permitir métodos HTTP específicos ou todos ()
        corsConfiguration.addAllowedMethod("");

        // Permitir headers específicos ou todos ()
        corsConfiguration.addAllowedHeader("");

        // Definir se os cookies devem ser compartilhados
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Aplicar para todos os endpoints

        return new CorsFilter(source);
    }
}