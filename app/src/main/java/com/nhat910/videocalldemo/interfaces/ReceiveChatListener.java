package com.nhat910.videocalldemo.interfaces;

import com.quickblox.chat.model.QBChatMessage;

public interface ReceiveChatListener {
    void receiveChat(QBChatMessage qbChatMessage);
}
