package com.ecommerce.petstation.daos.daosPg;

import com.ecommerce.petstation.daos.ClienteDAO;
import com.ecommerce.petstation.models.Cliente;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class PgClienteDAO implements ClienteDAO {
    private final Connection connection;

    private static final Logger LOGGER = Logger.getLogger(PgClienteDAO.class.getName());

    private static final String CREATE_QUERY =
            "INSERT INTO petstation.cliente (nome, sobrenome, sexo, data_nascimento, cpf, email, senha) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";

    private static final String READ_QUERY =
            "SELECT nome, sobrenome, sexo, data_nascimento, cpf, email, senha FROM petstation.cliente WHERE id_cliente = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE petstation.cliente SET nome = ?, sobrenome = ?, sexo = ?, data_nascimento = ?, cpf = ?, email = ?, senha = ? WHERE id_cliente = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM petstation.cliente WHERE id_cliente = ?;";

    private static final String ALL_QUERY =
            "SELECT id_cliente, nome, sobrenome, sexo, data_nascimento, cpf, email, senha FROM petstation.cliente ORDER BY id_cliente DESC;";

    private static final String FIND_BY_ID_CLIENTE_QUERY =
            "SELECT * FROM petstation.cliente WHERE id_cliente = ?;";

    private static final String CLIENTES_COM_MAIS_PEDIDOS_QUERY =
            "SELECT c.id_cliente, c.nome, c.sobrenome, c.sexo, c.data_nascimento, c.cpf, c.email, c.senha, COUNT(p.num) AS total_pedidos " +
                    "FROM petstation.cliente c " +
                    "JOIN petstation.pedido p ON c.id_cliente = p.id_cliente " +
                    "GROUP BY c.id_cliente, c.nome, c.sobrenome, c.sexo, c.data_nascimento, c.cpf, c.email, c.senha " +
                    "ORDER BY total_pedidos DESC;";

    public PgClienteDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Cliente cliente) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getSobrenome());
            statement.setString(3, cliente.getSexo());
            statement.setObject(4, cliente.getDataNascimento());
            statement.setString(5, cliente.getCpf());
            statement.setString(6, cliente.getEmail());
            statement.setString(7, cliente.getSenha());
            statement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao inserir cliente.", ex);
            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao inserir cliente: campos obrigatórios não podem ser nulos.");
            } else {
                throw new SQLException("Erro ao inserir cliente.");
            }
        }
    }

    @Override
    public Cliente read(Integer id) throws SQLException {
        Cliente cliente = new Cliente();
        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    cliente.setIdCliente(id);
                    cliente.setNome(rs.getString("nome"));
                    cliente.setSobrenome(rs.getString("sobrenome"));
                    cliente.setSexo(rs.getString("sexo"));
                    // Tive que dar um cast pra LocalDate, nao sei se esta correto
                    cliente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                    cliente.setCpf(rs.getString("cpf"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setSenha(rs.getString("senha"));
                } else {
                    throw new SQLException("Erro ao visualizar: cliente não encontrado.");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao ler cliente.", ex);
            if (ex.getMessage().equals("Erro ao visualizar: cliente não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao ler cliente.");
            }
        }
        return cliente;
    }

    @Override
    public void update(Cliente cliente) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getSobrenome());
            statement.setString(3, cliente.getSexo());
            statement.setObject(4, cliente.getDataNascimento());
            statement.setString(5, cliente.getCpf());
            statement.setString(6, cliente.getEmail());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: cliente não encontrado.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar cliente.", ex);
            if (ex.getMessage().equals("Erro ao editar: cliente não encontrado.")) {
                throw ex;
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar cliente: campos obrigatórios não podem ser nulos.");
            } else {
                throw new SQLException("Erro ao editar cliente.");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: cliente não encontrado.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir cliente.", ex);
            if (ex.getMessage().equals("Erro ao excluir: cliente não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao excluir cliente.");
            }
        }
    }

    @Override
    public List<Cliente> all() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setSobrenome(rs.getString("sobrenome"));
                cliente.setSexo(rs.getString("sexo"));
                cliente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                cliente.setCpf(rs.getString("cpf"));
                cliente.setEmail(rs.getString("email"));
                cliente.setSenha(rs.getString("senha"));
                clientes.add(cliente);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar clientes.", ex);
            throw new SQLException("Erro ao listar clientes.");
        }
        return clientes;
    }

    @Override
    public Cliente findByIdCliente(Integer id_cliente) throws SQLException {
        Cliente cliente = null;
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_CLIENTE_QUERY)) {
            statement.setInt(1, id_cliente);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setIdCliente(rs.getInt("id_cliente"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setSobrenome(rs.getString("sobrenome"));
                    cliente.setSexo(rs.getString("sexo"));
                    // Corrected conversion from java.sql.Date to java.time.LocalDate
                    cliente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                    cliente.setCpf(rs.getString("cpf"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setSenha(rs.getString("senha"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar cliente por ID.", ex);
            throw new SQLException("Erro ao buscar cliente por ID.");
        }
        return cliente;
    }


    @Override
    public Cliente findByCpfCliente(String cpf) throws SQLException {
        String sql = "SELECT * FROM petstation.cliente WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setSobrenome(rs.getString("sobrenome"));
                cliente.setSexo(rs.getString("sexo"));
                cliente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                cliente.setCpf(rs.getString("cpf"));
                cliente.setEmail(rs.getString("email"));
                cliente.setSenha(rs.getString("senha"));
                return cliente;
            } else {
                return null;
            }
        }
    }

    @Override
    public List<Cliente> findClientesComMaisPedidos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(CLIENTES_COM_MAIS_PEDIDOS_QUERY);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setSobrenome(rs.getString("sobrenome"));
                cliente.setSexo(rs.getString("sexo"));
                cliente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                cliente.setCpf(rs.getString("cpf"));
                cliente.setEmail(rs.getString("email"));
                cliente.setSenha(rs.getString("senha"));
                clientes.add(cliente);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar clientes com mais pedidos.", ex);
            throw new SQLException("Erro ao buscar clientes com mais pedidos.");
        }
        return clientes;
    }


}