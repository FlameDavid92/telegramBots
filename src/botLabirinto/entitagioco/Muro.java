package botLabirinto.entitagioco;

public class Muro extends EntitaLabirinto{
    public Muro(int posX, int posY) {
        super(posX, posY);
    }
    @Override
    public String toString(){
        return "◆";
    }
}
