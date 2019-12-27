package ru.ange.mhb.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ange.mhb.bot.msg.MovieInfoDetailsMsg;
import ru.ange.mhb.bot.msg.ResponseMsg;
import ru.ange.mhb.bot.msg.callback.fav.*;
import ru.ange.mhb.bot.msg.callback.findmovies.PagesCallback;
import ru.ange.mhb.bot.msg.callback.movie.BackToMovieInfoCallback;
import ru.ange.mhb.bot.msg.callback.movie.SetRatingCallback;
import ru.ange.mhb.bot.msg.callback.movie.detail.ActorsCallback;
import ru.ange.mhb.bot.msg.callback.movie.detail.CrewCallback;
import ru.ange.mhb.bot.msg.callback.movie.detail.DescriptionCallback;
import ru.ange.mhb.bot.msg.callback.movie.detail.MoreDetailsCallback;
import ru.ange.mhb.bot.msg.callback.movie.watched.AddToWatchedCallback;
import ru.ange.mhb.bot.msg.callback.movie.watched.RevertFromWatchedCallback;
import ru.ange.mhb.bot.msg.impl.HelloMsg;
import ru.ange.mhb.service.BotService;
import ru.ange.mhb.utils.Constants;

import java.util.function.Consumer;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;


public class MovieHatBot extends AbilityBotWithResponseMsg {

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
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    ResponseMsg rm = botService.getHelloResponseMsg(ctx);
                    send(rm);
                }).build();
    }

    public Ability favoriteCommand() {
        return Ability.builder()
                .name("favorite")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    ResponseMsg rm = botService.getFavListsResponseMsg(ctx);
                    send(rm);
                }).build();
    }

    public Ability addFavListCommand() {
        return Ability.builder()
                .name("addlist")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    ResponseMsg rm = botService.getAddFavListSendMessage(ctx);
                    send(rm);
                }).build();
    }


    // --------------------------
    // ---- Keyboard Buttons ----
    // --------------------------

    public Reply handleFavoriteKeyboardBtt() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.getFavListsResponseMsg(upd);
            send(rm);
        };
        return Reply.of(action, Predicates.isReplyKeyboardBttAction(HelloMsg.BRKM.FAVORITE_BTT));
    }


    // ---------------------
    // ---- Simple text ----
    // ---------------------

    public Reply searchMovies() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.getFindingMovieSendMessage(upd);
            send(rm);
        };
        return Reply.of(action, (Predicates.isTextMessage()
                .and(Predicates.isReplyKeyboardBttAction(HelloMsg.BRKM.FAVORITE_BTT).negate())
                //TODO .and(Predicates.isReplyKeyboardBttAction(HelloMsg.otherButtons).negate())
        ));
    }

    /*
    public Reply addNewFavList() {
        Consumer<Update> action = upd -> {
            FindingMoviesMsg msg = botService.getFindingMoviesMsg(upd);
            send(msg.getMessage(getChatId(upd)));
        };
        return Reply.of(action, (Predicates.isTextMessage()
                .and(Predicates.isReplyKeyboardBttAction(HelloMsg.BRKM.FAVORITE_BTT).negate())
        ));
    }
    */


    // ---------------------
    // ---- ID commands ----
    // ---------------------

    public Reply showMovieInfo() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.getMovieInfoBotMessage(upd, Constants.MOVIES_ID_PREFIX);
            send(rm);
        };
        return Reply.of(action, Predicates.isCommandStartsWith(Constants.MOVIES_ID_PREFIX));
    }

    public Reply deleteMovie() {
        Consumer<Update> action = upd -> {
            //ResponseMsg rm = botService.getMovieInfoBotMessage(upd, Constants.MOVIES_ID_PREFIX);
            //send(rm);

        };
        return Reply.of(action, Predicates.isCommandStartsWith(Constants.MOVIES_DEL_PREFIX));
    }


    // -------------------
    // ---- Callbacks ----
    // -------------------

    // -- Search movies --
    // TODO add change sort buttons
    // TODO add warched and in fav icons
    public Reply handlePagesCallback() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.handlePagesCallback(upd);
            send(rm);
        };
        return Reply.of(action, Predicates.isCallbackQuery(PagesCallback.class));
    }


    // -- Movie info --

    public Reply handleBackToMovieInfoCallback() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.handleBackToMovieInfoCallback(upd);
            send(rm);
        };
        return Reply.of(action, Predicates.isCallbackQuery(BackToMovieInfoCallback.class));
    }

    public Reply handleMoreDetailsCallback() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.handleDetailCallback(upd, MovieInfoDetailsMsg.Type.INFO);
            send(rm);
        };
        return Reply.of(action, Predicates.isCallbackQuery(MoreDetailsCallback.class));
    }

    public Reply handleDescriptionCallback() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.handleDetailCallback(upd, MovieInfoDetailsMsg.Type.DESCRIPTION);
            send(rm);
        };
        return Reply.of(action, Predicates.isCallbackQuery(DescriptionCallback.class));
    }

    public Reply handleActorsCallback() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.handleDetailCallback(upd, MovieInfoDetailsMsg.Type.ACTORS);
            send(rm);
        };
        return Reply.of(action, Predicates.isCallbackQuery(ActorsCallback.class));
    }

    public Reply handleCrewCallback() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.handleDetailCallback(upd, MovieInfoDetailsMsg.Type.CREW);
            send(rm);
        };
        return Reply.of(action, Predicates.isCallbackQuery(CrewCallback.class));
    }

    public Reply handleAddToFavoriteCallback() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.handleAddToFavoriteCallback(upd);
            send(rm);
        };
        return Reply.of(action, Predicates.isCallbackQuery(AddToFavoriteCallback.class));
    }

    public Reply handleChoiceFavListCallback() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.handleChoiceFavListCallback(upd);
            send(rm);
        };
        return Reply.of(action, Predicates.isCallbackQuery(ChoiceFavListToAddMovieCallback.class));
    }

    public Reply handleAddToWatchedCallback() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.handleWatchedCallback(upd, true);
            send(rm);
        };
        return Reply.of(action, Predicates.isCallbackQuery(AddToWatchedCallback.class));
    }

    public Reply handleRevertFromWatchedCallback() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.handleWatchedCallback(upd, false);
            send(rm);
        };
        return Reply.of(action, Predicates.isCallbackQuery(RevertFromWatchedCallback.class));
    }

    public Reply handleSetRatingCallback() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.handleSetRatingCallback(upd);
            send(rm);
        };
        return Reply.of(action, Predicates.isCallbackQuery(SetRatingCallback.class));
    }


    // -- Fav Movies --

    public Reply handleChoiceShowingFavListToCallback() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.handleChoiceShowingFavListToCallback(upd);
            send(rm);
        };
        return Reply.of(action, Predicates.isCallbackQuery(ChoiceShowingFavListToCallback.class));
    }

    public Reply handleShowWatchedFavMoviesCallback() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.handleShowWatchedFavMoviesCallback(upd);
            send(rm);
        };
        return Reply.of(action, Predicates.isCallbackQuery(ShowWatchedFavMoviesCallback.class));
    }

    public Reply handleEditFavListCallback() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.handleEditFavListCallback(upd);
            send(rm);
        };
        return Reply.of(action, Predicates.isCallbackQuery(EditFavListCallback.class));
    }

    public Reply handleDeleteFavListCallback() {
        Consumer<Update> action = upd -> {
            ResponseMsg rm = botService.handleEditFavListCallback(upd);
            send(rm);
        };
        return Reply.of(action, Predicates.isCallbackQuery(EditFavListCallback.class));
    }
}
