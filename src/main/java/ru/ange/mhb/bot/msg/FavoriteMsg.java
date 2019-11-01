package ru.ange.mhb.bot.msg;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.ange.mhb.pojo.fav.FavList;
import ru.ange.mhb.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMsg implements TextMsg {

    private List<FavList> favLists;

    public FavoriteMsg(List<FavList> favLists) {
        this.favLists = favLists;
    }

    @Override
    public BotApiMethod<Message> getMessage(long chatId) {
        return new SendMessage()
                .setChatId( chatId )
                .setText( Constants.MOVIE_LISTS_MSG_TXT )
                .setReplyMarkup( createInlineKeyboardMarkup() );
    }

    private InlineKeyboardMarkup createInlineKeyboardMarkup() {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (FavList favList : favLists) {

            String ptt = favList.isPubl() ? Constants.MOVIE_LIST_PUBLIC : Constants.MOVIE_LIST_PERS;

            List<InlineKeyboardButton> movieRow = new ArrayList<>();
            movieRow.add( new InlineKeyboardButton()
                    .setText( EmojiParser.parseToUnicode( String.format( ptt, favList.getName() ) ) )
                    .setCallbackData("aa") );

            keyboard.add( movieRow );
        }


        List<InlineKeyboardButton> actionRow = new ArrayList<>();
        actionRow.add( new InlineKeyboardButton()
                .setText( EmojiParser.parseToUnicode(":heavy_plus_sign: Добавить список") )
                .setCallbackData("aa") );
        actionRow.add( new InlineKeyboardButton()
                .setText( EmojiParser.parseToUnicode(":memo: Редактировать списки") )
                .setCallbackData("aa") );


        keyboard.add( actionRow );

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup()
                .setKeyboard( keyboard );

        return markupInline;
    }
}
