package botQuestionari;

import java.util.ArrayList;
import java.util.List;

public class Domanda {
    private String domanda;
    private List<String> risposte;
    private int indiceRispostaCorretta;

    public Domanda(String domanda, List<String> risposte, int indiceRispostaCorretta) throws Exception {
        if(risposte.size() > 1){
            if(indiceRispostaCorretta >= 0 && indiceRispostaCorretta < risposte.size()){
                this.domanda = domanda;
                this.risposte = risposte;
                this.indiceRispostaCorretta = indiceRispostaCorretta;
            }else throw new Exception("L'indice per la risposta corretta è errato!");
        }else throw new Exception("Le domande devono avere almeno 2 risposte!");
    }

    public String getDomanda() {
        return domanda;
    }

    public int getIndiceRispostaCorretta() {
        return indiceRispostaCorretta;
    }

    public List<String> getRisposte() {
        List<String> risposteCopy = new ArrayList<>();
        for(String risposta : risposte){
            risposteCopy.add(risposta);
        }
        return risposteCopy;
    }
}
