package com.algaworks.algafood.domain.model;

import java.util.Arrays;
import java.util.List;

public enum StatusPedido {

    CRIADO("Criado"),
    CONFIRMADO("Confirmado", CRIADO),
    ENTREGUE("Entregue", CONFIRMADO),
    CANCELADO("Cancelado", CRIADO);
    
    private String descricao;
    private List<StatusPedido> statusAnteriores;

    StatusPedido(String descricao, StatusPedido... statusAnterioes) {
    	this.descricao = descricao;
    	this.statusAnteriores = Arrays.asList(statusAnterioes);
    }
    
    public String getDescricao() {
    	return this.descricao;
    }
    
    public boolean naoPodeAlterarPara(StatusPedido novoStatus) {
    	return !novoStatus.statusAnteriores.contains(this);
    }
    
}    