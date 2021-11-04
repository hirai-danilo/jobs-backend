package com.simplesdental;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.simplesdental.dtos.ContatoDTO;
import com.simplesdental.entities.Contato;
import com.simplesdental.entities.Profissional;
import com.simplesdental.exceptions.EntityNotFoundException;
import com.simplesdental.exceptions.InvalidAttributeException;
import com.simplesdental.repositories.ContatoRepository;
import com.simplesdental.repositories.ProfissionalRepository;
import com.simplesdental.services.ContatoService;

class ContatoServiceTest {

    private ContatoRepository contatoRepository;

    private ProfissionalRepository profissionalRepository;

    private ContatoService contatoService;

    @BeforeEach
    public void init() {
        this.contatoRepository = mock(ContatoRepository.class);
        this.profissionalRepository = mock(ProfissionalRepository.class);
        this.contatoService = new ContatoService(contatoRepository,
                profissionalRepository);
    }

    @Test
    void deveBuscarContatoPeloId() {
        var idContato = 1L;
        var idProfissional = 2L;

        var contato = geraContatoParaTeste(idContato, idProfissional);

        when(contatoRepository.findById(idContato)).thenReturn(Optional.of(contato));
        var retorno = contatoService.buscarContatoPorId(idContato);

        verify(contatoRepository, times(1)).findById(idContato);

        Assertions.assertEquals(idContato, retorno.getId());
        Assertions.assertEquals(contato.getNome(), retorno.getNome());
        Assertions.assertEquals(contato.getContato(), retorno.getContato());
        Assertions.assertEquals(idProfissional, retorno.getIdProfissional());

    }

    @Test
    void deveRetornarExcecaoDeContatoNaoEncontradoNaBuscaDeContato() {
        var idContato = 1L;

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> contatoService.buscarContatoPorId(idContato),
                "Contato de id: 1 não encontrado");
        verify(contatoRepository, times(1)).findById(idContato);

    }

    @Test
    void deveExcluirContato() {
        var idContato = 1L;
        var idProfissional = 2L;

        var contato = geraContatoParaTeste(idContato, idProfissional);

        when(contatoRepository.findById(idContato)).thenReturn(Optional.of(contato));

        var retorno = contatoService.excluirContato(idContato);
        verify(contatoRepository, times(1)).deleteById(idContato);
        Assertions.assertEquals("Contato de id: 1 excluido com sucesso", retorno);

    }

    @Test
    void deveRetornarErroDeContatoComIdNaCriacao() {
        var idContato = 1L;

        var contato = geraContatoParaTeste(idContato, null);
        var contatoDto = new ContatoDTO(contato);

        Assertions.assertThrows(InvalidAttributeException.class,
                () -> contatoService.criarContato(contatoDto),
                "Contato não pode ser criado por já possui id");
        verify(contatoRepository, times(0)).save(any());

    }

    @Test
    void deveRetornarErroDeContatoSemNome() {
        var contato = geraContatoParaTeste(null, null);
        contato.setNome(null);

        var contatoDto = new ContatoDTO(contato);

        Assertions.assertThrows(InvalidAttributeException.class,
                () -> contatoService.criarContato(contatoDto),
                "Contato não possui nome");
        verify(contatoRepository, times(0)).save(any());

    }

    @Test
    void deveRetornarErroDeContatoSemContato() {
        var contato = geraContatoParaTeste(null, null);
        contato.setContato(null);

        var contatoDto = new ContatoDTO(contato);

        Assertions.assertThrows(InvalidAttributeException.class,
                () -> contatoService.criarContato(contatoDto),
                "Contato não possui contato");
        verify(contatoRepository, times(0)).save(any());

    }

    @Test
    void deveRetornarErroDeProfissionalNaoEncontrado() {
        var idProfissional = 2L;

        var contato = geraContatoParaTeste(null, idProfissional);

        var contatoDto = new ContatoDTO(contato);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> contatoService.criarContato(contatoDto),
                "Profissional de id: 2 não encontrado");

        verify(contatoRepository, times(0)).save(any());

    }

    @Test
    void deveCadastrarContato() {
        var idProfissional = 2L;

        var contato = geraContatoParaTeste(null, idProfissional);

        var contatoDto = new ContatoDTO(contato);

        var contatoCaptor = ArgumentCaptor.forClass(Contato.class);

        when(profissionalRepository.findById(idProfissional)).thenReturn(Optional.of(contato.getProfissional()));
        var retorno = contatoService.criarContato(contatoDto);
        verify(contatoRepository, times(1)).save(contatoCaptor.capture());

        var entidadeCapturada = contatoCaptor.getValue();


        Assertions.assertNull(entidadeCapturada.getId());
        Assertions.assertEquals(contatoDto.getNome(), entidadeCapturada.getNome());
        Assertions.assertEquals(contatoDto.getContato(), entidadeCapturada.getContato());
        Assertions.assertEquals(idProfissional, entidadeCapturada.getProfissional().getId());
        Assertions.assertEquals("Contato criado com sucesso", retorno);

    }

    @Test
    void deveRetornarErroDeContatoSemIdParaAtualizacao() {
        var idProfissional = 2L;

        var contato = geraContatoParaTeste(null, idProfissional);

        var contatoDto = new ContatoDTO(contato);

        Assertions.assertThrows(InvalidAttributeException.class,
                () -> contatoService.atualizarContato(contatoDto),
                "Contato não pode ser atualizado, id nulo");

        verify(contatoRepository, times(0)).save(any());

    }

    @Test
    void deveAtualizarContato() {
        var idContato = 1L;
        var idProfissional = 2L;

        var contato = geraContatoParaTeste(idContato, idProfissional);

        var contatoDto = new ContatoDTO(contato);

        var contatoCaptor = ArgumentCaptor.forClass(Contato.class);

        when(contatoRepository.findById(idContato)).thenReturn(Optional.of(contato));
        when(profissionalRepository.findById(idProfissional)).thenReturn(Optional.of(contato.getProfissional()));
        var retorno = contatoService.atualizarContato(contatoDto);
        verify(contatoRepository, times(1)).save(contatoCaptor.capture());

        var entidadeCapturada = contatoCaptor.getValue();


        Assertions.assertEquals(idContato, entidadeCapturada.getId());
        Assertions.assertEquals(contatoDto.getNome(), entidadeCapturada.getNome());
        Assertions.assertEquals(contatoDto.getContato(), entidadeCapturada.getContato());
        Assertions.assertEquals(idProfissional, entidadeCapturada.getProfissional().getId());
        Assertions.assertEquals("Contato atualizado com sucesso", retorno);

    }

    @Test
    void deveBuscarContatosPeloNome() {
        var primeiroContato = geraContatoParaTeste(1L, 2L);
        var segundoContato = geraContatoParaTeste(3L, 4L);

        var nome = "nome";

        when(contatoRepository.buscarContatosContendoNome(nome))
                .thenReturn(Arrays.asList(primeiroContato, segundoContato));
        var retorno = contatoService.buscarContidosNoNomeOuContato(nome, null);

        verify(contatoRepository, times(1)).buscarContatosContendoNome(nome);
        verify(contatoRepository, times(0)).buscarContatosContendoContato(any());
        Assertions.assertEquals(2, retorno.size());

    }

    @Test
    void deveBuscarContatosPeloContato() {
        var primeiroContato = geraContatoParaTeste(1L, 2L);
        var segundoContato = geraContatoParaTeste(3L, 4L);

        var contato = "contato";

        when(contatoRepository.buscarContatosContendoContato(contato))
                .thenReturn(Arrays.asList(primeiroContato, segundoContato));
        var retorno = contatoService.buscarContidosNoNomeOuContato(null, contato);

        verify(contatoRepository, times(0)).buscarContatosContendoNome(any());
        verify(contatoRepository, times(1)).buscarContatosContendoContato(contato);
        Assertions.assertEquals(2, retorno.size());

    }

    @Test
    void deveRetornarExcecaoDeNomeEContatoNaoInformados() {

        Assertions.assertThrows(InvalidAttributeException.class,
                () -> contatoService.buscarContidosNoNomeOuContato(null, null),
                "Contato não pode ser atualizado, id nulo");

        verify(contatoRepository, times(0)).buscarContatosContendoNome(any());
        verify(contatoRepository, times(0)).buscarContatosContendoContato(any());

    }


    private Contato geraContatoParaTeste(Long idContato, Long idProfissional) {
        var profissional = Profissional.builder()
                .withId(idProfissional)
                .build();
        return Contato.builder()
                .withContato("contato")
                .withId(idContato)
                .withNome("simples")
                .withProfissional(profissional)
                .build();
    }

}
