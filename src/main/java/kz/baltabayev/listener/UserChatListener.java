package kz.baltabayev.listener;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import kz.baltabayev.entity.Chat;
import kz.baltabayev.entity.UserChat;

public class UserChatListener {

    @PostPersist
    public void postPersist(UserChat userChat) {
        Chat chat = userChat.getChat();
        chat.setCount(chat.getCount() + 1);
    }

    @PostRemove
    public void postRemove(UserChat userChat) {
        Chat chat = userChat.getChat();
        chat.setCount(chat.getCount() - 1);
    }
}
