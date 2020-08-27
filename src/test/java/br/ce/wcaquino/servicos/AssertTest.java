package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssertTest {

    @Test
    public void testaAssertivas() {

        String nome = "Caio";

        assertTrue(true);
        assertFalse(false);
        assertEquals(1, 1);
        assertEquals("Caio", nome);

        Usuario user1 = new Usuario("Joao");
        Usuario user2 = new Usuario("Joao");

        assertEquals(user1, user2); //Utiliza toString da classe
    }

}
