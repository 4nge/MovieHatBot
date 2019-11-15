package ru.ange.mhb.bot.msg;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.ange.mhb.bot.msg.callback.fav.AddToFavoriteCallback;
import ru.ange.mhb.bot.msg.callback.fav.ChoiceFavListCallback;
import ru.ange.mhb.bot.msg.callback.movie.BackToMovieInfoCallback;
import ru.ange.mhb.bot.msg.callback.movie.detail.MoreDetailsCallback;
import ru.ange.mhb.bot.msg.callback.movie.SetRatingCallback;
import ru.ange.mhb.bot.msg.callback.movie.watched.AddToWatchedCallback;
import ru.ange.mhb.bot.msg.callback.movie.watched.RevertFromWatchedCallback;
import ru.ange.mhb.pojo.fav.FavList;
import ru.ange.mhb.pojo.fav.FavMovie;
import ru.ange.mhb.pojo.movie.*;
import ru.ange.mhb.pojo.user.BotUserExtended;
import ru.ange.mhb.utils.Constants;
import ru.ange.mhb.utils.ImagesUtil;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MovieInfoMsg {

    private int descMaxSize = 150;

    protected MovieFullInfo movie;
    protected FavMovie favMovie;
    protected BotUserExtended botUser;
    private Integer msgId;

    public MovieInfoMsg(MovieFullInfo movie, BotUserExtended botUser) {
        this.movie = movie;
        this.botUser = botUser;
        this.favMovie = getFavMovie(botUser, movie);
    }

    public MovieInfoMsg(MovieFullInfo movie, BotUserExtended botUser, int msgId) {
        this(movie, botUser);
        this.msgId = msgId;
    }

    public int getDescMaxSize() {
        return descMaxSize;
    }

    public boolean hasPhoto() {
        return movie.getPoster() != null;
    }

    public SendPhoto getPhotoMessage(long chatId) {
        SendPhoto sp = new SendPhoto()
                .setCaption(getText())
                .setChatId(chatId)
                .setReplyToMessageId(msgId)
                .setReplyMarkup(createInlineKeyboardMarkup());

        if (favMovie != null && favMovie.isWatched()) {
            BufferedImage img = ImagesUtil.addImageWatermark(movie.getPoster().getBufferedImage(),
                    Constants.WATERMARK_PATH);
            sp.setPhoto(movie.getPoster().getName(), ImagesUtil.getIsFromBm(img));
        } else {
            sp.setPhoto(movie.getPoster().getFullPath());
        }

//        if (msgId != null)
//            sp.setMessageId(msgId);


        return sp;
    }

    public SendMessage getTextMessage(long chatId) {
        SendMessage sm = new SendMessage()
                .setText(getText())
                .setChatId(chatId)
                .setReplyMarkup(createInlineKeyboardMarkup());

        if (msgId != null)
            sm.setReplyToMessageId(msgId);

        return sm;
    }


    private EditMessageCaption getBasicEditMsg(long chatId) {
        EditMessageCaption emc = new EditMessageCaption()
                .setChatId(String.valueOf(chatId));

        if (msgId != null)
            emc.setMessageId(msgId);

        return emc;

    }

    public EditMessageCaption getEditMsg(long chatId) {
        return getBasicEditMsg(chatId)
                .setCaption(getText())
                .setReplyMarkup(createInlineKeyboardMarkup());
    }

    public EditMessageMedia getEditMsgMedia(long chatId) {

        InputMediaPhoto imp = new InputMediaPhoto();
        imp.setCaption(getText());

        if (favMovie != null && favMovie.isWatched()) {
            BufferedImage img = ImagesUtil.addImageWatermark(movie.getPoster().getBufferedImage(),
                    Constants.WATERMARK_PATH);
            imp.setMedia(ImagesUtil.getIsFromBm(img), movie.getPoster().getName());
        } else {
            BufferedImage img = movie.getPoster().getBufferedImage();
            imp.setMedia(ImagesUtil.getIsFromBm(img), movie.getPoster().getName());
        }

        EditMessageMedia emm = new EditMessageMedia();
        emm.setChatId(chatId);
        emm.setReplyMarkup(createInlineKeyboardMarkup());
        emm.setMedia(imp);

        if (msgId != null)
            emm.setMessageId(msgId);

        return emm;
    }

    public EditMessageCaption getAddToFavEditMsg(long chatId) {
        return getBasicEditMsg(chatId)
                .setCaption(getText() + Constants.MOVIE_LIST_ADD_TXT)
                .setReplyMarkup(createFavListInlineKeyboardMarkup());
    }

    //TODO use builder
    protected String getText() {
        String txt =
                getTitle() +
                getCountries() +
                getDirector() +
                getGenres() +
                getRating() +
                getActors() + "\n" +
                getDesc();

        if (favMovie != null && favMovie.isWatched() && (favMovie.getRating() == null || favMovie.getRating() == 0))
            txt += "\n\nОцените фильм:";

        return EmojiParser.parseToUnicode(txt);
    }

    protected String getTitle() {
        String res = movie.getTitle();
        if (favMovie != null) {
            if (favMovie.isWatched())
                res += " " + Constants.MOVIES_WATCHED_ICON;
            else
                res += " " + Constants.MOVIES_FAV_ICON;
        }
        return res  + "\n\n";
    }



    private String getCountries() {
        if (movie.getCountries() != null && movie.getCountries().size() > 0) {
            String str = "";
            for (String county : movie.getCountries()) {
                str += county + " / ";
            }
            return String.format(Constants.MOVIES_COUNTRY, str.substring(0, str.length()-3));
        } else {
            return "";
        }
    }

    private String getDirector() {
        if (movie.getDirector() != null)
            return String.format(Constants.MOVIES_DIRECTOR, createPersRow(movie.getDirector()));
        else
            return "";
    }

    private String getGenres() {
        String genresStr = "";
        for (Genre genre : movie.getGenres()) {
            String сapLetter = genre.getName().toUpperCase().substring(0, 1);
            String name = сapLetter + genre.getName().substring(1);
            genresStr += name + " / ";
        }
        if (genresStr.length() > 3) {
            genresStr = genresStr.substring(0, genresStr.length()-3);
        }
        return String.format(Constants.MOVIES_GENRE, genresStr);
    }

    private String getRating() {
        String txt = "";
        if (movie.getKpInfo().getKpRating() > 0)
            txt += "кп " + movie.getKpInfo().getKpRating() + " / ";
        if (movie.getKpInfo().getIMDbRating() > 0)
            txt += "IMDb " + movie.getKpInfo().getIMDbRating() + " / ";
        if (movie.getRating() > 0)
            txt += "TMDb " + movie.getKpInfo().getIMDbRating() + " / ";

        if (txt.contains(" / "))
            txt = txt.substring(0, txt.length() - 3);

        return String.format(Constants.MOVIES_STARS, txt);
    }

    private String getActors() {
        if (movie.getActors().size() > 0)
            return String.format(Constants.MOVIES_ACTORS, createPersRow(movie.getActors()));
        else
            return "";
    }

    private String getDesc() {
        String desc = movie.getDesc().length() < getDescMaxSize() ? movie.getDesc() : (movie.getDesc().substring(0,
                movie.getDesc().lastIndexOf(" ", getDescMaxSize())) + "...");
        return desc;
    }


    private InlineKeyboardButton createSetRatingBtt(int rating) {
        String tmp = favMovie.getRating() == rating ? Constants.MOVIE_LIST_ADD_MARK + " %s"
                : Constants.MOVIE_BTT_RATING;

        return InlineUtils.createInlineKeyboardBtt(String.format(tmp, rating),
                new SetRatingCallback(favMovie.getId(), rating));
    }


    private InlineKeyboardMarkup createInlineKeyboardMarkup() {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> favRow = new ArrayList<>();

        if (favMovie != null) {

            if (favMovie.isWatched()) {

                List<InlineKeyboardButton> ratingRow = new ArrayList<>();
                ratingRow.add(createSetRatingBtt(1));
                ratingRow.add(createSetRatingBtt(2));
                ratingRow.add(createSetRatingBtt(3));
                ratingRow.add(createSetRatingBtt(4));
                ratingRow.add(createSetRatingBtt(5));

                keyboard.add(ratingRow);

                favRow.add(InlineUtils.createInlineKeyboardBtt(Constants.MOVIE_BTT_REMOVE_FROM_WATCHED,
                        new RevertFromWatchedCallback(favMovie.getId())));

                // TODO add search similar movies btt

            } else {
                favRow.add(InlineUtils.createInlineKeyboardBtt(Constants.MOVIE_BTT_ADD_TO_WATCHED,
                        new AddToWatchedCallback(favMovie.getId())));
                favRow.add(InlineUtils.createInlineKeyboardBtt(Constants.MOVIE_BTT_EDIT_FAV,
                        new AddToFavoriteCallback(movie.getTmdbId())));
            }

        } else {
            favRow.add(InlineUtils.createInlineKeyboardBtt(Constants.MOVIE_BTT_ADD_TO_FAV,
                    new AddToFavoriteCallback(movie.getTmdbId())));
        }

        keyboard.add(favRow);

        List<InlineKeyboardButton> actionRow = new ArrayList<>();
        actionRow.add(InlineUtils.createInlineKeyboardBtt(Constants.MOVIE_BTT_MORE_DETAILS,
                new MoreDetailsCallback(movie.getTmdbId())));

        keyboard.add( actionRow );

        if (!movie.getKpInfo().getLink().isEmpty()) {
            List<InlineKeyboardButton> linksRow = new ArrayList<>();
            linksRow.add( new InlineKeyboardButton()
                    .setText("КиноПоиск")
                    .setUrl( movie.getKpInfo().getLink() ) );
            // TODO add search buttons
//            linksRow.add( new InlineKeyboardButton()
//                    .setText("Википедия")
//                    .setUrl( movie.getKpInfo().getLink() ) );
//            linksRow.add( new InlineKeyboardButton()
//                    .setText("Google")
//                    .setUrl( movie.getKpInfo().getLink() ) );
            keyboard.add( linksRow );
        }

        return new InlineKeyboardMarkup().setKeyboard( keyboard );
    }

    private InlineKeyboardMarkup createFavListInlineKeyboardMarkup() {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (FavList favList : botUser.getFavLists()) {

            String ptt = favList.isPubl() ? Constants.MOVIE_LIST_PUBLIC : Constants.MOVIE_LIST_PERS;
            String text = String.format( ptt, favList.getName() );

            if (favList.getFavMovies().contains(movie)) {
                text += Constants.MOVIE_LIST_ADD_MARK;
            }

            ChoiceFavListCallback callback = new ChoiceFavListCallback( movie.getTmdbId(), favList.getId() );

            List<InlineKeyboardButton> movieRow = new ArrayList<>();
            movieRow.add( InlineUtils.createInlineKeyboardBtt( text, callback ) );

            keyboard.add( movieRow );
        }

        List<InlineKeyboardButton> actionRow = new ArrayList<>();
        actionRow.add( InlineUtils.createInlineKeyboardBtt( Constants.BACK_BTT_TXT,
                new BackToMovieInfoCallback( movie.getTmdbId() ) ) );
        keyboard.add( actionRow );

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup()
                .setKeyboard( keyboard );

        return markupInline;
    }

    private FavMovie getFavMovie(BotUserExtended botUser, MovieFullInfo movie) {
        for (FavList favList : botUser.getFavLists()) {
            for (FavMovie favMovie : favList.getFavMovies()) {
                if (favMovie.equals(movie))
                    return favMovie;
            }
        }
        return null;
    }


    protected String createPersRow(MovieCrewPerson person) {
        if (person.getLocalizedName() != null && !person.getLocalizedName().isEmpty())
            return person.getLocalizedName();
        else
            return person.getName();
    }

    protected String createPersRow(List<MovieCrewPerson> persons) {
        String res = "";
        for (MovieCrewPerson person : persons) {
            String persStr = "";
            if (person.getLocalizedName() != null && !person.getLocalizedName().isEmpty())
                persStr = person.getLocalizedName();
            else
                persStr = person.getName();
            res += persStr + " / ";
        }
        return res.substring(0, res.length()-3);
    }

    public Integer getMsgId() {
        return msgId;
    }

    public MovieInfoMsg setMsgId(Integer msgId) {
        this.msgId = msgId;
        return this;
    }
}
