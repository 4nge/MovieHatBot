package ru.ange.mhb.utils;

import ru.ange.mhb.bot.msg.impl.HelloMsg;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

public class Constants {

    private static DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols();
    static {
        SYMBOLS.setGroupingSeparator(' ');
    }

    public static final DateFormat PRINT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    public static final DecimalFormat PRINT_DECIMAL_FORMAT = new DecimalFormat("###,###.00", SYMBOLS);


    public static final String START_MSG_TEXT = "" +
            "Добро пожаловать!\n" +
            "Я бот для создания и редактирования ваших личных и общих списков фильмов, " +
            "которые вы собираетесь посмотреть. " +
            "Для того чтобы начать просто отправьте мне название фильма, " +
            "которой вы хотите добавить в список - я постараюсь его найти.\n" +
            "Для более подробней информации - используйте команду: /info";


    public static final String FOUND_MOVIES_NONE = "По запросу \"%s\" ничего не найдено \uD83E\uDD37\u200D♂️";
    public static final String FOUND_MOVIES = "По запросу \"%s\" найдено %s фильмов(а):\n\n";

    public static final String MOVIES_FAV_ICON = ":closed_book:";
    public static final String MOVIES_WATCHED_ICON = ":ballot_box_with_check:";
    public static final String MOVIES_TITLE = ":movie_camera: - %s ";
    public static final String MOVIES_PARAMS = "[%s] ";
    public static final String MOVIES_ID_PREFIX = "inf";
    public static final String MOVIES_ID = "/" + MOVIES_ID_PREFIX + "%s \n";
    public static final String MOVIES_COUNT_DEL = " ,%s";

    public static final String MOVIES_DEL_PREFIX = "del";
    public static final String MOVIES_DEL_ID = "/" + MOVIES_DEL_PREFIX + "%s ";

    public static final String MOVIES_MOV_PREFIX = "mov";
    public static final String MOVIES_MOV_ID = "/" + MOVIES_MOV_PREFIX + "%s ";

    public static final String MOVIES_WATCH_PREFIX = "watch";
    public static final String MOVIES_WATCH_ID = "/" + MOVIES_WATCH_PREFIX + "%s \n";

    public static final String MOVIES_UNWATCH_PREFIX = "unwatch";
    public static final String MOVIES_UNWATCH_ID = "/" + MOVIES_UNWATCH_PREFIX + "%s \n";

    public static final String MOVIES_COUNTRY = ":round_pushpin: - %s\n";
    public static final String MOVIES_DIRECTOR = ":clapper: - %s\n";
    public static final String MOVIES_GENRE = ":label: - %s\n";
    public static final String MOVIES_STARS = ":chart_with_upwards_trend: - %s\n";
    public static final String MOVIES_ACTORS = ":busts_in_silhouette: - %s\n";
    public static final String MOVIES_PRODUCER = ":sunglasses: - %s\n";
    public static final String MOVIES_COMPOSER = ":musical_score: - %s\n";
    public static final String MOVIES_WRITERS = ":writing_hand: - %s\n";
    public static final String MOVIES_PHOTOGRAPHER = ":movie_camera: - %s\n";

    public static final String MOVIES_RELEASE_DATE = ":calendar: - %s\n";
    public static final String MOVIES_DURATION = ":hourglass: - %s\n";
    public static final String MOVIES_BUDGET = ":money_with_wings: - %s $\n";
    public static final String MOVIES_REVENUE = ":moneybag: - %s $\n";
    public static final String MOVIES_TAGLINE = ":memo: - %s\n";
    public static final String MOVIES_ORGTITLE = ":name_badge: - %s\n";
    public static final String MOVIES_ACTOR = ":bust_in_silhouette: - %s\n";

    public static final String MOVIE_BTT_MORE_DETAILS = ":clipboard: Подробнее";
    public static final String MOVIE_BTT_ADD_TO_FAV = ":closed_book: Добавить в избранное";
    public static final String MOVIE_BTT_EDIT_FAV = ":closed_book: Редактировать";
    public static final String MOVIE_BTT_ADD_TO_WATCHED = ":ballot_box_with_check: В просмотреные";
    public static final String MOVIE_BTT_REMOVE_FROM_WATCHED = ":leftwards_arrow_with_hook: Вернуть из просмотренных";
    public static final String MOVIE_BTT_RATING = ":star: %s";

    public static final String MOVIE_DET_INFO = ":information_source: Инфо";
    public static final String MOVIE_DET_DESC = ":page_with_curl: Описание";
    public static final String MOVIE_DET_ACTS = ":busts_in_silhouette: В ролях";
    public static final String MOVIE_DET_CREW = ":movie_camera: Команда";

    public static final String KP_LINK_BTT = "КиноПоиск";
    public static final String WIKI_LINK_BTT = "Wiki";

    public static final String DEF_MOVIE_LIST_NAME = "К просмотру";
    public static final String MOVIE_LISTS_MSG_TXT = ":closed_book: Избранное: ";

    public static final String MOVIE_LIST_PUBLIC = ":busts_in_silhouette: - %s -";
    public static final String MOVIE_LIST_PERS = ":bust_in_silhouette: - %s -";
    public static final String MOVIE_LIST_ADD_MARK = " :heavy_check_mark:";
    public static final String MOVIE_LIST_ADD_TXT = "\n\nВыберите список, в который хотите добавить фильм:";

    public static final String BACK_BTT_TXT = ":arrow_left: Назад";


    public static final String ADD_FAV_LIST_MSG_EMPTY_NAME =
            ":x:\n" +
            "Название списка не может быть пустым.\n" +
            "Для добавление списка введите:\n" +
            "/addlist Название списка";

    public static final String ADD_FAV_LIST_MSG_SUCCESS = "Список \"%s\" успешно добавлен.\n" +
            "Для просмотра ваших списков воспользуйтесь коммандой /favorite или кнопкой \""+
            HelloMsg.BasicReplyKeyboardMarkup.FAVORITE_BTT.getText() + "\"";

    public static final String FAV_LIST_IS_EMPTY = "Список в данный момент пуст";
    public static final String FAV_LIST_IS_ONLY_WATCHED = "В данном спсике только просмотренные фильмы";

    public static final String FAV_LIST_TITLE_PTT = " (%s не просмотренно; %s просмотренно)\n\n";

    public static final String SHOW_WATCHED_FAVLIST_CB = ":ballot_box_with_check: Показать просмотренные";
    public static final String HIDE_WATCHED_FAVLIST_CB = ":ballot_box_with_check: Скрыть просмотренные";

    public static final String EDIT_FAVLIST_CB = ":memo: Редактировать";
    public static final String END_EDIT_FAVLIST_CB = ":memo: Завершить редактирование";

    public static final String EDIT_FAVLIST_NAME_CB = ":pencil2: Переименовать список";
    public static final String REMOVE_FAVLIST_CB = ":x: Удалить список";


    public static final String UNKNOW_ERROR = ":x:\nПри обработке запроса произошла неизвестная ошибка";
    public static final String ERROR = ":x:\nПри обработке запроса произошла ошибка \"%s\"";

    public static final String DELETE_FAVLIST_YES_NO = "Вы на полном серьезе уверены что хотите удалить удалить список \"%s\"?";

    public static final String DELETE_FAVLIST_YES_CB = ":warning: Да";
    public static final String DELETE_FAVLIST_NO_CB = ":arrow_heading_up: Нет";

    // -- Path --
    public static final String WATERMARK_PATH = "watchedWaterMark.png";
}
