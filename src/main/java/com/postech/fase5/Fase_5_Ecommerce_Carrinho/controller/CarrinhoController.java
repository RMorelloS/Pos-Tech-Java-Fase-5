package com.postech.fase5.Fase_5_Ecommerce_Carrinho.controller;

import com.postech.fase5.Fase_5_Ecommerce_Carrinho.model.Carrinho;
import com.postech.fase5.Fase_5_Ecommerce_Carrinho.model.ItemPedido;
import com.postech.fase5.Fase_5_Ecommerce_Carrinho.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @PostMapping("/adicionarItem")
    public ResponseEntity<?> adicionarItem(@RequestBody ItemPedido itemPedido){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(carrinhoService.adicionarItem(itemPedido));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/removerItem/{itemUUID}")
    public ResponseEntity<?> removerItem(@PathVariable UUID itemUUID){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(carrinhoService.removerItem(itemUUID));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/visualizarCarrinho")
    public ResponseEntity<?> visualizarCarrinho(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(carrinhoService.visualizarCarrinho());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/limparCarrinho")
    public ResponseEntity<?> limparCarrinho(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(carrinhoService.limparCarrinho());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



}
