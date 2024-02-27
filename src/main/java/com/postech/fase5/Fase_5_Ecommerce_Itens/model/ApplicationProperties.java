package com.postech.fase5.Fase_5_Ecommerce_Itens.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "appconfig")
@Getter
@Setter
public class ApplicationProperties {
    private String nomeTabela;
    private String jwtSecret;
    private String nomeTabelaLogin;
}