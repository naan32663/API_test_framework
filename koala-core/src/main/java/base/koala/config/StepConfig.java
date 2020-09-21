package com.yeepay.base.koala.config;

import com.yeepay.base.koala.command.StepCommand;


public abstract class StepConfig {

    /*
    This is tricky
     */
    protected String commandName;

    public abstract StepCommand createCommand();
}
