package botLabirinto.entitagioco;

public class Uscita extends EntitaLabirinto{
    public Uscita(int posX, int posY) {
        super(posX, posY);
    }
    @Override
    public String toString(){
        return "↳  ";
    }
}