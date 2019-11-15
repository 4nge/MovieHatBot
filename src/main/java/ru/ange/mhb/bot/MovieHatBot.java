package ru.ange.mhb.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ange.mhb.bot.msg.*;
import ru.ange.mhb.bot.msg.callback.fav.AddFavListCallback;
import ru.ange.mhb.bot.msg.callback.fav.AddToFavoriteCallback;
import ru.ange.mhb.bot.msg.callback.fav.ChoiceFavListCallback;
import ru.ange.mhb.bot.msg.callback.movie.BackToMovieInfoCallback;
import ru.ange.mhb.bot.msg.callback.movie.SetRatingCallback;
import ru.ange.mhb.bot.msg.callback.movie.detail.ActorsCallback;
import ru.ange.mhb.bot.msg.callback.movie.detail.CrewCallback;
import ru.ange.mhb.bot.msg.callback.movie.detail.DescriptionCallback;
import ru.ange.mhb.bot.msg.callback.movie.detail.MoreDetailsCallback;
import ru.ange.mhb.bot.msg.callback.findmovies.PagesCallback;
import ru.ange.mhb.bot.msg.callback.movie.watched.AddToWatchedCallback;
import ru.ange.mhb.bot.msg.callback.movie.watched.RevertFromWatchedCallback;
import ru.ange.mhb.service.BotService;

import java.util.function.Consumer;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;


public class MovieHatBot extends AbilityBot {

    @Autowired
    private BotService botService;

    public MovieHatBot(String token, String name, DefaultBotOptions botOptions) {
        super(token, name, botOptions);
    }

    public MovieHatBot(String token, String name) {
        super(token, name);
    }


    @Override
    public int creatorId() {
        return 0;
    }

    @Override
    public void onClosing() {}


    // ------------------
    // ---- Commands ----
    // ------------------

    public Ability startCommand() {
        return Ability.builder()
                .name("start")
                .info("Start work")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    HelloMsg msg = botService.getHelloMsg(ctx);
                    send(msg.getMessage(ctx.chatId()));
                }).build();
    }



    // --------------------------
    // ---- Keyboard Buttons ----
    // --------------------------

    public Reply handleFavoriteKeyboardBtt() {
        Consumer<Update> action = upd -> {
            FavListsMsg msg = botService.getFavListsMsg(upd);
            send(msg.getMessage(getChatId(upd)));
        };
        return Reply.of(action, Predicates.isReplyKeyboardBttAction(HelloMsg.BRKM.FAVORITE_BTT));
    }


    // ---------------------
    // ---- Simple text ----
    // ---------------------

    public Reply searchMovies() {
        Consumer<Update> action = upd -> {
            FindingMoviesMsg msg = botService.getFindingMoviesMsg(upd);
            send(msg.getMessage(getChatId(upd)));
        };
        return Reply.of(action, (Predicates.isTextMessage()
                .and(Predicates.isReplyKeyboardBttAction(HelloMsg.BRKM.FAVORITE_BTT).negate())
        ));
    }

    public Reply addNewFavList() {
        Consumer<Update> action = upd -> {
            FindingMoviesMsg msg = botService.getFindingMoviesMsg(upd);
            send(msg.getMessage(getChatId(upd)));
        };
        return Reply.of(action, (Predicates.isTextMessage()
                .and(Predicates.isReplyKeyboardBttAction(HelloMsg.BRKM.FAVORITE_BTT).negate())
                .and(upd -> botService.isEnableAddFavListMode(upd))
        ));
    }

    // ---------------------
    // ---- ID commands ----
    // ---------------------

    public Reply showMovieInfo() {
        String movieCommandsPrefix = "info";
        Consumer<Update> action = upd -> {
            MovieInfoMsg mim = botService.getMovieInfoMsg(upd, movieCommandsPrefix);
            if (mim.hasPhoto())
                send(mim.getPhotoMessage(getChatId(upd)));
            else
                send(mim.getTextMessage(getChatId(upd)));
        };
        return Reply.of(action, Predicates.isCommandStartsWith(movieCommandsPrefix));
    }


    // -------------------
    // ---- Callbacks ----
    // -------------------

    // -- Search movies -- // TODO add change sort buttons

    public Reply handlePagesCallback() {
        Consumer<Update> action = upd -> {
            FindingMoviesMsg msg = botService.handlePagesCallback(upd);
            send(msg.getEditMessageText(getChatId(upd)));
        };
        return Reply.of(action, Predicates.isCallbackQuery(PagesCallback.class));
    }


    // -- Movie info --

    public Reply handleBackToMovieInfoCallback() {
        Consumer<Update> action = upd -> {
            MovieInfoMsg msg = botService.handleBackToMovieInfoCallback(upd);
            send(msg.getEditMsg(getChatId(upd)));
        };
        return Reply.of(action, Predicates.isCallbackQuery(BackToMovieInfoCallback.class));
    }

    public Reply handleMoreDetailsCallback() {
        Consumer<Update> action = upd -> {
            MovieDetailsInfoMsg msg = botService.handleDetailCallback(upd, MovieDetailsInfoMsg.Type.INFO);
            send(msg.getEditMsg(getChatId(upd)));
        };
        return Reply.of(action, Predicates.isCallbackQuery(MoreDetailsCallback.class));
    }

    public Reply handleDescriptionCallback() {
        Consumer<Update> action = upd -> {
            MovieInfoMsg msg = botService.handleDetailCallback(upd, MovieDetailsInfoMsg.Type.DESCRIPTION);
            send(msg.getEditMsg(getChatId(upd)));
        };
        return Reply.of(action, Predicates.isCallbackQuery(DescriptionCallback.class));
    }

    public Reply handleActorsCallback() {
        Consumer<Update> action = upd -> {
            MovieInfoMsg msg = botService.handleDetailCallback(upd, MovieDetailsInfoMsg.Type.ACTORS);
            send(msg.getEditMsg(getChatId(upd)));
        };
        return Reply.of(action, Predicates.isCallbackQuery(ActorsCallback.class));
    }

    public Reply handleCrewCallback() {
        Consumer<Update> action = upd -> {
            MovieInfoMsg msg = botService.handleDetailCallback(upd, MovieDetailsInfoMsg.Type.CREW);
            send(msg.getEditMsg(getChatId(upd)));
        };
        return Reply.of(action, Predicates.isCallbackQuery(CrewCallback.class));
    }

    public Reply handleAddToFavoriteCallback() {
        Consumer<Update> action = upd -> {
            MovieInfoMsg msg = botService.handleAddToFavoriteCallback(upd);
            send(msg.getAddToFavEditMsg(getChatId(upd)));
        };
        return Reply.of(action, Predicates.isCallbackQuery(AddToFavoriteCallback.class));
    }

    public Reply handleChoiceFavListCallback() {
        Consumer<Update> action = upd -> {
            MovieInfoMsg msg = botService.handleChoiceFavListCallback(upd);
            send(msg.getAddToFavEditMsg(getChatId(upd)));
        };
        return Reply.of(action, Predicates.isCallbackQuery(ChoiceFavListCallback.class));
    }

    public Reply handleAddToWatchedCallback() {
        Consumer<Update> action = upd -> {
            MovieInfoMsg msg = botService.handleWatchedCallback(upd, true);
            send(msg.getEditMsgMedia(getChatId(upd)));
        };
        return Reply.of(action, Predicates.isCallbackQuery(AddToWatchedCallback.class));
    }

    public Reply handleRevertFromWatchedCallback() {
        Consumer<Update> action = upd -> {
            MovieInfoMsg msg = botService.handleWatchedCallback(upd, false);
            send(msg.getEditMsgMedia(getChatId(upd)));
        };
        return Reply.of(action, Predicates.isCallbackQuery(RevertFromWatchedCallback.class));
    }

    public Reply handleSetRatingCallback() {
        Consumer<Update> action = upd -> {
            MovieInfoMsg msg = botService.handleSetRatingCallback(upd);
            send(msg.getEditMsg(getChatId(upd)));
        };
        return Reply.of(action, Predicates.isCallbackQuery(SetRatingCallback.class));
    }

    // -- Fav Lists --
    public Reply handleAddFavListCallback() {
        Consumer<Update> action = upd -> {
            System.out.println(" --- AddFavListCallback --- ");
        };
        return Reply.of(action, Predicates.isCallbackQuery(AddFavListCallback.class));
    }

    // ------------------------------
    // ---- Send message methods ----
    // ------------------------------

    private void send(BotApiMethod method) {
        try {
            sender.execute(method);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(SendPhoto photo) {
        try {
            sender.sendPhoto(photo);
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
