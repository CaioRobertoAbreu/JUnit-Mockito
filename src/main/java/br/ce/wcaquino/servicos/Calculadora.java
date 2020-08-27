package br.ce.wcaquino.servicos;

public class Calculadora {

    public int soma(int a, int b) {
        return a + b;
    }

    public int subtrai(int a, int b) {
        return a - b;
    }

    public int dividi(int a, int b) {
        if(a == 0 || b == 0) {
            throw new ArithmeticException("Divisao por zero.");
        }
        return a/b;
    }
}


