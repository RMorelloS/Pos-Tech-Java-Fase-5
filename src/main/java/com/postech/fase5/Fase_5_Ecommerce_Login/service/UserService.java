package com.postech.fase5.Fase_5_Ecommerce_Login.service;

import com.postech.fase5.Fase_5_Ecommerce_Login.model.User;
import com.postech.fase5.Fase_5_Ecommerce_Login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public String criarUsuario(User novoUsuario, boolean permissaoAdmin) {
        if(!permissaoAdmin){
            novoUsuario.setAuthorities("USER");
        }
        return userRepository.criarUsuario(novoUsuario);
    }

    public String atualizarUsuario(User usuarioAtualizar, boolean permissaoAdmin) {

        if(!permissaoAdmin){
            usuarioAtualizar.setAuthorities("USER");
        }
        return userRepository.atualizarUsuario(usuarioAtualizar);
    }
}
