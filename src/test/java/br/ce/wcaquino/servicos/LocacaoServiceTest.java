package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;
import org.junit.rules.ErrorCollector;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


@EnableRuleMigrationSupport
public class LocacaoServiceTest {

    private Usuario pedro;
    private LocacaoService service;
    private Locacao locacao;

    @Rule
    public ErrorCollector error = new ErrorCollector();


    @BeforeEach
    public void criaUsuarioLocacaoELocacaoService() {
        pedro = new Usuario("Pedro");
        service = new LocacaoService();
        locacao = new Locacao();
    }


    @Test
    public void testeLocacao() throws Exception {
        Filme logan = new Filme("Logan", 7, 14.99);
        locacao = service.alugarFilme(pedro, logan);

        assertEquals(14.99, locacao.getValor(), 0.01);
        assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));

        //Usando assertThat
        assertThat(locacao.getValor(), equalTo(14.99));
        assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
                is(true));

        error.checkThat(locacao.getValor(), equalTo(14.99));
        error.checkThat(locacao.getValor(), equalTo(14.99));
    }

    @Test
    public void lancarExceptionFilmeSemEstoque() {
        Filme logan = new Filme("Logan", 0, 14.99);

        assertThrows(Exception.class, () -> service.alugarFilme(pedro, logan));
    }


}
