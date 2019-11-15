package ru.ange.mhb.bot.msg;

import com.vdurmont.emoji.EmojiParser;
import info.movito.themoviedbapi.model.ProductionCountry;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.ange.mhb.pojo.movie.MoviesPage;
import ru.ange.mhb.pojo.movie.SearchMovie;
import ru.ange.mhb.utils.Constants;
import ru.ange.mhb.bot.msg.callback.findmovies.PagesCallback;

import java.util.ArrayList;
import java.util.List;

public class FindingMoviesMsg implements TextMsg {

    private MoviesPage moviesPage;
    private String query;
    private Integer msgId;

    public FindingMoviesMsg(MoviesPage moviesPage, String query) {
        this.moviesPage = moviesPage;
        this.query = query;
    }

    public FindingMoviesMsg(MoviesPage moviesPage, String query, int msgId) {
        this(moviesPage, query);
        this.msgId = msgId;
    }

    private SendMessage getSendMessage(long chatId) {
        SendMessage sm = new SendMessage()
                .setText(getMsgText())
                .setChatId(chatId)
                .setReplyMarkup(createInlineKeyboardMarkup());

        if (msgId != null) {
            sm.setReplyToMessageId(msgId);
        }

        return sm;
    }

    @Override
    public BotApiMethod<Message> getMessage(long chatId) {
       return getSendMessage( chatId );
    }

    public EditMessageText getEditMessageText(long chatId) {
        EditMessageText emt = new EditMessageText()
                .setChatId(chatId)
                .setText(getMsgText())
                .setReplyMarkup(createInlineKeyboardMarkup());

        if (msgId != null)
            emt.setMessageId(msgId);

        return emt;
    }

    private InlineKeyboardMarkup createInlineKeyboardMarkup() {

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

    private String getMsgText() {

        if (moviesPage.getMovies().size() == 0) {
            return EmojiParser.parseToUnicode( String.format( Constants.FOUND_MOVIES_NONE, query ) );
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append( String.format( Constants.FOUND_MOVIES, query, moviesPage.getTotalMovies() ) );

            List<SearchMovie> movies = moviesPage.getMovies();
            for (SearchMovie movie : movies) {
                sb.append(String.format(Constants.MOVIES_TITLE, movie.getTitle()));
                sb.append(String.format(Constants.MOVIES_ID, movie.getTmdbId()));
            }
            
            return EmojiParser.parseToUnicode(sb.toString());
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
