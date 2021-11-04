package com.simplesdental.services;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simplesdental.dtos.ContatoDTO;
import com.simplesdental.entities.Contato;
import com.simplesdental.entities.Profissional;
import com.simplesdental.exceptions.EntityNotFoundException;
import com.simplesdental.exceptions.InvalidAttributeException;
import com.simplesdental.repositories.ContatoRepository;
import com.simplesdental.repositories.ProfissionalRepository;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;
    private final ProfissionalRepository profissionalRepository;

    public ContatoService(ContatoRepository contatoRepository,
                          ProfissionalRepository profissionalRepository) {
        this.contatoRepository = contatoRepository;
        this.profissionalRepository = profissionalRepository;
    }


    public ContatoDTO buscarContatoPorId(Long idContato) {
        var contato = contatoRepository.findById(idContato)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Contato de id: %d não encontrado", idContato)));
        return new ContatoDTO(contato);
    }

    @Transactional
    public String excluirContato(Long idContato) {
        buscarContatoPorId(idContato);
        contatoRepository.deleteById(idContato);
        return String.format("Contato de id: %d excluido com sucesso", idContato);
    }

    @Transactional
    public String criarContato(ContatoDTO contatoDTO) {
        validarAtributosContato(contatoDTO, true);
        converterESalvar(contatoDTO);
        return "Contato criado com sucesso";
    }

    @Transactional
    public String atualizarContato(ContatoDTO contatoDTO) {
        validarAtributosContato(contatoDTO, false);
        buscarContatoPorId(contatoDTO.getId());
        converterESalvar(contatoDTO);
        return "Contato atualizado com sucesso";
    }

    private void converterESalvar(ContatoDTO contatoDTO) {
        var contatoEntidade = buildarEntidadePeloDTO(contatoDTO);
        contatoRepository.save(contatoEntidade);
    }

    private void validarAtributosContato(ContatoDTO contatoDTO, boolean novoContato) {
        if (nonNull(contatoDTO.getId()) && novoContato) {
            throw new InvalidAttributeException("Contato não pode ser criado por já possui id");
        }

        if (isNull(contatoDTO.getId()) && !novoContato) {
            throw new InvalidAttributeException("Contato não pode ser atualizado, id nulo");
        }

        if (isNull(contatoDTO.getNome())) {
            throw new InvalidAttributeException("Contato não possui nome");
        }

        if (isNull(contatoDTO.getContato())) {
            throw new InvalidAttributeException("Contato não possui contato");
        }


    }

    private Profissional recuperaProfissional(Long idProfissional) {
        return profissionalRepository.findById(idProfissional)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Profissional de id: %d não encontrado", idProfissional)));
    }

    private Contato buildarEntidadePeloDTO(ContatoDTO contatoDTO) {
        var profissional = recuperaProfissional(contatoDTO.getIdProfissional());
        return Contato.builder()
                .withId(contatoDTO.getId())
                .withNome(contatoDTO.getNome())
                .withContato(contatoDTO.getContato())
                .withProfissional(profissional)
                .build();

    }

    public List<ContatoDTO> buscarContidosNoNomeOuContato(String nome, String contato) {
        if (nonNull(nome)) {
            return contatoRepository.buscarContatosContendoNome(nome)
                    .stream()
                    .map(ContatoDTO::new)
                    .collect(Collectors.toList());
        }

        if (nonNull(contato)) {
            return contatoRepository.buscarContatosContendoContato(contato)
                    .stream()
                    .map(ContatoDTO::new)
                    .collect(Collectors.toList());
        }

        throw new InvalidAttributeException("Nome e Contato não informados");
    }
}
