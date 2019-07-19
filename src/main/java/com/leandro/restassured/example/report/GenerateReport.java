package com.leandro.restassured.example.report;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.google.gson.Gson;

import io.restassured.response.Response;

public class GenerateReport {

	ExtentReports reports;
	ExtentTest testInfo;
	ExtentHtmlReporter htmlReporter;
	String message, finalReportPath, documentTitle, reportName;
	String testOutPutPath = System.getProperty("user.dir") + "/target/reports/";

	@BeforeClass
	public void beforeClass(ITestContext context) {
		try {
			ITestNGMethod[] methods = context.getAllTestMethods();
			String[] methodNames = null;
			for (ITestNGMethod iTestNGMethod : methods) {
				methodNames = iTestNGMethod.getInstance().getClass().getName().split("\\.");
				break;
			}

			System.out.println(String.format("\nStart test suite: %s.\n", methodNames[methodNames.length - 1]));
			reportName = String.format("%sReport", methodNames[methodNames.length - 1]);
			this.finalReportPath = String.format("%s/%s.html", testOutPutPath, reportName);
			this.documentTitle = String.format("Automation Test Report - %s", reportName);

			if (!new File(testOutPutPath).exists()) {
				new File(testOutPutPath).mkdir();
			}

			htmlReporter = new ExtentHtmlReporter(new File(finalReportPath));
			htmlReporter.config().setTheme(Theme.DARK);
			htmlReporter.config().setProtocol(Protocol.HTTPS);
			htmlReporter.config().setTimeStampFormat("dd/MM/yyyy HH:mm:ss");
			htmlReporter.config().setEncoding("cp1252");
			htmlReporter.config().setDocumentTitle(documentTitle);
			htmlReporter.config().setReportName(documentTitle);

			reports = new ExtentReports();
			reports.setSystemInfo("Environment", "QA");
			reports.attachReporter(htmlReporter);

		} catch (Exception e) {
			e.getMessage();
		}
	}

	@BeforeMethod
	public void beforeTest(Method method) {
		try {
			message = String.format("Init test '%s'.", method.getName());
			testInfo = reports.createTest(method.getName());
			System.out.println(String.format("\n%s", message));

		} catch (Exception e) {
			e.getMessage();
		}
	}

	@AfterMethod
	public void afterTest(ITestResult result) {
		try {
			if (result.getStatus() == ITestResult.SUCCESS) {
				message = String.format("Test '%s' is passed.", result.getName());
				testInfo.log(Status.PASS, MarkupHelper.createLabel(message, ExtentColor.GREEN));
			} else if (result.getStatus() == ITestResult.FAILURE) {
				message = String.format("Test '%s' failure.", result.getName());
				testInfo.log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED));
				testInfo.log(Status.FAIL, MarkupHelper.createCodeBlock(result.getThrowable().toString()));
				message = String.format("%s Details: %s.", message, result.getThrowable().toString());
			} else if (result.getStatus() == ITestResult.SKIP) {
				message = String.format("Test '%s' is skipped.", result.getName());
				testInfo.log(Status.SKIP, MarkupHelper.createLabel(message, ExtentColor.ORANGE));
				testInfo.log(Status.SKIP, MarkupHelper.createCodeBlock(result.getThrowable().toString()));
				message = String.format("%s Details: %s.", message, result.getThrowable().toString());
			} else {
				message = String.format("Test '%s' with status '%s'.", result.getName(), result.getStatus());
				testInfo.log(Status.WARNING, MarkupHelper.createLabel(message, ExtentColor.GREY));
				testInfo.log(Status.WARNING, MarkupHelper.createCodeBlock(result.getThrowable().toString()));
				message = String.format("%s Details: %s.", message, result.getThrowable().toString());
			}
			System.out.println(message);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@AfterClass
	public void afterClass() {
		try {
			reports.flush();
			System.out.println(String.format("\n\nFinish test suite. Report: \n%s\n",
					Paths.get(finalReportPath).toAbsolutePath().toString()));
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void log(String text) {
		try {
			testInfo.log(Status.INFO, text);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void logCodeBlock(String text, String codeBlock) {
		try {
			testInfo.log(Status.INFO, text);
			testInfo.log(Status.INFO, MarkupHelper.createCodeBlock(codeBlock));
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void logTimeout(Long value) {
		try {
			String text = "";

			text = String.format("Response time: %s ms.", value);
			if (value < 2000)
				testInfo.log(Status.INFO, MarkupHelper.createLabel(text, ExtentColor.BLUE));
			else
				testInfo.log(Status.INFO, MarkupHelper.createLabel(text, ExtentColor.AMBER));

			System.out.println(text);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void logTimeout(Long value, Long param) {
		try {
			String text = "";

			text = String.format("Response time: %s ms.", value);
			if (value < param)
				testInfo.log(Status.INFO, MarkupHelper.createLabel(text, ExtentColor.BLUE));
			else
				testInfo.log(Status.INFO, MarkupHelper.createLabel(text, ExtentColor.AMBER));

			System.out.println(text);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void afterAPITest(Map<String, Object> request, Response response) {
		try {
			String tempBodyResponse = response.getBody().asString();
			String tempRequest = "";

			if (request == null || request.isEmpty()) {
				tempRequest = "No request info";
			} else {
				tempRequest = new Gson().toJson(request);
			}

			if (response.getStatusCode() == 204) {
				tempBodyResponse = String.format("No response, status code: %s", response.getStatusCode());
			}

			if (tempRequest.length() > 1000) {
				this.logCodeBlock("Request:", tempRequest);
			} else {
				this.log(String.format("Request: %s", tempRequest));
			}

			if (tempBodyResponse.length() > 1000) {
				this.logCodeBlock("Response:", tempBodyResponse);
			} else {
				this.log(String.format("Response: %s", tempBodyResponse));
			}

			this.logTimeout(response.time());
		} catch (Exception e) {
			e.getMessage();
		}
	}

}
