package botLabirinto.entitagioco;

public abstract class EntitaLabirinto {
    protected int posX;
    protected int posY;
    public EntitaLabirinto(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
    }
    public int getPosX() {
        return posX;
    }
    public int getPosY() {
        return posY;
    }
}