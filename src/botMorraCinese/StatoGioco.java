package botMorraCinese;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StatoGioco {
    private long idUtente;
    private Integer[] carteGenerate;
    private int lastIndex;
    private int nSassi;
    private int nCarte;
    private int nForbici;
    private int puntiUtente;
    private int puntiBot;
    private ArrayList<Integer> manoUtente;
    private ArrayList<Integer> manoBot;
    private TipoGioco tipoGioco;

    public enum TipoGioco{NORMAL, SPECIAL, EVIL}

    public StatoGioco(long idUtente) {
        this.idUtente = idUtente;
        tipoGioco = TipoGioco.NORMAL;
        puntiUtente = 0;
        puntiBot = 0;
        carteGenerate = new Integer[60];
        lastIndex = 0;
        Random rndm = new Random();
        nSassi = 0;
        nCarte = 0;
        nForbici = 0;
        int temp;
        for (int i = 0; i < 60; i++) {
            temp = rndm.nextInt(3);
            switch (temp) {
                case 0:
                    nSassi++;
                    break;
                case 1:
                    nCarte++;
                    break;
                case 2:
                    nForbici++;
                    break;
            }
            carteGenerate[i] = rndm.nextInt(3);
        }
    }

    public int getnCarte() {
        return nCarte;
    }
    protected void minusCarte() {
        nCarte -= 1;
    }
    public int getnForbici() {
        return nForbici;
    }
    protected void minusForbici() {
        nForbici -= 1;
    }
    public int getnSassi() {
        return nSassi;
    }
    protected void minusSassi() {
        nSassi -= 1;
    }

    public boolean generaCarteTurno() {
        Integer[] ret = new Integer[6];
        if (lastIndex + 6 < carteGenerate.length) {
            manoUtente = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                manoUtente.add(carteGenerate[lastIndex++]);
            }
            manoBot =  new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                manoBot.add(carteGenerate[lastIndex++]);
            }
            return true;
        }else{
            System.out.println("Carte terminate!");
            return false;
        }
    }
    protected boolean vittoriaUtente(){
        puntiUtente++;
        return puntiUtente == 3;
    }
    protected boolean vittoriaBot(){
        puntiBot++;
        return puntiBot == 3;
    }
    protected void setTipoGiocoEvil(){
        tipoGioco = TipoGioco.EVIL;
    }
    protected void setTipoGiocoSpecial(){
        tipoGioco = TipoGioco.SPECIAL;
    }
    public TipoGioco getTipoGioco(){
        return tipoGioco;
    }

    public ArrayList<Integer> getManoUtente() {
        return manoUtente;
    }
    public ArrayList<Integer> getManoBot() {
        return manoBot;
    }
}
