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
    cpf CHAR(14) NOT NULL,
    email VARCHAR(80) NOT NULL,
    CONSTRAINT pk_cliente  PRIMARY KEY(id_cliente),
    CONSTRAINT uk_cliente_cpf UNIQUE(cpf),
    CONSTRAINT uk_cliente_email UNIQUE(email),
    CONSTRAINT ck_cliente_sexo CHECK(sexo in ('M','F')), 
    CONSTRAINT ck_cliente_cpf
		CHECK (cpf ~ '^\d{3}\.\d{3}\.\d{3}-\d{2}$') 
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

-- Rações e petiscos (Cachorro)

SELECT create_produto('Ração Golden para Cães Filhotes - 15Kg', 169.90, 
'Ração tipo seca, sabor frango, para grande, médio e pequeno porte', 100, 
'Rações e petiscos', 'Cachorro');

SELECT create_produto('Snack Bifinho Frango - 60g', 12.90, 
'Snack Bifinho sabor frango para grande, médio e pequeno porte', 100, 
'Rações e petiscos', 'Cachorro');

-- Brinquedos (Cachorro)

SELECT create_produto('Frisbee Vermelho', 49.90, 
'Frisbee feito de EVA Foam, medidas: 24 x 24 x 3,4cm', 100, 
'Brinquedos', 'Cachorro');

SELECT create_produto('Mordedor Galinha', 7.90, 
'Mordedor feito de PVC, medidas: 29 x 8 x 5cm', 100, 
'Brinquedos', 'Cachorro');

SELECT create_produto('Bola Cravo - Azul', 8.90, 
'Bolinha feita de Vinil, medidas: 6,5cm de diâmetro', 100, 
'Brinquedos', 'Cachorro');

-- Coleiras, guias e peitorais (Cachorro)

SELECT create_produto('Peitoral Anti Puxão', 149.90, 
'Peitoral cor vermelha, para porte médio, feita poliéster com fibras naturais', 100, 
'Coleiras, guias e peitorais', 'Cachorro');

SELECT create_produto('Coleira Waterproof Tech Blue', 27.90, 
'Coleira cor azul, feita de poliéster, zinco e borracha, medidas: 37 a 53cm', 100, 
'Coleiras, guias e peitorais', 'Cachorro');

SELECT create_produto('Guia Naked Golden ', 69.90, 
'Guia cor bege, feita de poliéster e zinco, medidas: 120cm', 100, 
'Coleiras, guias e peitorais', 'Cachorro');

-- Beleza e limpeza (Cachorro)

SELECT create_produto('Shampoo Neutro Pet - 500ml', 29.90, 
'Shampoo demartológico para cães de todos os portes', 100, 
'Beleza e limpeza', 'Cachorro');

SELECT create_produto('Condicionador Hidratante - 500ml', 34.90, 
'Condicionador para todos tipos de pelagem, para cães de todos os portes', 100, 
'Beleza e limpeza', 'Cachorro');

SELECT create_produto('Escova de Dentes para Pets', 15.90, 
'Escova dupla para cães, com cerdas macias', 100, 
'Beleza e limpeza', 'Cachorro');

-- Farmácia (Cachorro)

SELECT create_produto('Vermífugo Canex', 19.90, 
'Vermífugo em comprimidos para cães', 100, 
'Farmácia', 'Cachorro');

SELECT create_produto('Antipulgas NexGard', 89.90, 
'Comprimido mastigável para controle de pulgas e carrapatos', 100, 
'Farmácia', 'Cachorro');

SELECT create_produto('Pomada Cicatrizante - 30g', 24.90, 
'Pomada para cicatrização de feridas e cortes', 100, 
'Farmácia', 'Cachorro');

-- Casas e Camas (Cachorro)

SELECT create_produto('Cama Retangular Luxo', 199.90, 
'Cama de tecido lavável, para cães de grande porte', 100, 
'Casas e camas', 'Cachorro');

SELECT create_produto('Casa de Plástico para Pets', 299.90, 
'Casa plástica resistente para uso interno e externo, para médio porte', 100, 
'Casas e camas', 'Cachorro');

SELECT create_produto('Colchonete Impermeável', 89.90, 
'Colchonete dobrável e impermeável, para cães de porte pequeno', 100, 
'Casas e camas', 'Cachorro');

-- Roupas e acessórios (Cachorro)

SELECT create_produto('Capa de Chuva para Pets', 79.90, 
'Capa de chuva impermeável, cor amarela, para médio porte', 100, 
'Roupas e acessórios', 'Cachorro');

SELECT create_produto('Suéter de Lã para Pets', 59.90, 
'Suéter de lã, cor cinza, para pequeno porte', 100, 
'Roupas e acessórios', 'Cachorro');

SELECT create_produto('Camiseta Florida', 39.90, 
'Camiseta de algodão, com estampa florida, para grande porte', 100, 
'Roupas e acessórios', 'Cachorro');

SELECT p.* FROM petstation.produto p
JOIN petstation.subcategoria s ON p.id_subcategoria = s.id_subcategoria
JOIN petstation.categoria c ON s.id_categoria = c.id_categoria
WHERE c.nome = 'Cachorro';

-- Rações e petiscos (Gato)

SELECT create_produto('Ração Whiskas para Gatos Adultos - 1,5Kg', 49.90, 
'Ração tipo seca, sabor peixe, para gatos adultos', 100, 
'Rações e petiscos', 'Gato');

SELECT create_produto('Snack Dreamies Sabor Queijo - 60g', 12.90, 
'Snack crocante por fora e cremoso por dentro, sabor queijo', 100, 
'Rações e petiscos', 'Gato');

SELECT create_produto('Sachê Cat Chow Castrados - 85g', 3.90, 
'Ração úmida em sachê, sabor frango, para gatos castrados', 100, 
'Rações e petiscos', 'Gato');

-- Brinquedos (Gato)

SELECT create_produto('Varinha com Penas Coloridas', 19.90, 
'Brinquedo interativo com penas coloridas para gatos', 100, 
'Brinquedos', 'Gato');

SELECT create_produto('Bola de Pelúcia com Guizo', 9.90, 
'Bola de pelúcia macia com guizo interno, para gatos', 100, 
'Brinquedos', 'Gato');

SELECT create_produto('Rato de Pelúcia com Catnip', 24.90, 
'Rato de pelúcia recheado com catnip para estimular brincadeiras', 100, 
'Brinquedos', 'Gato');

-- Coleiras e guias (Gato)

SELECT create_produto('Coleira com Plaquinha de Identificação', 29.90, 
'Coleira ajustável com plaquinha para gravação de nome', 100, 
'Coleiras e guias', 'Gato');

SELECT create_produto('Guia Retrátil para Gatos', 49.90, 
'Guia retrátil leve e resistente, comprimento de 3 metros', 100, 
'Coleiras e guias', 'Gato');

SELECT create_produto('Coleira de Nylon com Sino', 19.90, 
'Coleira de nylon ajustável com sino para gatos', 100, 
'Coleiras e guias', 'Gato');

-- Beleza e limpeza (Gato)

SELECT create_produto('Shampoo Seco para Gatos - 300ml', 34.90, 
'Shampoo seco, indicado para limpeza rápida e prática', 100, 
'Beleza e limpeza', 'Gato');

SELECT create_produto('Lenços Umedecidos para Gatos', 19.90, 
'Lenços umedecidos para higienização rápida, pacote com 50 unidades', 100, 
'Beleza e limpeza', 'Gato');

SELECT create_produto('Escova Removedora de Pelos', 24.90, 
'Escova para remoção de pelos soltos, com cerdas de silicone', 100, 
'Beleza e limpeza', 'Gato');

-- Farmácia (Gato)

SELECT create_produto('Vermífugo Milbemax', 29.90, 
'Vermífugo em comprimidos para gatos', 100, 
'Farmácia', 'Gato');

SELECT create_produto('Antipulgas Revolution', 89.90, 
'Solução tópica para controle de pulgas e vermes', 100, 
'Farmácia', 'Gato');

SELECT create_produto('Pomada Otológica para Gatos', 34.90, 
'Pomada para tratamento de otites e inflamações', 100, 
'Farmácia', 'Gato');

-- Casas e camas (Gato)

SELECT create_produto('Cama Igloo para Gatos', 149.90, 
'Cama estilo igloo, acolchoada e confortável, tamanho médio', 100, 
'Casas e camas', 'Gato');

SELECT create_produto('Arranhador com Cama Suspensa', 199.90, 
'Arranhador com cama suspensa integrada, estrutura de sisal', 100, 
'Casas e camas', 'Gato');

SELECT create_produto('Colchonete Térmico para Gatos', 99.90, 
'Colchonete com isolamento térmico, ideal para dias frios', 100, 
'Casas e camas', 'Gato');

-- Roupas e acessórios e acessórios (Gato)

SELECT create_produto('Suéter de Lã para Gatos', 59.90, 
'Suéter de lã, cor cinza, tamanho pequeno', 100, 
'Roupas e acessórios', 'Gato');

SELECT create_produto('Capa de Chuva para Gatos', 69.90, 
'Capa de chuva impermeável, cor vermelha, tamanho médio', 100, 
'Roupas e acessórios', 'Gato');

SELECT create_produto('Pijama de Algodão para Gatos', 49.90, 
'Pijama de algodão macio, com estampa estrelada, tamanho médio', 100, 
'Roupas e acessórios', 'Gato');

SELECT p.* FROM petstation.produto p
JOIN petstation.subcategoria s ON p.id_subcategoria = s.id_subcategoria
JOIN petstation.categoria c ON s.id_categoria = c.id_categoria
WHERE c.nome = 'Gato';

-- Alimentação (Pássaro)

SELECT create_produto('Ração Alcon Club para Calopsitas - 500g', 19.90, 
'Ração balanceada com grãos selecionados para calopsitas', 100, 
'Alimentação', 'Pássaro');

SELECT create_produto('Mistura de Sementes para Canários - 1Kg', 24.90, 
'Mix de sementes selecionadas, indicado para canários', 100, 
'Alimentação', 'Pássaro');

SELECT create_produto('Farinhada para Periquitos - 250g', 14.90, 
'Farinhada completa, enriquecida com vitaminas e minerais', 100, 
'Alimentação', 'Pássaro');

-- Brinquedos (Pássaro)

SELECT create_produto('Balanço de Madeira para Pássaros', 29.90, 
'Balanço em madeira natural, ideal para pequenos e médios pássaros', 100, 
'Brinquedos', 'Pássaro');

SELECT create_produto('Espelho com Guizo para Pássaros', 12.90, 
'Brinquedo com espelho e guizo, indicado para periquitos e calopsitas', 100, 
'Brinquedos', 'Pássaro');

SELECT create_produto('Escada de Madeira para Pássaros', 19.90, 
'Escada em madeira, com 5 degraus, para gaiolas de diversos tamanhos', 100, 
'Brinquedos', 'Pássaro');

-- Beleza e limpeza (Pássaro)

SELECT create_produto('Shampoo para Pássaros - 200ml', 29.90, 
'Shampoo suave para limpeza de penas e pele', 100, 
'Beleza e limpeza', 'Pássaro');

SELECT create_produto('Lixa de Unha para Pássaros', 9.90, 
'Lixa especial para manutenção das unhas de pássaros', 100, 
'Beleza e limpeza', 'Pássaro');

SELECT create_produto('Spray Higienizador de Gaiolas - 500ml', 24.90, 
'Spray antibacteriano para limpeza e desinfecção de gaiolas', 100, 
'Beleza e limpeza', 'Pássaro');

-- Farmácia (Pássaro)

SELECT create_produto('Vitaminas para Pássaros - 30ml', 19.90, 
'Suplemento vitamínico líquido, indicado para todas as espécies de pássaros', 100, 
'Farmácia', 'Pássaro');

SELECT create_produto('Antibiótico para Pássaros - 10ml', 34.90, 
'Medicação antibiótica para tratamento de infecções respiratórias', 100, 
'Farmácia', 'Pássaro');

SELECT create_produto('Cálcio Líquido para Pássaros - 50ml', 14.90, 
'Suplemento de cálcio para fortalecimento de ossos e casca de ovos', 100, 
'Farmácia', 'Pássaro');

-- Gaiolas e Viveiros (Pássaro)

SELECT create_produto('Gaiola para Canários', 149.90, 
'Gaiola de metal com bandeja removível, ideal para canários', 100, 
'Gaiolas e viveiros', 'Pássaro');

SELECT create_produto('Viveiro de Madeira para Pássaros', 299.90, 
'Viveiro espaçoso em madeira, ideal para aves de médio porte', 100, 
'Gaiolas e viveiros', 'Pássaro');

SELECT create_produto('Poleiro de Madeira Natural', 24.90, 
'Poleiro de madeira natural, fácil de instalar em qualquer gaiola', 100, 
'Gaiolas e viveiros', 'Pássaro');

SELECT p.* FROM petstation.produto p
JOIN petstation.subcategoria s ON p.id_subcategoria = s.id_subcategoria
JOIN petstation.categoria c ON s.id_categoria = c.id_categoria
WHERE c.nome = 'Pássaro';

-- Alimentaçao (Peixe)

SELECT create_produto('Ração Alcon Goldfish - 100g', 12.90, 
'Ração em flocos, indicada para peixes Goldfish', 100, 
'Alimentação', 'Peixe');

SELECT create_produto('Ração TetraMin - 250ml', 34.90, 
'Ração balanceada em flocos, ideal para peixes tropicais', 100, 
'Alimentação', 'Peixe');

SELECT create_produto('Ração Spirulina para Peixes - 50g', 19.90, 
'Ração rica em spirulina, ideal para peixes herbívoros', 100, 
'Alimentação', 'Peixe');

-- Decoracao (Peixe)

SELECT create_produto('Planta Artificial para Aquário', 14.90, 
'Planta artificial de plástico, fácil de limpar, cor verde', 100, 
'Decoração', 'Peixe');

SELECT create_produto('Caverna de Resina para Aquário', 39.90, 
'Decoração em resina, forma de caverna, ideal para aquários médios e grandes', 100, 
'Decoração', 'Peixe');

SELECT create_produto('Substrato Colorido para Aquário - 1kg', 24.90, 
'Substrato colorido, não tóxico, ideal para aquários de água doce', 100, 
'Decoração', 'Peixe');

-- Limpeza (Peixe)

SELECT create_produto('Limpador Magnético para Vidros de Aquário', 29.90, 
'Limpador magnético para remover algas e sujeira dos vidros', 100, 
'Limpeza', 'Peixe');

SELECT create_produto('Sifão para Limpeza de Aquário', 19.90, 
'Sifão manual para limpeza de fundo de aquário', 100, 
'Limpeza', 'Peixe');

SELECT create_produto('Escova de Limpeza para Filtros de Aquário', 14.90, 
'Escova de cerdas macias, ideal para limpeza de filtros e tubos', 100, 
'Limpeza', 'Peixe');

-- Farmácia (Peixe) 

SELECT create_produto('Anti Cloro para Aquário - 120ml', 12.90, 
'Solução para eliminar cloro e cloramina da água do aquário', 100, 
'Farmácia', 'Peixe');

SELECT create_produto('Bactericida para Peixes - 30ml', 24.90, 
'Solução bactericida, tratamento de doenças bacterianas em peixes', 100, 
'Farmácia', 'Peixe');

SELECT create_produto('Remédio para Ictio - 30ml', 19.90, 
'Solução para tratamento de ictio (doença dos pontos brancos)', 100, 
'Farmácia', 'Peixe');

-- Aquários e equipamentos (Peixe)

SELECT create_produto('Aquário de Vidro 20L', 149.90, 
'Aquário de vidro, capacidade de 20 litros, ideal para qualquer ambiente', 100, 
'Aquários e equipamentos', 'Peixe');

SELECT create_produto('Filtro Externo para Aquário', 89.90, 
'Filtro externo para aquário de até 60 litros, com fluxo ajustável', 100, 
'Aquários e equipamentos', 'Peixe');

SELECT create_produto('Termostato para Aquário 50W', 39.90, 
'Termostato ajustável, ideal para manter a temperatura da água estável', 100, 
'Aquários e equipamentos', 'Peixe');

SELECT p.* FROM petstation.produto p
JOIN petstation.subcategoria s ON p.id_subcategoria = s.id_subcategoria
JOIN petstation.categoria c ON s.id_categoria = c.id_categoria
WHERE c.nome = 'Peixe';

-- * Cadastro de Clientes *

INSERT INTO petstation.cliente(nome, sobrenome, sexo, data_nascimento, cpf, email) 
VALUES ('Gabryel', 'Leite', 'M', TO_DATE('17-12-2004','dd-mm-yyyy'), '111.111.111-11', 'gabryeleite@uel.br');

INSERT INTO petstation.cliente(nome, sobrenome, sexo, data_nascimento, cpf, email) 
VALUES ('Beatriz', 'Passoni', 'F', TO_DATE('17-12-2004','dd-mm-yyyy'), '111.111.111-12', 'biapassoni@uel.br');

INSERT INTO petstation.cliente(nome, sobrenome, sexo, data_nascimento, cpf, email) 
VALUES ('Daniel', 'Kaster', 'M', TO_DATE('17-12-2004','dd-mm-yyyy'), '111.111.111-13', 'dskaster@uel.br');

INSERT INTO petstation.cliente(nome, sobrenome, sexo, data_nascimento, cpf, email) 
VALUES ('Bill', 'Gates', 'M', TO_DATE('17-12-2004','dd-mm-yyyy'), '111.111.111-14', 'billgates@uel.br');

SELECT * FROM petstation.cliente;

-- * Simulando Pedidos *

ALTER TABLE petstation.produto ADD CONSTRAINT uk_produto UNIQUE(nome);

-- Pedido 01 --

-- CPF_cliente  : 111.111.111-11
-- Data         : Atual 
-- Horário      : Atual

-- Carrinho --

-- Produto      : 'Snack Dreamies Sabor Queijo - 60g'
-- Qnt          : 3 unid
-- Produto      : 'Rato de Pelúcia com Catnip'
-- Qnt          : 1 unid

INSERT INTO petstation.pedido(num, id_cliente, data_pedido, hora_pedido)
VALUES (1, (SELECT id_cliente FROM petstation.cliente WHERE cpf = '111.111.111-11'), CURRENT_DATE, CURRENT_TIME);

INSERT INTO petstation.carrinho(num_pedido, qnt_produto, num_produto)
VALUES (1, 3, (SELECT num FROM petstation.produto WHERE nome = 'Snack Dreamies Sabor Queijo - 60g'));

INSERT INTO petstation.carrinho(num_pedido, qnt_produto, num_produto)
VALUES (1, 1, (SELECT num FROM petstation.produto WHERE nome = 'Rato de Pelúcia com Catnip'));

SELECT * from petstation.pedido;
SELECT * from petstation.carrinho;

-- Pedido 02 --

-- CPF_cliente  : 111.111.111-12
-- Data         : Atual
-- Horário      : Atual

-- Carrinho --

-- Produto      : 'Mistura de Sementes para Canários - 1Kg'
-- Qnt          : 2 unid
-- Produto      : 'Espelho com Guizo para Pássaros'
-- Qnt          : 1 unid
-- Produto      : 'Vitaminas para Pássaros - 30ml'
-- Qnt          : 4 unid

INSERT INTO petstation.pedido(num, id_cliente, data_pedido, hora_pedido)
VALUES (2, (SELECT id_cliente FROM petstation.cliente WHERE cpf = '111.111.111-12'), CURRENT_DATE, CURRENT_TIME);

INSERT INTO petstation.carrinho(num_pedido, qnt_produto, num_produto)
VALUES (2, 2, (SELECT num FROM petstation.produto WHERE nome = 'Mistura de Sementes para Canários - 1Kg'));

INSERT INTO petstation.carrinho(num_pedido, qnt_produto, num_produto)
VALUES (2, 1, (SELECT num FROM petstation.produto WHERE nome = 'Espelho com Guizo para Pássaros'));

INSERT INTO petstation.carrinho(num_pedido, qnt_produto, num_produto)
VALUES (2, 4, (SELECT num FROM petstation.produto WHERE nome = 'Vitaminas para Pássaros - 30ml'));

-- Pedido 03 --

-- CPF_cliente  : 111.111.111-13
-- Data         : Atual
-- Horário      : Atual

-- Carrinho --

-- Produto      : 'Ração Golden para Cães Adultos - 15Kg'
-- Qnt          : 2 unid

INSERT INTO petstation.pedido(num, id_cliente, data_pedido, hora_pedido)
VALUES (3, (SELECT id_cliente FROM petstation.cliente WHERE cpf = '111.111.111-13'), CURRENT_DATE, CURRENT_TIME);

INSERT INTO petstation.carrinho(num_pedido, qnt_produto, num_produto)
VALUES (3, 2, (SELECT num FROM petstation.produto WHERE nome = 'Ração Golden para Cães Adultos - 15Kg'));

-- Pedido 04 --

-- CPF_cliente  : 111.111.111-14
-- Data         : Atual
-- Horário      : Atual

-- Carrinho --

-- Produto      : 'Ração Alcon Goldfish - 100g'
-- Qnt          : 3 unid
-- Produto      : 'Planta Artificial para Aquário'
-- Qnt          : 2 unid

INSERT INTO petstation.pedido(num, id_cliente, data_pedido, hora_pedido)
VALUES (4, (SELECT id_cliente FROM petstation.cliente WHERE cpf = '111.111.111-14'), CURRENT_DATE, CURRENT_TIME);

INSERT INTO petstation.carrinho(num_pedido, qnt_produto, num_produto)
VALUES (4, 3, (SELECT num FROM petstation.produto WHERE nome = 'Ração Alcon Goldfish - 100g'));

INSERT INTO petstation.carrinho(num_pedido, qnt_produto, num_produto)
VALUES (4, 2, (SELECT num FROM petstation.produto WHERE nome = 'Planta Artificial para Aquário'));

-- Pedido 05 --

-- CPF_cliente  : 111.111.111-13
-- Data         : Atual
-- Horário      : Atual

-- Carrinho --

-- Produto      : 'Peitoral Anti Puxão'
-- Qnt          : 1 unid

INSERT INTO petstation.pedido(num, id_cliente, data_pedido, hora_pedido)
VALUES (5, (SELECT id_cliente FROM petstation.cliente WHERE cpf = '111.111.111-13'), CURRENT_DATE, CURRENT_TIME);

INSERT INTO petstation.carrinho(num_pedido, qnt_produto, num_produto)
VALUES (5, 1, (SELECT num FROM petstation.produto WHERE nome = 'Peitoral Anti Puxão'));

SELECT * from petstation.pedido;
SELECT * from petstation.carrinho;

-- pedidos de um cliente
SELECT * from petstation.pedido 
WHERE id_cliente = (SELECT id_cliente FROM petstation.cliente WHERE cpf = '111.111.111-13');

-- produtos pedidos por um cliente
SELECT p.nome AS produto, ped.num, ped.data_pedido
FROM petstation.produto p
JOIN petstation.carrinho c ON p.num = c.num_produto
JOIN petstation.pedido ped ON c.num_pedido = ped.num
JOIN petstation.cliente cli ON ped.id_cliente = cli.id_cliente
WHERE cli.cpf = '111.111.111-13';

-- Atualiza estoque

-- Pedido 01:
-- 'Snack Dreamies Sabor Queijo - 60g'   | 3 unid
-- 'Rato de Pelúcia com Catnip'          | 1 unid

UPDATE petstation.produto SET estoque = estoque - 3
WHERE nome = 'Snack Dreamies Sabor Queijo - 60g';

UPDATE petstation.produto SET estoque = estoque - 1
WHERE nome = 'Rato de Pelúcia com Catnip';

-- Pedido 02:
-- 'Mistura de Sementes para Canários - 1Kg'    | 2 unid
-- 'Espelho com Guizo para Pássaros'    | 1 unid
-- 'Vitaminas para Pássaros - 30ml'     | 4 unid

UPDATE petstation.produto SET estoque = estoque - 2
WHERE nome = 'Mistura de Sementes para Canários - 1Kg';

UPDATE petstation.produto SET estoque = estoque - 1
WHERE nome = 'Espelho com Guizo para Pássaros';

UPDATE petstation.produto SET estoque = estoque - 4
WHERE nome = 'Vitaminas para Pássaros - 30ml';

-- Pedido 03:
-- 'Ração Golden para Cães Adultos - 15Kg'  |  2 unid

UPDATE petstation.produto SET estoque = estoque - 2
WHERE nome = 'Ração Golden para Cães Adultos - 15Kg';

-- Pedido 04:
-- 'Ração Alcon Goldfish - 100g'    | 3 unid
-- 'Planta Artificial para Aquário' | 2 unid

UPDATE petstation.produto SET estoque = estoque - 3
WHERE nome = 'Ração Alcon Goldfish - 100g';

UPDATE petstation.produto SET estoque = estoque - 2
WHERE nome = 'Planta Artificial para Aquário';

-- Pedido 05:
-- Peitoral Anti Puxão'     |  1 unid

UPDATE petstation.produto SET estoque = estoque - 1
WHERE nome = 'Peitoral Anti Puxão';

SELECT * from petstation.produto ORDER BY num;

-- * Views *

-- View Produto:

CREATE OR REPLACE VIEW petstation.vw_produto AS
SELECT 
    num AS "Número",
    nome AS "Nome",
    preco AS "Preço",
    descricao AS "Descrição",
    estoque AS "Estoque"
FROM 
    petstation.produto ORDER BY num;

SELECT * FROM petstation.vw_produto;

-- View Cliente:

CREATE OR REPLACE VIEW petstation.vw_cliente AS
SELECT 
    id_cliente AS "ID Cliente",
    nome AS "Nome",
    sobrenome AS "Sobrenome",
    sexo AS "Sexo",
    data_nascimento AS "Nascimento",
    cpf AS "CPF",
    email AS "E-mail"
FROM 
    petstation.cliente ORDER BY id_cliente;

SELECT * FROM petstation.vw_cliente;
SELECT * FROM petstation.vw_cliente WHERE "Nascimento" >= TO_DATE('19-12-1970','dd-mm-yyyy');

-- View Pedido:

CREATE OR REPLACE VIEW petstation.vw_pedido AS
SELECT 
    p.num AS "Nº Pedido",
    p.id_cliente AS "ID Cliente",
    c.nome || ' ' || c.sobrenome AS "Cliente",
    p.data_pedido AS "Data",
    p.hora_pedido AS "Hora"
FROM 
    petstation.pedido p
JOIN 
    petstation.cliente c ON p.id_cliente = c.id_cliente
ORDER BY 
    p.num;

SELECT * FROM petstation.vw_pedido;

-- View Carrinho:

CREATE OR REPLACE VIEW petstation.vw_carrinho AS
SELECT 
    c.num_pedido AS "Nº do Pedido",
    c.num_produto AS "Nº do Produto",
    p.nome AS "Produto",
    p.preco AS "Preço",
    c.qnt_produto AS "Quantidade",
    (c.qnt_produto * p.preco) AS "Total"
FROM 
    petstation.carrinho c
JOIN 
    petstation.produto p ON c.num_produto = p.num
ORDER BY 
    c.num_pedido;

SELECT * FROM petstation.vw_carrinho;

-- Resumo Pedido (Pedido + Carrinho):

CREATE OR REPLACE VIEW petstation.vw_resumo_pedido AS
SELECT
    p.num AS "Nº Pedido",
    SUM(c.qnt_produto) AS "Quantidade",
    SUM(c.qnt_produto * pr.preco) AS "Preço Total",
    TO_CHAR(p.data_pedido, 'dd-mm-yyyy') AS "Data"
FROM
    petstation.pedido p
INNER JOIN 
    petstation.carrinho c ON p.num = c.num_pedido
INNER JOIN 
    petstation.produto pr ON c.num_produto = pr.num
GROUP BY
    p.num, p.data_pedido
ORDER BY
    p.num;

SELECT * FROM petstation.vw_resumo_pedido;

-- * Consultas *

-- Produtos mais vendidos (Geral)

SELECT 
    p.nome AS "Produto",
    ctg.nome AS "Categoria",
    SUM(c.qnt_produto) AS "Quantidade Vendida"
FROM 
    petstation.carrinho c
JOIN 
    petstation.produto p ON c.num_produto = p.num
JOIN 
    petstation.subcategoria sc ON p.id_subcategoria = sc.id_subcategoria
JOIN 
    petstation.categoria ctg ON sc.id_categoria = ctg.id_categoria
GROUP BY 
    p.nome, ctg.nome
ORDER BY 
    "Quantidade Vendida" DESC
LIMIT 5;

-- Produtos mais vendidos (Categoria)

SELECT 
    p.nome AS "Produto",
    SUM(c.qnt_produto) AS "Quantidade Vendida"
FROM 
    petstation.carrinho c
JOIN 
    petstation.produto p ON c.num_produto = p.num
JOIN 
    petstation.subcategoria sc ON p.id_subcategoria = sc.id_subcategoria
JOIN 
    petstation.categoria ctg ON sc.id_categoria = ctg.id_categoria
WHERE 
    ctg.nome = 'Pássaro'
GROUP BY 
    p.nome
ORDER BY 
    "Quantidade Vendida" DESC
LIMIT 5;

-- Clientes mais ativos (com mais pedidos)

SELECT 
    c.id_cliente AS "ID Cliente",
    c.nome || ' ' || c.sobrenome AS "Nome do Cliente",
    COUNT(p.num) AS "Número de Pedidos"
FROM 
    petstation.cliente c
JOIN 
    petstation.pedido p ON c.id_cliente = p.id_cliente
GROUP BY 
    c.id_cliente, c.nome, c.sobrenome
ORDER BY 
    "Número de Pedidos" DESC
LIMIT 3;

-- Número de produtos vendidos por dia em determinado período 

SELECT 
    p.data_pedido AS "Data",
    SUM(c.qnt_produto) AS "Quantidade Total de Produtos"
FROM 
    petstation.pedido p
JOIN 
    petstation.carrinho c ON p.num = c.num_pedido
WHERE 
    p.data_pedido BETWEEN '2024-08-01' AND '2024-08-31'  
GROUP BY 
    p.data_pedido
ORDER BY 
    p.data_pedido;

-- Subcategorias com maior faturamento

SELECT 
    sc.nome AS "Subcategoria",
    ctg.nome AS "Categoria",
    SUM(c.qnt_produto * p.preco) AS "Faturamento Total"
FROM 
    petstation.carrinho c
JOIN 
    petstation.produto p ON c.num_produto = p.num
JOIN 
    petstation.subcategoria sc ON p.id_subcategoria = sc.id_subcategoria
JOIN 
    petstation.categoria ctg ON sc.id_categoria = ctg.id_categoria
GROUP BY 
    sc.nome, ctg.nome
ORDER BY 
    "Faturamento Total" DESC
LIMIT 5;
