package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.ChildrenConfig;
import com.yeepay.base.koala.command.StepCommand;

import java.util.ArrayList;
import java.util.List;


public abstract class CompositeStepConfig extends StepConfig {
    @ChildrenConfig
    private List<StepConfig> childrenConfig;

    protected final List<StepCommand> createChildren() {
        List<StepCommand> commands = new ArrayList<StepCommand>();
        for (StepConfig config : childrenConfig) {
            StepCommand command = config.createCommand();
            commands.add(command);
        }
        return commands;
    }
}
