package botLabirinto.entitagioco;

public class Mostro extends EntitaVivente{
    public Mostro() { super(0,0); }
    public Mostro(int posX, int posY) {
        super(posX, posY);
    }
    @Override
    public String toString(){
        return "孽";
    }
}
