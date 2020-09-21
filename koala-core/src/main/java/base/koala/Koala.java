package com.yeepay.base.koala;

import com.qunar.base.validator.JsonValidator;	
import com.qunar.base.validator.factories.ValidatorFactory;
import com.qunar.base.validator.validators.Validator;

import com.yeepay.base.koala.annotation.Filter;
import com.yeepay.base.koala.annotation.Interceptor;
import com.yeepay.base.koala.casefilter.CaseFilter;
import com.yeepay.base.koala.casereader.Dom4jCaseReader;
import com.yeepay.base.koala.context.Context;
import com.yeepay.base.koala.intercept.InterceptorFactory;
import com.yeepay.base.koala.intercept.StepCommandInterceptor;
import com.yeepay.base.koala.model.Environment;
import com.yeepay.base.koala.model.Operation;
import com.yeepay.base.koala.model.SvnInfo;
import com.yeepay.base.koala.model.TestSuite;
import com.yeepay.base.koala.paramfilter.FilterFactory;
import com.yeepay.base.koala.paramfilter.ParamFilter;
import com.yeepay.base.koala.reporter.Reporter;
import com.yeepay.base.koala.transport.command.ServiceFactory;
import com.yeepay.base.koala.util.IpUtil;
import com.yeepay.base.koala.util.PropertyUtils;
import com.yeepay.base.koala.util.ReflectionUtils;
import com.yeepay.koala.koala.dsl.DSLCommandReader;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import java.io.FileNotFoundException;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Koala extends ParentRunner<TestSuiteRunner> {

    private final static Context GLOBALCONTEXT = new Context(null);

    private List<TestSuiteRunner> children = new ArrayList<TestSuiteRunner>();

    private Reporter qjsonReporter;

    private final CaseFilter filter;

    private KoalaOptions options;

    public Koala(Class<?> testClass) throws InitializationError, DocumentException, FileNotFoundException {
        super(testClass);
        options = new KoalaOptions(testClass);

        addJobAndIdToContext(options);
        MonitorLog.start(options);
        setArrayValidateMethod();

        List<String> files = options.testCases();//取用例
        ensureHasCases(files);//判断用例是否存在
        List<String> beforeFiles = options.before();
        List<String> afterFiles = options.after();

        this.qjsonReporter = options.reporter();

        SvnInfo svnInfo = new SvnInfoReader().read();
        this.qjsonReporter.addSvnInfo(svnInfo);

        determinedLocalHost();

        attatchHandlers(testClass);
        attatchInterceptors(testClass);

        new DSLCommandReader().read(options.dslFile(), qjsonReporter);//解析dsl.xml

        ServiceFactory.getInstance().init(options.serviceConfig(), qjsonReporter);//解析services文件
        Environment.initEnvironment(testClass);//操作数据库

        filter = options.createCaseFilter();//

        addChildren(beforeFiles);//testsuite执行
        addChildren(files);
        addChildren(afterFiles);
    }

    private void addJobAndIdToContext(KoalaOptions options) {
        if (StringUtils.isNotBlank(options.jobName()) && StringUtils.isNotBlank(options.jobName())) {
            GLOBALCONTEXT.addContext("job", options.jobName());
            GLOBALCONTEXT.addContext("build", options.buildNumber());
        }
    }

    private void setArrayValidateMethod() {
        String property = PropertyUtils.getProperty("array_default_order_validate", "false");
        JsonValidator.arrayDefaultOrderValidate = Boolean.valueOf(property);
    }

    private void ensureHasCases(List<String> files) {
        if (files == null || files.size() == 0) {
            throw new RuntimeException("Case文件不存在: 请检查你指定的case文件是否存在");
        }
    }

    private void determinedLocalHost() {
        GLOBALCONTEXT.addContext("jenkins.host", IpUtil.getLocalNetworkAddress());
    }

    private void attatchInterceptors(Class<?> testClass) {
        if (!testClass.isAnnotationPresent(Interceptor.class)) return;
        Interceptor interceptor = testClass.getAnnotation(Interceptor.class);
        Class<? extends StepCommandInterceptor>[] value = interceptor.value();
        for (Class<? extends StepCommandInterceptor> interceptorClass : value) {
            StepCommandInterceptor stepCommandInterceptor = ReflectionUtils.newInstance(interceptorClass);
            InterceptorFactory.registerInterceptor(stepCommandInterceptor);
        }
    }

    private void attatchHandlers(Class<?> testClass) {
        Field[] fields = testClass.getDeclaredFields();
        for (Field field : fields) {
            if (isFilter(field)) {
                ParamFilter filter = (ParamFilter) ReflectionUtils.newInstance(field.getType());
                FilterFactory.register(filter);
            }
        }
    }

    private boolean isFilter(Field field) {
        return field.isAnnotationPresent(Filter.class)
                && ParamFilter.class.isAssignableFrom(field.getType());
    }

    @Override
    protected List<TestSuiteRunner> getChildren() {
        return children;
    }

    @Override
    protected Description describeChild(TestSuiteRunner child) {
        return child.getDescription();
    }

    @Override
    protected void runChild(TestSuiteRunner child, RunNotifier notifier) {
        child.run(notifier);
    }

    @Override
    public void run(RunNotifier notifier) {
        try {
            super.run(notifier);
        } finally {
            qjsonReporter.close();
        }
    }

    private void addChildren(List<String> files) throws InitializationError, DocumentException, FileNotFoundException {
        List<TestSuite> suites = new ArrayList<TestSuite>(files.size());
        for (String file : files) {
            TestSuite testSuite = new Dom4jCaseReader().readTestCase(file);
            if (testSuite == null) continue;
            filter.filter(testSuite);
            if (!testSuite.getTestCases().isEmpty()) {
                suites.add(testSuite);
            }
        }
        Collections.sort(suites);
        for (TestSuite suite : suites) {
            Context suitContext = new Context(GLOBALCONTEXT);
            children.add(new TestSuiteRunner(getTestClass().getJavaClass(), suite, suitContext, this.qjsonReporter));
        }
    }

    public static void registerValidator(String validatorName, Class<? extends Validator> validatorClass) {
        ValidatorFactory.registerValidator(validatorName, validatorClass);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Documented
    @Inherited
    public @interface Options {

        String[] files() default "";

        String[] before() default "";

        String[] after() default "";

        String[] tags() default "*";

        String ids() default "";

        String[] service() default "service.xml";

        String dsl() default "";

        Operation operation() default Operation.CLEAR_INSERT;
    }

}
