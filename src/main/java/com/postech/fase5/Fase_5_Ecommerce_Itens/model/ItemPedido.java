package com.postech.fase5.Fase_5_Ecommerce_Itens.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {
    private UUID idItem;
    private int quantidade;

}
