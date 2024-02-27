package com.postech.fase5.Fase_5_Ecommerce_Pagamento.controller;

import com.postech.fase5.Fase_5_Ecommerce_Pagamento.model.FormaPagamento;
import com.postech.fase5.Fase_5_Ecommerce_Pagamento.service.FormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/formaPagamento/")
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoService formaPagamentoService;
    @GetMapping("/{apelido}")
    public ResponseEntity<?> obterFormasPagamento(@PathVariable String apelido){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(formaPagamentoService.obterFormaPagamento(apelido));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrarFormaPagamento(@RequestBody FormaPagamento formaPagamento){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(formaPagamentoService.cadastrarFormaPagamento(formaPagamento));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{apelido}")
    public ResponseEntity<?> atualizarFormaPagamento(@RequestBody FormaPagamento formaPagamento,
                                                     @PathVariable String apelido){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(formaPagamentoService.atualizarFormaPagamento(apelido, formaPagamento));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{apelido}")
    public ResponseEntity<?> atualizarFormaPagamento(@PathVariable String apelido){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(formaPagamentoService.deletarFormaPagamento(apelido));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

