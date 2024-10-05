package com.ecommerce.petstation.daos;

import java.sql.SQLException;
import java.util.List;

import com.ecommerce.petstation.dtos.PedidoProdutoResumoDTO;
import com.ecommerce.petstation.dtos.PedidoProdutoDTO;
import com.ecommerce.petstation.models.PedidoProduto;

public interface PedidoProdutoDAO extends DAO<PedidoProduto> {
    List<PedidoProdutoDTO> filtrarVendas() throws SQLException;

    List<PedidoProdutoResumoDTO> resumoVendas() throws SQLException;
}
