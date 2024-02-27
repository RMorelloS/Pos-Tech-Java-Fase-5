package com.postech.fase5.Fase_5_Ecommerce_Pagamento;

import com.postech.fase5.Fase_5_Ecommerce_Pagamento.model.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class Fase5EcommercePagamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(Fase5EcommercePagamentoApplication.class, args);
	}

}
