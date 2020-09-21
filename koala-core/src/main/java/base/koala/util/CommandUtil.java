package com.yeepay.base.koala.util;

import com.yeepay.base.koala.command.StepCommand;


public class CommandUtil {

    public static StepCommand concatCommand(StepCommand oneCommand, StepCommand otherCommand) {
        if (oneCommand == null || otherCommand == null) {
            return oneCommand == null ? otherCommand : oneCommand;
        }
        StepCommand command = oneCommand;
        while (command.hasNextCommand()) {
            command = command.getNextCommand();
        }
        command.setNextCommand(otherCommand);
        return oneCommand;
    }

}
