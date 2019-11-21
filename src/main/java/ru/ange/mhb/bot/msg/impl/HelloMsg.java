package ru.ange.mhb.bot.msg.impl;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.ange.mhb.bot.msg.ResponseMsg;
import ru.ange.mhb.bot.msg.rkm.ReplyKeyboardBtt;
import ru.ange.mhb.utils.Constants;

import java.util.ArrayList;
import java.util.List;


public class HelloMsg extends ResponseMsg {

    public static final BasicReplyKeyboardMarkup BRKM = new BasicReplyKeyboardMarkup();

    public HelloMsg(long chatId) {
        super(chatId);
    }

    @Override
    public String getText() {
        return Constants.START_MSG_TEXT;
    }

    @Override
    protected ReplyKeyboardMarkup createReplyKeyboard() {
        return BRKM;
    }

    public static class BasicReplyKeyboardMarkup extends ReplyKeyboardMarkup {

        public static final ReplyKeyboardBtt FAVORITE_BTT = new ReplyKeyboardBtt(":closed_book: Избранное" );
        public static final ReplyKeyboardBtt INFO_BTT = new ReplyKeyboardBtt(":information_source: Инфо" );
        public static final ReplyKeyboardBtt SETTING_BTT = new ReplyKeyboardBtt(":gear: Настройки" );

        public BasicReplyKeyboardMarkup() {
            super();

            KeyboardRow row = new KeyboardRow();
            row.add(FAVORITE_BTT);
            row.add(INFO_BTT);
            row.add(SETTING_BTT);

            List<KeyboardRow> keyboard = new ArrayList<>();
            keyboard.add(row);

            this.setKeyboard(keyboard);
            this.setResizeKeyboard(true);
        }
    }

}
