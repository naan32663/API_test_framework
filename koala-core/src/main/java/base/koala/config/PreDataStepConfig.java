package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.Property;
import com.yeepay.base.koala.command.PreDataStepCommand;
import com.yeepay.base.koala.command.StepCommand;
import com.yeepay.base.koala.model.KeyValueStore;

import java.util.Arrays;



public class PreDataStepConfig extends StepConfig {

    public final static String FILE = "file";
    public final static String DATABASE = "database";
    public final static String REPLACETABLENAME = "replaceTableName";

    @Property(value = "file", required = true)
    String file;

    @Property(defaultValue = "default")
    String database;

    @Property("replaceTableName")
    String replaceTableName;

    @Override
    public StepCommand createCommand() {
        return new PreDataStepCommand(Arrays.asList(
                new KeyValueStore(FILE, file),
                new KeyValueStore(DATABASE, database),
                new KeyValueStore(REPLACETABLENAME, replaceTableName)
        ));
    }
}
