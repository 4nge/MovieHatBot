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
import ru.ange.mhb.bot.msg.callback.CallbackWithTmdbIdImpl;
import ru.ange.mhb.bot.msg.callback.fav.*;
import ru.ange.mhb.bot.msg.callback.findmovies.PagesCallback;
import ru.ange.mhb.bot.msg.callback.movie.BackToMovieInfoCallback;
import ru.ange.mhb.bot.msg.callback.movie.SetRatingCallback;
import ru.ange.mhb.bot.msg.impl.HelloMsg;
import ru.ange.mhb.pojo.fav.FavList;
import ru.ange.mhb.pojo.fav.FavMovie;
import ru.ange.mhb.pojo.movie.MovieFullInfo;
import ru.ange.mhb.pojo.movie.MoviesPage;
import ru.ange.mhb.pojo.user.BotUserExtended;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;


@Service
public class BotService {

    private interface HandleCallbackStrategy {
        ResponseMsg getResponseMsg(CallbackQuery cq, Message msg, Message rtMsg) throws IOException;
    }

    private static final ObjectMapper OM = new ObjectMapper();


    @Autowired
    private DbService dbService;

    @Autowired
    private MovieService movieService;


    private ResponseMsg handleCallback(Update update, HandleCallbackStrategy strategy) {
        CallbackQuery cq = update.getCallbackQuery();
        try {
            Message msg = cq.getMessage();
            Message rtMsg = msg.getReplyToMessage();
            return strategy.getResponseMsg(cq, msg, rtMsg);
        } catch (Exception e) {
            return new ErrorMsg(getChatId(update))
                    .setReason(e)
                    .setCallbackQueryId(cq.getId());
        }
    }

    public ResponseMsg getHelloResponseMsg(MessageContext ctx) {
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
        return new HelloMsg(ctx.chatId());
    }

    public ResponseMsg getFavListsResponseMsg(Update upd) {
        return getFavListsMsg(upd.getMessage().getFrom().getId(), getChatId(upd));
    }

    public ResponseMsg getFavListsResponseMsg(MessageContext ctx) {
        return getFavListsMsg(ctx.user().getId(), ctx.chatId());
    }

    private ResponseMsg getFavListsMsg(int userId, long chatId) {
        List<FavList> favLists = dbService.getBotUserByTelUserId(userId).getFavLists();
        return new FavListsMsg(favLists, favLists.get(0).getId(), chatId);
    }

    public ResponseMsg getAddFavListSendMessage(MessageContext ctx) {
        String listName = new String();
        for (String arg : ctx.arguments())
            listName += " " + arg;

        BotUserExtended botUser = dbService.getBotUserByTelUserId(ctx.user().getId());
        FavList newFavList = dbService.addFavList(new FavList(
                listName.trim(),
                new Date(),
                botUser.getId(),
                false));

        return new AddFavListMsg(ctx.chatId(), newFavList.getName());
    }

    public ResponseMsg getFindingMovieSendMessage(Update upd) {
        Message msg = upd.getMessage();
        String query = msg.getText();
        BotUserExtended botUser = dbService.getBotUserByTelUserId(msg.getFrom().getId());
        MoviesPage moviesPage = movieService.searchMovies(query,1, botUser.getLanguage(), botUser.isAdult());
        return new FindingMoviesMsg(moviesPage, query, getChatId(upd), botUser)
                .setReplyToMsgId(upd.getMessage().getMessageId());
    }

    public ResponseMsg getMovieInfoBotMessage(Update upd, String movieCommandsPrefix) {
        Message msg = upd.getMessage();
        String query = upd.getMessage().getText().replace("/", "");
        int movieId = Integer.valueOf(query.replace(movieCommandsPrefix, ""));

        MovieFullInfo movie = movieService.getMovie(movieId);
        BotUserExtended botUser = dbService.getBotUserByTelUserId(msg.getFrom().getId());
        return new MovieInfoMsg(getChatId(upd), movie, botUser)
                .setReplyToMsgId(upd.getMessage().getMessageId());
    }


    public ResponseMsg handlePagesCallback(Update upd) {
        return handleCallback(upd, (cq, msg, rtMsg) -> {
            PagesCallback pcb = OM.readValue(cq.getData(), PagesCallback.class);
            String query = msg.getReplyToMessage().getText();

            BotUserExtended botUser = dbService.getBotUserByTelUserId(rtMsg.getFrom().getId());
            MoviesPage moviesPage = movieService.searchMovies(query, pcb.getPageNumber(),
                    botUser.getLanguage(), botUser.isAdult());

            return new FindingMoviesMsg(moviesPage, query, getChatId(upd), botUser)
                    .setEditMsgId(msg.getMessageId())
                    .setReplyToMsgId(rtMsg.getMessageId());
        });
    }

    public ResponseMsg handleBackToMovieInfoCallback(Update upd) {
        return handleCallback(upd, (cq, msg, rtMsg) -> {
            BackToMovieInfoCallback callback = OM.readValue(cq.getData(), BackToMovieInfoCallback.class);
            BotUserExtended botUser = dbService.getBotUserByTelUserId(rtMsg.getFrom().getId());
            MovieFullInfo mfi = movieService.getMovie(callback.getMovieId());
            return new MovieInfoMsg(getChatId(upd), mfi, botUser)
                    .setReplyToMsgId(rtMsg.getMessageId())
                    .setEditMsgId(msg.getMessageId());
        });
    }

    public ResponseMsg handleDetailCallback(Update upd, MovieInfoDetailsMsg.Type type) {
        return handleCallback(upd, (cq, msg, rtMsg) -> {
            CallbackWithTmdbIdImpl cb = OM.readValue(cq.getData(), CallbackWithTmdbIdImpl.class);
            BotUserExtended botUser = dbService.getBotUserByTelUserId((msg.getReplyToMessage().getFrom().getId()));
            MovieFullInfo mfi = movieService.getMovieDetails(cb.getTmdbMovieId());
            return new MovieInfoDetailsMsg(getChatId(upd), mfi, botUser, type)
                    .setEditMsgId(msg.getMessageId())
                    .setReplyToMsgId(rtMsg.getMessageId());
        });
    }

    public ResponseMsg handleAddToFavoriteCallback(Update upd) {
        return handleCallback(upd, (cq, msg, rtMsg) -> {
            AddToFavoriteCallback atfCallback = OM.readValue(cq.getData(), AddToFavoriteCallback.class);
            BotUserExtended botUser = dbService.getBotUserByTelUserId(rtMsg.getFrom().getId());
            return new MovieInfoAddToFavMsg(getChatId(upd), movieService.getMovie(atfCallback.getMovieId()), botUser)
                    .setReplyToMsgId(rtMsg.getMessageId())
                    .setEditMsgId(msg.getMessageId());
        });
    }

    public ResponseMsg handleChoiceFavListCallback(Update upd) {
        return handleCallback(upd, (cq, msg, rtMsg) -> {
            ChoiceFavListToAddMovieCallback cflc = OM.readValue(cq.getData(), ChoiceFavListToAddMovieCallback.class);
            BotUserExtended botUser = dbService.getBotUserByTelUserId((rtMsg.getFrom().getId()));

            MovieFullInfo movie = movieService.getMovie(cflc.getMovieId());
            FavMovie newFavMovie = dbService.addFavMovie(new FavMovie(movie.getTitle(), movie.getTmdbId())
                    .setAddDate(new Date())
                    .setFavListId(cflc.getFavListId())
                    .setAddUserId(botUser.getId()));

            botUser.getFavListById(cflc.getFavListId()).getFavMovies().add(newFavMovie);
            //return new MovieInfoMsg(movie, botUser, msg.getMessageId());  // if need to go to main movie msg
            return new MovieInfoAddToFavMsg(getChatId(upd), movie, botUser)
                    .setReplyToMsgId(rtMsg.getMessageId())
                    .setEditMsgId(msg.getMessageId());
        });
    }

    public ResponseMsg handleWatchedCallback(Update upd, boolean watched) {
        return handleCallback(upd, (cq, msg, rtMsg) -> {
            CallbackWithFavMovieIdImpl cb = OM.readValue(cq.getData(), CallbackWithFavMovieIdImpl.class);
            BotUserExtended botUser = dbService.getBotUserByTelUserId(msg.getReplyToMessage().getFrom().getId());

            FavMovie favMovie = botUser.getFavMovieById(cb.getFavMoviedId())
                    .setWatchedDate(watched ? new Date() : null);

            FavMovie newFavMovie = dbService.updateFavMovie(favMovie);

            return new MovieInfoMsg(getChatId(upd), movieService.getMovie(newFavMovie.getTmdbId()), botUser)
                    .setReplyToMsgId(rtMsg.getMessageId())
                    .setEditMsgId(msg.getMessageId());
        });
    }

    public ResponseMsg handleSetRatingCallback(Update upd) {
        return handleCallback(upd, (cq, msg, rtMsg) -> {
            SetRatingCallback callback = OM.readValue(cq.getData(), SetRatingCallback.class);
            BotUserExtended botUser = dbService.getBotUserByTelUserId(msg.getReplyToMessage().getFrom().getId());
            FavMovie favMovie = botUser.getFavMovieById(callback.getFavMoviedId())
                    .setRating(callback.getRating());
            dbService.updateFavMovie(favMovie);

            return new MovieInfoMsg(getChatId(upd), movieService.getMovie(favMovie.getTmdbId()), botUser)
                    .setReplyToMsgId(rtMsg.getMessageId())
                    .setEditMsgId(msg.getMessageId());
        });
    }

    private ResponseMsg handleFavListCallback(FavoriteListCallback cb, CallbackQuery cq, long chatId) {
        BotUserExtended botUser = dbService.getBotUserByTelUserId(cq.getFrom().getId());
        return new FavListsMsg(botUser.getFavLists(), cb.getFLsId(), chatId)
                .setShowWatched(cb.isWtchd())
                .setEditMode(cb.isEdit())
                .setEditMsgId(cq.getMessage().getMessageId());
    }

    public ResponseMsg handleChoiceShowingFavListToCallback(Update upd) {
        return handleCallback(upd, (cq, msg, rtMsg) -> {
            ChoiceShowingFavListToCallback cb = OM.readValue(cq.getData(), ChoiceShowingFavListToCallback.class);
            return handleFavListCallback(cb, cq, getChatId(upd));
        });
    }

    public ResponseMsg handleShowWatchedFavMoviesCallback(Update upd) {
        return handleCallback(upd, (cq, msg, rtMsg) -> {
            ShowWatchedFavMoviesCallback cb = OM.readValue(cq.getData(), ShowWatchedFavMoviesCallback.class);
            return handleFavListCallback(cb, cq, getChatId(upd));
        });
    }

    public ResponseMsg handleEditFavListCallback(Update upd) {
        return handleCallback(upd, (cq, msg, rtMsg) -> {
            EditFavListCallback cb = OM.readValue(cq.getData(), EditFavListCallback.class);
            return handleFavListCallback(cb, cq, getChatId(upd));
        });
    }

    public ResponseMsg handleDeleteFavListCallback(Update upd) {
        return handleCallback(upd, (cq, msg, rtMsg) -> {
            DeleteFavListCallback cb = OM.readValue(cq.getData(), DeleteFavListCallback.class);
            BotUserExtended botUser = dbService.getBotUserByTelUserId(cq.getFrom().getId());
            FavList favList = botUser.getFavListById(cb.getFLsId());
            return new DeleteYesNoMsg(getChatId(upd), favList, cb.isWtchd())
                    .setEditMsgId(cq.getMessage().getMessageId());
        });
    }

    public ResponseMsg handleDeleteConfirmFavListCallback(Update upd) {
        return handleCallback(upd, (cq, msg, rtMsg) -> {
            DeleteConfirmFavListCallback cb = OM.readValue(cq.getData(), DeleteConfirmFavListCallback.class);
            BotUserExtended botUser = dbService.getBotUserByTelUserId(cq.getFrom().getId());
            FavList favList = botUser.getFavListById(cb.getFLsId());
            dbService.deleteFavList(favList);

            return new FavListsMsg(botUser.getFavLists(), cb.getFLsId(), getChatId(upd))
                    .setShowWatched(cb.isWtchd())
                    .setEditMode(false)
                    .setEditMsgId(cq.getMessage().getMessageId());
        });
    }
}
