package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.Element;
import com.yeepay.base.koala.annotation.Property;
import com.yeepay.base.koala.command.CallStepCommand;
import com.yeepay.base.koala.command.StepCommand;
import com.yeepay.base.koala.model.KeyValueStore;
import com.yeepay.base.koala.transport.command.ExecuteCommand;
import com.yeepay.base.koala.transport.command.ServiceFactory;

import java.util.List;


public class CallStepConfig extends StepConfig {

    @Property(required = true)
    String service;

    @Element
    List<KeyValueStore> params;

    @Override
    public StepCommand createCommand() {
        ExecuteCommand command = ServiceFactory.getInstance().getCommand(service);
        return new CallStepCommand(command, params);
    }
}
