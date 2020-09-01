package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.daos.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {

	private LocacaoDao dao;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {
		for(Filme f : filmes){
			if(f.getEstoque() == 0){
				throw new Exception();
			}
		}

		aplicarDescontos(filmes);

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(valorTotalLocacao(filmes));

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);

		dao.salvar(locacao);
		
		return locacao;
	}

	private double valorTotalLocacao(List<Filme> filmes) {
		double soma = 0;
		for (Filme f: filmes){
			soma += f.getPrecoLocacao();
		}
		return soma;
	}

	private void aplicarDescontos(List<Filme> filmes) {
		for(int i = 0; i <= filmes.size(); i++){
			if(i == 3){
				filmes.get(2).setPrecoLocacao(filmes.get(2).getPrecoLocacao()*0.75);
			}

			if(i == 4){
				filmes.get(3).setPrecoLocacao(filmes.get(3).getPrecoLocacao()*0.5);
			}

			if(i >= 5){
				filmes.get(i-1).setPrecoLocacao(0d);
			}
		}
	}

	public void setDao(LocacaoDao dao) {
		this.dao = dao;
	}
}