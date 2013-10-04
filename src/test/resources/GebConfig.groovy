import org.openqa.selenium.firefox.FirefoxDriver

driver = { new FirefoxDriver() }

baseUrl = "http://localhost:8080"

reportsDir = new File("target/geb-reports")
reportOnTestFailureOnly = true