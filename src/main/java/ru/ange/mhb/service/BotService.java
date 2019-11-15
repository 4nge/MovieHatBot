package ru.ange.mhb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ange.mhb.bot.msg.*;
import ru.ange.mhb.bot.msg.callback.CallbackWithFavMovieIdImpl;
import ru.ange.mhb.bot.msg.callback.fav.AddToFavoriteCallback;
import ru.ange.mhb.bot.msg.callback.movie.BackToMovieInfoCallback;
import ru.ange.mhb.bot.msg.callback.fav.ChoiceFavListCallback;
import ru.ange.mhb.bot.msg.callback.findmovies.PagesCallback;
import ru.ange.mhb.bot.msg.callback.CallbackWithTmdbIdImpl;
import ru.ange.mhb.bot.msg.callback.movie.SetRatingCallback;
import ru.ange.mhb.pojo.fav.FavMovie;
import ru.ange.mhb.pojo.movie.MovieFullInfo;
import ru.ange.mhb.pojo.movie.MoviesPage;
import ru.ange.mhb.pojo.user.BotUserExtended;

import java.io.IOException;
import java.util.Date;

@Service
public class BotService {

    // TODO
    // add send error msg in all Exception methods

    private static final ObjectMapper OM = new ObjectMapper();

    @Autowired
    private DbService dbService;

    @Autowired
    private MovieService movieService;


    public HelloMsg getHelloMsg(MessageContext ctx) {
        //TODO use common method to get user
//        User telUser = ctx.update().getMessage().getFrom();
//        int telId = telUser.getId();

        //TODO use BotUserBasic ?
//        BotUserBasic botUser = botService.getBotUserBasicByTelUserId( telId );
//        if (botUser == null || botUser.getId() == 0) {
//            BotUserBasic newBotUser = new BotUserBasic(0, telId,
//                    telUser.getUserName(), telUser.getLanguageCode(), false );
//            botUser = botService.addBotUser( newBotUser );
//        }
        return new HelloMsg();
    }

    public FavListsMsg getFavListsMsg(Update upd) {
        BotUserExtended botUser = dbService.getBotUserByTelUserId(upd.getMessage().getFrom().getId());
        return new FavListsMsg(botUser.getFavLists(), 0);
    }

    public FindingMoviesMsg getFindingMoviesMsg(Update upd) {
        Message msg = upd.getMessage();
        String query = msg.getText();
        BotUserExtended botUser = dbService.getBotUserByTelUserId(msg.getFrom().getId());
        MoviesPage moviesPage = movieService.searchMovies(query,1, botUser.getLanguage(), botUser.isAdult());
        return new FindingMoviesMsg(moviesPage, query, msg.getMessageId());
    }

    public MovieInfoMsg getMovieInfoMsg(Update upd, String movieCommandsPrefix) {
        Message msg = upd.getMessage();
        String query = upd.getMessage().getText().replace("/", "");
        int movieId = Integer.valueOf(query.replace(movieCommandsPrefix, ""));

        MovieFullInfo movie = movieService.getMovie(movieId);
        BotUserExtended botUser = dbService.getBotUserByTelUserId(msg.getFrom().getId());
        return new MovieInfoMsg(movie, botUser, msg.getMessageId());
    }

    public FindingMoviesMsg handlePagesCallback(Update upd) {
        CallbackQuery cq = upd.getCallbackQuery();
        Message msg = cq.getMessage();
        Message rtMsg = msg.getReplyToMessage();

        try {
            PagesCallback pcb = OM.readValue(cq.getData(), PagesCallback.class);
            String query = msg.getReplyToMessage().getText();

            BotUserExtended botUser = dbService.getBotUserByTelUserId(rtMsg.getFrom().getId());
            MoviesPage moviesPage = movieService.searchMovies(query, pcb.getPageNumber(),
                    botUser.getLanguage(), botUser.isAdult());
            return new FindingMoviesMsg(moviesPage, query);
        } catch (IOException e) {
            return null;
        }
    }

    public MovieInfoMsg handleAddToFavoriteCallback(Update upd) {
        CallbackQuery cq = upd.getCallbackQuery();
        Message msg = cq.getMessage();
        Message rtMsg = msg.getReplyToMessage();

        try {
            AddToFavoriteCallback atfCallback = OM.readValue(cq.getData(), AddToFavoriteCallback.class);
            BotUserExtended botUser = dbService.getBotUserByTelUserId(rtMsg.getFrom().getId());
            return new MovieInfoMsg(movieService.getMovie(atfCallback.getMovieId()), botUser, msg.getMessageId());
        } catch (IOException e) {
            return null;
        }
    }


    public MovieInfoMsg handleBackToMovieInfoCallback(Update upd) {
        CallbackQuery cq = upd.getCallbackQuery();
        Message msg = cq.getMessage();
        Message rtMsg = msg.getReplyToMessage();

        try {
            BackToMovieInfoCallback callback = OM.readValue(cq.getData(), BackToMovieInfoCallback.class);
            BotUserExtended botUser = dbService.getBotUserByTelUserId(rtMsg.getFrom().getId());
            MovieFullInfo mfi = movieService.getMovie(callback.getMovieId());
            return new MovieInfoMsg(mfi, botUser, msg.getMessageId());
        } catch (IOException e) {
            return null;
        }
    }

    public MovieDetailsInfoMsg handleDetailCallback(Update upd, MovieDetailsInfoMsg.Type type) {
        CallbackQuery cq = upd.getCallbackQuery();
        Message msg = cq.getMessage();

        try {
            CallbackWithTmdbIdImpl cb = OM.readValue(cq.getData(), CallbackWithTmdbIdImpl.class);
            BotUserExtended botUser = dbService.getBotUserByTelUserId((msg.getReplyToMessage().getFrom().getId()));
            MovieFullInfo mfi = movieService.getMovieDetails(cb.getTmdbMovieId());
            return new MovieDetailsInfoMsg(mfi, botUser, type, msg.getMessageId());
        } catch (IOException e) {
            return null;
        }
    }


    public MovieInfoMsg handleChoiceFavListCallback(Update upd) {
        CallbackQuery cq = upd.getCallbackQuery();
        Message msg = cq.getMessage();
        Message rtMsg = msg.getReplyToMessage();
        try {
            ChoiceFavListCallback cflc =  OM.readValue(cq.getData(), ChoiceFavListCallback.class);
            BotUserExtended botUser = dbService.getBotUserByTelUserId((rtMsg.getFrom().getId()));

            MovieFullInfo movie = movieService.getMovie(cflc.getMovieId());
            FavMovie newFavMovie = dbService.addFavMovie(new FavMovie(
                    movie.getTitle(),
                    movie.getTmdbId(),
                    new Date(),
                    cflc.getFavListId(),
                    botUser.getId(),false,null));

            botUser.getFavListById(cflc.getFavListId()).getFavMovies().add(newFavMovie);

            //return new MovieInfoMsg( movie, botUser, msg.getMessageId());  // if need to go to main movie msg
            return new MovieInfoMsg(movie, botUser, msg.getMessageId());

        } catch (IOException e) {
            return null;
        }
    }

    public MovieInfoMsg handleWatchedCallback(Update upd, boolean watched) {
        CallbackQuery cq = upd.getCallbackQuery();
        Message msg = cq.getMessage();
        try {
            CallbackWithFavMovieIdImpl cb = OM.readValue(cq.getData(), CallbackWithFavMovieIdImpl.class);
            BotUserExtended botUser = dbService.getBotUserByTelUserId(msg.getReplyToMessage().getFrom().getId());
            FavMovie favMovie = botUser.getFavMovieById(cb.getFavMoviedId())
                    .setWatched(watched);
            dbService.updateFavMovie(favMovie);
            return new MovieInfoMsg(movieService.getMovie(favMovie.getTmdbId()), botUser, msg.getMessageId());
        } catch (IOException e) {
            return null;
        }
    }

    public MovieInfoMsg handleSetRatingCallback(Update upd) {
        CallbackQuery cq = upd.getCallbackQuery();
        Message msg = cq.getMessage();

        try {
            SetRatingCallback callback = OM.readValue(cq.getData(), SetRatingCallback.class);
            BotUserExtended botUser = dbService.getBotUserByTelUserId(msg.getReplyToMessage().getFrom().getId());
            FavMovie favMovie = botUser.getFavMovieById(callback.getFavMoviedId())
                .setRating(callback.getRating());
            dbService.updateFavMovie(favMovie);
            return new MovieInfoMsg(movieService.getMovie(favMovie.getTmdbId()), botUser, msg.getMessageId());
        } catch (IOException e) {
            return null;
        }
    }

    public boolean isEnableAddFavListMode(Update upd) {
        return false;
    }
}
