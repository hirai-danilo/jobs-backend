package com.simplesdental.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.simplesdental.dtos.ProfissionalDTO;
import com.simplesdental.enums.TipoCargo;
import com.simplesdental.services.ProfissionalService;

@RestController
@RequestMapping("/profissional")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    @GetMapping("/{idProfissional}")
    @ResponseStatus(HttpStatus.OK)
    public ProfissionalDTO buscarPorId(@PathVariable("idProfissional") Long idProfissional) {
        return profissionalService.buscarProfissionalPorId(idProfissional);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String criarProfissional(@RequestBody ProfissionalDTO profissionalDTO) {
        return profissionalService.criarProfissional(profissionalDTO);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String atualizarContato(@RequestBody ProfissionalDTO profissionalDTO) {
        return profissionalService.atualizarProfissional(profissionalDTO);
    }

    @DeleteMapping("/{idProfissional}")
    @ResponseStatus(HttpStatus.OK)
    public String excluirContato(@PathVariable("idProfissional") Long idProfissional) {
        return profissionalService.excluirProfissional(idProfissional);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProfissionalDTO> buscarContidosNoNome(@RequestParam(value = "nome") String nome) {
        return profissionalService.buscarContidosNoNome(nome);
    }

    @GetMapping("/cargo")
    @ResponseStatus(HttpStatus.OK)
    public List<ProfissionalDTO> buscarPorCargo(@RequestParam(value = "cargo") TipoCargo cargo) {
        return profissionalService.buscarPorCargo(cargo);
    }

}
