Categoria(<u>id</u>, <span style="border-bottom: double;">nome</span>)

Subcategoria(<u>id</u>, nome, <span style="text-decoration: overline; border-top: double;">id_categoria</span><span style="position: relative; top: -10px; font-size: 0.8em;">Categoria</span>)

Produto(<u>num</u>, <span style="border-bottom: double;">nome</span>, preco, estoque, descricao, <u><span style="text-decoration: overline; border-top: double;">id_subcategoria</span></u><span style="position: relative; top: -10px; font-size: 0.8em;">Subcategoria</span>)

Cliente(<u>id</u>, pnome, snome, <span style="border-bottom: double;">CPF</span>, sexo, <u style="border-bottom: double;">email</u>, dt_nascimento)

Pedido(<u>num</u>, data_pedido, hora_pedido, <u><span style="text-decoration: overline; border-top: double;">id_cliente</span></u><span style="position: relative; top: -10px; font-size: 0.8em;">Cliente</span>)

Carrinho(qnt_produto, <u><span style="text-decoration: overline; border-top: double;">num_produto</span></u><span style="position: relative; top: -10px; font-size: 0.8em;">Produto</span>, <u><span style="text-decoration: overline; border-top: double;">num_pedido</span></u><span style="position: relative; top: -10px; font-size: 0.8em;">Pedido</span>)
