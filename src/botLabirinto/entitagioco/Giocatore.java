package botLabirinto.entitagioco;

public class Giocatore extends EntitaVivente{
    public Giocatore() { super(0,0); }
    public Giocatore(int posX, int posY) {
        super(posX, posY);
    }
    @Override
    public String toString(){
        return "〠";
    }
}
