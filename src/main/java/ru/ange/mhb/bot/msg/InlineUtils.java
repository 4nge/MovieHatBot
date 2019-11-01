package ru.ange.mhb.bot.msg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.ange.mhb.bot.msg.callback.Callback;
import ru.ange.mhb.bot.msg.callback.PagesCallback;

import java.util.ArrayList;
import java.util.List;

public class InlineUtils {

    private static final ObjectMapper OM = new ObjectMapper();

    public static List<List<InlineKeyboardButton>> createInlineRowsKeyboard(List<InlineKeyboardButton> buttons) {
        List<List<InlineKeyboardButton>> result = new ArrayList<>();
        for (InlineKeyboardButton btt : buttons) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add( btt );
            result.add( rowInline );
        }
        return result;
    }

    public static List<List<InlineKeyboardButton>> createInlineKeyboard(List<InlineKeyboardButton> buttons) {
        List<List<InlineKeyboardButton>> result = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        for (InlineKeyboardButton btt : buttons) {
            rowInline.add( btt );
        }
        result.add( rowInline );
        return result;
    }

    public static InlineKeyboardButton createInlineKeyboardBtt(String text, String callback) {
        return new InlineKeyboardButton()
                .setText( EmojiParser.parseToUnicode( text ) )
                .setCallbackData( callback );
    }

    public static InlineKeyboardButton createInlineKeyboardBtt(String text, Callback callback) {
        try {
            return new InlineKeyboardButton()
                    .setText( EmojiParser.parseToUnicode( text ) )
                    .setCallbackData( OM.writeValueAsString( callback ) );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
