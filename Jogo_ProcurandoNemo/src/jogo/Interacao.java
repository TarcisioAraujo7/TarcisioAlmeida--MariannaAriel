package Jogo;

import java.util.ArrayList;
import java.util.List;

public class Interacao {
    private List<Room> salas_death = new ArrayList<>();
    private List<Room> salas_dialog = new ArrayList<>();
    private List<String> dialogos = new ArrayList<>();


    public List<Room> getSalas_dialog() {
        return salas_dialog;
    }

    public List<String> getDialogos() {
        return dialogos;
    }

    public List<Room> getSalas_death() {
        return salas_death;
    }
    
    public void setDeathRoom(Room sala_atual){
        salas_death.add(sala_atual);
    }

    public void setDialogRoom(Room sala_atual, String dialogo){
        salas_dialog.add(sala_atual);
        dialogos.add(dialogo);
    }
    
    public Boolean checkDeathRoom(Room sala){
        return salas_death.contains(sala);
    }

    public void interact(Room sala_atual){
        if (salas_dialog.contains(sala_atual)) {
            String dialogo = dialogos.get(salas_dialog.indexOf(sala_atual));
            System.out.println("\n" + dialogo);
        }
    }



}
