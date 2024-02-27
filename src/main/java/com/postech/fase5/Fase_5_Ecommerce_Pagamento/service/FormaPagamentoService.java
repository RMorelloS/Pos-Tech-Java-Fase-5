package com.postech.fase5.Fase_5_Ecommerce_Pagamento.service;

import com.postech.fase5.Fase_5_Ecommerce_Pagamento.model.FormaPagamento;
import com.postech.fase5.Fase_5_Ecommerce_Pagamento.repository.FormaPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormaPagamentoService {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    public FormaPagamento obterFormaPagamento(String apelido) throws Exception {
        return formaPagamentoRepository.obterFormaPagamento(apelido);
    }

    public FormaPagamento cadastrarFormaPagamento(FormaPagamento formaPagamento) {
        return formaPagamentoRepository.cadastrarFormaPagamento(formaPagamento);
    }

    public FormaPagamento atualizarFormaPagamento(String apelido, FormaPagamento formaPagamento) {
        return formaPagamentoRepository.atualizarFormaPagamento(apelido, formaPagamento);
    }

    public String deletarFormaPagamento(String apelido) {
        return formaPagamentoRepository.deletarFormaPagamento(apelido);
    }
}
