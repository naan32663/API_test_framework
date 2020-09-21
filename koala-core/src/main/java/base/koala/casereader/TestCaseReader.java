package com.yeepay.base.koala.casereader;

import com.yeepay.base.koala.model.TestSuite;

import org.dom4j.DocumentException;

import java.io.FileNotFoundException;


public interface TestCaseReader {

    /**
     * 解析指定文件里的所有测试用例
     * @param file 测试用例文件
     * @return 所有测试用例
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public TestSuite readTestCase(String file) throws FileNotFoundException, DocumentException;

}
