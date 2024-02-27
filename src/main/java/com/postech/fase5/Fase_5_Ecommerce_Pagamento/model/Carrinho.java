package com.postech.fase5.Fase_5_Ecommerce_Pagamento.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrinho {
    private List<ItemPedido> itensCarrinho;
    private Float totalCarrinho;
    private String userLogin;
    private FormaPagamento formaPagamento;
}
