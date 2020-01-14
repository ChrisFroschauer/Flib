package com.hawla.flib.viewmodel.entities;

import com.hawla.flib.views.game.CustomInfoDialog;

public class DialogValue {

    private int messageId;
    private CustomInfoDialog.DialogType type;
    private int value;
    private Integer subMessageId;

    public DialogValue(int messageId, int value, CustomInfoDialog.DialogType type) {
        this.messageId = messageId;
        this.type = type;
        this.value = value;
        subMessageId = null;
    }

    public DialogValue(int messageId, int value, CustomInfoDialog.DialogType type, int subMessageId) {
        this.messageId = messageId;
        this.type = type;
        this.value = value;
        this.subMessageId = subMessageId;
    }

    public boolean isVisible() {
        return type != CustomInfoDialog.DialogType.INVISIBLE;
    }

    public int getMessageId() {
        return messageId;
    }

    public boolean hasSubMessage() {
        return subMessageId != null;
    }

    public int getSubMessageId(){
        return subMessageId;
    }
    public CustomInfoDialog.DialogType getType(){
        return type;
    }

    public int getValue(){
        return value;
    }

}
