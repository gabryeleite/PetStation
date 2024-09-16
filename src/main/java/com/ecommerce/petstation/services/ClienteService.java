package com.ecommerce.petstation.services;

import java.util.Optional;
import java.time.LocalDate;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.petstation.models.Cliente;
import com.ecommerce.petstation.repositories.ClienteRepository;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente findById(Integer id_cliente) {
        Optional<Cliente> cliente = this.clienteRepository.findById(id_cliente);
        return cliente.orElseThrow(() -> new RuntimeException("Cliente nao encontrado! id:" + id_cliente));
    }

    public List<Cliente> findAllClientes() {
        return clienteRepository.findAll();
    }

    public void createCliente(String nome, String sobrenome, String sexo, 
        LocalDate dataNascimento, String cpf, String email) throws Exception {
        clienteRepository.createCliente(nome, sobrenome, sexo, dataNascimento, cpf, email);
    }

    //@Transactional
    public void deleteCliente(Integer id_cliente) {
        findById(id_cliente);
        try {
            this.clienteRepository.deleteById(id_cliente);
        } catch (Exception e) {
            throw new RuntimeException("Nao eh possivel deletar cliente, relacoes envolvidas!");
        }
    }
}
