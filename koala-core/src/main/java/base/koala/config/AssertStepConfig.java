package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.ConfigElement;
import com.yeepay.base.koala.annotation.Element;
import com.yeepay.base.koala.annotation.Property;
import com.yeepay.base.koala.command.AssertStepCommand;
import com.yeepay.base.koala.command.StepCommand;
import com.yeepay.base.koala.model.KeyValueStore;
import com.yeepay.base.koala.util.CloneUtil;

import org.apache.commons.lang.StringUtils;

import java.util.List;

/*
<assert>
    <body>{'ret':true}</body>
</assert>
    equals
<assert>
{'ret':true}
</assert>
 */
@ConfigElement(defaultProperty = AssertStepConfig.BODY_TAG_NAME)
public class AssertStepConfig extends StepConfig {
    public static final String BODY_TAG_NAME = "body";

    @Property
    private String body;

    @Element
    List<KeyValueStore> params;

    @Override
    public StepCommand createCommand() {
        appendBodyIfExists();
        return new AssertStepCommand(CloneUtil.cloneKeyValueStore(params));
    }

    private void appendBodyIfExists() {
        if (StringUtils.isNotBlank(body)) {
            params.add(new KeyValueStore(BODY_TAG_NAME, body));
        }
    }


}
