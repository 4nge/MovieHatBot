package ru.ange.mhb.bot.msg.rkm;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class ReplyKeyboardBtt extends KeyboardButton  {

    public ReplyKeyboardBtt(String text) {
        super();
        super.setText( EmojiParser.parseToUnicode( text ) );
    }


    public boolean isQuery(String query) {
        return query.equals( getText() );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReplyKeyboardBtt) {
            ReplyKeyboardBtt bttObj = (ReplyKeyboardBtt) obj;
            if (bttObj.getText().equals(this.getText())) {
                return true;
            }
        }
        return false;
    }

}
