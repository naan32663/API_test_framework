package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.ConfigElement;
import com.yeepay.base.koala.annotation.Property;
import com.yeepay.base.koala.command.SqlStepCommand;
import com.yeepay.base.koala.command.StepCommand;
import com.yeepay.base.koala.model.KeyValueStore;

import java.util.Arrays;


@ConfigElement(defaultProperty = "sql")
public class SqlStepConfig extends StepConfig {

    public static final String SQL = "sql";
    public static final String DATABASE = "database";

    @Property(required = true)
    String sql;

    @Property(defaultValue = "default")
    String database;

    @Override
    public StepCommand createCommand() {
        return new SqlStepCommand(Arrays.asList(new KeyValueStore(SQL, sql), new KeyValueStore(DATABASE, database)));
    }

}
