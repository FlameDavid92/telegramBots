package botLabirinto;

import botLabirinto.entitagioco.*;
import com.botticelli.bot.Bot;
import com.botticelli.bot.request.methods.MessageToSend;
import com.botticelli.bot.request.methods.types.*;

import java.util.*;

public class BotLabirinto extends Bot {
    Map<Long, StatoGiocoLabirinto> statoUtenti;
    EntitaLabirinto[][] labirinto1;


    public BotLabirinto(String token) {
        super(token);
        labirinto1 = getLabirinto1();
        statoUtenti = new HashMap<>();
    }

    @Override
    public void textMessage(Message message) {
        MessageToSend messageToSend = null;
        Long idUtente = message.getFrom().getId();
        String inputUtente = message.getText();
        if (statoUtenti.containsKey(message.getFrom().getId())) {
            StatoGiocoLabirinto statoUtente = statoUtenti.get(idUtente);
            if (statoUtente.isInGame()) {
                ComandoGioco cg = ComandoGioco.fromString(inputUtente);
                switch (cg){
                    case SU:
                        messageToSend = new MessageToSend(idUtente, "SU");
                        break;
                    case DESTRA:
                        messageToSend = new MessageToSend(idUtente, "DESTRA");
                        break;
                    case GIU:
                        messageToSend = new MessageToSend(idUtente, "GIÙ");
                        break;
                    case SINISTRA:
                        messageToSend = new MessageToSend(idUtente, "SINISTRA");
                        break;
                    case TERMINA:
                        statoUtente.endGame();
                        messageToSend = new MessageToSend(idUtente, "Hai terminato il gioco prima di completarlo!");
                        messageToSend.setReplyMarkup(getStartKeyboard());
                        break;
                    default:
                        messageToSend = new MessageToSend(idUtente, "Input non riconosciuto!");
                        messageToSend.setReplyMarkup(getGameKeyboard());
                }
            } else {
                ComandoBotLabirinto cbl = ComandoBotLabirinto.fromString(inputUtente);
                switch (cbl) {
                    case LABIRINTO1:
                        Giocatore giocatore = statoUtente.getGiocatore();
                        /*Posizione Giocatore per labirinto 1*/
                        giocatore.setPosX(1);
                        giocatore.setPosY(1);
                        /*************************************/
                        Mostro mostro = statoUtente.getMostro();
                        /*Posizione Mostro per labirinto 1*/
                        mostro.setPosX(9);
                        mostro.setPosY(1);
                        /*************************************/
                        statoUtente.play();
                        messageToSend = new MessageToSend(idUtente, getStringLabirinto(labirinto1, giocatore, mostro));
                        messageToSend.setReplyMarkup(getGameKeyboard());
                        /*cambiare stato gioco inGame e presentare tastiera gioco*/
                        break;
                    case LABIRINTO2:
                        messageToSend = new MessageToSend(idUtente, "Labirinto 2 ancora non presente!");
                        messageToSend.setReplyMarkup(getStartKeyboard());
                        break;
                    default:
                        messageToSend = new MessageToSend(idUtente, "Input non riconosciuto!");
                        messageToSend.setReplyMarkup(getStartKeyboard());
                }
            }
            sendMessage(messageToSend);
        } else {
            statoUtenti.put(message.getFrom().getId(), new StatoGiocoLabirinto());
            messageToSend = new MessageToSend(message.getFrom().getId(), "Benvenuto " + message.getFrom().getUserName() + ", scegli un labirinto con cui giocare.");
            messageToSend.setReplyMarkup(getStartKeyboard());
            sendMessage(messageToSend);
        }
        /*giocatore = new Giocatore();
        mostro = new Mostro();
        EntitaLabirinto[][] labirinto = getLabirinto1(giocatore, mostro);
        MessageToSend messageToSend = new MessageToSend(message.getFrom().getId(), getStringLabirinto(labirinto, giocatore, mostro));
        sendMessage(messageToSend);*/
    }

    private EntitaLabirinto[][] getLabirinto1() {
        int dim = 11;
        EntitaLabirinto[][] labirinto = new EntitaLabirinto[dim][dim];

        for (int i = 0; i < dim; i++) {
            labirinto[i][0] = new Muro(i, 0);
            labirinto[i][dim - 1] = new Muro(i, dim - 1);
        }
        for (int j = 0; j < dim; j++) {
            labirinto[0][j] = new Muro(0, j);
            labirinto[dim - 1][j] = new Muro(dim - 1, j);
        }

        for (int i = 1; i < dim - 1; i++) {
            for (int j = 1; j < dim - 1; j++) {
                if (i == 1 && j == 1) {
                    labirinto[i][j] = new SpazioVuoto(i, j);
                } else if (i == dim - 2 && j == 1) {
                    labirinto[i][j] = new SpazioVuoto(i, j);
                } else if (i == 5 && j == dim - 2) {
                    labirinto[i][j] = new Uscita(i, j);
                } else if ((i == dim - 2 && j == dim - 2) || (i == 1 && j == dim - 2)) {
                    labirinto[i][j] = new Vortice(i, j);
                } else if (i % 2 != 0 || j % 2 != 0) {
                    labirinto[i][j] = new SpazioVuoto(i, j);
                } else {
                    labirinto[i][j] = new Muro(i, j);
                }
            }
        }
        return labirinto;
    }

    private String getStringLabirinto(EntitaLabirinto[][] labirinto, Giocatore giocatore, Mostro mostro) {
        String ret = "";
        for (int i = 0; i < labirinto.length; i++) {
            for (int j = 0; j < labirinto[0].length; j++) {
                if (i == mostro.getPosX() && j == mostro.getPosY()) {
                    ret += mostro.toString();
                } else if (i == giocatore.getPosX() && j == giocatore.getPosY()) {
                    ret += giocatore.toString();
                } else {
                    ret += labirinto[i][j].toString();
                }
            }
            ret += "\n";
        }
        return ret;
    }

    private ReplyKeyboardMarkupWithButtons getStartKeyboard() {
        List<List<KeyboardButton>> keyboard = new ArrayList<>();
        List<KeyboardButton> line = new ArrayList<>();
        line.add(new KeyboardButton("Labirinto 1"));
        line.add(new KeyboardButton("Labirinto 2"));
        keyboard.add(line);
        ReplyKeyboardMarkupWithButtons replyKeyboard = new ReplyKeyboardMarkupWithButtons(keyboard);
        replyKeyboard.setResizeKeyboard(true);
        return replyKeyboard;
    }

    private ReplyKeyboardMarkupWithButtons getGameKeyboard() {
        List<List<KeyboardButton>> keyboard = new ArrayList<>();
        List<KeyboardButton> line = new ArrayList<>();
        line.add(new KeyboardButton("SU"));
        line.add(new KeyboardButton("DESTRA"));
        keyboard.add(line);
        line = new ArrayList<>();
        line.add(new KeyboardButton("GIÙ"));
        line.add(new KeyboardButton("SINISTRA"));
        keyboard.add(line);
        line = new ArrayList<>();
        line.add(new KeyboardButton("TERMINA"));
        keyboard.add(line);
        ReplyKeyboardMarkupWithButtons replyKeyboard = new ReplyKeyboardMarkupWithButtons(keyboard);
        replyKeyboard.setResizeKeyboard(true);
        return replyKeyboard;
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
