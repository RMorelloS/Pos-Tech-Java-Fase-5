package com.postech.fase5.Fase_5_Ecommerce_Login.controller;

import com.postech.fase5.Fase_5_Ecommerce_Login.model.User;
import com.postech.fase5.Fase_5_Ecommerce_Login.repository.UserRepository;
import com.postech.fase5.Fase_5_Ecommerce_Login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/criarUsuario")
    private ResponseEntity<String> criarUsuario(@RequestBody User novoUsuario){
        try {
            novoUsuario.setAuthorities("USER");
            return ResponseEntity.status(HttpStatus.OK).body(userService.criarUsuario(novoUsuario, false));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/criarUsuarioAdmin")
    private ResponseEntity<String> criarUsuarioAdmin(@RequestBody User novoUsuario){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if(!userDetails.getAuthorities().stream().findFirst().isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário sem permissões atribuídas");
            }
            if(!userDetails.getAuthorities().stream().findFirst().get().toString().equals("ADMIN")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário sem permissão de administrador");
            }
            return ResponseEntity.status(HttpStatus.OK).body(userService.criarUsuario(novoUsuario, true));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/atualizarUsuario")
    private ResponseEntity<String> atualizarUsuario(@RequestBody User usuarioAtualizar){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.atualizarUsuario(usuarioAtualizar, false));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/atualizarUsuarioAdmin")
    private ResponseEntity<String> atualizarUsuarioAdmin(@RequestBody User usuarioAtualizar){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if(!userDetails.getAuthorities().stream().findFirst().isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário sem permissões atribuídas");
            }
            if(!userDetails.getAuthorities().stream().findFirst().get().toString().equals("ADMIN")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário sem permissão de administrador");
            }
            return ResponseEntity.status(HttpStatus.OK).body(userService.atualizarUsuario(usuarioAtualizar, true));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
