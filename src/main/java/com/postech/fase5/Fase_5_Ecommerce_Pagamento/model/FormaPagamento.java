package com.postech.fase5.Fase_5_Ecommerce_Pagamento.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormaPagamento {
    private String userLogin;
    private String apelido;
    private String numeroCartao;
    private int CVV;
    private String dataVencimento;




    public static FormaPagamento fromMap(Map<String, AttributeValue> attributeMap) {
        FormaPagamento formaPagamento = new FormaPagamento();

        if (attributeMap.containsKey("userLogin")) {
            formaPagamento.setUserLogin(attributeMap.get("userLogin").s());
        }

        if (attributeMap.containsKey("apelido")) {
            formaPagamento.setApelido(attributeMap.get("apelido").s());
        }

        if (attributeMap.containsKey("CVV")) {
            formaPagamento.setCVV(Integer.parseInt((attributeMap.get("CVV").s())));
        }
        if (attributeMap.containsKey("numeroCartao")) {
            formaPagamento.setNumeroCartao(attributeMap.get("numeroCartao").s());
        }

        if (attributeMap.containsKey("dataVencimento")) {
            formaPagamento.setDataVencimento(attributeMap.get("dataVencimento").s());
        }

        return formaPagamento;
    }


}
