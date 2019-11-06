package ru.ange.mhb.utils;

public class Constants {

    public static final String START_MSG_TEXT = "" +
            "Добро пожаловать!\n" +
            "Я бот для создания и редактирования ваших личных и общих списков фильмов, " +
            "которые вы собираетесь посмотреть. " +
            "Для того чтобы начать просто отправьте мне название фильма, " +
            "которой вы хотите добавить в список - я постараюсь его найти.\n" +
            "Для более подробней информации - используйте команду: /info";


    public static final String FOUND_MOVIES_NONE = "По запросу \"%s\" ничего не найдено \uD83E\uDD37\u200D♂️";
    public static final String FOUND_MOVIES = "По запросу \"%s\" найдено %s фильмов(а):\n\n";

    public static final String MOVIES_TITLE = ":movie_camera: - %s ";
    public static final String MOVIES_PARAMS = "[%s] ";
    public static final String MOVIES_ID = "/mv%s \n";
    public static final String MOVIES_COUNT_DEL = " ,%s";

    public static final String MOVIES_DIRECTOR = ":clapper: - %s\n";
    public static final String MOVIES_GENRE = ":label: - %s\n";
    public static final String MOVIES_STARS = ":star: - %s\n";
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


    public static final String DEF_MOVIE_LIST_NAME = "К просмотру";

    public static final String MOVIE_LISTS_MSG_TXT = "Списки избранных фильмов:";

    public static final String MOVIE_LIST_PUBLIC = ":busts_in_silhouette: - %s -";
    public static final String MOVIE_LIST_PERS = ":bust_in_silhouette: - %s -";
    public static final String MOVIE_LIST_ADD_MARK = " :heavy_check_mark:";
    public static final String MOVIE_LIST_ADD_TXT = "\n\nВыберите список, в который хотите добавить фильм:";

    public static final String BACK_BTT_TXT = ":arrow_left: Назад";

    //public static final String MOVIES_INFO_MSG = "%s\n%s\n%s\n%s\n:star: - %s \n\n%s";

    public static final String PAGES_CURRENT = " - %s - ";
    public static final String PAGES_OVER = " %s ";

    public static final String PAGES_CALLBACK = "{name:mv_page_callback,query:%s,page:%s}";


    // -- Path --
    public static final String WATERMARK_PATH = "watchedWaterMark.png";
}
