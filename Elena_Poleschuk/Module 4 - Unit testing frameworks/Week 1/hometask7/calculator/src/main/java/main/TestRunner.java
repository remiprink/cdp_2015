package main;

import java.util.ArrayList;
import java.util.List;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;

public class TestRunner {
	public static void main(String[] args) {
		TestNG tng = new TestNG();
		XmlSuite suite = new XmlSuite();
		suite.setName("TmpSuite");

		List<String> files = new ArrayList<String>();
		files.addAll(new ArrayList<String>() {
			{
				add("testng.xml");
			}
		});

		suite.setSuiteFiles(files);

		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suites.add(suite);
		tng.setXmlSuites(suites);

		tng.run();
	}
}
