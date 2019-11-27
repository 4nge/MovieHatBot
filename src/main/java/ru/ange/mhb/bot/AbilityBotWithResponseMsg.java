package ru.ange.mhb.bot;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;
import ru.ange.mhb.bot.msg.ErrorMsg;
import ru.ange.mhb.bot.msg.ResponseMsg;

public abstract class AbilityBotWithResponseMsg extends AbilityBot {

    private org.telegram.abilitybots.api.sender.MessageSender sender;

    protected AbilityBotWithResponseMsg(String botToken, String botUsername, DBContext db, DefaultBotOptions botOptions) {
        super(botToken, botUsername, db, botOptions);
        this.sender = super.sender;
    }

    protected AbilityBotWithResponseMsg(String botToken, String botUsername, DBContext db) {
        super(botToken, botUsername, db);
        this.sender = super.sender;
    }

    protected AbilityBotWithResponseMsg(String botToken, String botUsername, DefaultBotOptions botOptions) {
        super(botToken, botUsername, botOptions);
        this.sender = super.sender;
    }

    protected AbilityBotWithResponseMsg(String botToken, String botUsername) {
        super(botToken, botUsername);
    }

    public void send(ResponseMsg respMsg) {

        if (respMsg instanceof ErrorMsg) {
            ErrorMsg errorMsg = (ErrorMsg) respMsg;
            if (errorMsg.getReason() != null)
                errorMsg.getReason().printStackTrace(); // TODO to logger
        }

        try {
            if (respMsg.getCallbackQueryId() != null && !respMsg.getCallbackQueryId().isEmpty()) {
                sender.execute(new AnswerCallbackQuery()
                        .setCallbackQueryId(respMsg.getCallbackQueryId())
                        .setText(respMsg.getUnicodeText()));
            } else if (respMsg.hasPhoto()) {
                if (respMsg.isEditMsg()) {
                    try {
                        execute(respMsg.getEditMessageMedia());
                    } catch (TelegramApiValidationException e) {
                        e.printStackTrace();
                        sender.execute(respMsg.getEditMessageCaption());
                    }
                } else {
                    sender.sendPhoto(respMsg.getSendPhoto());
                }
            } else {
                if (respMsg.isEditMsg())
                    sender.execute(respMsg.getEditMessageText());
                else
                    sender.execute(respMsg.getSendMessage());
            }
        } catch (Exception e) {
            e.printStackTrace(); // TODO add logger to constructor or override this method with loggered version
        }
    }
}
