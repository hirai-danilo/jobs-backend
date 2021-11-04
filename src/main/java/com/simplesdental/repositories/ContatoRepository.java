package com.simplesdental.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.simplesdental.entities.Contato;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

    @Query("SELECT c FROM Contato c WHERE UPPER(c.nome) like UPPER(concat('%' ,:nome, '%'))")
    List<Contato> buscarContatosContendoNome(@Param("nome") String nome);

    @Query("SELECT c FROM Contato c WHERE UPPER(c.contato) like UPPER(concat('%' ,:contato, '%'))")
    List<Contato> buscarContatosContendoContato(@Param("contato") String contato);

}
