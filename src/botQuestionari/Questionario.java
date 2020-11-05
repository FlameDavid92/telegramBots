package botQuestionari;

import java.util.ArrayList;
import java.util.List;

public class Questionario {
    private String nome;
    private List<Domanda> domande;
    public Questionario(String nome){
        this.nome = nome;
        domande = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    protected void addDomanda(Domanda domanda){
        domande.add(domanda);
    }

    protected void removeDomanda(Domanda domanda){
        domande.remove(domanda);
    }

    public int getNDomande(){
        return domande.size();
    }

    public List<Domanda> getDomande() throws Exception {
        List<Domanda> domandeCopy = new ArrayList<>();
        for(Domanda domanda : domande){
            domandeCopy.add(new Domanda(domanda.getDomanda(),domanda.getRisposte(),domanda.getIndiceRispostaCorretta()));
        }
        return domandeCopy;
    }
}
