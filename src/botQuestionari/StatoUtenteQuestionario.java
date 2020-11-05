package botQuestionari;

public class StatoUtenteQuestionario {
    private enum StatoUtente{ START,DOMANDA,RISPOSTA }
    private StatoUtente statoUtente;
    private int indiceQuestionario;
    private int indiceDomanda;
    private int nDomande;
    private int punti;

    public StatoUtenteQuestionario(){
        this.indiceQuestionario = -1;
        statoUtente = StatoUtente.START;
        indiceDomanda = 0;
        punti = 0;
    }

    public int getIndiceQuestionario() {
        return indiceQuestionario;
    }

    protected void setIndiceQuestionario(int indiceQuestionario, int nDomande){
        this.indiceQuestionario = indiceQuestionario;
        this.nDomande = nDomande;
    }

    public int getIndiceDomanda() {
        return indiceDomanda;
    }

    public String getStringPunteggio() {
        if(punti == nDomande) return "Complimenti hai risposto correttamente a tutte le domande!!!";
        else return "Hai risposto correttamente a "+((punti!=1) ? punti+" domande." : punti+" domanda.");
    }

    protected void addPunto(){
        punti++;
    }
    protected void prossimaDomanda(){
        indiceDomanda++;
    }

    protected boolean isEnded(){ return indiceDomanda == nDomande; }

    protected void setStatoStart(){
        statoUtente = StatoUtente.START;
    }
    protected void setStatoDomanda(){
        statoUtente = StatoUtente.DOMANDA;
    }
    protected void setStatoRisposta(){
        statoUtente = StatoUtente.RISPOSTA;
    }

    public boolean isStarted(){
        return statoUtente.equals(StatoUtente.START);
    }
    public boolean isWaitingQuestion(){
        return statoUtente.equals(StatoUtente.DOMANDA);
    }
    public boolean answered(){
        return statoUtente.equals(StatoUtente.RISPOSTA);
    }
}
