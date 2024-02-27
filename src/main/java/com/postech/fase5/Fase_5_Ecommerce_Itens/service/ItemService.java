package com.postech.fase5.Fase_5_Ecommerce_Itens.service;

import com.postech.fase5.Fase_5_Ecommerce_Itens.model.Item;
import com.postech.fase5.Fase_5_Ecommerce_Itens.model.ItemPedido;
import com.postech.fase5.Fase_5_Ecommerce_Itens.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Validated
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    public String criarItem(@Valid Item item){
        validateItem(item);
        item.setItemUUID(UUID.randomUUID());
        return itemRepository.criarItem(item);
    }

    public Item obterItemPorId(@NotBlank UUID idItem) throws Exception {
        return itemRepository.obterItemPorId(idItem);
    }

    public Integer obterQtdeItemPorId(@NotBlank UUID idItem) throws Exception {
        return itemRepository.obterQtdeItemPorId(idItem);
    }

    public List<Item> obterItens() {
        return itemRepository.obterItens();
    }

    public String deletarItem(@NotBlank UUID idItem) {
        return itemRepository.deletarItem(idItem);
    }

    public String atualizarItem(@NotBlank UUID item, @Valid Item itemAtualizar) {
        validateItem(itemAtualizar);
        return itemRepository.atualizarItem(item, itemAtualizar);
    }

    private void validateItem(Item item) {
        Objects.requireNonNull(item, "Item não pode ser nulo.");

        Objects.requireNonNull(item.getPrecoItem(), "O campo 'precoItem' não pode ser nulo.");
        if (item.getPrecoItem() <= 0) {
            throw new IllegalArgumentException("O campo 'precoItem' deve ser maior que zero.");
        }
        Objects.requireNonNull(item.getNomeItem(), "Nome do item não pode ser nulo.");
        Objects.requireNonNull(item.getMarcaItem(), "Marca do item não pode ser nulo.");
        Objects.requireNonNull(item.getDescricao(), "Descrição do item não pode ser nulo.");
        Objects.requireNonNull(item.getQuantidadeEstoque(), "Quantidade em estoque do item não pode ser nulo.");
        if (item.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("O campo 'quantidade em estoque' do item deve ser maior que zero.");
        }

    }


    public String obterPrecoItemPorId(UUID idItem) throws Exception {
        return itemRepository.obterPrecoItemPorId(idItem);

    }

    public String atualizarQuantidade(boolean flagQtde, ItemPedido itemPedido) throws Exception {
        if(itemPedido.getQuantidade() <= 0){
            return "Quantidade não pode ser igual ou menor a zero";
        }
        var item = itemRepository.obterItem(itemPedido.getIdItem());
        if(itemPedido.getQuantidade() > item.getQuantidadeEstoque() && flagQtde){
            return "Quantidade solicitada superior à quantidade em estoque";
        }
        if(flagQtde)
            item.setQuantidadeEstoque(item.getQuantidadeEstoque() - itemPedido.getQuantidade());
        else
            item.setQuantidadeEstoque(item.getQuantidadeEstoque() + itemPedido.getQuantidade());

        atualizarItem(item.getItemUUID(), item);
        return item.toString();
    }
}
