package com.postech.fase5.Fase_5_Ecommerce_Carrinho.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.fase5.Fase_5_Ecommerce_Carrinho.model.ApplicationProperties;
import com.postech.fase5.Fase_5_Ecommerce_Carrinho.model.Carrinho;
import com.postech.fase5.Fase_5_Ecommerce_Carrinho.model.ItemPedido;
import com.postech.fase5.Fase_5_Ecommerce_Carrinho.repository.CarrinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class CarrinhoService {

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private UserService userService;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public Carrinho adicionarItem(ItemPedido itemPedido) throws Exception {
        try {
            validaQtdeItem(itemPedido, userService.getTokenRequisicao());
            Float precoItem = obterPrecoItem(itemPedido.getIdItem());
            Carrinho carrinho = carrinhoRepository.obterCarrinho();
            var precoPedido = precoItem * itemPedido.getQuantidade();
            reqAtualizaQuantidade(applicationProperties.getUrlItens() + "gestaoItens/atualizarQuantidade/true", itemPedido);
            if(carrinho != null){
                var itensCarrinho = carrinho.getItensCarrinho();
                if(itensCarrinho == null) itensCarrinho = new ArrayList<>();
                var itemExistente = carrinho.getItensCarrinho().stream().filter(filter -> {return filter.getIdItem().toString().equals(itemPedido.getIdItem().toString());}).findFirst();
                if(itemExistente.isPresent()){
                    itensCarrinho.remove(itemExistente);
                    itemExistente.get().setQuantidade(itemExistente.get().getQuantidade() + itemPedido.getQuantidade());
                }else {
                    itensCarrinho.add(new ItemPedido(itemPedido.getIdItem(), itemPedido.getQuantidade()));
                }
                carrinho.setItensCarrinho(itensCarrinho);
                carrinho.setTotalCarrinho(carrinho.getTotalCarrinho() + precoPedido);
                return carrinhoRepository.atualizarCarrinho(carrinho);
            }else{
                var itens = new ArrayList<ItemPedido>();
                itens.add(new ItemPedido(itemPedido.getIdItem(), itemPedido.getQuantidade()));
                carrinho = new Carrinho();
                carrinho.setItensCarrinho(itens);
                carrinho.setUserLogin(userService.getCurrentUser().getName());
                carrinho.setTotalCarrinho(precoPedido);
                return carrinhoRepository.criarCarrinho(carrinho);
            }


        }catch(Exception e){
            throw e;
        }
    }



    private Float obterPrecoItem(UUID itemUUID) throws IOException {
        String precoStr = getRequest(applicationProperties.getUrlItens() + "gestaoItens/preco/" + itemUUID);
        return Float.parseFloat(precoStr);
    }


    private String getRequest(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("token", userService.getTokenRequisicao());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        connection.disconnect();
        return response.toString();
    }

    private String reqAtualizaQuantidade(String urlString, ItemPedido pedido) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("token", userService.getTokenRequisicao());
        connection.setDoOutput(true);

        // Serializa o objeto ItemPedido para JSON
        String jsonPedido = objectMapper.writeValueAsString(pedido);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPedido.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            connection.disconnect();
        }

    }

    private void validaQtdeItem(ItemPedido itemPedido, String token) throws Exception {
        try {
            String quantidadeStr = getRequest(applicationProperties.getUrlItens() + "gestaoItens/quantidade/" + itemPedido.getIdItem());

            if(itemPedido.getQuantidade() > Integer.parseInt(quantidadeStr)){
                throw new Exception("Quantidade solicitada superior à quantidade em estoque!");
            }

        }catch(Exception e){
            throw e;
        }
    }

    public Carrinho removerItem(UUID itemUUID) throws Exception {
        Carrinho carrinho = carrinhoRepository.obterCarrinho();

        if(carrinho == null){
            throw new Exception("Não existe carrinho para o usuário logado!");
        }
        if(carrinho.getItensCarrinho().stream().count() == 0){
            throw new Exception("Não existem itens no carrinho!");
        }
        var itemPedido = carrinho.getItensCarrinho().stream().filter(filter -> {return filter.getIdItem().toString().equals(itemUUID.toString());}).findFirst();
        if(itemPedido.isEmpty()){
            throw new Exception("Item não consta no carrinho do usuário, impossivel remover!");
        }


        var listaItens = carrinho.getItensCarrinho();
        listaItens.remove(itemPedido.get());
        var precoItem = obterPrecoItem(itemUUID);
        carrinho.setItensCarrinho(listaItens);
        carrinho.setTotalCarrinho(carrinho.getTotalCarrinho() - precoItem*itemPedido.get().getQuantidade());
        carrinhoRepository.atualizarCarrinho(carrinho);


        reqAtualizaQuantidade(applicationProperties.getUrlItens() + "gestaoItens/atualizarQuantidade/false", itemPedido.get());
        return carrinho;

    }

    public Carrinho visualizarCarrinho() throws JsonProcessingException {
        var carrinho = carrinhoRepository.obterCarrinho();
        return carrinho;
    }

    public String limparCarrinho() throws Exception {
        Carrinho carrinho = carrinhoRepository.obterCarrinho();

        if(carrinho == null){
            throw new Exception("Não existe carrinho para o usuário logado!");
        }
        if(carrinho.getItensCarrinho().stream().count() == 0){
            throw new Exception("Não existem itens no carrinho!");
        }

        return carrinhoRepository.limparCarrinho();

    }
}
