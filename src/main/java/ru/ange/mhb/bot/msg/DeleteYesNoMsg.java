package ru.ange.mhb.bot.msg;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.ange.mhb.bot.msg.callback.Callback;
import ru.ange.mhb.bot.msg.callback.fav.DeleteConfirmFavListCallback;
import ru.ange.mhb.bot.msg.callback.fav.EditFavListCallback;
import ru.ange.mhb.bot.msg.utils.InlineUtils;
import ru.ange.mhb.pojo.fav.FavList;
import ru.ange.mhb.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class DeleteYesNoMsg extends ResponseMsg {

    private FavList favList;
    private boolean showWatched;

    public DeleteYesNoMsg(long chatId, FavList favList, boolean showWatched) {
        super(chatId);
        this.favList = favList;
        this.showWatched = showWatched;
    }

    @Override
    public String getText() {
        return String.format(Constants.DELETE_FAVLIST_YES_NO, favList.getName());
    }

    @Override
    protected InlineKeyboardMarkup createReplyKeyboard() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        row.add(InlineUtils.createInlineKeyboardBtt(Constants.DELETE_FAVLIST_YES_CB,
                new DeleteConfirmFavListCallback(favList.getId(), showWatched)));

        row.add(InlineUtils.createInlineKeyboardBtt(Constants.DELETE_FAVLIST_NO_CB,
                new EditFavListCallback(favList.getId(), showWatched, true)));

        keyboard.add(row);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup()
                .setKeyboard( keyboard );

        return markupInline;
    }
}
