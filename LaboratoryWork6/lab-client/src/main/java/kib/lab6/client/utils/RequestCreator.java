package kib.lab6.client.utils;

import kib.lab6.client.Config;
import kib.lab6.client.user_command_line.HumanInfoInput;
import kib.lab6.common.InputedCommand;
import kib.lab6.common.entities.HumanBeing;
import kib.lab6.common.entities.enums.Mood;
import kib.lab6.common.util.ErrorMessage;
import kib.lab6.common.util.Request;
import kib.lab6.common.util.StringToTypeConverter;

import java.util.Arrays;

public class RequestCreator {

    private static final int AMOUNT_OF_ARGS_FOR_HUMAN_BEING_REQUEST = 4;
    private static final int AMOUNT_OF_ARGS_FOR_HUMAN_BEING_AND_NUMBER_REQUEST = 5;

    public Request createRequestFromInputedCommand(InputedCommand inputedCommand) {
        Request request;
        if (AvailableCommands.COMMANDS_WITHOUT_ARGUMENTS.contains(inputedCommand.getName().toLowerCase()) && inputedCommand.getArguments().length == 0) {
            //Обработка команды без аргументов
            request = new Request(inputedCommand.getName());
        } else if (AvailableCommands.COMMANDS_WITH_NUMBER_ARGUMENT.contains(inputedCommand.getName().toLowerCase()) && inputedCommand.getArguments().length == 1) {
            //Обработка команды с числовым аргументом
            request = createNumberRequest(inputedCommand);
        } else if (AvailableCommands.COMMANDS_WITH_MOOD_ARGUMENT.contains(inputedCommand.getName().toLowerCase()) && inputedCommand.getArguments().length == 1) {
            //Обработка команды с аргументом "настроение"
            request = createMoodRequest(inputedCommand);
        } else if (AvailableCommands.COMMANDS_WITH_HUMAN_BEING_ARGUMENT.contains(inputedCommand.getName().toLowerCase()) && inputedCommand.getArguments().length == AMOUNT_OF_ARGS_FOR_HUMAN_BEING_REQUEST) {
            //Обработка команды с аргументом "HumanBeing"
            request = createHumanBeingRequest(inputedCommand);
        } else if (AvailableCommands.COMMANDS_WITH_HUMAN_BEING_AND_NUMBER_ARGUMENTS.contains(inputedCommand.getName().toLowerCase()) && inputedCommand.getArguments().length == AMOUNT_OF_ARGS_FOR_HUMAN_BEING_AND_NUMBER_REQUEST) {
            //Обработка команды с аргументами "число" и "человек"
            request = createHumanBeingAndNumberRequest(inputedCommand);
        } else {
            request = null;
        }
        return request;
    }

    private Request createHumanBeingAndNumberRequest(InputedCommand inputedCommand) {
        int num;
        try {
            num = (int) StringToTypeConverter.toObject(Integer.class, inputedCommand.getArguments()[0]);
        } catch (IllegalArgumentException e) {
            Config.getTextSender().printMessage(new ErrorMessage("Введен неправильный числовой аргумент"));
            return null;
        }
        try {
            String[] argumentsForHuman = Arrays.copyOfRange(inputedCommand.getArguments(), 1, inputedCommand.getArguments().length);
            HumanInfoInput humanInfoInput = new HumanInfoInput(argumentsForHuman);
            humanInfoInput.inputHuman();
            HumanBeing humanForRequest = humanInfoInput.getNewHumanToInput();
            return new Request(inputedCommand.getName(), num, humanForRequest);
        } catch (IllegalArgumentException e) {
            Config.getTextSender().printMessage(new ErrorMessage(e.getMessage()));
            return null;
        }
    }

    private Request createHumanBeingRequest(InputedCommand inputedCommand) {
        try {
            HumanInfoInput humanInfoInput = new HumanInfoInput(inputedCommand.getArguments());
            humanInfoInput.inputHuman();
            HumanBeing humanForRequest = humanInfoInput.getNewHumanToInput();
            return new Request(inputedCommand.getName(), humanForRequest);
        } catch (IllegalArgumentException e) {
            Config.getTextSender().printMessage(new ErrorMessage(e.getMessage()));
            return null;
        }
    }

    private Request createMoodRequest(InputedCommand inputedCommand) {
        if ("".equals(inputedCommand.getArguments()[0])) {
            return new Request(inputedCommand.getName(), (Mood) null);
        } else {
            try {
                return new Request(inputedCommand.getName(), Mood.valueOf(inputedCommand.getArguments()[0]));
            } catch (IllegalArgumentException e) {
                Config.getTextSender().printMessage(new ErrorMessage("Такого настроения не существует,"
                        + " введите одно из: " + Arrays.toString(Mood.values())));
                return null;
            }
        }
    }

    private Request createNumberRequest(InputedCommand inputedCommand) {
        try {
            return new Request(inputedCommand.getName(), (int) StringToTypeConverter.toObject(Integer.class, inputedCommand.getArguments()[0]));
        } catch (IllegalArgumentException e) {
            Config.getTextSender().printMessage(new ErrorMessage("Введен неправильный числовой аргумент"));
            return null;
        }
    }
}
