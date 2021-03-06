package com.datingbot.bot.handlers;

import com.datingbot.bot.Bot;
import com.datingbot.entity.ChatStatus;
import com.datingbot.entity.Language;
import com.datingbot.entity.Match;
import com.datingbot.entity.User;
import com.datingbot.service.ButtonsService;
import com.datingbot.service.MatchService;
import com.datingbot.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MenuHandler {

    private final Bot bot;
    private final MatchService matchService;
    private final UserService userService;
    private final ButtonsService buttonsService;

    public MenuHandler(@Lazy Bot bot, MatchService matchService, UserService userService, ButtonsService buttonsService) {
        this.bot = bot;
        this.matchService = matchService;
        this.userService = userService;
        this.buttonsService = buttonsService;
    }

    public void setLanguageProfile(Update update, User user){
        if (update.getMessage().getText().equals("\uD83C\uDDFA\uD83C\uDDE6")){
            user.setLanguage(Language.UKRAINIAN);
        }else if (update.getMessage().getText().equals("\uD83C\uDDF7\uD83C\uDDFA")){
            user.setLanguage(Language.RUSSIAN);
        }else if (update.getMessage().getText().equals("\uD83C\uDDFA\uD83C\uDDF8")){
            user.setLanguage(Language.ENGLISH);
        }
        userService.updateUser(user);
    }

    public SendMessage displayLanguageQuestion (Update update, User user){

        SendMessage sendMessage = new SendMessage();

        if (user.getLanguage() == Language.RUSSIAN || user.getLanguage() == null) {
            sendMessage = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                    .text("???? ?????????? ?????????? ?????????? ?????????????????")
                    .replyMarkup(createButtons(List.of("\uD83C\uDDFA\uD83C\uDDE6", "\uD83C\uDDF7\uD83C\uDDFA", "\uD83C\uDDFA\uD83C\uDDF8")))
                    .build();
        } else if (user.getLanguage() == Language.UKRAINIAN) {
            sendMessage = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                    .text("???? ???????? ???????? ???????????? ?????????????????????????")
                    .replyMarkup(createButtons(List.of("\uD83C\uDDFA\uD83C\uDDE6", "\uD83C\uDDF7\uD83C\uDDFA", "\uD83C\uDDFA\uD83C\uDDF8")))
                    .build();
        } else if (user.getLanguage() == Language.ENGLISH) {
            sendMessage = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                    .text("What language should we talk?")
                    .replyMarkup(createButtons(List.of("\uD83C\uDDFA\uD83C\uDDE6", "\uD83C\uDDF7\uD83C\uDDFA", "\uD83C\uDDFA\uD83C\uDDF8")))
                    .build();
        }
        user.setChatStatus(ChatStatus.LANGUAGE);
        userService.updateUser(user);

        return sendMessage;
    }

    public void determineStatus (Update update, User user){

        if (update.getMessage().getText().equals("1")){
            user.setChatStatus(ChatStatus.START);
        } else if (update.getMessage().getText().equals("2")) {
            user.setChatStatus(ChatStatus.DISLIKE);
        }else if(update.getMessage().getText().equals("???")){
            user.setChatStatus(ChatStatus.LIKE);
        }else if(update.getMessage().getText().equals("\uD83D\uDC4E")) {
            user.setChatStatus(ChatStatus.DISLIKE);
        }else if(update.getMessage().getText().equals("\uD83D\uDCA4")){
            user.setChatStatus(ChatStatus.SLEEP);
        }else if(update.getMessage().getText().equals("3")){
            user.setChatStatus(ChatStatus.SHOW_MATCHES);
        }else if (update.getMessage().getText().equals("4")){
            user.setChatStatus(ChatStatus.REGISTRATION);
        }

        userService.updateUser(user);
    }

    public void setDescriptionProfile(Update update, User user){

        String message = update.getMessage().getText();

        switch (user.getLanguage()){

            case RUSSIAN:
                if (message.equals("????????????????????")) {
                    user.setDescription("");
                } else if (message.equals("????????????????")) {
                    user.setDescription(user.getDescription());
                } else {
                    user.setDescription(message);
                }
                break;

            case UKRAINIAN:

                if (message.equals("????????????????????")) {
                    user.setDescription("");
                } else if (message.equals("????????????????")) {
                    user.setDescription(user.getDescription());
                } else {
                    user.setDescription(message);
                }
                break;

            case ENGLISH:

                if (message.equals("Skip")) {
                    user.setDescription("");
                } else if (message.equals("Leave")) {
                    user.setDescription(user.getDescription());
                } else {
                    user.setDescription(message);
                }
                break;
        }

        userService.updateUser(user);
    }

    public SendMessage setCiteProfile(Update update, User user){

        SendMessage message = new SendMessage();

        user.setCity(update.getMessage().getText());

        user.setChatStatus(ChatStatus.ENTER_DESCRIPTION);
        userService.updateUser(user);

        switch (user.getLanguage()) {
            case RUSSIAN:

                if (user.getDescription() == null) {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("???????????????? ?? ???????? ?? ???????? ???????????? ??????????, ?????? ?????????????????????? ????????????????. " +
                                    "?????? ?????????????? ?????????? ?????????????????? ???????? ????????????????.")
                            .replyMarkup(createButtons(List.of("????????????????????"))).build();
                } else {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("???????????????? ?? ???????? ?? ???????? ???????????? ??????????, ?????? ?????????????????????? ????????????????. " +
                                    "?????? ?????????????? ?????????? ?????????????????? ???????? ????????????????.")
                            .replyMarkup(createButtons(List.of("????????????????????", "????????????????"))).build();
                }
                break;

            case UKRAINIAN:

                if (user.getDescription() == null) {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("?????????????? ?????? ???????? ???? ???????? ?????????? ????????????, ?????? ?????????????????? ??????????????????. " +
                                    "???? ???????????????? ?????????? ?????????????????? ???????? ????????????????.")
                            .replyMarkup(createButtons(List.of("????????????????????"))).build();
                } else {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("?????????????? ?????? ???????? ???? ???????? ?????????? ????????????, ?????? ?????????????????? ??????????????????. " +
                                    "???? ???????????????? ?????????? ?????????????????? ???????? ????????????????.")
                            .replyMarkup(createButtons(List.of("????????????????????", "????????????????"))).build();
                }
                break;
            case ENGLISH:

                if (user.getDescription() == null) {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("Tell us about yourself and who you want to find, what you propose to do. " +
                                    "This will help you find the best company for you.")
                            .replyMarkup(createButtons(List.of("Skip"))).build();
                } else {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("Tell us about yourself and who you want to find, what you propose to do. " +
                                    "This will help you find the best company for you.")
                            .replyMarkup(createButtons(List.of("Skip", "Leave"))).build();
                }
                break;
        }
        return message;
    }

    public SendMessage setOppositeSexProfile(Update update, User user){
        SendMessage message = new SendMessage();

        switch (user.getLanguage()) {
            case RUSSIAN:

                if (update.getMessage().getText().equals("??????????????") || update.getMessage().getText().equals("??????????")) {

                    if (update.getMessage().getText().equals("??????????????")) {
                        user.setOppositeSex(1);
                    } else if (update.getMessage().getText().equals("??????????")) {
                        user.setOppositeSex(0);
                    }

                    user.setChatStatus(ChatStatus.ENTER_CITY);
                    userService.updateUser(user);

                    if (user.getCity() == null) {
                        message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                                .text("???? ???????????? ???? ?????????????").replyMarkup(new ReplyKeyboardRemove(true)).build();
                    } else {
                        message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                                .text("???? ???????????? ???? ?????????????").replyMarkup(createButtons(List.of(user.getCity()))).build();
                    }

                } else {
                    return SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("???????????? ???? ???????? ?????? ???????? ???? ?????????????? \uD83D\uDC47").build();
                }
                break;

            case UKRAINIAN:

                if (update.getMessage().getText().equals("??????????????") || update.getMessage().getText().equals("????????????")) {

                    if (update.getMessage().getText().equals("??????????????")) {
                        user.setOppositeSex(1);
                    } else if (update.getMessage().getText().equals("????????????")) {
                        user.setOppositeSex(0);
                    }

                    user.setChatStatus(ChatStatus.ENTER_CITY);
                    userService.updateUser(user);

                    if (user.getCity() == null) {
                        message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                                .text("?? ?????????? ???? ???????????").replyMarkup(new ReplyKeyboardRemove(true)).build();
                    } else {
                        message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                                .text("?? ?????????? ???? ???????????").replyMarkup(createButtons(List.of(user.getCity()))).build();
                    }

                } else {
                    return SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("???????????? ?? ????????, ???? ?? ???? ?????????????? \uD83D\uDC47").build();
                }

                break;

            case ENGLISH:

                if (update.getMessage().getText().equals("Girls") || update.getMessage().getText().equals("Guys")) {

                    if (update.getMessage().getText().equals("Girls")) {
                        user.setOppositeSex(1);
                    } else if (update.getMessage().getText().equals("Guys")) {
                        user.setOppositeSex(0);
                    }

                    user.setChatStatus(ChatStatus.ENTER_CITY);
                    userService.updateUser(user);

                    if (user.getCity() == null) {
                        message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                                .text("What city are you from?").replyMarkup(new ReplyKeyboardRemove(true)).build();
                    } else {
                        message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                                .text("What city are you from?").replyMarkup(createButtons(List.of(user.getCity()))).build();
                    }

                } else {
                    return SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("Choose from what is on the buttons \uD83D\uDC47").build();
                }

                break;
        }
        return message;
    }

    public SendMessage setSexProfile(Update update, User user){

        SendMessage message = new SendMessage();

        switch (user.getLanguage()) {
            case RUSSIAN:

                if (update.getMessage().getText().equals("?? ????????????") || update.getMessage().getText().equals("?? ??????????????")) {

                    if (update.getMessage().getText().equals("?? ????????????")) {
                        user.setSex(0);
                    } else if (update.getMessage().getText().equals("?? ??????????????")) {
                        user.setSex(1);
                    }

                    user.setChatStatus(ChatStatus.ENTER_OPPOSITE_SEX);
                    userService.updateUser(user);

                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("?????? ???????? ???????????????????").replyMarkup(createButtons(List.of("??????????????", "??????????"))).build();

                } else {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("???????????? ???? ???????? ?????? ???????? ???? ?????????????? \uD83D\uDC47").build();
                }

                break;
            case UKRAINIAN:

                if (update.getMessage().getText().equals("?? ??????????????") || update.getMessage().getText().equals("?? ??????????????")) {

                    if (update.getMessage().getText().equals("?? ??????????????")) {
                        user.setSex(0);
                    } else if (update.getMessage().getText().equals("?? ??????????????")) {
                        user.setSex(1);
                    }

                    user.setChatStatus(ChatStatus.ENTER_OPPOSITE_SEX);
                    userService.updateUser(user);

                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("?????? ???????? ???????????????").replyMarkup(createButtons(List.of("??????????????", "????????????"))).build();

                } else {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("???????????? ?? ????????, ???? ?? ???? ?????????????? \uD83D\uDC47").build();
                }

                break;

            case ENGLISH:

                if (update.getMessage().getText().equals("I'm a guy") || update.getMessage().getText().equals("I am a girl")) {

                    if (update.getMessage().getText().equals("I'm a guy")) {
                        user.setSex(0);
                    } else if (update.getMessage().getText().equals("I am a girl")) {
                        user.setSex(1);
                    }

                    user.setChatStatus(ChatStatus.ENTER_OPPOSITE_SEX);
                    userService.updateUser(user);

                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("Who are you interested in?").replyMarkup(createButtons(List.of("Girls", "Guys"))).build();

                } else {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("Choose from what is on the buttons \uD83D\uDC47").build();
                }

                break;
        }
        return message;
    }

    public SendMessage setAgeProfile(Update update, User user){

        SendMessage message = new SendMessage();

        switch (user.getLanguage()) {
            case RUSSIAN:

                try {
                    user.setAge(Integer.parseInt(update.getMessage().getText()));

                    user.setChatStatus(ChatStatus.ENTER_SEX);
                    userService.updateUser(user);

                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("???????????? ?????????????????????? ?? ??????????").replyMarkup(createButtons(List.of("?? ????????????", "?? ??????????????"))).build();

                } catch (NumberFormatException e) {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("?? ???????????? ???????????? ???????? \uD83D\uDE21").build();
                }

                break;

            case UKRAINIAN:

                try {
                    user.setAge(Integer.parseInt(update.getMessage().getText()));

                    user.setChatStatus(ChatStatus.ENTER_SEX);
                    userService.updateUser(user);

                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("?????????? ?????????????????????? ???? ????????????").replyMarkup(createButtons(List.of("?? ??????????????", "?? ??????????????"))).build();

                } catch (NumberFormatException e) {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("?? ???????????? ???????????? ?????????? \uD83D\uDE21").build();
                }

                break;

            case ENGLISH:

                try {
                    user.setAge(Integer.parseInt(update.getMessage().getText()));

                    user.setChatStatus(ChatStatus.ENTER_SEX);
                    userService.updateUser(user);

                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("Now let's decide on the article").replyMarkup(createButtons(List.of("I'm a guy", "I am a girl"))).build();

                } catch (NumberFormatException e) {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("I will only accept the number \uD83D\uDE21").build();
                }

                break;
        }
        return message;
    }

    public SendMessage setNameProfile(Update update, User user){

        SendMessage message = new SendMessage();

        user.setName(update.getMessage().getText());

        user.setChatStatus(ChatStatus.ENTER_AGE);
        userService.updateUser(user);

        switch (user.getLanguage()) {
            case RUSSIAN:

                if (user.getAge() == 0) {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("?????????????? ???????? ???????").replyMarkup(new ReplyKeyboardRemove(true)).build();
                } else {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("?????????????? ???????? ???????").replyMarkup(createButtons(List.of(String.valueOf(user.getAge())))).build();
                }

                break;

            case UKRAINIAN:

                if (user.getAge() == 0) {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("?????????????? ???????? ???????????").replyMarkup(new ReplyKeyboardRemove(true)).build();
                } else {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("?????????????? ???????? ???????????").replyMarkup(createButtons(List.of(String.valueOf(user.getAge())))).build();
                }

                break;

            case ENGLISH:

                if (user.getAge() == 0) {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("How old are you?").replyMarkup(new ReplyKeyboardRemove(true)).build();
                } else {
                    message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("How old are you?").replyMarkup(createButtons(List.of(String.valueOf(user.getAge())))).build();
                }

                break;
        }
        return message;
    }

    public SendMessage startProfile(Update update, User user){

        SendMessage message = new SendMessage();

        user.setChatStatus(ChatStatus.ENTER_NAME);
        userService.updateUser(user);

        switch (user.getLanguage()) {
            case RUSSIAN:

                message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                        .text("?????? ?????? ???????? ???????????????").replyMarkup(createButtons(List.of(user.getName()))).build();

                break;

            case UKRAINIAN:

                message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                        .text("???? ???????? ???????? ???????????????").replyMarkup(createButtons(List.of(user.getName()))).build();

                break;

            case ENGLISH:

                message = SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                        .text("What is your name?").replyMarkup(createButtons(List.of(user.getName()))).build();

                break;
        }
        return message;
    }

    public void likeProfile(Update update, User user){
        List<SendMessage> responses = new ArrayList<>();
        List<Match> matches = matchService.findAllByUserId(user.getId());
        List<Match> matchesOpposite;
        boolean matching = false;

        if(matches.size() != 0) {
            for (Match temp : matches){

                matchesOpposite = matchService.findAllByUserId(temp.getOppositeUserId().getId());

                if (matchesOpposite != null){
                    for (Match tempOpposite : matchesOpposite){
                        if(tempOpposite.getOppositeUserId().getId() == temp.getUserId()){

                            switch (user.getLanguage()){
                                case RUSSIAN:

                                    responses.add(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                                            .text(outputProfile(temp.getOppositeUserId())).build());
                                    responses.add(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                                            .text("???????? ???????????????? ????????????????! ?????????????? ????????????????\uD83D\uDC49" + "@" + temp.getOppositeUserId().getNickname())
                                            .build());

                                    break;

                                case UKRAINIAN:

                                    responses.add(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                                            .text(outputProfile(temp.getOppositeUserId())).build());
                                    responses.add(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                                            .text("?? ?????????????? ????????????????! ?????????????? ????????????????????????\uD83D\uDC49" + "@" + temp.getOppositeUserId().getNickname())
                                            .build());

                                    break;

                                case ENGLISH:

                                    responses.add(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                                            .text(outputProfile(temp.getOppositeUserId())).build());
                                    responses.add(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                                            .text("There is mutual sympathy! Start chatting\uD83D\uDC49" + "@" + temp.getOppositeUserId().getNickname())
                                            .build());

                                    break;
                            }

                            switch (userService.findById(tempOpposite.getUserId()).getLanguage()){
                                case RUSSIAN:

                                    responses.add(SendMessage.builder().chatId(String.valueOf(userService.findById(tempOpposite.getUserId()).getUserId()))
                                            .text(outputProfile(temp.getOppositeUserId())).build());
                                    responses.add(SendMessage.builder().chatId(String.valueOf(userService.findById(tempOpposite.getUserId()).getUserId()))
                                            .text("???????? ???????????????? ????????????????! ?????????????? ????????????????\uD83D\uDC49" + "@" + tempOpposite.getOppositeUserId().getNickname())
                                            .build());

                                    break;

                                case UKRAINIAN:

                                    responses.add(SendMessage.builder().chatId(String.valueOf(userService.findById(tempOpposite.getUserId()).getUserId()))
                                            .text(outputProfile(temp.getOppositeUserId())).build());
                                    responses.add(SendMessage.builder().chatId(String.valueOf(userService.findById(tempOpposite.getUserId()).getUserId()))
                                            .text("?? ?????????????? ????????????????! ?????????????? ????????????????????????\uD83D\uDC49" + "@" + tempOpposite.getOppositeUserId().getNickname())
                                            .build());

                                    break;

                                case ENGLISH:

                                    responses.add(SendMessage.builder().chatId(String.valueOf(userService.findById(tempOpposite.getUserId()).getUserId()))
                                            .text(outputProfile(temp.getOppositeUserId())).build());
                                    responses.add(SendMessage.builder().chatId(String.valueOf(userService.findById(tempOpposite.getUserId()).getUserId()))
                                            .text("There is mutual sympathy! Start chatting\uD83D\uDC49" + "@" + tempOpposite.getOppositeUserId().getNickname())
                                            .build());

                                    break;
                            }

                            matchService.deleteById(tempOpposite);
                            matchService.deleteById(temp);
                            matching = true;
                            break;
                        }
                    }
                }
            }
        }

        if (!matching){
            switch (user.getLanguage()) {
                case RUSSIAN:
                    responses.add(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("?????? ?????? ???????????????? ????????????????").build());
                    break;

                case UKRAINIAN:
                    responses.add(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("?????? ?????? ???????????????? ????????????????").build());
                    break;

                case ENGLISH:
                    responses.add(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                            .text("There is no mutual sympathy yet").build());
                    break;
            }
        }

        bot.listSendMessage(responses);
    }

    public SendMessage waitingProfile(Update update, User user) {

        String message = null;

        switch (user.getLanguage()){
            case RUSSIAN:
                message = "1. ?????? ????????????.\n" +
                        "2. ???????????????? ????????????.\n" +
                        "3. ???????????????? ???????????????? ????????????????.\n" +
                        "4. ???????????????? ????????.";
                break;
            case UKRAINIAN:
                message = "1. ?????? ????????????.\n" +
                        "2. ???????????????? ????????????.\n" +
                        "3. ???????????????? ?????????????? ????????????????.\n" +
                        "4. ?????????????? ????????.";
                break;
            case ENGLISH:
                message = "1. My profile.\n" +
                        "2. Look at profiles.\n" +
                        "3. Show mutual sympathy.\n" +
                        "4. Change language.";
                break;
        }

        return SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                .text(message).replyMarkup(createButtons(List.of("1", "2", "3", "4"))).build();
    }

    public void outputWaitingProfile(Update update, User user){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));

        if (user.getLanguage() == Language.RUSSIAN) {
            sendMessage.setText("???????????????? ???????? ??????-???? ???????????? ???????? ????????????");
        } else if (user.getLanguage() == Language.UKRAINIAN) {
            sendMessage.setText("??????????????????, ???????? ?????????? ???????????????? ???????? ????????????");
        } else if (user.getLanguage() == Language.ENGLISH) {
            sendMessage.setText("Wait for someone to see your profile");
        }

        bot.listSendMessage(Collections.singletonList(sendMessage));
    }

    public SendMessage nextProfile(Update update, User user){

        User oppositeUser = userService.findById(user.getOppositeSexId());

        if (user.getChatStatus() == ChatStatus.LIKE){
            Match match = new Match();
            List<Match> list = new ArrayList<>();
            match.setOppositeUserId(oppositeUser);
            list.add(match);
            user.setMatches(list);
            match.setUserId(user.getId());
            matchService.saveMatch(match);
            userService.saveUser(user);
            user.setChatStatus(ChatStatus.DISLIKE);
        }

        oppositeUser = userService.nextProfile(user.getOppositeSexId(), user.getOppositeSex());

        user.setOppositeSexId(oppositeUser.getId());
        userService.saveUser(user);

        return SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                .text(outputProfile(oppositeUser)).replyMarkup(createButtons(List.of("???", "\uD83D\uDC4E", "\uD83D\uDCA4"))).build();
    }

    public String outputProfile(User user){
        return user.getName() + ", " + user.getAge() + ", " + user.getCity() + ", " + user.getDescription();
    }

    public void myProfile(Update update, User user){
        List<SendMessage> responses = new ArrayList<>();

        responses.add(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                .text(user.getName() + ", " + user.getAge() + ", " + user.getCity() + ", " + user.getDescription()).build());

        switch (user.getLanguage()) {
            case RUSSIAN:
                responses.add(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                        .text("1. ???????????? ?????????????????? ????????????\n2. ???????????????? ????????????").replyMarkup(createButtons(List.of("1", "2"))).build());
                break;
            case UKRAINIAN:
                responses.add(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                        .text("1. ????-???????????? ?????????????????? ????????????\n2. ???????????????? ????????????").replyMarkup(createButtons(List.of("1", "2"))).build());
                break;
            case ENGLISH:
                responses.add(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId()))
                        .text("1. Fill out the form again\n2. See questionnaires").replyMarkup(createButtons(List.of("1", "2"))).build());
                break;
        }

        bot.listSendMessage(responses);
    }

    public ReplyKeyboardMarkup createButtons(List<String> list){
        return buttonsService.setButtons(buttonsService.createButtons(list));
    }

}
