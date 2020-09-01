package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static br.ce.wcaquino.utils.DataUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


@EnableRuleMigrationSupport
public class LocacaoServiceTest {

    private Usuario pedro;
    private List<Filme> filmes;
    private Locacao locacao;

    @InjectMocks
    private LocacaoService service;

    @Mock
    private LocacaoDao dao;
    @Mock
    private SPCSerasaService spcSerasa;
    @Mock
    private EmailService email;

    @Rule
    public ErrorCollector error = new ErrorCollector();


    @BeforeEach
    public void criaUsuarioLocacaoELocacaoService() {
        pedro = new Usuario("Pedro");
        filmes = new ArrayList<>();
        service = new LocacaoService();
        locacao = new Locacao();

        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testeLocacao() throws Exception {
        filmes = Arrays.asList(new Filme("Logan", 7, 14.99),
                new Filme("Xmen", 5, 9.99));

        locacao = service.alugarFilme(pedro, filmes);

        assertEquals(14.99, locacao.getFilmes().get(0).getPrecoLocacao(), 0.01);
        assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
        assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));

        //Usando assertThat
        assertThat(locacao.getFilmes().get(0).getPrecoLocacao(), equalTo(14.99));
        assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)),
                is(true));

        error.checkThat(locacao.getFilmes().get(0).getPrecoLocacao(), equalTo(14.99));
        error.checkThat(locacao.getFilmes().get(0).getPrecoLocacao(), equalTo(14.99));
    }

    @Test
    public void lancarExceptionFilmeSemEstoque() {
        filmes = Arrays.asList(new Filme("Logan", 0, 14.99),
                new Filme("Xmen", 5, 9.99));

        assertThrows(Exception.class, () -> service.alugarFilme(pedro, filmes));
    }

    @Test
    public void deveDarDescontoDe25PorcentoNoTerceiroFilme() throws Exception {
        filmes = Arrays.asList(new Filme("Alice no país das Maravilhas", 4, 4.99),
                new Filme("O mágico de Oz", 2, 4.99),
                new Filme("Branca de Neve", 6, 4.99));

        locacao = service.alugarFilme(pedro, filmes);

        assertEquals(3.74,filmes.get(2).getPrecoLocacao(), 0.01);
    }

    @Test
    public void deVeDarDescontoDe50PorcentoNoQuartoFilme() throws Exception {
        filmes = Arrays.asList(new Filme("Alice no país das Maravilhas", 4, 4.99),
                new Filme("O mágico de Oz", 2, 4.99),
                new Filme("Branca de Neve", 6, 4.99),
                new Filme("A bela e a fera", 13, 15.99));

        locacao = service.alugarFilme(pedro, filmes);

        assertEquals(7.99,filmes.get(3).getPrecoLocacao(), 0.01);
    }

    @Test
    public void deveDarDesconto100PorcentoAPartirdoQuintoFilme() throws Exception {
        filmes = Arrays.asList(new Filme("Alice no país das Maravilhas", 4, 4.99),
                new Filme("O mágico de Oz", 2, 4.99),
                new Filme("Branca de Neve", 6, 4.99),
                new Filme("A bela e a fera", 13, 15.99),
                new Filme("Frozen", 13, 15.99),
                new Filme("Rapunzel", 9, 10.99));

        locacao = service.alugarFilme(pedro, filmes);

        assertEquals(0,filmes.get(4).getPrecoLocacao(), 0.01);
        assertEquals(0,filmes.get(5).getPrecoLocacao(), 0.01);
    }

    @Test
    public void somaComDesconto() throws Exception {
        filmes = Arrays.asList(new Filme("Alice no país das Maravilhas", 4, 10.),
                new Filme("O mágico de Oz", 2, 10.),
                new Filme("Branca de Neve", 6, 10.),
                new Filme("A bela e a fera", 13, 10.),
                new Filme("Frozen", 13, 10.),
                new Filme("Rapunzel", 9, 10.0));

        locacao = service.alugarFilme(pedro, filmes);

        Double soma = 10 + 10 + 7.5 + 5;

        assertEquals(soma, locacao.getValor());
    }

    @Ignore
    public void deveDevolverNaSegundaSeAlugadoSabado() throws Exception {
        filmes = Arrays.asList(new Filme("Logan", 7, 14.99),
                new Filme("Xmen", 5, 9.99));

        locacao = service.alugarFilme(pedro, filmes);

        boolean eSegunda = verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);

        assertTrue(eSegunda);

    }

    @Test
    public void naoDevePassarNegativado() throws Exception {
        filmes = Arrays.asList(new Filme("Filme 1", 7, 7.00),
                new Filme("Filme 2", 7, 7.00));

        Mockito.when(spcSerasa.estaNegativado(pedro)).thenReturn(true);

        assertThrows(Exception.class, () -> service.alugarFilme(pedro, filmes));
    }

    @Test
    public void deveEnviarEmailDeCobrança() {
        Locacao locacao = new Locacao();
        Usuario usuario = new Usuario("user");
        locacao.setUsuario(usuario);
        locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(-2));


        List<Locacao> pendentes = new ArrayList<>();
        pendentes.add(locacao);

        Mockito.when(dao.obterAtrasados()).thenReturn(pendentes);
        service.notificarLocacaoEmAtraso();
        Mockito.verify(email).enviarEmailCobranca(usuario);

    }
}
