package com.postech.fase5.Fase_5_Ecommerce_Carrinho.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UserService {

    public Authentication getCurrentUser(){
        return SecurityContextHolder.getContext().getAuthentication();
    }



    public String getTokenRequisicao() {
        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        var token = httpRequest.getHeader("token");
        return token;
    }


}
