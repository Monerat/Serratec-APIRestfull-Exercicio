package br.com.webank.webank.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.webank.webank.dto.contaBancaria.ContaBancariaRequestDTO;
import br.com.webank.webank.dto.contaBancaria.ContaBancariaResponseDTO;
import br.com.webank.webank.model.ContaBancaria;
import br.com.webank.webank.repository.ContaBancariaRepository;


@Service
public class ContaBancariaService {
    
    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    @Autowired
    private ModelMapper mapper;

    public List<ContaBancariaResponseDTO> obterTodos(){
        List<ContaBancaria> contaBancariaModel = contaBancariaRepository.findAll();

        List<ContaBancariaResponseDTO> ContaBancariaResponse = new ArrayList<>();

        for(ContaBancaria contaBancaria : contaBancariaModel){
            ContaBancariaResponse.add(mapper.map(contaBancaria, ContaBancariaResponseDTO.class));
        }
        return ContaBancariaResponse;
    }

    public ContaBancariaResponseDTO obterPorId(long id){
        Optional<ContaBancaria> optContaBancaria = contaBancariaRepository.findById(id);

        if(optContaBancaria.isEmpty()){
            throw new RuntimeException("Nenhum registro encontrado para o ID: " + id);
        }

        return mapper.map(optContaBancaria.get(), ContaBancariaResponseDTO.class);
    }

    // O save serve tanto para adicionar quanto para atualizar.
    // se tiver id, ele atualiza, s enão tiver id ele adiciona.
    public ContaBancariaResponseDTO adicionar(ContaBancariaRequestDTO contaBancariaRequest){

        ContaBancaria contaBancariaModel = mapper.map(contaBancariaRequest, ContaBancaria.class);
        contaBancariaModel = contaBancariaRepository.save(contaBancariaModel);

        return mapper.map(contaBancariaModel, ContaBancariaResponseDTO.class);
    }

    public ContaBancariaResponseDTO atualizar(long id, ContaBancariaRequestDTO contaBancariaRequest){

        // Se não lançar exception é porque o cara existe no banco.
        obterPorId(id);
        ContaBancaria contaBancariaModel = mapper.map(contaBancariaRequest, ContaBancaria.class);
        contaBancariaModel.setId(id);
        
        contaBancariaModel = contaBancariaRepository.save(contaBancariaModel);

        return mapper.map(contaBancariaModel,ContaBancariaResponseDTO.class);
    }

    public void deletar(Long id){
        obterPorId(id);

        contaBancariaRepository.deleteById(id);
    }

}
