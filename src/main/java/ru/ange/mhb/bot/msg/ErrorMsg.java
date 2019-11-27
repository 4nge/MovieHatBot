package ru.ange.mhb.bot.msg;

import ru.ange.mhb.utils.Constants;

public class ErrorMsg extends ResponseMsg {

    private Exception reason;
    private String name;

    public ErrorMsg(long chatId) {
        super(chatId);
    }

    @Override
    public String getText() {
        if (name != null && !name.isEmpty())
            return String.format(Constants.ERROR, name);
        else
            return Constants.UNKNOW_ERROR;
    }

    public String getName() {
        return name;
    }

    public ErrorMsg setName(String name) {
        this.name = name;
        return this;
    }

    public Exception getReason() {
        return reason;
    }

    public ErrorMsg setReason(Exception reason) {
        this.reason = reason;
        return this;
    }

}
