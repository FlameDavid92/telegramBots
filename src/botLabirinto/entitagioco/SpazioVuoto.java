package botLabirinto.entitagioco;

public class SpazioVuoto extends EntitaLabirinto{
    public SpazioVuoto(int posX, int posY) {
        super(posX, posY);
    }
    @Override
    public String toString(){
        return "◇";
    }
}