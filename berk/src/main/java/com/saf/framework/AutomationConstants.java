package com.saf.framework;

public class AutomationConstants 

{
	
	public static String sIEDriverPath      	= "/Exes/IEDriverServer.exe";
	public static String sChromeDriverPath  	= "/Exes/chromedriver.exe";
	public static String sGeckoDriverPath   	= "/Exes/geckodriver.exe";
	
	public static String sProxyHostName 		= "genproxy.amdocs.com";
	public static int iProxyPort 				= 8080;
	
	public static long lngPageLoadTimeout 		= 90L;
	public static long lngImplicitWaitTimeout	= 60L;
	
	public static String testDataPath = ".\\TestData\\";
	public static String CommonFileName = "COMMON.xlsx";
	
	public static String reportsDBName = "reports.db";
	
	public static int itestCaseID;

	public static String oracleUsername = "TESTUSER";
	public static String oraclePassword = "TESTUSER";
	public static String msisdn = "";
	public static String connectionString = "jdbc:oracle:thin:@10.86.143.27:1522:DCCA";


}
