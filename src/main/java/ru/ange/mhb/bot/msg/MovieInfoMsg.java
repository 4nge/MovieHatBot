package ru.ange.mhb.bot.msg;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.ange.mhb.bot.msg.callback.fav.AddToFavoriteCallback;
import ru.ange.mhb.bot.msg.callback.movie.SetRatingCallback;
import ru.ange.mhb.bot.msg.callback.movie.detail.MoreDetailsCallback;
import ru.ange.mhb.bot.msg.callback.movie.watched.AddToWatchedCallback;
import ru.ange.mhb.bot.msg.callback.movie.watched.RevertFromWatchedCallback;
import ru.ange.mhb.bot.msg.utils.InlineUtils;
import ru.ange.mhb.pojo.fav.FavList;
import ru.ange.mhb.pojo.fav.FavMovie;
import ru.ange.mhb.pojo.movie.Genre;
import ru.ange.mhb.pojo.movie.MovieCrewPerson;
import ru.ange.mhb.pojo.movie.MovieFullInfo;
import ru.ange.mhb.pojo.user.BotUserExtended;
import ru.ange.mhb.service.WikipediaService;
import ru.ange.mhb.utils.Constants;
import ru.ange.mhb.utils.ImagesUtil;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class MovieInfoMsg extends ResponseMsg {

    private int descMaxSize = 150;

    protected MovieFullInfo movie;
    protected FavMovie favMovie;
    protected BotUserExtended botUser;

    public MovieInfoMsg(long chatId, MovieFullInfo movie, BotUserExtended botUser) {
        super(chatId);
        this.movie = movie;
        this.botUser = botUser;
        this.favMovie = getFavMovie(botUser, movie);

        if (favMovie != null && favMovie.isWatched()) {
            BufferedImage poster = movie.getPoster().getBufferedImage();
            BufferedImage waterMark = ImagesUtil.addImageWatermark(poster, Constants.WATERMARK_PATH);
            super.setPhotoPath(movie.getPoster().getName());
            super.setPhotoStream(ImagesUtil.getIsFromBm(waterMark));
        } else {
            super.setPhotoPath(movie.getPoster().getFullPath());
        }
    }

    @Override
    public String getText() {
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

    @Override
    public ReplyKeyboard createReplyKeyboard() {
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

        keyboard.add(actionRow);

        String kpLink = movie.getKpInfo().getLink();
        String wikiLink = movie.getWikiLink();

        if ((kpLink != null && !kpLink.isEmpty()) || (wikiLink != null && !wikiLink.isEmpty())) {
            List<InlineKeyboardButton> linksRow = new ArrayList<>();

            if (kpLink != null && !kpLink.isEmpty())
                linksRow.add( new InlineKeyboardButton().setText(Constants.KP_LINK_BTT).setUrl(kpLink));

            if (wikiLink != null && !wikiLink.isEmpty())
                linksRow.add( new InlineKeyboardButton().setText(Constants.WIKI_LINK_BTT).setUrl(wikiLink));

            keyboard.add(linksRow);
        }
        return new InlineKeyboardMarkup().setKeyboard(keyboard);
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
        String desc = movie.getDesc().length() < descMaxSize ? movie.getDesc() : (movie.getDesc().substring(0,
                movie.getDesc().lastIndexOf(" ", descMaxSize)) + "...");
        return desc;
    }


    private InlineKeyboardButton createSetRatingBtt(int rating) {
        String tmp = favMovie.getRating() == rating ? Constants.MOVIE_LIST_ADD_MARK + " %s"
                : Constants.MOVIE_BTT_RATING;

        return InlineUtils.createInlineKeyboardBtt(String.format(tmp, rating),
                new SetRatingCallback(favMovie.getId(), rating));
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

}
