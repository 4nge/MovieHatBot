package ru.ange.mhb.bot.msg;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.ange.mhb.bot.msg.callback.fav.AddFavListCallback;
import ru.ange.mhb.pojo.fav.FavList;
import ru.ange.mhb.pojo.fav.FavMovie;
import ru.ange.mhb.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class FavListsMsg implements TextMsg {

    private List<FavList> favLists;
    private int actIdx;

    public FavListsMsg(List<FavList> favLists, int actIdx) {
        this.favLists = favLists;
        this.actIdx = actIdx;
    }

    @Override
    public BotApiMethod<Message> getMessage(long chatId) {
        return new SendMessage()
                .setChatId(chatId)
                .setText(getText())
                .setReplyMarkup(createInlineKeyboardMarkup());
    }

    private String getText() {
        String result = Constants.MOVIE_LISTS_MSG_TXT + "\n\n";
        if (favLists.size() >= actIdx) {
            FavList actFavList = favLists.get(actIdx);
            for (FavMovie movie : actFavList.getFavMovies()) {
                result += String.format(Constants.MOVIES_TITLE, movie.getName());
                result += String.format(Constants.MOVIES_ID, movie.getTmdbId());
            }
        }
        return EmojiParser.parseToUnicode(result);
    }

    private InlineKeyboardMarkup createInlineKeyboardMarkup() {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        if (favLists.size() > 1) {
            for (int i = 0; i < favLists.size(); i++) {
                FavList favList = favLists.get(i);
                String ptt = favList.isPubl() ? Constants.MOVIE_LIST_PUBLIC : Constants.MOVIE_LIST_PERS;

                if (i == actIdx)
                    ptt += Constants.MOVIE_LIST_ADD_MARK;

                List<InlineKeyboardButton> movieRow = new ArrayList<>();
                movieRow.add(new InlineKeyboardButton()
                        .setText(EmojiParser.parseToUnicode(String.format(ptt, favList.getName())))
                        .setCallbackData("aa"));

                keyboard.add( movieRow );
            }
        }


        List<InlineKeyboardButton> actionRow = new ArrayList<>();
        actionRow.add(InlineUtils.createInlineKeyboardBtt(Constants.ADD_FAV_LIST, new AddFavListCallback()));

        actionRow.add( new InlineKeyboardButton()
                .setText( EmojiParser.parseToUnicode(":memo: Редактировать списки") )
                .setCallbackData("aa") );


        keyboard.add( actionRow );

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup()
                .setKeyboard( keyboard );

        return markupInline;
    }
}
