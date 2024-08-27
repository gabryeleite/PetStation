CREATE SCHEMA petstation;

CREATE SEQUENCE petstation.categoria_id_seq
	START 1 INCREMENT 1;

CREATE TABLE petstation.categoria(
	id_categoria INT DEFAULT nextval('petstation.categoria_id_seq'),
	nome VARCHAR(50) NOT NULL,
	CONSTRAINT pk_categoria PRIMARY KEY(id_categoria),
	CONSTRAINT uk_categoria_nome UNIQUE(nome)
);

CREATE SEQUENCE petstation.subcategoria_id_seq
	START 1 INCREMENT 1;

CREATE TABLE petstation.subcategoria(
	id_subcategoria INT DEFAULT nextval('petstation.subcategoria_id_seq'),
	nome VARCHAR(50) NOT NULL,
	id_categoria INT,
	CONSTRAINT pk_subcategoria PRIMARY KEY(id_subcategoria),
	CONSTRAINT fk_categoria_id FOREIGN KEY(id_categoria)
		REFERENCES petstation.categoria(id_categoria) ON DELETE CASCADE
);

CREATE SEQUENCE petstation.produto_num_seq
	START 1 INCREMENT 1;

CREATE TABLE petstation.produto( 
    num INT DEFAULT nextval('petstation.produto_num_seq'),
    nome VARCHAR(50) NOT NULL,
    preco NUMERIC(10,2) NOT NULL,
	descricao VARCHAR(1000),
    estoque INT NOT NULL,
	id_subcategoria INT NOT NULL,
	CONSTRAINT pk_produto PRIMARY KEY(num),
	CONSTRAINT fk_subcategoria_id FOREIGN KEY(id_subcategoria)
		REFERENCES petstation.subcategoria(id_subcategoria) ON DELETE CASCADE
);

CREATE SEQUENCE petstation.cliente_id_seq
	START 1 INCREMENT 1;

CREATE TABLE petstation.cliente(
    id_cliente INT DEFAULT nextval('petstation.cliente_id_seq'),
    nome VARCHAR(50) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    sexo CHAR(1),
    data_nascimento DATE NOT NULL,
    telefone CHAR(14) NOT NULL,
    email VARCHAR(80) NOT NULL,
    CONSTRAINT pk_cliente  PRIMARY KEY(id_cliente),
    CONSTRAINT uk_cliente_telefone UNIQUE(telefone),
    CONSTRAINT uk_cliente_email UNIQUE(email),
    CONSTRAINT ck_cliente_sexo CHECK(sexo in ('M','F')), 
    CONSTRAINT ck_cliente_telefone 
		CHECK (telefone ~ '^\([0-9]{2}\)9[0-9]{4}-[0-9]{4}$') 
);

CREATE SEQUENCE petstation.pedido_num_seq
	START 1 INCREMENT 1;

CREATE TABLE petstation.pedido(
    num INT DEFAULT nextval('petstation.pedido_num_seq'),
    id_cliente INT NOT NULL,
    data_pedido DATE NOT NULL,
    hora_pedido TIME NOT NULL,
	status_pedido VARCHAR(20),
    CONSTRAINT pk_pedido PRIMARY KEY(num),
    CONSTRAINT fk_cliente_id FOREIGN KEY(id_cliente)
        REFERENCES petstation.cliente(id_cliente) ON DELETE CASCADE
);

CREATE TABLE petstation.carrinho(
    qnt_produto INT NOT NULL,
    num_pedido INT NOT NULL,
    num_produto INT NOT NULL,
    CONSTRAINT pk_carrinho PRIMARY KEY(num_pedido, num_produto),
    CONSTRAINT fk_pedido_num FOREIGN KEY(num_pedido)
        REFERENCES petstation.pedido(num) ON DELETE CASCADE,
    CONSTRAINT fk_produto_num FOREIGN KEY(num_produto)
        REFERENCES petstation.produto(num) ON DELETE CASCADE,
    CONSTRAINT ck_qnt_produto CHECK (qnt_produto > 0)   
);

-- Carregando Dados 

-- * Categorias e Subcategorias *

INSERT INTO petstation.categoria(nome, id_categoria) VALUES ('Cachorro', 1);
INSERT INTO petstation.categoria(nome, id_categoria) VALUES ('Gato', 2);
INSERT INTO petstation.categoria(nome, id_categoria) VALUES ('Pássaro', 3);
INSERT INTO petstation.categoria(nome, id_categoria) VALUES ('Peixe', 4);

SELECT * from petstation.categoria;

INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Rações e petiscos', 1);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Brinquedos', 1);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Coleiras, guias e peitorais', 1);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Beleza e limpeza', 1);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Farmácia', 1);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Casas e camas', 1);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Roupas', 1);

INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Rações e petiscos', 2);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Brinquedos', 2);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Coleiras e guias', 2);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Beleza e limpeza', 2);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Farmácia', 2);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Casas e camas', 2);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Roupas', 2);

INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Alimentação', 3);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Brinquedos', 3);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Beleza e limpeza', 3);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Farmácia', 3);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Gaiolas e viveiros', 3);

INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Alimentação', 4);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Decoração', 4);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Limpeza', 4);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Farmácia', 4);
INSERT INTO petstation.subcategoria(nome, id_categoria) VALUES ('Aquários e equipamentos', 4);

SELECT * from petstation.subcategoria WHERE id_categoria IN (1, 2);

UPDATE petstation.subcategoria SET nome = 'Roupas e acessórios' WHERE nome = 'Roupas';

-- * Cadastro de Produtos *

INSERT INTO petstation.produto (nome, preco, descricao, estoque, id_subcategoria) 
VALUES ('Ração Golden para Cães Adultos - 15Kg', 159.90, 
'Ração tipo seca, sabor frango, para grande e médio porte', 100, 
        (SELECT id_subcategoria FROM petstation.subcategoria 
         WHERE nome = 'Rações e petiscos' AND id_categoria = (
		 SELECT id_categoria FROM petstation.categoria 
		 WHERE nome = 'Cachorro')));

SELECT * from petstation.produto;

CREATE OR REPLACE FUNCTION create_produto(
    nome_produto VARCHAR,
    preco_produto NUMERIC(10, 2),
    descricao_produto VARCHAR,
    estoque_produto INT,
    nome_subcategoria VARCHAR,
    nome_categoria VARCHAR
) RETURNS VOID AS $$
DECLARE
    subcategoria_id INT;
BEGIN
    -- Obter o id_subcategoria com base na subcategoria e categoria fornecidas
    SELECT sc.id_subcategoria 
    INTO subcategoria_id
    FROM petstation.subcategoria sc
    JOIN petstation.categoria c ON sc.id_categoria = c.id_categoria
    WHERE sc.nome = nome_subcategoria AND c.nome = nome_categoria;
    
    -- Inserir o produto na tabela produto
    INSERT INTO petstation.produto (nome, preco, descricao, estoque, id_subcategoria)
    VALUES (nome_produto, preco_produto, descricao_produto, estoque_produto, subcategoria_id);
END;
$$ LANGUAGE plpgsql;

SELECT create_produto('Ração Golden para Cães Filhotes - 15Kg', 169.90, 
'Ração tipo seca, sabor frango, para grande, médio e pequeno porte', 100, 
'Rações e petiscos', 'Cachorro');

SELECT create_produto('Snack Bifinho Frango - 60g', 12.90, 
'Snack Bifinho sabor frango para grande, médio e pequeno porte', 100, 
'Rações e petiscos', 'Cachorro');
