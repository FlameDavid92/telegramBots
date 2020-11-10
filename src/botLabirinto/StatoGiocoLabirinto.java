package botLabirinto;

import botLabirinto.entitagioco.EntitaLabirinto;
import botLabirinto.entitagioco.Giocatore;
import botLabirinto.entitagioco.Mostro;
import botLabirinto.entitagioco.Uscita;

public class StatoGiocoLabirinto {
    private Giocatore giocatore;
    private Mostro mostro;
    private boolean inGame;
    private EntitaLabirinto[][] labirinto;
    private Uscita uscitaLabirinto;

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

    public EntitaLabirinto[][] getLabirinto() {
        return labirinto;
    }
    public Uscita getUscitaLabirinto() {
        return uscitaLabirinto;
    }
    public void setLabirintoEUscita(EntitaLabirinto[][] labirinto, Uscita uscita) {
        this.labirinto = labirinto;
        this.uscitaLabirinto = uscita;
    }
}
