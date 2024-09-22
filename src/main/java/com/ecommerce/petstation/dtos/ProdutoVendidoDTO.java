package com.ecommerce.petstation.dtos;

public class ProdutoVendidoDTO {
    private String categoriaNome;
    private String produtoNome;
    private int totalVendido;

    public ProdutoVendidoDTO(String categoriaNome, String produtoNome, int totalVendido) {
        this.categoriaNome = categoriaNome;
        this.produtoNome = produtoNome;
        this.totalVendido = totalVendido;
    }

    // Getters e Setters
    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public int getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(int totalVendido) {
        this.totalVendido = totalVendido;
    }
}