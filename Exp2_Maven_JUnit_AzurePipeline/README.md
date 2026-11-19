# Exp2 Maven JUnit AzurePipeline

Maven + JUnit tests + Azure Pipeline (build and regression tests)

## Questions

- **Q05** `Q05_Calculator/` - Develop a Java calculator application to perform addition, subtraction, multiplication, and division, including division-by-zero handling. Write JUnit test cases for all arithmetic operations and configure an Azure Pipeline using Maven to build the application and run the regression tests.

- **Q06** `Q06_PassFail/` - Develop a Java application to determine whether a student has passed or failed based on the marks obtained (Pass: marks >= 50; Fail: marks < 50). Write JUnit test cases for marks 35, 50, 75, and 100, and configure an Azure Pipeline using Maven to build the application and run the regression tests.

- **Q07** `Q07_LeapYear/` - Develop a Java application to determine whether a given year is a leap year. Write JUnit test cases covering leap years, non-leap years, and century years, and configure an Azure Pipeline using Maven to build the application and run the regression tests.

- **Q08** `Q08_Discount/` - Develop a Java application to calculate the final purchase amount by applying a 10% discount when the purchase amount is Rs.1,000 or more; otherwise, no discount is applied. Write JUnit test cases for purchase amounts below, equal to, and above Rs.1,000, and configure an Azure Pipeline using Maven to build the application and run the regression tests.


Each question folder = ready project + `*_ALL_IN_ONE.md` (question, aim, procedure, all code, xml/yml, commands, output).


## Viva-voce

- **What is JUnit?** Unit testing framework for Java (@Test, assertEquals, assertThrows).
- **Regression testing?** Re-running tests after each change to make sure old features still work.
- **Surefire plugin?** Maven plugin that runs tests and writes reports to target/surefire-reports.
- **Why publishJUnitResults?** Shows test results in Azure DevOps Tests tab.
- **assertThrows?** Checks that code throws expected exception (division by zero).
