package com.yeepay.base.koala.config;

import com.yeepay.base.koala.annotation.Property;
import com.yeepay.base.koala.command.StepCommand;
import com.yeepay.base.koala.command.Transform;
import com.yeepay.base.koala.command.TransformStepCommand;


public class TransformStepConfig extends StepConfig {

    @Property(value = "class", required = true)
    String transform;

    @Override
    public StepCommand createCommand() {
        try {
            Class t = Class.forName(transform);
            Object instance = t.newInstance();
            if (instance instanceof Transform) {
                Transform transform = (Transform) instance;
                return new TransformStepCommand(transform);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("未找到指定的Transform对象： %s", this.transform), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("创建Transform对象失败: %s", this.transform), e);
        } catch (InstantiationException e) {
            throw new RuntimeException(String.format("创建Transform对象失败: %s", this.transform), e);
        }
        return null;
    }
}
