package com.simplesdental.dtos;

import com.simplesdental.entities.Contato;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContatoDTO {

    private Long id;

    private String nome;

    private String contato;

    private Long idProfissional;

    public ContatoDTO(Contato entidade) {
        this.id = entidade.getId();
        this.nome = entidade.getNome();
        this.contato = entidade.getContato();
        this.idProfissional = entidade.getProfissional().getId();
    }
}
