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

import com.simplesdental.dtos.ContatoDTO;
import com.simplesdental.services.ContatoService;

@RestController
@RequestMapping("/contato")
public class ContatoController {

    private final ContatoService contatoService;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    @GetMapping("/{idContato}")
    @ResponseStatus(HttpStatus.OK)
    public ContatoDTO buscarPorId(@PathVariable("idContato") Long idContato) {
        return contatoService.buscarContatoPorId(idContato);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String criarContato(@RequestBody ContatoDTO contatoDTO) {
        return contatoService.criarContato(contatoDTO);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String atualizarContato(@RequestBody ContatoDTO contatoDTO) {
        return contatoService.atualizarContato(contatoDTO);
    }

    @DeleteMapping("/{idContato}")
    @ResponseStatus(HttpStatus.OK)
    public String excluirContato(@PathVariable("idContato") Long idContato) {
        return contatoService.excluirContato(idContato);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ContatoDTO> buscarContidosNoNomeOuContato(@RequestParam(value = "nome", required = false) String nome,
                                                          @RequestParam(value = "contato", required = false) String contato) {
        return contatoService.buscarContidosNoNomeOuContato(nome, contato);
    }

}
