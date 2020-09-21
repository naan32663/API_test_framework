package com.yeepay.base.koala.statement;

import com.yeepay.base.koala.KoalaFrameworkMethod;
import com.yeepay.base.koala.command.StepCommand;
import com.yeepay.base.koala.event.StepNotifier;
import com.yeepay.base.koala.model.TestCase;
import com.yeepay.base.koala.reporter.Reporter;

import org.junit.runners.model.Statement;


public class QunitStatement extends Statement {

    private KoalaFrameworkMethod frameworkMethod;

    private Reporter reporter;

    public QunitStatement(KoalaFrameworkMethod frameworkMethod, Reporter rpt) {
        this.frameworkMethod = frameworkMethod;
        this.reporter = rpt;
    }

    @Override
    public void evaluate() throws Throwable {
        StepNotifier sNotifier = new StepNotifier();
        sNotifier.addStepEventListener(this.reporter.createStepListener());
        TestCase testCase = frameworkMethod.getTestCase();
        try {
            sNotifier.fireCaseStarted(testCase, frameworkMethod.getContext());
            runBeforeCommand(sNotifier, testCase);
            runPipeline(sNotifier, testCase);
        } finally {
            runTearDownCommand(sNotifier, testCase);
            runAfterCommand(sNotifier, testCase);
            sNotifier.fireCaseFinished(testCase, frameworkMethod.getContext());
        }
    }

    private void runAfterCommand(StepNotifier sNotifier, TestCase testCase) throws Throwable {
        StepCommand afterCommand = testCase.getAfterCommand();
        if (afterCommand != null) {
            afterCommand.execute(null, frameworkMethod.getContext(), sNotifier);
        }
    }

    private void runTearDownCommand(StepNotifier sNotifier, TestCase testCase) throws Throwable {
        StepCommand tearDownCommand = testCase.getTearDownCommand();
        if (tearDownCommand != null) {
            tearDownCommand.execute(null, frameworkMethod.getContext(), sNotifier);
        }
    }

    private void runPipeline(StepNotifier sNotifier, TestCase testCase) throws Throwable {
        StepCommand pipeline = testCase.pipeline();
        if (pipeline != null) {
            pipeline.execute(null, frameworkMethod.getContext(), sNotifier);
        }
    }

    private void runBeforeCommand(StepNotifier sNotifier, TestCase testCase) throws Throwable {
        StepCommand beforeCommand = testCase.getBeforeCommand();
        if (beforeCommand != null) {
            beforeCommand.execute(null, frameworkMethod.getContext(), sNotifier);
        }
    }

}
