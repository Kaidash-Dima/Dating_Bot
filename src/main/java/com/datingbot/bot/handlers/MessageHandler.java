package com.datingbot.bot.handlers;

import com.datingbot.entity.ChatStatus;
import com.datingbot.entity.Language;
import com.datingbot.entity.User;
import com.datingbot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageHandler {
    private final UserService userService;
    private final MenuHandler menuHandler;

    @Autowired
    public MessageHandler(UserService userService, MenuHandler menuHandler) {
        this.userService = userService;
        this.menuHandler = menuHandler;
    }

    public SendMessage handle(Update update) {

        SendMessage sendMessage = null;

        if (update.getMessage().getText().equals("/start") &&  !userService.existsByUserId(update.getMessage().getFrom().getId())) {

            User user = new User();
            user.setUserId(update.getMessage().getFrom().getId());
            user.setNickname(update.getMessage().getFrom().getUserName());
            user.setName(update.getMessage().getFrom().getFirstName());
            if (userService.getCountUsers() == 0) {
                user.setOppositeSexId(1);
            }else{
                user.setOppositeSexId(userService.getFirstUser().getId());
            }
            user.setLanguage(Language.RUSSIAN);
            user.setChatStatus(ChatStatus.REGISTRATION);
            userService.saveUser(user);
            sendMessage = menuHandler.displayLanguageQuestion(update, user);

        } else {

            User user = userService.findByUserId(update.getMessage().getFrom().getId());

            menuHandler.determineStatus(update, user);

            switch (user.getChatStatus()) {
                case REGISTRATION:
                    sendMessage = menuHandler.displayLanguageQuestion(update, user);
                    break;
                case LANGUAGE:
                    menuHandler.setLanguageProfile(update, user);
                    if (user.getAge() == 0) {
                        sendMessage = menuHandler.startProfile(update, user);
                    } else {
                        sendMessage = menuHandler.waitingProfile(update, user);
                    }
                    break;
                case START:
                    sendMessage = menuHandler.startProfile(update, user);
                    break;
                case ENTER_NAME:
                    sendMessage = menuHandler.setNameProfile(update, user);
                    break;
                case ENTER_AGE:
                    sendMessage = menuHandler.setAgeProfile(update, user);
                    break;
                case ENTER_SEX:
                    sendMessage = menuHandler.setSexProfile(update, user);
                    break;
                case ENTER_OPPOSITE_SEX:
                    sendMessage = menuHandler.setOppositeSexProfile(update, user);
                    break;
                case ENTER_CITY:
                    sendMessage = menuHandler.setCiteProfile(update, user);
                    break;
                case ENTER_DESCRIPTION:
                    menuHandler.setDescriptionProfile(update, user);
                    menuHandler.myProfile(update, user);
                    break;
                case LIKE:
                case DISLIKE:
                    sendMessage = menuHandler.nextProfile(update, user);
                    break;
                case SLEEP:
                    menuHandler.outputWaitingProfile(update, user);
                    sendMessage = menuHandler.waitingProfile(update, user);
                    break;
                case SHOW_MATCHES:
                    menuHandler.likeProfile(update, user);
                    sendMessage = menuHandler.waitingProfile(update, user);
                    break;
            }
        }
        return sendMessage;
    }
}
