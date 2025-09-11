package com.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class ExtentReporterManager implements ITestListener {

	ExtentSparkReporter sparkReporter;
	ExtentReports extentReports;
	ExtentTest extentTest;
	static String reportName;

	public void onStart(ITestContext context) {
		
		String timeStamp= new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		reportName="ReqResAutoReport_"+timeStamp+".html";
		
		sparkReporter = new ExtentSparkReporter(
				System.getProperty("user.dir") + "\\target\\ExecutionReports\\"+reportName);

		sparkReporter.config().setDocumentTitle("RestTestAutomation");
		sparkReporter.config().setReportName("AutomationTestExecutionReport");
		sparkReporter.config().setTheme(Theme.STANDARD);
		
		extentReports = new ExtentReports();
		extentReports.attachReporter(sparkReporter);
		extentReports.setSystemInfo("Application", "RestAssuredTestNG");
		extentReports.setSystemInfo("UserName", System.getProperty("user.name"));
		extentReports.setSystemInfo("Environment", "QA");

	}

	public void onTestSuccess(ITestResult result) {
		extentTest = extentReports.createTest(result.getName());
		extentTest.log(Status.PASS, result.getMethod() + " got passed");

	}

	public void onTestFailure(ITestResult result) {
		extentTest = extentReports.createTest(result.getName());
		extentTest.log(Status.FAIL, result.getMethod() + " got failed");
	}

	public void onTestSkipped(ITestResult result) {
		extentTest = extentReports.createTest(result.getName());
		extentTest.log(Status.SKIP, result.getMethod() + " got skipped");
	}

	public void onFinish(ITestContext context) {
		extentReports.flush();
		//sendReportByEmail();
		
	}
	
	public static void sendReportByEmail() {
        try {
            // Load properties from a file (optional)
            Properties props = new Properties();
            props.load(new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\mail.properties"));

            // Get session object
            
            
            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(props.getProperty("mail.from"), props.getProperty("mail.password"));
                }
            };
            
            Session session = Session.getInstance(props, auth);

            // Compose the message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("mail.from")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("ssarnaik8@gmail.com"));
            message.setSubject("Rest Assured TestNG tests execution Report :"+reportName);

            // Create a multipart message
            MimeMultipart multipart = new MimeMultipart();

            // Create a text part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("Please find the attached test execution report.");
            multipart.addBodyPart(textPart);

            // Create an attachment part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(System.getProperty("user.dir") + "\\target\\ExecutionReports\\"+reportName));
            multipart.addBodyPart(attachmentPart);

            // Set the multipart content to the message
            message.setContent(multipart);

            // Send the message
            Transport.send(message);

            System.out.println("Report sent successfully ");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to send report via email.");
        }
    }
	
	

}
