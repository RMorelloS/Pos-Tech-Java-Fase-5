package com.postech.fase5.Fase_5_Ecommerce_Pagamento.model;

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
    private String urlCarrinho;
    private String urlItem;
}