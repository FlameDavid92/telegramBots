package botQuestionari;

import botListaDellaSpesa.BotListaDellaSpesa;
import com.botticelli.bot.Bot;
import com.botticelli.bot.request.methods.MessageToSend;
import com.botticelli.bot.request.methods.types.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotQuestionari extends Bot {
    Questionario[] questionari;
    Map<Long, StatoUtenteQuestionario> statoUtenti;

    public BotQuestionari(String token) {
        super(token);
        /*Questionari hardcoded*/
        Questionario questionario1 = generaQuestionario1();
        Questionario questionario2 = generaQuestionario2();
        questionari = new Questionario[2];
        questionari[0] = questionario1;
        questionari[1] = questionario2;
        statoUtenti = new HashMap<>();
    }

    @Override
    public void textMessage(Message message) {
        String inputUserMessage = message.getText();
        MessageToSend mts = null;
        Long idUtente = message.getFrom().getId();
        StatoUtenteQuestionario utStatus = statoUtenti.get(idUtente);
        if (utStatus == null || utStatus.getIndiceQuestionario() == -1) {
            /*Scegli questionario*/
            if (utStatus == null || utStatus.isStarted()) {
                statoUtenti.put(idUtente,new StatoUtenteQuestionario());
                mts = new MessageToSend(message.getFrom().getId(), "Scegli un questionario");
                mts.setReplyMarkup(getStartKeyboard());
                statoUtenti.get(idUtente).setStatoDomanda();
            } else {
                boolean check = false;
                for (int i = 0; i < questionari.length; i++) {
                    if (inputUserMessage.equals(questionari[i].getNome())) {
                        check = true;
                        utStatus.setIndiceQuestionario(i, questionari[i].getNDomande());
                        break;
                    }
                }
                if (!check) utStatus.setStatoStart();
            }
        }

        if (utStatus!=null && utStatus.getIndiceQuestionario() >= 0) {
            try{
                Domanda domanda = questionari[utStatus.getIndiceQuestionario()].getDomande().get(utStatus.getIndiceDomanda());
                List<String> risposte = domanda.getRisposte();
                if (utStatus.answered()) {
                    /*controlla risposta e inserisci punti*/
                    if (inputUserMessage.equals(risposte.get(domanda.getIndiceRispostaCorretta()))){
                        statoUtenti.get(idUtente).addPunto();
                    }
                    statoUtenti.get(idUtente).prossimaDomanda();
                    statoUtenti.get(idUtente).setStatoDomanda();
                }

                /*invia domanda se c'è una prossima domanda, altrimenti invia punti.*/
                if (!utStatus.isEnded()) {
                    domanda = questionari[utStatus.getIndiceQuestionario()].getDomande().get(utStatus.getIndiceDomanda());
                    risposte = domanda.getRisposte();

                    List<List<KeyboardButton>> keyboard = new ArrayList<>();
                    List<KeyboardButton> line = null;
                    for (int i = 0; i < risposte.size(); i++) {
                        if (i % 2 == 0) {
                            line = new ArrayList<>();
                            line.add(new KeyboardButton(risposte.get(i)));
                            if (i == risposte.size() - 1) keyboard.add(line);
                        } else {
                            line.add(new KeyboardButton(risposte.get(i)));
                            keyboard.add(line);
                        }
                    }
                    utStatus.setStatoRisposta();
                    ReplyKeyboardMarkupWithButtons replyKeyboard = new ReplyKeyboardMarkupWithButtons(keyboard);
                    replyKeyboard.setResizeKeyboard(true);
                    mts = new MessageToSend(message.getFrom().getId(), domanda.getDomanda());
                    mts.setReplyMarkup(replyKeyboard);
                } else {
                    mts = new MessageToSend(message.getFrom().getId(), utStatus.getStringPunteggio()+"\nVuoi giocare ancora? Scegli un questionario.");
                    mts.setReplyMarkup(getStartKeyboard());
                    statoUtenti.put(idUtente,new StatoUtenteQuestionario());
                    statoUtenti.get(idUtente).setStatoDomanda();
                }
            }catch (Exception e) {
                System.out.println("ERRORE DOMANDE!!!");
            }
        }
        sendMessage(mts);
    }

    private ReplyKeyboardMarkupWithButtons getStartKeyboard(){
        List<List<KeyboardButton>> keyboard = new ArrayList<>();
        List<KeyboardButton> line = new ArrayList<>();
        for (Questionario questionario : questionari) {
            line.add(new KeyboardButton(questionario.getNome()));
        }
        keyboard.add(line);
        ReplyKeyboardMarkupWithButtons replyKeyboard = new ReplyKeyboardMarkupWithButtons(keyboard);
        replyKeyboard.setResizeKeyboard(true);
        return replyKeyboard;
    }

    private Questionario generaQuestionario1() {
        Questionario questionario = new Questionario("ProvaQuiz");
        try{
            ArrayList<String> risposte = new ArrayList<>();
            risposte.add("Paolo");
            risposte.add("Davide");
            risposte.add("Giovanni");
            risposte.add("Fabio");
            Domanda domanda1 = new Domanda("Come mi chiamo?",risposte,1);
            risposte = new ArrayList<>();
            risposte.add("Onofrio");
            risposte.add("Gesualdo");
            risposte.add("Di solito mi chiamano gli altri");
            risposte.add("PincoPallino");
            Domanda domanda2 = new Domanda("Come ti chiami?",risposte,2);
            questionario.addDomanda(domanda1);
            questionario.addDomanda(domanda2);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return questionario;
    }

    private Questionario generaQuestionario2() {
        Questionario questionario = new Questionario("JavaTest :P");
        try{
            ArrayList<String> risposte = new ArrayList<>();
            risposte.add("Console.WriteLine(\"Hello World\");");
            risposte.add("System.out.println(\"Hello World\");");
            risposte.add("echo(\"Hello World\");");
            risposte.add("print (\"Hello World\");");
            Domanda domanda1 = new Domanda("What is a correct syntax to output \"Hello World\" in Java?",risposte,1);
            risposte = new ArrayList<>();
            risposte.add("False");
            risposte.add("True");
            Domanda domanda2 = new Domanda("Java is short for \"JavaScript\".",risposte,0);
            risposte = new ArrayList<>();
            risposte.add("/*This is a comment");
            risposte.add("//This is a comment");
            risposte.add("#This is a comment");
            Domanda domanda3 = new Domanda("How do you insert COMMENTS in Java code?",risposte,1);
            risposte = new ArrayList<>();
            risposte.add("String");
            risposte.add("Txt");
            risposte.add("string");
            risposte.add("myString");
            Domanda domanda4 = new Domanda("Which data type is used to create a variable that should store text?",risposte,0);
            risposte = new ArrayList<>();
            risposte.add("x = 2.8f;");
            risposte.add("byte x = 2.8f;");
            risposte.add("float x = 2.8f;");
            risposte.add("int x = 2.8f;");
            Domanda domanda5 = new Domanda("How do you create a variable with the floating number 2.8?",risposte,2);
            risposte = new ArrayList<>();
            risposte.add("True");
            risposte.add("False");
            Domanda domanda6 = new Domanda("The value of a string variable can be surrounded by single quotes.",risposte,1);
            risposte = new ArrayList<>();
            risposte.add("tuc()");
            risposte.add("toUpperCase()");
            risposte.add("upperCase()");
            risposte.add("touppercase()");
            Domanda domanda7 = new Domanda("Which method can be used to return a string in upper case letters?",risposte,1);
            risposte = new ArrayList<>();
            risposte.add("class myObj = new MyClass();");
            risposte.add("MyClass myObj = new MyClass();");
            risposte.add("class MyClass = new myObj();");
            risposte.add("new myObj = MyClass();");
            Domanda domanda8 = new Domanda("What is the correct way to create an object called myObj of MyClass?",risposte,1);
            questionario.addDomanda(domanda1);
            questionario.addDomanda(domanda2);
            questionario.addDomanda(domanda3);
            questionario.addDomanda(domanda4);
            questionario.addDomanda(domanda5);
            questionario.addDomanda(domanda6);
            questionario.addDomanda(domanda7);
            questionario.addDomanda(domanda8);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return questionario;
    }

    @Override
    public void audioMessage(Message message) {

    }

    @Override
    public void videoMessage(Message message) {

    }

    @Override
    public void voiceMessage(Message message) {

    }

    @Override
    public void stickerMessage(Message message) {

    }

    @Override
    public void documentMessage(Message message) {

    }

    @Override
    public void photoMessage(Message message) {

    }

    @Override
    public void contactMessage(Message message) {

    }

    @Override
    public void locationMessage(Message message) {

    }

    @Override
    public void venueMessage(Message message) {

    }

    @Override
    public void newChatMemberMessage(Message message) {

    }

    @Override
    public void newChatMembersMessage(Message message) {

    }

    @Override
    public void leftChatMemberMessage(Message message) {

    }

    @Override
    public void newChatTitleMessage(Message message) {

    }

    @Override
    public void newChatPhotoMessage(Message message) {

    }

    @Override
    public void groupChatPhotoDeleteMessage(Message message) {

    }

    @Override
    public void groupChatCreatedMessage(Message message) {

    }

    @Override
    public void inLineQuery(InlineQuery inlineQuery) {

    }

    @Override
    public void chose_inline_result(ChosenInlineResult chosenInlineResult) {

    }

    @Override
    public void callback_query(CallbackQuery callbackQuery) {

    }

    @Override
    public void gameMessage(Message message) {

    }

    @Override
    public void videoNoteMessage(Message message) {

    }

    @Override
    public void pinnedMessage(Message message) {

    }

    @Override
    public void preCheckOutQueryMessage(PreCheckoutQuery preCheckoutQuery) {

    }

    @Override
    public void shippingQueryMessage(ShippingQuery shippingQuery) {

    }

    @Override
    public void invoiceMessage(Message message) {

    }

    @Override
    public void successfulPaymentMessage(Message message) {

    }

    @Override
    public void routine() {

    }
}
