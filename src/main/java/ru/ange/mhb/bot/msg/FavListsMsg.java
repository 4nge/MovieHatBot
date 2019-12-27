package ru.ange.mhb.bot.msg;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.ange.mhb.bot.msg.callback.fav.ChoiceShowingFavListToCallback;
import ru.ange.mhb.bot.msg.callback.fav.EditFavListCallback;
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

    private boolean showWatched = false;
    private boolean editMode = false;


    public FavListsMsg(List<FavList> favLists, int activeFavListdId, long chatId) {
        super(chatId);
        this.favLists = favLists;
        this.activeFavListdId = activeFavListdId;
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

        if (actFavList != null && actFavList.getFavMovies() != null) {
            for (FavMovie movie : actFavList.getFavMovies()) {
                if (movie.isWatched())
                    watchedSize++;
                else
                    unWatchedSize++;
            }
        }

        String ptt = actFavList.isPubl() ? Constants.MOVIE_LIST_PUBLIC : Constants.MOVIE_LIST_PERS;

        String title = String.format(ptt, actFavList.getName());
        String result = title + String.format(Constants.FAV_LIST_TITLE_PTT, unWatchedSize ,watchedSize);

        if (watchedSize == 0 && unWatchedSize == 0) {
            result += Constants.FAV_LIST_IS_EMPTY;
        } else if (unWatchedSize > 0) {

            for (FavMovie movie : actFavList.getFavMovies()) {
                if (!movie.isWatched()) {
                    result += String.format(Constants.MOVIES_TITLE, movie.getName());
                    if (!editMode) {
                        result += String.format(Constants.MOVIES_ID, movie.getTmdbId());
                    } else {
                        result += String.format(Constants.MOVIES_DEL_ID, movie.getTmdbId());
                        result += String.format(Constants.MOVIES_MOV_ID, movie.getTmdbId());
                        result += String.format(Constants.MOVIES_WATCH_ID, movie.getTmdbId());
                    }
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
                    if (!editMode) {
                        result += String.format(Constants.MOVIES_ID, movie.getTmdbId());
                    } else {
                        result += String.format(Constants.MOVIES_DEL_ID, movie.getTmdbId());
                        result += String.format(Constants.MOVIES_MOV_ID, movie.getTmdbId());
                        result += String.format(Constants.MOVIES_UNWATCH_ID, movie.getTmdbId());
                    }

                }
            }
        }
        return EmojiParser.parseToUnicode(result);
    }

    @Override
    protected InlineKeyboardMarkup createReplyKeyboard() {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        int buttonsInRow = 3;

        if (!editMode) {
            List<InlineKeyboardButton> movieRow = new ArrayList<>();
            for (FavList favList : favLists) {
                String ptt = favList.isPubl() ? Constants.MOVIE_LIST_PUBLIC : Constants.MOVIE_LIST_PERS;

                if (favList.getId() == activeFavListdId)
                    ptt = Constants.MOVIE_LIST_ADD_MARK + ptt;

                movieRow.add(InlineUtils.createInlineKeyboardBtt(String.format(ptt, favList.getName()),
                        new ChoiceShowingFavListToCallback(favList.getId(), showWatched, editMode)));

                if (movieRow.size() >= buttonsInRow) {
                    keyboard.add(movieRow);
                    movieRow = new ArrayList<>();
                }
            }
            keyboard.add(movieRow);
        } else {
            List<InlineKeyboardButton> delRow = new ArrayList<>();

            delRow.add(InlineUtils.createInlineKeyboardBtt(Constants.REMOVE_FAVLIST_CB,
                    new EditFavListCallback(activeFavListdId, showWatched, !editMode)));

            keyboard.add(delRow);

            List<InlineKeyboardButton> editRow = new ArrayList<>();
            editRow.add(InlineUtils.createInlineKeyboardBtt(Constants.EDIT_FAVLIST_NAME_CB,
                    new EditFavListCallback(activeFavListdId, showWatched, !editMode)));

            keyboard.add(editRow);
        }

        List<InlineKeyboardButton> actionRow = new ArrayList<>();

        String edBttTxt = editMode ? Constants.END_EDIT_FAVLIST_CB : Constants.EDIT_FAVLIST_CB;
        actionRow.add(InlineUtils.createInlineKeyboardBtt(
                edBttTxt, new EditFavListCallback(activeFavListdId, showWatched, !editMode)));

        String swBttTxt = (showWatched ? Constants.HIDE_WATCHED_FAVLIST_CB: Constants.SHOW_WATCHED_FAVLIST_CB);
        actionRow.add(InlineUtils.createInlineKeyboardBtt(
                swBttTxt, new ShowWatchedFavMoviesCallback(activeFavListdId, !showWatched, editMode)));

        keyboard.add(actionRow);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup()
                .setKeyboard( keyboard );

        // TODO add change order btts

        return markupInline;
    }

    public boolean isShowWatched() {
        return showWatched;
    }

    public FavListsMsg setShowWatched(boolean showWatched) {
        this.showWatched = showWatched;
        return this;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public FavListsMsg setEditMode(boolean editMode) {
        this.editMode = editMode;
        return this;
    }
}
