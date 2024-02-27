package com.postech.fase5.Fase_5_Ecommerce_Pagamento.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fatura {
    private List<Item> itensCarrinho;
    private float totalCarrinho;
    private String userLogin;
    private FormaPagamento formaPagamento;

}
