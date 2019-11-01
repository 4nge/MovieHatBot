package ru.ange.mhb.bot.msg;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface TextMsg {

    BotApiMethod<Message> getMessage(long chatId);

}
