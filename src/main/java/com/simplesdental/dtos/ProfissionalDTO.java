package com.simplesdental.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.simplesdental.entities.Contato;
import com.simplesdental.entities.Profissional;
import com.simplesdental.enums.TipoCargo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfissionalDTO {

    private Long id;

    private String nome;

    private TipoCargo cargo;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate nascimento;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataCriacao;

    private List<ContatoDTO> contatos;

    public ProfissionalDTO(Profissional profissional) {
        this.id = profissional.getId();
        this.nome = profissional.getNome();
        this.cargo = profissional.getCargo();
        this.nascimento = profissional.getNascimento();
        this.dataCriacao = profissional.getDataCriacao();
        this.contatos = profissional.getContatos()
                .stream()
                .map(ContatoDTO::new)
                .collect(Collectors.toList());
    }
}
