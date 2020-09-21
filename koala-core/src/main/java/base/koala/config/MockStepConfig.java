package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.Element;
import com.yeepay.base.koala.annotation.Property;
import com.yeepay.base.koala.command.MockStepCommand;
import com.yeepay.base.koala.command.StepCommand;
import com.yeepay.base.koala.model.KeyValueStore;

import java.util.ArrayList;
import java.util.List;

import static com.yeepay.base.koala.model.MockInfo.*;


public class MockStepConfig extends StepConfig {

    //目标API
    @Property(required = true)
    String service;

    //此次调用的标识
    @Property
    String key;

    //目标api所属的业务线
    @Property(required = true)
    String target;

    //调用目标API的来源IP(一般就是被测系统)
    @Property
    String source;

    //返回值
    @Property("return")
    String returnValue;

    //回调
    @Element
    List<KeyValueStore> params;

    public List<KeyValueStore> getParams() {
        return params;
    }

    @Override
    public StepCommand createCommand() {
        List<KeyValueStore> newParams = new ArrayList<KeyValueStore>();
        newParams.addAll(params);
        newParams.add(new KeyValueStore(SOURCE, this.source));
        newParams.add(new KeyValueStore(TARGET, this.target));
        newParams.add(new KeyValueStore(KEY, this.key));
        newParams.add(new KeyValueStore(RETURN_VALUE, this.returnValue));
        newParams.add(new KeyValueStore(SERVICE, this.service));
        params = newParams;
        return new MockStepCommand(this.params);
    }

}
