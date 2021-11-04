package com.simplesdental.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.simplesdental.enums.TipoCargo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "profissional")
public class Profissional {

    @Id
    @SequenceGenerator(name = "profissional_seq", sequenceName = "profissional_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profissional_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "nascimento", nullable = false)
    private LocalDate nascimento;

    @Column(name = "cargo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoCargo cargo;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "profissional", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<Contato> contatos = new ArrayList<>();

    public static ProfissionalBuilder builder() {
        return new ProfissionalBuilder();
    }

    public static final class ProfissionalBuilder {
        private Long id;
        private String nome;
        private LocalDate nascimento;
        private TipoCargo cargo;
        private List<Contato> contatos;

        private ProfissionalBuilder() {
        }

        public ProfissionalBuilder withId(Long idContato) {
            this.id = idContato;
            return this;
        }

        public ProfissionalBuilder withNome(String nome) {
            this.nome = nome;
            return this;
        }

        public ProfissionalBuilder withNascimento(LocalDate nascimento) {
            this.nascimento = nascimento;
            return this;
        }

        public ProfissionalBuilder withCargo(TipoCargo cargo) {
            this.cargo = cargo;
            return this;
        }

        public ProfissionalBuilder withContatos(List<Contato> contatos) {
            this.contatos = contatos;
            return this;
        }

        public Profissional build() {
            Profissional entidade = new Profissional();
            entidade.setId(id);
            entidade.setNome(nome);
            entidade.setNascimento(nascimento);
            entidade.setCargo(cargo);
            entidade.setContatos(contatos);
            return entidade;
        }
    }
}
