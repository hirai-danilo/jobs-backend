package com.simplesdental.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.simplesdental.entities.Profissional;
import com.simplesdental.enums.TipoCargo;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {


    @Query("SELECT p FROM Profissional p WHERE UPPER(p.nome) like UPPER(concat('%' ,:nome, '%'))")
    List<Profissional> buscarProfissionalContendoNome(@Param("nome") String nome);

    @Query("SELECT p FROM Profissional p WHERE p.cargo = :cargo")
    List<Profissional> buscarProfissionalPorCargo(@Param("cargo") TipoCargo cargo);

}
