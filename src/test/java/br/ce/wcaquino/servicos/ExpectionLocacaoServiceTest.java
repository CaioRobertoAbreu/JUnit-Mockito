package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

@EnableRuleMigrationSupport
public class ExpectionLocacaoServiceTest {

    @InjectMocks
    private LocacaoService service;

    private Usuario pedro;
    private Locacao locacao;
    private List<Filme> filmes;

    @Mock
    private SPCSerasaService spcSerasa;
    @Mock
    private LocacaoDao dao;



    @BeforeEach
    public void setup(){
        pedro = new Usuario("Pedro");
        locacao = new Locacao();
        filmes = new ArrayList<>();

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void deveTratarErroSPCSerasa() {
        filmes.add(new Filme("Logan", 6, 9.99));

        Mockito.when(spcSerasa.estaNegativado(pedro)).thenThrow(new RuntimeException());

        Assertions.assertThrows(RuntimeException.class, () -> spcSerasa.estaNegativado(pedro));

    }

}
