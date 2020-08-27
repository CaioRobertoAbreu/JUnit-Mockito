package br.ce.wcaquino.servicos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class CalculadoraTest {

   int a, b, resultado;
   private Calculadora calculadora;

   @BeforeEach
   public void instanciaCalculadora() {
       calculadora = new Calculadora();
   }

    @Test
    public void deveSomarDoisNumeros() {
        a = 3;
        b = 5;

        resultado = calculadora.soma(a, b);

        assertEquals(8, resultado);
    }

    @Test
    public void deveSubtrairDoisNumeros(){
        a = 10;
        b = 20;

        resultado = calculadora.subtrai(a, b);

        assertEquals(-10, resultado);
    }

    @Test
    public void deveDividirDoisNumeros() {
       a = 20;
       b = 2;

       resultado = calculadora.dividi(a, b);

       assertEquals(10, resultado);
    }

    @Test
    public void lancarExcecaoDivisaoPorZero() {
        a = 20;
        b = 0;

        ArithmeticException exception = assertThrows(ArithmeticException.class, () ->
                calculadora.dividi(a, b));

        assertTrue(exception.getMessage().equalsIgnoreCase("divisao por zero."));

    }
}
