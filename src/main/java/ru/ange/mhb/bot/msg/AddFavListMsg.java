package ru.ange.mhb.bot.msg;

import ru.ange.mhb.utils.Constants;

public class AddFavListMsg extends ResponseMsg {

    private String listName;

    public AddFavListMsg(long chatId, String listName) {
        super(chatId);
        this.listName = listName;
    }

    @Override
    public String getText() {
        if (listName.isEmpty())
            return Constants.ADD_FAV_LIST_MSG_EMPTY_NAME;
        else
            return String.format(Constants.ADD_FAV_LIST_MSG_SUCCESS, listName);
    }

}
