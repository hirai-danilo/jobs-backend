package com.simplesdental.services;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simplesdental.dtos.ContatoDTO;
import com.simplesdental.dtos.ProfissionalDTO;
import com.simplesdental.entities.Contato;
import com.simplesdental.entities.Profissional;
import com.simplesdental.enums.TipoCargo;
import com.simplesdental.exceptions.EntityNotFoundException;
import com.simplesdental.exceptions.InvalidAttributeException;
import com.simplesdental.repositories.ContatoRepository;
import com.simplesdental.repositories.ProfissionalRepository;

@Service
public class ProfissionalService {

    private final ContatoRepository contatoRepository;
    private final ProfissionalRepository profissionalRepository;

    public ProfissionalService(ContatoRepository contatoRepository,
                               ProfissionalRepository profissionalRepository) {
        this.contatoRepository = contatoRepository;
        this.profissionalRepository = profissionalRepository;
    }


    public ProfissionalDTO buscarProfissionalPorId(Long idProfissional) {
        var profissional = profissionalRepository.findById(idProfissional)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Profissional de id: %d não encontrado", idProfissional)));
        return new ProfissionalDTO(profissional);
    }

    @Transactional
    public String excluirProfissional(Long idProfissional) {
        buscarProfissionalPorId(idProfissional);
        profissionalRepository.deleteById(idProfissional);
        return String.format("Profissional de id: %d excluido com sucesso", idProfissional);
    }

    @Transactional
    public String criarProfissional(ProfissionalDTO profissionalDTO) {
        validarAtributosProfissional(profissionalDTO, true);
        var profissionalEntidade = buildarEntidadePeloDTO(profissionalDTO);
        profissionalEntidade.setDataCriacao(LocalDateTime.now());
        profissionalEntidade = profissionalRepository.save(profissionalEntidade);
        criarOuAtualizarContatos(profissionalDTO.getContatos(), profissionalEntidade);
        return "Profissional criado com sucesso";
    }

    @Transactional
    public String atualizarProfissional(ProfissionalDTO profissionalDTO) {
        validarAtributosProfissional(profissionalDTO, false);
        var entidadeAntiga = buscarProfissionalPorId(profissionalDTO.getId());
        var profissionalEntidade = buildarEntidadePeloDTO(profissionalDTO);
        var contatos = criarOuAtualizarContatos(profissionalDTO.getContatos(), profissionalEntidade);
        profissionalEntidade.setContatos(contatos);
        profissionalEntidade.setDataCriacao(entidadeAntiga.getDataCriacao());
        profissionalRepository.save(profissionalEntidade);
        return "Profissional atualizado com sucesso";
    }


    private void validarAtributosProfissional(ProfissionalDTO profissionalDTO, boolean novoProfissional) {
        if (nonNull(profissionalDTO.getId()) && novoProfissional) {
            throw new InvalidAttributeException("Profissional não pode ser criado por já possui id");
        }

        if (isNull(profissionalDTO.getId()) && !novoProfissional) {
            throw new InvalidAttributeException("Profissional não pode ser atualizado, id nulo");
        }

        if (isNull(profissionalDTO.getNome())) {
            throw new InvalidAttributeException("Profissional não possui nome");
        }

        if (isNull(profissionalDTO.getNascimento())) {
            throw new InvalidAttributeException("Profissional não possui data de nascimento");
        }

        if (isNull(profissionalDTO.getCargo())) {
            throw new InvalidAttributeException("Profissional não possui cargo");
        }

    }

    private Profissional buildarEntidadePeloDTO(ProfissionalDTO profissionalDTO) {

        return Profissional.builder()
                .withId(profissionalDTO.getId())
                .withNome(profissionalDTO.getNome())
                .withCargo(profissionalDTO.getCargo())
                .withNascimento(profissionalDTO.getNascimento())
                .build();

    }

    private List<Contato> criarOuAtualizarContatos(List<ContatoDTO> contatosDTO, Profissional profissional) {
        var contatos = new ArrayList<Contato>();
        if (nonNull(contatosDTO)) {
            for (ContatoDTO dto : contatosDTO) {
                if (nonNull(dto.getId())) {
                    contatoRepository.findById(dto.getId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    String.format("Contato de id: %d não encontrado", dto.getId())));
                }
                var contatoEntidade = Contato.builder()
                        .withId(dto.getId())
                        .withNome(dto.getNome())
                        .withContato(dto.getContato())
                        .withProfissional(profissional)
                        .build();
                contatos.add(contatoRepository.save(contatoEntidade));
            }
        }
        return contatos;
    }

    public List<ProfissionalDTO> buscarContidosNoNome(String nome) {
        return profissionalRepository.buscarProfissionalContendoNome(nome)
                .stream()
                .map(ProfissionalDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProfissionalDTO> buscarPorCargo(TipoCargo cargo) {
        return profissionalRepository.buscarProfissionalPorCargo(cargo)
                .stream()
                .map(ProfissionalDTO::new)
                .collect(Collectors.toList());
    }
}
