package ru.ange.mhb.bot.msg;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.ange.mhb.bot.msg.rkm.ReplyKeyboardBtt;
import ru.ange.mhb.utils.Constants;

import java.util.ArrayList;
import java.util.List;


public class HelloMsg implements TextMsg {

    public static final BasicReplyKeyboardMarkup BRKM = new BasicReplyKeyboardMarkup();

    @Override
    public BotApiMethod<Message> getMessage(long chatId) {
        return new SendMessage()
                .setText( Constants.START_MSG_TEXT )
                .setReplyMarkup( BRKM )
                .setChatId( chatId );
    }


    public static class BasicReplyKeyboardMarkup extends ReplyKeyboardMarkup {

        public static final ReplyKeyboardBtt INFO_BTT = new ReplyKeyboardBtt(":information_source: Информация" );
        public static final ReplyKeyboardBtt SETTING_BTT = new ReplyKeyboardBtt(":gear: Настройки" );
        public static final ReplyKeyboardBtt FAVORITE_BTT = new ReplyKeyboardBtt(":closed_book: Избранное" );

        public BasicReplyKeyboardMarkup() {
            super();

            KeyboardRow row = new KeyboardRow();
            row.add( INFO_BTT );
            row.add( SETTING_BTT );
            row.add( FAVORITE_BTT );

            List<KeyboardRow> keyboard = new ArrayList<>();
            keyboard.add( row );

            this.setKeyboard( keyboard );
            this.setResizeKeyboard( true );
        }
    }
}
