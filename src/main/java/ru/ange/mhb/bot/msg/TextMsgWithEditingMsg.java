package ru.ange.mhb.bot.msg;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface TextMsgWithEditingMsg extends TextMsg {

    EditMessageText getMessage(long chatId, int msgId);

}
