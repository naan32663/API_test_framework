package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.Element;
import com.yeepay.base.koala.command.SetStepCommand;
import com.yeepay.base.koala.command.StepCommand;
import com.yeepay.base.koala.model.KeyValueStore;

import java.util.List;


public class SetStepConfig extends StepConfig{

    @Element
    List<KeyValueStore> parameter;

    @Override
    public StepCommand createCommand() {
        return new SetStepCommand(parameter);
    }

}
