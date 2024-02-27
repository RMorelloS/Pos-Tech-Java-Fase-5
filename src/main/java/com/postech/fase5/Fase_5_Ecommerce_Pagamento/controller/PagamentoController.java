package com.postech.fase5.Fase_5_Ecommerce_Pagamento.controller;

import com.postech.fase5.Fase_5_Ecommerce_Pagamento.model.ApplicationProperties;
import com.postech.fase5.Fase_5_Ecommerce_Pagamento.model.Carrinho;
import com.postech.fase5.Fase_5_Ecommerce_Pagamento.service.PagamentoService;
import com.postech.fase5.Fase_5_Ecommerce_Pagamento.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/pagamento/")
public class PagamentoController {


    @Autowired
    private UserService userService;



    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping("/visualizarFatura/{apelidoFormaPagamento}")
    private ResponseEntity<?> visualizarCarrinho(@PathVariable String apelidoFormaPagamento) throws IOException {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(pagamentoService.visualizarCarrinho(apelidoFormaPagamento));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }


    @PostMapping("/pagarFatura/{apelidoFormaPagamento}")
    private ResponseEntity<?> pagarFatura(@PathVariable String apelidoFormaPagamento) throws IOException {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(pagamentoService.pagarFatura(apelidoFormaPagamento));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}
