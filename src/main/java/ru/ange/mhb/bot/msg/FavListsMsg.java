package ru.ange.mhb.bot.msg;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.ange.mhb.bot.msg.callback.fav.ChoiceShowingFavListToCallback;
import ru.ange.mhb.bot.msg.callback.fav.ShowWatchedFavMoviesCallback;
import ru.ange.mhb.bot.msg.utils.InlineUtils;
import ru.ange.mhb.pojo.fav.FavList;
import ru.ange.mhb.pojo.fav.FavMovie;
import ru.ange.mhb.utils.Constants;
import ru.ange.mhb.utils.StrikeThrough;

import java.util.ArrayList;
import java.util.List;

public class FavListsMsg extends ResponseMsg {

    private List<FavList> favLists;
    private int activeFavListdId;
    private boolean showWatched;

    public FavListsMsg(List<FavList> favLists, int activeFavListdId, long chatId) {
        super(chatId);
        this.favLists = favLists;
        this.activeFavListdId = activeFavListdId;
    }

    public FavListsMsg(List<FavList> favLists, int activeFavListdId, long chatId, boolean showWatched) {
        this(favLists, activeFavListdId, chatId);
        this.showWatched = showWatched;
    }


    @Override
    public String getText() {

        FavList actFavList = null;
        for (FavList favList : favLists) {
            if (favList.getId() == activeFavListdId) {
                actFavList = favList;
                break;
            }
        }

        int watchedSize = 0, unWatchedSize = 0;
        for (FavMovie movie : actFavList.getFavMovies()) {
            if (movie.isWatched())
                watchedSize++;
            else
                unWatchedSize++;
        }

        String ptt = actFavList.isPubl() ? Constants.MOVIE_LIST_PUBLIC : Constants.MOVIE_LIST_PERS;

        String title = String.format(ptt, actFavList.getName());
        String result = String.format(Constants.FAV_LIST_TITLE_PTT, title, unWatchedSize ,watchedSize);

        if (watchedSize == 0 && unWatchedSize == 0) {
            result += Constants.FAV_LIST_IS_EMPTY;
        } else if (unWatchedSize > 0) {

            for (FavMovie movie : actFavList.getFavMovies()) {
                if (!movie.isWatched()) {
                    result += String.format(Constants.MOVIES_TITLE, movie.getName());
                    result += String.format(Constants.MOVIES_ID, movie.getTmdbId());
                }
            }
        } else if (!showWatched && unWatchedSize == 0) {
            result += Constants.FAV_LIST_IS_ONLY_WATCHED;
        }

        if (showWatched && watchedSize > 0) {
            for (FavMovie movie : actFavList.getFavMovies()) {
                if (movie.isWatched()) {
                    String name = StrikeThrough.getStrikeThroughText(movie.getName());
                    String date = Constants.PRINT_DATE_FORMAT.format(movie.getWatchedDate());
                    result += String.format(Constants.MOVIES_TITLE, name);
                    result += " (" + date;

                    if (movie.getRating() > 0)
                        result += "; " + String.format(Constants.MOVIE_BTT_RATING, movie.getRating());

                    result += ") ";
                    result += String.format(Constants.MOVIES_ID, movie.getTmdbId());
                }
            }
        }
        return EmojiParser.parseToUnicode(result);
    }

    @Override
    protected InlineKeyboardMarkup createReplyKeyboard() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        int buttonsInRow = 3;
        List<InlineKeyboardButton> movieRow = new ArrayList<>();

        for (FavList favList : favLists) {
            String ptt = favList.isPubl() ? Constants.MOVIE_LIST_PUBLIC : Constants.MOVIE_LIST_PERS;

            if (favList.getId() == activeFavListdId)
                ptt = Constants.MOVIE_LIST_ADD_MARK + ptt;

            movieRow.add(InlineUtils.createInlineKeyboardBtt(String.format(ptt, favList.getName()),
                    new ChoiceShowingFavListToCallback(favList.getId(), showWatched)));

            if (movieRow.size() >= buttonsInRow) {
                keyboard.add(movieRow);
                movieRow = new ArrayList<>();
            }
        }

        keyboard.add(movieRow);

        List<InlineKeyboardButton> actionRow = new ArrayList<>();
        actionRow.add(new InlineKeyboardButton()
                .setText(EmojiParser.parseToUnicode(":memo: Редактировать"))
                .setCallbackData("aa"));
        actionRow.add(InlineUtils.createInlineKeyboardBtt((showWatched ? Constants.HIDE_WATCHED_FAVLIST_CB
                        : Constants.SHOW_WATCHED_FAVLIST_CB),
                new ShowWatchedFavMoviesCallback(activeFavListdId, !showWatched)));

        keyboard.add(actionRow);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup()
                .setKeyboard( keyboard );

        // TODO add change order btts

        return markupInline;
    }
}
