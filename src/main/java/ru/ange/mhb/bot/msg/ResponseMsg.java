package ru.ange.mhb.bot.msg;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.ange.mhb.utils.Constants;
import ru.ange.mhb.utils.ImagesUtil;

import java.awt.image.BufferedImage;
import java.io.InputStream;

public abstract class ResponseMsg {

    private long chatId;
    private String photoPath;
    private InputStream photoStream;
    private BufferedImage bufferedImage;

    private int replyToMsgId;
    private int editMsgId;

    private String callbackQueryId;

    public ResponseMsg(long chatId) {
        this.chatId = chatId;
    }

    public abstract String getText();


    private SendMessage getBasicSendMessage() {
        SendMessage sm = new SendMessage()
                .setText(getUnicodeText())
                .setChatId(chatId)
                .setReplyMarkup(createReplyKeyboard());
        return sm;
    }

    public SendMessage getSendMessage() {
        if (replyToMsgId != 0)
            return getBasicSendMessage().setReplyToMessageId(replyToMsgId);
        else
            return getBasicSendMessage();
    }

    public EditMessageText getEditMessageText() {
        return new EditMessageText()
                .setText(getUnicodeText())
                .setChatId(chatId)
                .setMessageId(editMsgId)
                .setReplyMarkup((InlineKeyboardMarkup) createReplyKeyboard());
    }

    private SendPhoto getBasicSendPhoto() {
        SendPhoto sp = new SendPhoto()
                .setCaption(getUnicodeText())
                .setChatId(chatId)
                .setReplyMarkup(createReplyKeyboard());

        if (photoStream != null)
            sp.setPhoto(photoPath, photoStream);
        else
            sp.setPhoto(photoPath);

        return sp;
    }

    public SendPhoto getSendPhoto() {
        if (replyToMsgId != 0)
            return getBasicSendPhoto().setReplyToMessageId(replyToMsgId);
        else
            return getBasicSendPhoto();
    }

    public EditMessageCaption getEditMessageCaption() {
        EditMessageCaption emc = new EditMessageCaption()
                .setChatId(String.valueOf(chatId))
                .setMessageId(editMsgId)
                .setCaption(getUnicodeText())
                .setReplyMarkup((InlineKeyboardMarkup) createReplyKeyboard());
        return emc;
    }

    public EditMessageMedia getEditMessageMedia() {
        InputMediaPhoto imp = new InputMediaPhoto();
        imp.setCaption(getUnicodeText());

        if (photoStream != null)
            imp.setMedia(photoStream, photoPath);
        else
            imp.setMedia(photoPath);

        EditMessageMedia emm = new EditMessageMedia();
        emm.setChatId(chatId);
        emm.setReplyMarkup((InlineKeyboardMarkup) createReplyKeyboard());
        emm.setMedia(imp);
        emm.setMessageId(editMsgId);
        return emm;
    }

    public long getChatId() {
        return chatId;
    }

    public ResponseMsg setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getUnicodeText() {
        return EmojiParser.parseToUnicode(getText());
    }


    public String getPhotoPath() {
        return photoPath;
    }

    public ResponseMsg setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
        return this;
    }

    public InputStream getPhotoStream() {
        return photoStream;
    }

    public ResponseMsg setPhotoStream(InputStream photoStream) {
        this.photoStream = photoStream;
        return this;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public ResponseMsg setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        return this;
    }

    protected org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard createReplyKeyboard() {
        return null;
    }

    public boolean hasPhoto() {
        return !(photoPath == null && photoStream == null);
    }

    public int getReplyToMsgId() {
        return replyToMsgId;
    }

    public ResponseMsg setReplyToMsgId(int replyToMsgId) {
        this.replyToMsgId = replyToMsgId;
        return this;
    }

    public int getEditMsgId() {
        return editMsgId;
    }

    public ResponseMsg setEditMsgId(int editMsgId) {
        this.editMsgId = editMsgId;
        return this;
    }

    public boolean isEditMsg() {
        return editMsgId != 0;
    }

    public String getCallbackQueryId() {
        return callbackQueryId;
    }

    public ResponseMsg setCallbackQueryId(String callbackQueryId) {
        this.callbackQueryId = callbackQueryId;
        return this;
    }
}
