package ru.ange.mhb.bot.msg;


import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.ange.mhb.bot.msg.callback.BackToShowMovieInfoCallback;
import ru.ange.mhb.bot.msg.callback.detail.ActorsCallback;
import ru.ange.mhb.bot.msg.callback.detail.CrewCallback;
import ru.ange.mhb.bot.msg.callback.detail.DescriptionCallback;
import ru.ange.mhb.bot.msg.callback.detail.MoreDetailsCallback;
import ru.ange.mhb.pojo.movie.MovieCrewPerson;
import ru.ange.mhb.pojo.movie.MovieFullInfo;
import ru.ange.mhb.pojo.user.BotUserExtended;
import ru.ange.mhb.utils.Constants;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailsInfoMsg extends MovieInfoMsg {

    private static DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols();
    static {
        SYMBOLS.setGroupingSeparator(' ');
    }
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###.00", SYMBOLS);


    public enum Type {
        INFO, ACTORS, CREW, DESCRIPTION
    }

    private int txtMaxSize = 400;
    private Type type;

    public MovieDetailsInfoMsg(MovieFullInfo movie, BotUserExtended botUser, Type type) {
        super(movie, botUser);
        this.movie = movie;
        this.type = type;
    }

    @Override
    public EditMessageCaption getEditMsg(long chatId, int msgId) {
        return new EditMessageCaption()
                .setChatId(String.valueOf(chatId))
                .setMessageId(msgId)
                .setCaption(EmojiParser.parseToUnicode(getText()))
                .setReplyMarkup(createInlineKeyboardMarkup());
    }

    private InlineKeyboardMarkup createInlineKeyboardMarkup() {

        String infoBtt = Constants.MOVIE_DET_INFO + (type == Type.INFO ? Constants.MOVIE_LIST_ADD_MARK : "");
        String descBtt = Constants.MOVIE_DET_DESC + (type == Type.DESCRIPTION ? Constants.MOVIE_LIST_ADD_MARK : "");
        String actsBtt = Constants.MOVIE_DET_ACTS + (type == Type.ACTORS ? Constants.MOVIE_LIST_ADD_MARK : "");
        String crewBtt = Constants.MOVIE_DET_CREW + (type == Type.CREW ? Constants.MOVIE_LIST_ADD_MARK : "");

        List<InlineKeyboardButton> mainRow = new ArrayList<>();
        mainRow.add(InlineUtils.createInlineKeyboardBtt(infoBtt, new MoreDetailsCallback(movie.getTmdbId())));
        mainRow.add(InlineUtils.createInlineKeyboardBtt(descBtt, new DescriptionCallback(movie.getTmdbId())));

        List<InlineKeyboardButton> persRow = new ArrayList<>();
        persRow.add(InlineUtils.createInlineKeyboardBtt(actsBtt, new ActorsCallback(movie.getTmdbId())));
        persRow.add(InlineUtils.createInlineKeyboardBtt(crewBtt, new CrewCallback(movie.getTmdbId())));

        List<InlineKeyboardButton> controlRow = new ArrayList<>();
        controlRow.add(InlineUtils.createInlineKeyboardBtt(Constants.BACK_BTT_TXT,
                new BackToShowMovieInfoCallback(movie.getTmdbId())));

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(mainRow);
        keyboard.add(persRow);
        keyboard.add(controlRow);

        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }

    @Override
    protected String getText() {
        switch (type) {
            case INFO:
                return getInfoText();
            case ACTORS:
                return getActorsText();
            case CREW:
                return getCrewText();
            default:
                return getDescText();
        }
    }

    private String getInfoText() {
        String txt = getTitle() + "\n\n" +
                getOriginalTitle() +
                getDuration() +
                getReleaseDate() +
                getBudget() +
                getRevenue() +
                getTagLine();
        return txt;
    }

    private String getDescText() {
        return getTitle() + "\n\n" + movie.getDesc();
    }

    private String getActorsText() {
        String txt = getTitle() + "\n\n";
        for (MovieCrewPerson person :movie.getActors()) {
            String name = "";
            if (person.getLocalizedName() != null && !person.getLocalizedName().isEmpty())
                name = person.getLocalizedName();
            else
                name = person.getName();
            txt += String.format(Constants.MOVIES_ACTOR, name);
        }
        return txt;
    }


    private String getCrewText() {
        String txt = getTitle() + "\n\n";

        if (movie.getDirector() != null)
            txt += String.format(Constants.MOVIES_DIRECTOR, createPersRow(movie.getDirector()));

        if (movie.getPhotographer() != null)
            txt += String.format(Constants.MOVIES_PHOTOGRAPHER, createPersRow(movie.getPhotographer()));

        if (movie.getProducers().size() > 0)
            txt += String.format(Constants.MOVIES_PRODUCER, createPersRow(movie.getProducers()));

        if (movie.getComposer() != null)
            txt += String.format(Constants.MOVIES_COMPOSER, createPersRow(movie.getComposer()));

        if (movie.getScreenwriters().size() > 0)
            txt += String.format(Constants.MOVIES_WRITERS, createPersRow(movie.getScreenwriters()));

        return txt;
    }

    private String getReleaseDate() {
        return String.format(Constants.MOVIES_RELEASE_DATE, DATE_FORMAT.format(movie.getReleaseDate()));
    }

    private String getDuration() {
        String txt = (int) Math.floor(movie.getDuration() / 60) + " ч. " + movie.getDuration() % 60 + " м.";
        return String.format(Constants.MOVIES_DURATION, txt);
    }

    private String getBudget() {
        return getMoneyStr(movie.getBurget(), Constants.MOVIES_BUDGET);
    }

    private String getRevenue() {
        return getMoneyStr(movie.getRevenue(), Constants.MOVIES_REVENUE);
    }

    private String getMoneyStr(long val, String tempalte) {
        if (val > 0)
            return String.format(tempalte, DECIMAL_FORMAT.format(val));
        else
            return "";
    }

    private String getTagLine() {
        if (movie.getTagline() != null && !movie.getTagline().isEmpty())
            return String.format(Constants.MOVIES_TAGLINE, movie.getTagline());
        else
            return "";
    }

    private String getOriginalTitle() {
        if (movie.getOriginalTitle() != null && !movie.getOriginalTitle().isEmpty())
            return String.format(Constants.MOVIES_ORGTITLE, movie.getOriginalTitle());
        else
            return "";
    }
}