package com.postech.fase5.Fase_5_Ecommerce_Pagamento.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.fase5.Fase_5_Ecommerce_Pagamento.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class PagamentoService {

    private PagamentoService(){
         objectMapper = new ObjectMapper();
    }
    @Autowired
    private UserService userService;

    @Autowired
    private FormaPagamentoService formaPagamentoService;

    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private RequestService requestService;


    public Fatura visualizarCarrinho(String apelidoFormaPagamento) throws Exception {

        var fatura = new Fatura();

        var carrinhoStr = requestService.getRequest(applicationProperties.getUrlCarrinho() + "/visualizarCarrinho");
        Carrinho carrinho = objectMapper.readValue(carrinhoStr, new TypeReference<Carrinho>() {});

        fatura.setItensCarrinho(montaListaItens(carrinho));
        fatura.setTotalCarrinho(carrinho.getTotalCarrinho());
        fatura.setUserLogin(userService.getCurrentUser().getName());
        fatura.setFormaPagamento(formaPagamentoService.obterFormaPagamento(apelidoFormaPagamento));

        return fatura;
    }
    public List<Item> montaListaItens(Carrinho carrinho) throws IOException {
        List<Item> listaItens = new ArrayList<Item>();
        for (var itemPedido : carrinho.getItensCarrinho()){
            var itemStr = requestService.getRequest(applicationProperties.getUrlItem() + itemPedido.getIdItem());
            var item = objectMapper.readValue(itemStr, new TypeReference<Item>() {});
            listaItens.add(item);
        }
        return listaItens;

    }

    public String pagarFatura(String apelidoFormaPagamento) throws Exception {
        var carrinhoStr = requestService.postRequest(applicationProperties.getUrlCarrinho() + "/limparCarrinho", apelidoFormaPagamento);
        return carrinhoStr;
    }
}
