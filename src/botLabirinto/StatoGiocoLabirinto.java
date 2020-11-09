package botLabirinto;

import botLabirinto.entitagioco.Giocatore;
import botLabirinto.entitagioco.Mostro;

public class StatoGiocoLabirinto {
    private Giocatore giocatore;
    private Mostro mostro;
    private boolean inGame;

    public StatoGiocoLabirinto(){
        giocatore = new Giocatore();
        mostro = new Mostro();
        inGame = false;
    }

    public Giocatore getGiocatore() {
        return giocatore;
    }
    public Mostro getMostro() {
        return mostro;
    }
    public void play(){
        inGame = true;
    }
    public void endGame(){
        inGame = false;
    }
    public boolean isInGame(){
        return inGame;
    }
}
