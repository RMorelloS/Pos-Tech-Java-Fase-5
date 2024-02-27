package com.postech.fase5.Fase_5_Ecommerce_Itens.controller;

import com.postech.fase5.Fase_5_Ecommerce_Itens.model.Item;
import com.postech.fase5.Fase_5_Ecommerce_Itens.model.ItemPedido;
import com.postech.fase5.Fase_5_Ecommerce_Itens.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/gestaoItens")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<Item> obterItens(){
        return itemService.obterItens();
    }

    @GetMapping("/{idItem}")
    public ResponseEntity<?> obterItemPorId(@PathVariable UUID idItem){
        try {
            var item = ResponseEntity.status(HttpStatus.OK).body(itemService.obterItemPorId(idItem));
            return item;
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/quantidade/{idItem}")
    public ResponseEntity<String> obterQtdeItemPorId(@PathVariable UUID idItem){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(itemService.obterQtdeItemPorId(idItem).toString());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/preco/{idItem}")
    public ResponseEntity<String> obterPrecoItemPorId(@PathVariable UUID idItem){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(itemService.obterPrecoItemPorId(idItem).toString());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<String> criarItem(@RequestBody Item novoItem){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(itemService.criarItem(novoItem).toString());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @DeleteMapping("/{idItem}")
    public ResponseEntity<String> deletarItem(@PathVariable UUID idItem){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(itemService.deletarItem(idItem).toString());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{idItem}")
    public ResponseEntity<String> atualizarItem(@PathVariable UUID idItem, @RequestBody Item itemAtualizar){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(itemService.atualizarItem(idItem, itemAtualizar).toString());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/atualizarQuantidade/{flagQtde}")
    public ResponseEntity<String> atualizarQuantidade(@PathVariable boolean flagQtde, @RequestBody ItemPedido itemPedido){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(itemService.atualizarQuantidade(flagQtde, itemPedido));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
