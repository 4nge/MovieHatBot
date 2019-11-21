package ru.ange.mhb.bot.msg;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.awt.image.BufferedImage;
import java.io.InputStream;

public abstract class ResponseMsg {

    private long chatId;
    private int replyMsgId;
    private String photoPath;
    private InputStream photoStream;
    private BufferedImage bufferedImage;


    public ResponseMsg(long chatId) {
        this.chatId = chatId;
    }

    public abstract String getText();


    public SendMessage getSendMessage() {
        return new SendMessage()
                .setText(getUnicodeText())
                .setChatId(chatId)
                .setReplyMarkup(createReplyKeyboard());
    }

    public SendMessage getSendMessage(int replyMsgId) {
        return getSendMessage().setReplyToMessageId(replyMsgId);
    }

    public EditMessageText getEditMessageText(int editMsgId) {
        return new EditMessageText()
                .setText(getUnicodeText())
                .setChatId(chatId)
                .setMessageId(editMsgId)
                .setReplyMarkup((InlineKeyboardMarkup) createReplyKeyboard());
    }

    public EditMessageCaption getEditMessageCaption(int editMsgId) {
        return new EditMessageCaption()
                .setCaption(getUnicodeText())
                .setChatId(String.valueOf(chatId))
                .setMessageId(editMsgId)
                .setReplyMarkup((InlineKeyboardMarkup) createReplyKeyboard());
    }

    public SendPhoto getSendPhoto() {
        SendPhoto sp = new SendPhoto()
                .setCaption(getUnicodeText())
                .setChatId(chatId)
                .setReplyToMessageId(replyMsgId)
                .setReplyMarkup(createReplyKeyboard());

        if (photoStream != null)
            sp.setPhoto(photoPath, photoStream);
        else
            sp.setPhoto(photoPath);

        return sp;
    }

    public SendPhoto getSendPhoto(int replyMsgId) {
        return getSendPhoto().setReplyToMessageId(replyMsgId);
    }

    public EditMessageMedia getEditMessageMedia(int editMsgId) {
        InputMediaPhoto imp = new InputMediaPhoto();
        imp.setCaption(getUnicodeText());
        imp.setMedia(photoStream, photoPath);

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

    private String getUnicodeText() {
        return EmojiParser.parseToUnicode(getText());
    }


    public int getReplyMsgId() {
        return replyMsgId;
    }

    public ResponseMsg setReplyMsgId(int replyMsgId) {
        this.replyMsgId = replyMsgId;
        return this;
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
        System.out.println(" -=- hasPhoto : photoPath = " + photoPath + "; photoStream = " + photoStream);
        return !(photoPath == null && photoStream == null);
    }
}
