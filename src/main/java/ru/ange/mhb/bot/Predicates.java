package ru.ange.mhb.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ange.mhb.bot.msg.callback.Callback;
import ru.ange.mhb.bot.msg.rkm.ReplyKeyboardBtt;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;

public class Predicates {

    private static final ObjectMapper OM = new ObjectMapper();

    public static Predicate<Update> isTextMessage() {
        return upd -> upd.hasMessage() && upd.getMessage().hasText() && !upd.getMessage().getText().startsWith("/");
    }


    public static Predicate<Update> isCallbackQuery() {
        return upd -> upd.hasCallbackQuery() && upd.getCallbackQuery().getData() != null;
    }

    public static Predicate<Update> isCallbackQuery(Class<? extends Callback> callbackClass) {
        Predicate<Update> predicate = upd -> {
            CallbackQuery cq = upd.getCallbackQuery();
            String cbData = cq.getData();
            try {
                Callback callbackImpl = OM.readValue(cbData, callbackClass);
                Constructor<? extends Callback> constructor = callbackClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                return callbackImpl.equals(constructor.newInstance());
            } catch (IOException | ReflectiveOperationException e) {
                return false;
            }
        };
        return isCallbackQuery().and(predicate);
    }


    public static Predicate<Update> isCommand() {
        return upd -> upd.hasMessage() && upd.getMessage().hasText() && upd.getMessage().getText().startsWith("/");
    }

    public static Predicate<Update> isCommandStartsWith(String prefix) {
        return upd -> upd.hasMessage() && upd.getMessage().hasText()
                && upd.getMessage().getText().startsWith("/" + prefix);
    }

    // ------ inline actions ------

    public static Predicate<Update> isInlineQuery() {
        return upd -> upd.hasInlineQuery() && upd.getInlineQuery().hasQuery();
    }

    public static Predicate<Update> isReplyKeyboardBttAction(ReplyKeyboardBtt replyKeyboardBtt) {
        return isTextMessage().and(upd -> replyKeyboardBtt.isQuery(upd.getMessage().getText()));
    }
}
