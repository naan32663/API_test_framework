package com.yeepay.base.koala.reporter;

import com.yeepay.base.koala.model.ServiceDesc;
import com.yeepay.base.koala.model.SvnInfo;
import com.yeepay.base.koala.model.TestSuite;
import com.yeepay.base.koala.reporter.QJSONReporter.ReporterEventListener;
import com.yeepay.koala.koala.dsl.DSLCommandDesc;


public interface Reporter {

    /**
     * called in a runner before running an XML file
     */
    void report(TestSuite testSuite);

    /**
     * called in a runner after running an XML file
     */
    void done();

    /**
     * return case reports as a String instance
     */
    String reportAsString();

    /**
     * */
    void close();

    /**
     * */
    ReporterEventListener createStepListener();

    void addSvnInfo(SvnInfo svnInfo);

    void addService(ServiceDesc serviceDesc);

    void addDSLCommand(DSLCommandDesc dslCommandDesc);
}
