package Testes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Test;

import Jogo.Interacao;
import Jogo.Room;

public class InteracaoTest {
    @Test
    public void testCheckDeathRoom() {
        Interacao interacao = new Interacao();
        Room room1 = new Room("sala teste 1");
        Room room2 = new Room("sala teste 2");

        interacao.setDeathRoom(room1);

        assertSame(true, interacao.checkDeathRoom(room1));

        assertSame(false, interacao.checkDeathRoom(room2));

    }

    @Test
    public void testSetDeathRoom() {
        Interacao interacao = new Interacao();
        Room room = new Room("sala teste");
        List<Room> salas = interacao.getSalas_death();

        assertEquals(0, salas.size());

        interacao.setDeathRoom(room);

        assertEquals(1, salas.size());

    }

    @Test
    public void testSetDialogRoom(){
        Interacao interacao = new Interacao();
        Room room = new Room("sala teste");

        List<Room> salasDialogos = interacao.getSalas_dialog();
        List<String> dialogos = interacao.getDialogos();

        assertEquals(0, salasDialogos.size());
        assertEquals(0, dialogos.size());

        interacao.setDialogRoom(room, "dialogo teste");

        assertEquals(1, salasDialogos.size());
        assertEquals(1, dialogos.size());

    }
}
