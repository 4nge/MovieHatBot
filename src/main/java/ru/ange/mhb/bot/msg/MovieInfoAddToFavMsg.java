package ru.ange.mhb.bot.msg;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.ange.mhb.bot.msg.callback.fav.ChoiceFavListToAddMovieCallback;
import ru.ange.mhb.bot.msg.callback.movie.BackToMovieInfoCallback;
import ru.ange.mhb.bot.msg.utils.InlineUtils;
import ru.ange.mhb.pojo.fav.FavList;
import ru.ange.mhb.pojo.movie.MovieFullInfo;
import ru.ange.mhb.pojo.user.BotUserExtended;
import ru.ange.mhb.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MovieInfoAddToFavMsg extends MovieInfoMsg {

    public MovieInfoAddToFavMsg(long chatId, MovieFullInfo movie, BotUserExtended botUser) {
        super(chatId, movie, botUser);
    }

    @Override
    public String getText() {
        if (botUser.getFavLists().contains(movie))
            return super.getText() + Constants.MOVIE_LIST_ADD_TXT;
        else
            return super.getText();
    }

    @Override
    public ReplyKeyboard createReplyKeyboard() {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        int movieId = movie.getTmdbId();

        // TODO разбивка листов на два ряда если больше какого то числа

        for (FavList favList : botUser.getFavLists()) {

            String ptt = favList.isPubl() ? Constants.MOVIE_LIST_PUBLIC : Constants.MOVIE_LIST_PERS;
            String text = String.format(ptt, favList.getName());

            if (favList.getFavMovies().contains(movie)) {
                text += Constants.MOVIE_LIST_ADD_MARK;
            }

            ChoiceFavListToAddMovieCallback cb = new ChoiceFavListToAddMovieCallback(movieId, favList.getId());
            List<InlineKeyboardButton> movieRow = new ArrayList<>();
            movieRow.add(InlineUtils.createInlineKeyboardBtt(text, cb));

            keyboard.add(movieRow);
        }

        List<InlineKeyboardButton> actionRow = new ArrayList<>();
        actionRow.add( InlineUtils.createInlineKeyboardBtt(Constants.BACK_BTT_TXT,
                new BackToMovieInfoCallback(movie.getTmdbId())));
        keyboard.add(actionRow);

        return new InlineKeyboardMarkup()
                .setKeyboard(keyboard);
    }

}
