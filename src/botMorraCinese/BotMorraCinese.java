package botMorraCinese;

import com.botticelli.bot.Bot;
import com.botticelli.bot.request.methods.MessageToSend;
import com.botticelli.bot.request.methods.types.*;

import java.util.*;

public class BotMorraCinese extends Bot {
    private Map<Long, StatoGioco> statoGiocoUtenti;
    private Random rndm;

    public BotMorraCinese(String token) {
        super(token);
        statoGiocoUtenti = new HashMap<>();
        rndm = new Random();
    }

    @Override
    public void textMessage(Message message) {
        String inputUserMessage = message.getText();
        MessageToSend mts = null;
        Long idUtente = message.getFrom().getId();
        StatoGioco statoGioco = statoGiocoUtenti.get(idUtente);
        if (statoGioco == null) {
            ComandoGioco c = ComandoGioco.fromString(inputUserMessage);
            StatoGioco nuovoGioco = null;
            switch(c){
                case BENVENUTO:
                    mts = new MessageToSend(message.getFrom().getId(), "Benvenuto " + message.getFrom().getUserName() + ", per avviare un nuovo gioco clicca su \"Avvia Gioco\"");
                    mts.setReplyMarkup(getStartKeyboard());
                    break;
                case AVVIASPECIAL:
                    nuovoGioco = new StatoGioco(idUtente);
                    nuovoGioco.setTipoGiocoSpecial(); /*no break così esegue anche AVVIA*/
                case AVVIAEVIL:
                    if(nuovoGioco == null){
                        nuovoGioco = new StatoGioco(idUtente);
                        nuovoGioco.setTipoGiocoEvil(); /*no break così esegue anche AVVIA*/
                    }
                case AVVIA:
                    if(nuovoGioco == null) nuovoGioco = new StatoGioco(idUtente);
                    statoGiocoUtenti.put(idUtente, nuovoGioco);
                    mts = new MessageToSend(message.getFrom().getId(), "Sono state generate " +
                            nuovoGioco.getnSassi() + " carte 'Sasso', " +
                            nuovoGioco.getnCarte() + " carte 'Carta' e " +
                            nuovoGioco.getnForbici() + " carte 'Forbice'");
                    nuovoGioco.generaCarteTurno();
                    mts.setReplyMarkup(getGameKeyboard(nuovoGioco));
            }
            sendMessage(mts);
        } else {
            /*System.out.println("manoUtente: "+statoGioco.getManoUtente());
            System.out.println("manoBot: "+statoGioco.getManoBot());*/

            int indexCartaUtente = Integer.parseInt(inputUserMessage.split(":")[0]);
            int cartaUtente = statoGioco.getManoUtente().remove(indexCartaUtente);

            int cartaBot;
            switch(statoGioco.getTipoGioco()){
                case EVIL:
                    cartaBot = getCartaBotEvil(statoGioco, cartaUtente);
                    break;
                case SPECIAL:
                    cartaBot = getCartaBotSpecial(statoGioco);
                    break;
                default:
                    cartaBot = statoGioco.getManoBot().remove(rndm.nextInt(statoGioco.getManoBot().size()));
            }

            aggiornaNumeri(statoGioco,cartaUtente);
            aggiornaNumeri(statoGioco,cartaBot);

            boolean utWin = false, botWin = false, newGen = true;
            if (cartaUtente == cartaBot) {
                mts = new MessageToSend(message.getFrom().getId(), getStringMossa(cartaBot) + "\n\nPareggio");
                if (statoGioco.getManoUtente().size() != 0) newGen = false;
            } else if ((cartaUtente == 0 && cartaBot == 2) || cartaUtente == cartaBot + 1) {
                utWin = statoGioco.vittoriaUtente();
                mts = new MessageToSend(message.getFrom().getId(), getStringMossa(cartaBot) + "\n\nVittoria " + message.getFrom().getUserName() + "");
            } else {
                botWin = statoGioco.vittoriaBot();
                mts = new MessageToSend(message.getFrom().getId(), getStringMossa(cartaBot) + "\n\nVittoria bot");
            }

            if (utWin) {
                mts = new MessageToSend(message.getFrom().getId(), getStringMossa(cartaBot) + "\n\nHai vinto!!!\nPer avviare un nuovo gioco clicca su \"Avvia Gioco\"");
                mts.setReplyMarkup(getStartKeyboard());
                statoGiocoUtenti.put(idUtente, null);
                sendMessage(mts);
            } else if (botWin) {
                mts = new MessageToSend(message.getFrom().getId(), getStringMossa(cartaBot) + "\n\nHai perso!!!\nPer avviare un nuovo gioco clicca su \"Avvia Gioco\"");
                mts.setReplyMarkup(getStartKeyboard());
                statoGiocoUtenti.put(idUtente, null);
                sendMessage(mts);
            } else {
                if (newGen && !statoGioco.generaCarteTurno()) {
                    mts = new MessageToSend(message.getFrom().getId(), getStringMossa(cartaBot) + "\n\nPartita conclusa in parità!\nPer avviare un nuovo gioco clicca su \"Avvia Gioco\"");
                    statoGiocoUtenti.put(idUtente, null);
                    sendMessage(mts);
                } else {
                    mts.setReplyMarkup(getGameKeyboard(statoGioco));
                    sendMessage(mts);
                }
            }
        }
    }

    private String getStringMossa(int carta) {
        if (carta == 0) {
            return "\uD83D\uDC4A";
        } else if (carta == 1) {
            return "\uD83D\uDD90";
        } else return "✌";
    }

    private ReplyKeyboardMarkupWithButtons getGameKeyboard(StatoGioco statoGioco) {
        List<List<KeyboardButton>> keyboard = new ArrayList<>();
        List<KeyboardButton> line = new ArrayList<>();
        ArrayList<Integer> manoUtente = statoGioco.getManoUtente();
        for (int i = 0; i < manoUtente.size(); i++) {
            if (manoUtente.get(i) == 0) {
                line.add(new KeyboardButton(i + ": \uD83D\uDC4A"));
            } else if (manoUtente.get(i) == 1) {
                line.add(new KeyboardButton(i + ": \uD83D\uDD90"));
            } else line.add(new KeyboardButton(i + ": ✌"));
        }
        keyboard.add(line);
        ReplyKeyboardMarkupWithButtons replyKeyboard = new ReplyKeyboardMarkupWithButtons(keyboard);
        replyKeyboard.setResizeKeyboard(true);
        return replyKeyboard;
    }

    private ReplyKeyboardMarkupWithButtons getStartKeyboard() {
        List<List<KeyboardButton>> keyboard = new ArrayList<>();
        List<KeyboardButton> line = new ArrayList<>();
        line.add(new KeyboardButton("Avvia Gioco"));
        line.add(new KeyboardButton("Avvia Special"));
        line.add(new KeyboardButton("Avvia Evil"));
        keyboard.add(line);
        ReplyKeyboardMarkupWithButtons replyKeyboard = new ReplyKeyboardMarkupWithButtons(keyboard);
        replyKeyboard.setResizeKeyboard(true);
        return replyKeyboard;
    }

    private int getCartaBotEvil(StatoGioco statoGioco, int cartaUtente) {
        ArrayList<Integer> manoBot = statoGioco.getManoBot();
        int index = -1;
        /*Prova a vincere*/
        for (int i = 0; i < manoBot.size(); i++) {
            if ((cartaUtente == 2 && manoBot.get(i) == 0) || (cartaUtente == manoBot.get(i) - 1)) {
                index = i;
                break;
            }
        }
        /*Prova a pareggiare*/
        if (index == -1) {
            for (int i = 0; i < manoBot.size(); i++) {
                if (cartaUtente == manoBot.get(i)) {
                    index = i;
                    break;
                }
            }
        }
        /*Perdi scegliendo a caso*/
        if (index == -1) index = rndm.nextInt(manoBot.size());

        return manoBot.remove(index);
    }

    private int getCartaBotSpecial(StatoGioco statoGioco) {
        /*Controlla quale dei tre tipi di carta ha più occorrenze nel gioco
        * (quindi ha probabilità maggiore di trovarsi nella mano dell'utente)
        * e sceglie di giocare la carta che la batte se è presente nella mano del bot
        * altrimenti ne sceglie randomicamente una.
        */
        ArrayList<Integer> manoBot = statoGioco.getManoBot();
        int choice = 0;
        /*System.out.println("Sassi:"+statoGioco.getnSassi()+
                "\nCarte:"+statoGioco.getnCarte()+
                "\nForbici:"+statoGioco.getnForbici());*/
        int temp = statoGioco.getnSassi();
        if(statoGioco.getnCarte() > temp) {
            temp = statoGioco.getnCarte();
            choice = 1;
        }
        if(statoGioco.getnForbici() > temp){
            choice = 2;
        }

        int index = -1;
        for (int i = 0; i < manoBot.size(); i++) {
            if((choice == 2 && manoBot.get(i) == 0)||(manoBot.get(i) == choice+1)){
                index = i;
                break;
            }
        }
        if (index == -1){
            index = rndm.nextInt(manoBot.size());
        }

        return manoBot.remove(index);
    }

    private void aggiornaNumeri(StatoGioco statoGioco, int carta){
        switch(carta){
            case 0:
                statoGioco.minusSassi();
                break;
            case 1:
                statoGioco.minusCarte();
                break;
            case 2:
                statoGioco.minusForbici();
        }
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
