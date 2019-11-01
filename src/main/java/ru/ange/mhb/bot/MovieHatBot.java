package ru.ange.mhb.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ange.mhb.bot.msg.*;
import ru.ange.mhb.bot.msg.callback.*;
import ru.ange.mhb.bot.msg.callback.detail.ActorsCallback;
import ru.ange.mhb.bot.msg.callback.detail.CrewCallback;
import ru.ange.mhb.bot.msg.callback.detail.DescriptionCallback;
import ru.ange.mhb.bot.msg.callback.detail.MoreDetailsCallback;
import ru.ange.mhb.pojo.fav.FavMovie;
import ru.ange.mhb.pojo.movie.Movie;
import ru.ange.mhb.pojo.movie.MovieFullInfo;
import ru.ange.mhb.pojo.movie.MoviesPage;
import ru.ange.mhb.pojo.user.BotUserBasic;
import ru.ange.mhb.pojo.user.BotUserExtended;
import ru.ange.mhb.service.BotService;

import java.io.IOException;
import java.util.Date;
import java.util.function.Consumer;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;



// TODO Add start button command handler!
public class MovieHatBot extends AbilityBot {

    private static final ObjectMapper OM = new ObjectMapper();

    @Autowired
    private BotService botService;

    public MovieHatBot(String token, String name, DefaultBotOptions botOptions) {
        super( token, name, botOptions );
    }

    public MovieHatBot(String token, String name) {
        super( token, name );
    }


    @Override
    public int creatorId() {
        return 0;
    }

    @Override
    public void onClosing() {}


    public Ability startCommand() {
        return Ability.builder()
                .name( "start" )
                .info( "Start work" )
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {

//                    TODO use common method to get user
//                    User telUser = ctx.update().getMessage().getFrom();
//                    int telId = telUser.getId();
//
//                    TODO use BotUserBasic ?
//                    BotUserBasic botUser = botService.getBotUserBasicByTelUserId( telId );
//                    if (botUser == null || botUser.getId() == 0) {
//                        BotUserBasic newBotUser = new BotUserBasic(0, telId,
//                                telUser.getUserName(), telUser.getLanguageCode(), false );
//                        botUser = botService.addBotUser( newBotUser );
//                    }

                    HelloMsg msg = new HelloMsg();
                    send( msg.getMessage( ctx.chatId() ) );

                }).build();
    }

    public Reply handleTextMessage() {

        Consumer<Update> action = upd -> {

            Message msg = upd.getMessage();
            String query = msg.getText();

            BotUserExtended botUser = botService.getBotUserByTelUserId( msg.getFrom().getId() );

            // - Keyboard buttons -

            if (HelloMsg.BRKM.INFO_BTT.isQuery(query)) {
                System.out.println("HelloMsg.BRKM.INFO_BTT.isQuery(query)");
            } else if (HelloMsg.BRKM.SETTING_BTT.isQuery(query)) {
                System.out.println("HelloMsg.BRKM.SETTING_BTT.isQuery(query)");
            } else if (HelloMsg.BRKM.FAVORITE_BTT.isQuery(query)) {
                System.out.println("HelloMsg.BRKM.FAVORITE_BTT.isQuery(query)");

                //FavoriteMsg favMsg = new FavoriteMsg( botService.getUserFavLists( botUser ) );
                //send( favMsg.getMessage( getChatId(upd) ) );

            // - Query not from buttons -
            } else {
                // TODO use BotUserBasic ?
                FindingMoviesMsg fmm = getFindingMoviesMsg( query, botUser );
                send( fmm.getMessage( getChatId(upd), msg.getMessageId() ) );
            }

        };
        return Reply.of(action, Predicates.isTextMessage());
    }

    public Reply showMovie() {

        String mvCommandPrefix = "mv";
        Consumer<Update> action = upd -> {

            Message msg = upd.getMessage();
            String query = upd.getMessage().getText().replace("/", "");
            int movieId = Integer.valueOf(query.replace(mvCommandPrefix, ""));

            MovieFullInfo movie = botService.getMovie(movieId);
            BotUserExtended botUser = botService.getBotUserByTelUserId( msg.getFrom().getId());

            MovieInfoMsg mim = new MovieInfoMsg(movie, botUser);

            if (mim.hasPhoto())
                send( mim.getPhotoMessage(getChatId(upd), msg.getMessageId()));
            else
                send( mim.getTextMessage(getChatId(upd), msg.getMessageId()));

        };
        return Reply.of(action, Predicates.isCommandStartsWith(mvCommandPrefix));
    }



    public Reply handlePagesCallback() {
        Consumer<Update> action = upd -> {

            CallbackQuery cq = upd.getCallbackQuery();
            Message msg = cq.getMessage();
            Message rtMsg = msg.getReplyToMessage();

            try {
                PagesCallback pcb = OM.readValue(cq.getData(), PagesCallback.class);
                String query = msg.getReplyToMessage().getText();

                BotUserExtended botUser = botService.getBotUserByTelUserId(rtMsg.getFrom().getId());
                MoviesPage moviesPage = botService.searchMovies(query, pcb.getPageNumber(),
                        botUser.getLanguage(), botUser.isAdult());

                FindingMoviesMsg fmMsg = new FindingMoviesMsg( moviesPage, query );
                send(fmMsg.getEditMessageText( getChatId(upd), msg.getMessageId() ) );

            } catch (IOException e) {
                e.printStackTrace();
            }

        };
        return Reply.of(action, Predicates.isCallbackQuery(PagesCallback.class));
    }


    public Reply handleAddToFavoriteCallback() {
        Consumer<Update> action = upd -> {

            CallbackQuery cq = upd.getCallbackQuery();
            Message msg = cq.getMessage();
            Message rtMsg = msg.getReplyToMessage();

            try {
                AddToFavoriteCallback atfCallback = OM.readValue(cq.getData(), AddToFavoriteCallback.class);
                BotUserExtended botUser = botService.getBotUserByTelUserId(rtMsg.getFrom().getId());

                MovieInfoMsg atfcMim = new MovieInfoMsg(botService.getMovie(atfCallback.getMovieId()), botUser);
                send(atfcMim.getAddToFavEditMsg(getChatId(upd), msg.getMessageId()));

            } catch (IOException e) {
                e.printStackTrace();
            }

        };
        return Reply.of(action, Predicates.isCallbackQuery(AddToFavoriteCallback.class));
    }


    public Reply handleChoiceFavListCallback() {
        Consumer<Update> action = upd -> {

            CallbackQuery cq = upd.getCallbackQuery();
            Message msg = cq.getMessage();
            Message rtMsg = msg.getReplyToMessage();

            try {
                ChoiceFavListCallback cflc =  OM.readValue(cq.getData(), ChoiceFavListCallback.class);
                BotUserExtended botUser = botService.getBotUserByTelUserId(rtMsg.getFrom().getId());

                MovieFullInfo movie = botService.getMovie(cflc.getMovieId());
                FavMovie newFavMovie =  botService.addFavMovie( new FavMovie(
                        movie.getName(),
                        movie.getTmdbId(),
                        new Date(),
                        cflc.getFavListId(),
                        botUser.getId(),
                        false ) );

                botUser.getFavListById( cflc.getFavListId() ).getFavMovies().add( newFavMovie );

                // if need to go to main movie msg
                //MovieInfoMsg cflcMim = new MovieInfoMsg( movie, botUser );
                //send( cflcMim.getEditMsg( getChatId(upd), msg.getMessageId() ) );

                MovieInfoMsg cflcMim = new MovieInfoMsg( movie, botUser );
                send( cflcMim.getAddToFavEditMsg( getChatId(upd), msg.getMessageId() ) );

            } catch (IOException e) {
                e.printStackTrace();
            }

        };
        return Reply.of(action, Predicates.isCallbackQuery(ChoiceFavListCallback.class));
    }


    public Reply handleChoiceFavListBackCallback() {
        Consumer<Update> action = upd -> {

            CallbackQuery cq = upd.getCallbackQuery();
            Message msg = cq.getMessage();
            Message rtMsg = msg.getReplyToMessage();

            try {
                BackToShowMovieInfoCallback callback = OM.readValue(cq.getData(), BackToShowMovieInfoCallback.class);
                BotUserExtended botUser = botService.getBotUserByTelUserId(rtMsg.getFrom().getId());

                MovieInfoMsg cflbcMim = new MovieInfoMsg(botService.getMovie(callback.getMovieId()), botUser);
                send(cflbcMim.getEditMsg(getChatId(upd), msg.getMessageId()));

            } catch (IOException e) {
                e.printStackTrace();
            }

        };
        return Reply.of(action, Predicates.isCallbackQuery(BackToShowMovieInfoCallback.class));
    }


    private void updateFavMovieWatched(int favMovieId, boolean watched, long chatId, Message msg) {
        BotUserExtended botUser = botService.getBotUserByTelUserId(msg.getReplyToMessage().getMessageId());
        FavMovie favMovie = botUser.getFavMovieById(favMovieId)
                .setWatched(watched);

        botService.updateFavMovie(favMovie);

        MovieInfoMsg atwcbMim = new MovieInfoMsg( botService.getMovie(favMovie.getTmdbId()), botUser);
        send(atwcbMim.getEditMsgMedia(chatId, msg.getReplyToMessage().getFrom().getId()));
    }


    public Reply handleAddToWatchedCallback() {
        Consumer<Update> action = upd -> {

            CallbackQuery cq = upd.getCallbackQuery();
            Message msg = cq.getMessage();

            try {
                AddToWatchedCallback callback = OM.readValue(cq.getData(), AddToWatchedCallback.class);
                updateFavMovieWatched(callback.getFavMovieId(),true, getChatId(upd), msg);

            } catch (IOException e) {
                e.printStackTrace();
            }

        };
        return Reply.of(action, Predicates.isCallbackQuery(AddToWatchedCallback.class));
    }


    public Reply handleRevertFromWatchedCallback() {
        Consumer<Update> action = upd -> {

            CallbackQuery cq = upd.getCallbackQuery();
            Message msg = cq.getMessage();

            try {
                RevertFromWatchedCallback callback = OM.readValue(cq.getData(), RevertFromWatchedCallback.class);
                updateFavMovieWatched(callback.getFavMovieId(),false, getChatId(upd), msg);

            } catch (IOException e) {
                e.printStackTrace();
            }

        };
        return Reply.of(action, Predicates.isCallbackQuery(RevertFromWatchedCallback.class));
    }


    public Reply handleMoreDetailsCallback() {
        Consumer<Update> action = upd -> {
            try {
                CallbackQuery cq = upd.getCallbackQuery();
                MoreDetailsCallback callback = OM.readValue(cq.getData(), MoreDetailsCallback.class);
                handleMoreDetailsCallbacks(callback.getTmdbMovieId(), MovieDetailsInfoMsg.Type.INFO,
                        getChatId(upd), cq.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return Reply.of(action, Predicates.isCallbackQuery(MoreDetailsCallback.class));
    }

    public Reply handleDescriptionCallback() {
        Consumer<Update> action = upd -> {
            try {
                CallbackQuery cq = upd.getCallbackQuery();
                DescriptionCallback callback = OM.readValue(cq.getData(), DescriptionCallback.class);
                handleMoreDetailsCallbacks(callback.getTmdbMovieId(), MovieDetailsInfoMsg.Type.DESCRIPTION,
                        getChatId(upd), cq.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return Reply.of(action, Predicates.isCallbackQuery(DescriptionCallback.class));
    }

    public Reply handleActorsCallback() {
        Consumer<Update> action = upd -> {
            try {
                CallbackQuery cq = upd.getCallbackQuery();
                ActorsCallback callback = OM.readValue(cq.getData(), ActorsCallback.class);
                handleMoreDetailsCallbacks(callback.getTmdbMovieId(), MovieDetailsInfoMsg.Type.ACTORS,
                        getChatId(upd), cq.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return Reply.of(action, Predicates.isCallbackQuery(ActorsCallback.class));
    }

    public Reply handleCrewCallback() {
        Consumer<Update> action = upd -> {
            try {
                CallbackQuery cq = upd.getCallbackQuery();
                CrewCallback callback = OM.readValue(cq.getData(), CrewCallback.class);
                handleMoreDetailsCallbacks(callback.getTmdbMovieId(), MovieDetailsInfoMsg.Type.CREW,
                        getChatId(upd), cq.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return Reply.of(action, Predicates.isCallbackQuery(CrewCallback.class));
    }

    private void handleMoreDetailsCallbacks(int tmdbId, MovieDetailsInfoMsg.Type type, long chatId, Message msg) {
        BotUserExtended botUser = botService.getBotUserByTelUserId(msg.getReplyToMessage().getFrom().getId());
        MovieFullInfo mfi = botService.getMovieDetails(tmdbId);

        MovieDetailsInfoMsg mdMsg = new MovieDetailsInfoMsg(mfi, botUser, type);
        send(mdMsg.getEditMsg(chatId, msg.getMessageId()));
    }

    private FindingMoviesMsg getFindingMoviesMsg(String query, BotUserBasic botUser) {
        MoviesPage moviesPage = botService.searchMovies(query, 1,
                botUser.getLanguage(), botUser.isAdult());
        return new FindingMoviesMsg(moviesPage, query);
    }


    private void send(BotApiMethod method) {
        try {
            sender.execute( method );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(SendPhoto photo) {
        try {
            sender.sendPhoto( photo );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(EditMessageMedia editMessageMedia) {
        try {
            execute(editMessageMedia);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
