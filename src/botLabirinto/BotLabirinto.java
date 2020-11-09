package botLabirinto;

import com.botticelli.bot.Bot;
import com.botticelli.bot.request.methods.MessageToSend;
import com.botticelli.bot.request.methods.types.*;

public class BotLabirinto extends Bot {
    String muro = "▉";
    String spazioVuoto = "    ";
    String giocatore = "〠";
    String mostro = "孽";
    String vortice = "✺";
    String uscita = "↳";

    public BotLabirinto(String token)
    {
        super(token);
    }

    @Override
    public void textMessage(Message message) {
        String labirinto = getLabirinto1();
        MessageToSend messageToSend = new MessageToSend(message.getFrom().getId(), labirinto);
        sendMessage(messageToSend);
    }

    private String getLabirinto1(){
        String ret = "";
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if(i%2!=0 && j%2 != 0) ret += spazioVuoto;
                else ret += muro;
            }
            ret +="\n";
        }
        return ret;
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
