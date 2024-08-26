CREATE DATABASE petstation;

CREATE TABLE categoria(
	id_categoria INT AUTO_INCREMENT PRIMARY KEY,
	nome VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE subcategoria(
	id_subcategoria INT AUTO_INCREMENT PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	id_categoria INT NOT NULL,
	CONSTRAINT fk_categoria_id FOREIGN KEY(id_categoria)
		REFERENCES categoria(id_categoria) ON DELETE CASCADE
);

CREATE TABLE produto( 
    id_produto INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
	descricao VARCHAR(1000),
    estoque INT NOT NULL,
	id_subcategoria INT NOT NULL,
	CONSTRAINT fk_subcategoria_id FOREIGN KEY(id_subcategoria)
		REFERENCES subcategoria(id_subcategoria) ON DELETE CASCADE
);

CREATE TABLE cliente(
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    sexo CHAR(1),
    data_nascimento DATE NOT NULL,
    telefone CHAR(14) NOT NULL UNIQUE,
    email VARCHAR(80) NOT NULL UNIQUE,
    CONSTRAINT ck_cliente_sexo CHECK(sexo in ('M','F')), 
    CONSTRAINT ck_cliente_telefone 
		CHECK (telefone REGEXP '^\\([0-9]{2}\\)9[0-9]{4}-[0-9]{4}$') 
);

CREATE TABLE pedido(
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    data_pedido DATE NOT NULL,
    hora_pedido TIME NOT NULL,
    CONSTRAINT fk_cliente_id FOREIGN KEY(id_cliente)
        REFERENCES cliente(id_cliente) ON DELETE CASCADE
);

CREATE TABLE carrinho(
    qnt_produto INT NOT NULL,
    id_pedido INT NOT NULL,
    id_produto INT NOT NULL,
    CONSTRAINT pk_carrinho PRIMARY KEY(id_pedido, id_produto),
    CONSTRAINT fk_pedido_id FOREIGN KEY(id_pedido)
        REFERENCES pedido(id_pedido) ON DELETE CASCADE,
    CONSTRAINT fk_produto_id FOREIGN KEY(id_produto)
        REFERENCES produto(id_produto) ON DELETE CASCADE,
    CONSTRAINT ck_qnt_produto CHECK (qnt_produto > 0)   
);

INSERT INTO categoria(nome, id_categoria) VALUES ('Cachorro', 1);
INSERT INTO categoria(nome, id_categoria) VALUES ('Gato', 2);
INSERT INTO categoria(nome, id_categoria) VALUES ('Pássaro', 3);
INSERT INTO categoria(nome, id_categoria) VALUES ('Peixe', 4);

INSERT INTO subcategoria(nome, id_categoria) VALUES ('Rações e Pestiscos', 1);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Brinquedos', 1);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Coleiras, guias e peitorais', 1);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Beleza e Limpeza', 1);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Farmácia', 1);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Casas e camas', 1);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Roupas', 1);

INSERT INTO subcategoria(nome, id_categoria) VALUES ('Rações e Pestiscos', 2);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Brinquedos', 2);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Coleiras e guias', 2);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Beleza e Limpeza', 2);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Farmácia', 2);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Casas e camas', 2);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Roupas', 2);

INSERT INTO subcategoria(nome, id_categoria) VALUES ('Alimentação', 3);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Brinquedos', 3);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Beleza e Limpeza', 3);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Farmácia', 3);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Gaiolas e viveiros', 3);

INSERT INTO subcategoria(nome, id_categoria) VALUES ('Alimentação', 4);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Decoração', 4);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Limpeza', 4);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Farmácia', 4);
INSERT INTO subcategoria(nome, id_categoria) VALUES ('Aquários e equipamentos', 4);

SELECT * from categoria;

SELECT * from subcategoria WHERE id_categoria = (2);

UPDATE subcategoria SET nome = 'Roupas e acessórios' WHERE nome = 'Roupas';
UPDATE subcategoria SET nome = 'Rações e petiscos' WHERE nome = 'Rações e Pestiscos';

SET SQL_SAFE_UPDATES = 0;
