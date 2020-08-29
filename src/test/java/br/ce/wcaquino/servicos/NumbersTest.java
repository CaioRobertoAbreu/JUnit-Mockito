package br.ce.wcaquino.servicos;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NumbersTest {


    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 11, 19})
    public void deveRetornarVerdadeiroParaImpar(int number) {

        assertTrue(Numbers.isOdd(number));

    }

}