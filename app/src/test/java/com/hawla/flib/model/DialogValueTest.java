package com.hawla.flib.model;

import com.hawla.flib.views.game.CustomInfoDialog;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;

public class DialogValueTest {

    @Test
    public void test_dialogValue_constructor1() {
        int messageId = 1337;
        int value = 42;
        CustomInfoDialog.DialogType dialogType = CustomInfoDialog.DialogType.GAME_OVER;
        // create:
        DialogValue dialogValue = new DialogValue(messageId, value, dialogType);
        // check
        assertEquals(messageId, dialogValue.getMessageId());
        assertNull(dialogValue.getSubMessageId());
        assertEquals(value, dialogValue.getValue());
        assertFalse(dialogValue.hasSubMessage());
        assertTrue(dialogValue.isVisible());
        assertEquals(dialogType, dialogValue.getType());
    }

    @Test
    public void test_dialogValue_constructor1_invisible() {
        int messageId = 1337;
        int value = 42;
        CustomInfoDialog.DialogType dialogType = CustomInfoDialog.DialogType.INVISIBLE;
        // create:
        DialogValue dialogValue = new DialogValue(messageId, value, dialogType);
        // check
        assertFalse(dialogValue.isVisible());
    }

    @Test
    public void test_dialogValue_constructor2_submessage() {
        int messageId = 1337;
        int value = 42;
        int submessageId = 12345;
        CustomInfoDialog.DialogType dialogType = CustomInfoDialog.DialogType.INVISIBLE;
        // create:
        DialogValue dialogValue = new DialogValue(messageId, value, dialogType, submessageId);
        // check
        assertEquals(new Integer(submessageId), dialogValue.getSubMessageId());
    }

}
