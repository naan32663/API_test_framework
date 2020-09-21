package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.Property;
import com.yeepay.base.koala.command.CompareDatabaseStepCommand;
import com.yeepay.base.koala.command.StepCommand;
import com.yeepay.base.koala.model.KeyValueStore;

import java.util.Arrays;


public class CompareDatabaseStepConfig extends StepConfig {

    public final static String NAME = "compareDatabase";

    public final static String DATABASE = "database";
    public final static String IGNORE = "ignore";
    public final static String EXPECTED = "expected";
    public final static String REPLACETABLENAME = "replaceTableName";

    @Property(defaultValue = "default")
    private String database;

    //tb1;tb2(col1,col2);
    @Property
    private String ignore;

    @Property
    private String expected;

    @Property("replaceTableName")
    private String replaceTableName;

    @Override
    public StepCommand createCommand() {
        return new CompareDatabaseStepCommand(Arrays.asList(
                new KeyValueStore(DATABASE, database),
                new KeyValueStore(IGNORE, ignore),
                new KeyValueStore(EXPECTED, expected),
                new KeyValueStore(REPLACETABLENAME, replaceTableName)
        ));
    }
}
