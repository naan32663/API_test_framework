package com.yeepay.base.koala.model;

import com.yeepay.base.koala.command.ExamplesCommand;
import com.yeepay.base.koala.command.StepCommand;

import java.util.List;
import java.util.Map;


public class DataDrivenTestCase extends TestCase {
    public List<Map<String, String>> getExamples() {
        StepCommand current = getBodyCommand();
        StepCommand pre = null;
        while (current != null) {
            if (current instanceof ExamplesCommand) {
                removeExamples(current, pre);
                return ((ExamplesCommand) current).getExamples();
            }
            pre = current;
            current = current.getNextCommand();
        }
        throw new RuntimeException("数据驱动的Case必须有data节点");
    }

    private void removeExamples(StepCommand examples, StepCommand pre) {
        if (pre == null) {
            this.setBodyCommand(examples.getNextCommand());
        } else {
            pre.setNextCommand(examples.getNextCommand());
        }
    }
}
