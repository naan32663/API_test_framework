package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.Property;
import com.yeepay.base.koala.command.DbAssertStepCommand;
import com.yeepay.base.koala.command.StepCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Deprecated
public class DbAssertStepConfig extends StepConfig {
    private final static Logger logger = LoggerFactory.getLogger(DbAssertStepConfig.class);

    @Property
    String sql;

    @Property
    String expect;

    @Override
    public StepCommand createCommand() {
        logger.warn("<dbassert>已不推荐使用，请使用<sql> + <assert>替代");
        return new DbAssertStepCommand(sql, expect);
    }

}
