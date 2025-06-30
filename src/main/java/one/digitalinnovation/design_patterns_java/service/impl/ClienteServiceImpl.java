package one.digitalinnovation.design_patterns_java.service.impl;

import one.digitalinnovation.design_patterns_java.model.Cliente;
import one.digitalinnovation.design_patterns_java.model.ClienteRepository;
import one.digitalinnovation.design_patterns_java.model.Endereco;
import one.digitalinnovation.design_patterns_java.model.EnderecoRepository;
import one.digitalinnovation.design_patterns_java.service.ClienteService;
import one.digitalinnovation.design_patterns_java.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;


    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }


    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }


    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
    }


    public void atualizar(Long id, Cliente cliente) {
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()) {
            salvarClienteComCep(cliente);
        }
    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }

    private void salvarClienteComCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }
}
