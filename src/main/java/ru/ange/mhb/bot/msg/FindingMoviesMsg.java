package ru.ange.mhb.bot.msg;

import com.vdurmont.emoji.EmojiParser;
import info.movito.themoviedbapi.model.ProductionCountry;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.ange.mhb.bot.msg.utils.InlineUtils;
import ru.ange.mhb.pojo.fav.FavList;
import ru.ange.mhb.pojo.fav.FavMovie;
import ru.ange.mhb.pojo.movie.MoviesPage;
import ru.ange.mhb.pojo.movie.SearchMovie;
import ru.ange.mhb.pojo.user.BotUserExtended;
import ru.ange.mhb.utils.Constants;
import ru.ange.mhb.bot.msg.callback.findmovies.PagesCallback;
import ru.ange.mhb.utils.StrikeThrough;

import java.util.ArrayList;
import java.util.List;

public class FindingMoviesMsg extends ResponseMsg {

    private MoviesPage moviesPage;
    private String query;
    private BotUserExtended botUser;

    public FindingMoviesMsg(MoviesPage moviesPage, String query, long chatId, BotUserExtended botUser) {
        super(chatId);
        this.moviesPage = moviesPage;
        this.query = query;
        this.botUser = botUser;
    }

    @Override
    public String getText() {
        if (moviesPage.getMovies().size() == 0) {
            return EmojiParser.parseToUnicode( String.format( Constants.FOUND_MOVIES_NONE, query ) );
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append( String.format( Constants.FOUND_MOVIES, query, moviesPage.getTotalMovies() ) );

            List<SearchMovie> movies = moviesPage.getMovies();
            for (SearchMovie movie : movies) {
                FavMovie favMovie = botUser.getFavMovieByTmdbId(movie.getTmdbId());
                if (favMovie == null) {
                    sb.append(String.format(Constants.MOVIES_TITLE, movie.getTitle()));
                } else {
                    if (favMovie.isWatched()) {
                        sb.append(Constants.MOVIES_FAV_ICON + " - " + movie.getTitle() + " ");
                    } else {
                        String title = StrikeThrough.getStrikeThroughText(movie.getName());
                        sb.append(Constants.MOVIES_WATCHED_ICON + " - " + title + " ");
                    }
                }
                sb.append(String.format(Constants.MOVIES_ID, movie.getTmdbId()));
            }
            return EmojiParser.parseToUnicode(sb.toString());
        }
    }

    @Override
    protected InlineKeyboardMarkup createReplyKeyboard() {

        int current = moviesPage.getPageIdx();
        int pages = moviesPage.getTotalPages();

        if (pages > 1) {

            List<InlineKeyboardButton> btts = new ArrayList<InlineKeyboardButton>();

            if (pages < 3) {

                if (current == 1) {
                    btts.add(InlineUtils.createInlineKeyboardBtt("1",
                            new PagesCallback( 1 ) ) );
                    btts.add(InlineUtils.createInlineKeyboardBtt(":arrow_forward: 2",
                            new PagesCallback( 2 ) ) );
                } else {
                    btts.add(InlineUtils.createInlineKeyboardBtt(":arrow_backward: " + (current-1),
                            new PagesCallback( current-1) ) );
                    btts.add(InlineUtils.createInlineKeyboardBtt("2",
                            new PagesCallback( 2 ) ) );
                }

            } else {

                if (current >= 3)
                    btts.add(InlineUtils.createInlineKeyboardBtt("⏮ 1",
                            new PagesCallback( 1 ) ) );

                if (current == 1) {

                    btts.add(InlineUtils.createInlineKeyboardBtt("1",
                            new PagesCallback( 1 ) ) );
                    btts.add(InlineUtils.createInlineKeyboardBtt("2 :arrow_forward:",
                            new PagesCallback( 2 ) ) );
                    btts.add(InlineUtils.createInlineKeyboardBtt("3 :fast_forward:",
                            new PagesCallback( 3 ) ) );

                } else if (current == pages) {

                    btts.add(InlineUtils.createInlineKeyboardBtt(":rewind: " + (current-2),
                            new PagesCallback( current-2 ) ) );
                    btts.add(InlineUtils.createInlineKeyboardBtt(":arrow_backward: " + (current-1),
                            new PagesCallback( current-1 ) ) );
                    btts.add(InlineUtils.createInlineKeyboardBtt("" + current,
                            new PagesCallback( current ) ) );

                } else {

                    btts.add(InlineUtils.createInlineKeyboardBtt(":arrow_backward: " + (current-1),
                            new PagesCallback( current-1 ) ) );
                    btts.add(InlineUtils.createInlineKeyboardBtt("" + current,
                            new PagesCallback( current ) ) );
                    btts.add(InlineUtils.createInlineKeyboardBtt( (current+1) + " :arrow_forward:",
                            new PagesCallback( current+1 ) ) );
                }

                if (current < pages - 1)
                    btts.add(InlineUtils.createInlineKeyboardBtt(pages + " ⏭",
                            new PagesCallback( pages ) ) );
            }

            // TODO добавить кнопки смены сортировки (дате, по рейтингу, алфавиту)

            List<List<InlineKeyboardButton>> keyboard = InlineUtils.createInlineKeyboard( btts );

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup()
                    .setKeyboard( keyboard );

            return markupInline;

        } else {
            return null;
        }
    }

    private String getCountry(List<ProductionCountry> countries) {
        if (countries != null && countries.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (ProductionCountry country : countries) {
                sb.append(country.getName());
            }
            return String.format( Constants.MOVIES_COUNT_DEL, sb.toString() );
        } else {
            return "";
        }

    }

}
