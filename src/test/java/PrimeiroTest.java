import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrimeiroTest {

    private Usuario pedro;
    private LocacaoService locacao;

    @BeforeEach
    public void criaUsuarioELocacao() {
        pedro = new Usuario("Pedro");
        locacao = new LocacaoService();
    }

    @Test
    public void deveRetornaDataDevolucao() {

        Filme filme = new Filme("Avatar", 5, 9.90);
        locacao.alugarFilme(pedro, filme);

        assertEquals("Avatar", filme.getNome());
        assertEquals(5, filme.getEstoque());
        assertEquals(9.90, filme.getPrecoLocacao());
    }


}
