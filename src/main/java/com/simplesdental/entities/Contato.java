package com.simplesdental.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "contato")
public class Contato {

    @Id
    @SequenceGenerator(name = "contato_seq", sequenceName = "contato_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contato_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "contato", nullable = false)
    private String contato;

    @ManyToOne
    @JoinColumn(name = "id_profissional")
    private Profissional profissional;

    public static ContatoBuilder builder() {
        return new ContatoBuilder();
    }

    public static final class ContatoBuilder {
        private Long id;
        private String nome;
        private String contato;
        private Profissional profissional;

        private ContatoBuilder() {
        }

        public ContatoBuilder withId(Long idContato) {
            this.id = idContato;
            return this;
        }

        public ContatoBuilder withNome(String nome) {
            this.nome = nome;
            return this;
        }

        public ContatoBuilder withContato(String contato) {
            this.contato = contato;
            return this;
        }

        public ContatoBuilder withProfissional(Profissional profissional) {
            this.profissional = profissional;
            return this;
        }

        public Contato build() {
            Contato entidade = new Contato();
            entidade.setId(id);
            entidade.setNome(nome);
            entidade.setContato(contato);
            entidade.setProfissional(profissional);
            return entidade;
        }
    }

}
