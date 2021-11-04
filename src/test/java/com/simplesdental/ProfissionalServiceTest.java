package com.simplesdental;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.simplesdental.dtos.ProfissionalDTO;
import com.simplesdental.entities.Contato;
import com.simplesdental.entities.Profissional;
import com.simplesdental.enums.TipoCargo;
import com.simplesdental.exceptions.EntityNotFoundException;
import com.simplesdental.exceptions.InvalidAttributeException;
import com.simplesdental.repositories.ContatoRepository;
import com.simplesdental.repositories.ProfissionalRepository;
import com.simplesdental.services.ProfissionalService;

class ProfissionalServiceTest {

    private ContatoRepository contatoRepository;

    private ProfissionalRepository profissionalRepository;

    private ProfissionalService profissionalService;

    @BeforeEach
    public void init() {
        this.contatoRepository = mock(ContatoRepository.class);
        this.profissionalRepository = mock(ProfissionalRepository.class);
        this.profissionalService = new ProfissionalService(contatoRepository,
                profissionalRepository);
    }

    @Test
    void deveBuscarProfissionalPeloId() {
        var idContato = 1L;
        var idProfissional = 2L;

        var profissional = geraProfissionalParaTeste(idContato, idProfissional);

        when(profissionalRepository.findById(idProfissional)).thenReturn(Optional.of(profissional));
        var retorno = profissionalService.buscarProfissionalPorId(idProfissional);

        Assertions.assertEquals(idProfissional, retorno.getId());
        Assertions.assertEquals(profissional.getNome(), retorno.getNome());
        Assertions.assertEquals(profissional.getCargo(), retorno.getCargo());
        Assertions.assertEquals(profissional.getNascimento(), retorno.getNascimento());
        Assertions.assertEquals(1, retorno.getContatos().size());
        Assertions.assertEquals(idContato, retorno.getContatos().get(0).getId());

    }

    @Test
    void deveRetornarExcecaoDeContatoNaoEncontradoNaBuscaDeProfissional() {
        var idProfissional = 2L;

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> profissionalService.buscarProfissionalPorId(idProfissional),
                "Profissional de id: 2 não encontrado");
        verify(profissionalRepository, times(1)).findById(idProfissional);

    }

    @Test
    void deveExcluirProfissional() {
        var idContato = 1L;
        var idProfissional = 2L;

        var profissional = geraProfissionalParaTeste(idContato, idProfissional);

        when(profissionalRepository.findById(idProfissional)).thenReturn(Optional.of(profissional));

        var retorno = profissionalService.excluirProfissional(idProfissional);
        verify(profissionalRepository, times(1)).deleteById(idProfissional);
        Assertions.assertEquals("Profissional de id: 2 excluido com sucesso", retorno);

    }

    @Test
    void deveRetornarErroDeProfissionalComIdNaCriacao() {
        var idProfissional = 2L;

        var profissional = geraProfissionalParaTeste(null, idProfissional);
        var profissionalDto = new ProfissionalDTO(profissional);

        Assertions.assertThrows(InvalidAttributeException.class,
                () -> profissionalService.criarProfissional(profissionalDto),
                "Profissional não pode ser criado por já possui id");
        verify(profissionalRepository, times(0)).save(any());

    }

    @Test
    void deveRetornarErroDeProfissionalSemNome() {
        var profissional = geraProfissionalParaTeste(null, null);
        profissional.setNome(null);

        var profissionalDto = new ProfissionalDTO(profissional);

        Assertions.assertThrows(InvalidAttributeException.class,
                () -> profissionalService.criarProfissional(profissionalDto),
                "Profissional não possui nome");
        verify(profissionalRepository, times(0)).save(any());

    }

    @Test
    void deveRetornarErroDeProfissionalSemNascimento() {
        var profissional = geraProfissionalParaTeste(null, null);
        profissional.setNascimento(null);

        var profissionalDto = new ProfissionalDTO(profissional);

        Assertions.assertThrows(InvalidAttributeException.class,
                () -> profissionalService.criarProfissional(profissionalDto),
                "Profissional não possui data de nascimento");
        verify(profissionalRepository, times(0)).save(any());

    }

    @Test
    void deveRetornarErroDeProfissionalSemCargo() {
        var profissional = geraProfissionalParaTeste(null, null);
        profissional.setCargo(null);

        var profissionalDto = new ProfissionalDTO(profissional);

        Assertions.assertThrows(InvalidAttributeException.class,
                () -> profissionalService.criarProfissional(profissionalDto),
                "Profissional não possui cargo");
        verify(profissionalRepository, times(0)).save(any());

    }

    @Test
    void deveRetornarErroDeContatoNaoEncontrado() {
        var idContato = 1L;

        var profissional = geraProfissionalParaTeste(idContato, null);

        var profissionalDto = new ProfissionalDTO(profissional);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> profissionalService.criarProfissional(profissionalDto),
                "Contato de id: 1 não encontrado");

        verify(contatoRepository, times(0)).save(any());

    }

    @Test
    void deveCadastrarProfissionalComContatoExistente() {
        var idContato = 1L;

        var profissional = geraProfissionalParaTeste(idContato, null);
        var contato = profissional.getContatos().get(0);

        var profissionalDto = new ProfissionalDTO(profissional);

        var contatoCaptor = ArgumentCaptor.forClass(Contato.class);
        var profissionalCaptor = ArgumentCaptor.forClass(Profissional.class);

        when(contatoRepository.findById(idContato)).thenReturn(Optional.of(contato));

        var retorno = profissionalService.criarProfissional(profissionalDto);
        verify(profissionalRepository, times(1)).save(profissionalCaptor.capture());

        var profissionalCapturado = profissionalCaptor.getValue();

        Assertions.assertNull(profissionalCapturado.getId());
        Assertions.assertEquals(profissional.getNome(), profissionalCapturado.getNome());
        Assertions.assertEquals(profissional.getNascimento(), profissionalCapturado.getNascimento());
        Assertions.assertEquals(profissional.getCargo(), profissionalCapturado.getCargo());
        Assertions.assertNotNull(profissionalCapturado.getDataCriacao());

        verify(contatoRepository, times(1)).save(contatoCaptor.capture());
        var contatoCapturado = contatoCaptor.getValue();

        Assertions.assertEquals(contato.getId(), contatoCapturado.getId());
        Assertions.assertEquals(contato.getNome(), contatoCapturado.getNome());
        Assertions.assertEquals(contato.getContato(), contatoCapturado.getContato());

        Assertions.assertEquals("Profissional criado com sucesso", retorno);

    }

    @Test
    void deveCadastrarProfissionalComContatoNovo() {

        var profissional = geraProfissionalParaTeste(null, null);
        var contato = profissional.getContatos().get(0);

        var profissionalDto = new ProfissionalDTO(profissional);

        var contatoCaptor = ArgumentCaptor.forClass(Contato.class);
        var profissionalCaptor = ArgumentCaptor.forClass(Profissional.class);

        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

        var retorno = profissionalService.criarProfissional(profissionalDto);
        verify(profissionalRepository, times(1)).save(profissionalCaptor.capture());

        var profissionalCapturado = profissionalCaptor.getValue();

        Assertions.assertNull(profissionalCapturado.getId());
        Assertions.assertEquals(profissional.getNome(), profissionalCapturado.getNome());
        Assertions.assertEquals(profissional.getNascimento(), profissionalCapturado.getNascimento());
        Assertions.assertEquals(profissional.getCargo(), profissionalCapturado.getCargo());
        Assertions.assertNotNull(profissionalCapturado.getDataCriacao());

        verify(contatoRepository, times(1)).save(contatoCaptor.capture());
        var contatoCapturado = contatoCaptor.getValue();

        Assertions.assertEquals(contato.getId(), contatoCapturado.getId());
        Assertions.assertEquals(contato.getNome(), contatoCapturado.getNome());
        Assertions.assertEquals(contato.getContato(), contatoCapturado.getContato());

        Assertions.assertEquals("Profissional criado com sucesso", retorno);

    }

    @Test
    void deveRetornarErroDeProfissionalSemIdParaAtualizacao() {

        var profissional = geraProfissionalParaTeste(null, null);

        var profissionalDto = new ProfissionalDTO(profissional);

        Assertions.assertThrows(InvalidAttributeException.class,
                () -> profissionalService.atualizarProfissional(profissionalDto),
                "Profissional não pode ser atualizado, id nulo");

        verify(profissionalRepository, times(0)).save(any());

    }

    @Test
    void deveAtualizarProfissional() {
        var idContato = 1L;
        var idProfissional = 2L;

        var profissional = geraProfissionalParaTeste(idContato, idProfissional);
        var contato = profissional.getContatos().get(0);

        var profissionalDto = new ProfissionalDTO(profissional);

        var contatoCaptor = ArgumentCaptor.forClass(Contato.class);
        var profissionalCaptor = ArgumentCaptor.forClass(Profissional.class);

        when(contatoRepository.findById(idContato)).thenReturn(Optional.of(contato));
        when(profissionalRepository.findById(idProfissional)).thenReturn(Optional.of(profissional));

        var retorno = profissionalService.atualizarProfissional(profissionalDto);
        verify(profissionalRepository, times(1)).save(profissionalCaptor.capture());

        var profissionalCapturado = profissionalCaptor.getValue();

        Assertions.assertEquals(idProfissional, profissionalCapturado.getId());
        Assertions.assertEquals(profissional.getNome(), profissionalCapturado.getNome());
        Assertions.assertEquals(profissional.getNascimento(), profissionalCapturado.getNascimento());
        Assertions.assertEquals(profissional.getCargo(), profissionalCapturado.getCargo());

        verify(contatoRepository, times(1)).save(contatoCaptor.capture());

        var entidadeCapturada = contatoCaptor.getValue();

        Assertions.assertEquals(idContato, entidadeCapturada.getId());
        Assertions.assertEquals(contato.getNome(), entidadeCapturada.getNome());
        Assertions.assertEquals(contato.getContato(), entidadeCapturada.getContato());
        Assertions.assertEquals(idProfissional, entidadeCapturada.getProfissional().getId());
        Assertions.assertEquals("Profissional atualizado com sucesso", retorno);

    }

    @Test
    void deveBuscarProfissionalPeloNome() {
        var primeiroProfissional = geraProfissionalParaTeste(1L, 2L);
        var segundoProfissional = geraProfissionalParaTeste(3L, 4L);

        var nome = "nome";

        when(profissionalRepository.buscarProfissionalContendoNome(nome))
                .thenReturn(Arrays.asList(primeiroProfissional, segundoProfissional));
        var retorno = profissionalService.buscarContidosNoNome(nome);

        Assertions.assertEquals(2, retorno.size());
    }

    @Test
    void deveBuscarProfissionalPeloCargo() {
        var primeiroProfissional = geraProfissionalParaTeste(1L, 2L);
        var segundoProfissional = geraProfissionalParaTeste(3L, 4L);

        var cargo = TipoCargo.DESENVOLVEDOR;

        when(profissionalRepository.buscarProfissionalPorCargo(cargo))
                .thenReturn(Arrays.asList(primeiroProfissional, segundoProfissional));
        var retorno = profissionalService.buscarPorCargo(cargo);

        Assertions.assertEquals(2, retorno.size());

    }

    private Profissional geraProfissionalParaTeste(Long idContato, Long idProfissional) {
        var profissional = Profissional.builder()
                .withId(idProfissional)
                .withNascimento(LocalDate.of(1990, 3, 20))
                .withNome("dental")
                .withCargo(TipoCargo.DESENVOLVEDOR)
                .build();

        var contato = Contato.builder()
                .withContato("contato")
                .withId(idContato)
                .withNome("simples")
                .withProfissional(profissional)
                .build();

        profissional.setContatos(Collections.singletonList(contato));
        return profissional;
    }

}
