# Exp3 Jenkins CloudVM Job

Jenkins on cloud VM + Jenkins job to compile & execute Java

## Questions

- **Q09** `Q09_GrossSalary/` - Develop a Java application to calculate and display the gross salary of an employee based on the basic salary, House Rent Allowance (HRA), and Dearness Allowance (DA) provided as input, using the formula Gross Salary = Basic Salary + HRA + DA. Install and configure Jenkins on a cloud virtual machine, configure Java, and create a Jenkins job to compile and execute the application.

- **Q10** `Q10_PositiveNegativeZero/` - Develop a Java application to read an integer and determine whether it is positive, negative, or zero. Install and configure Jenkins on a cloud virtual machine, configure the required Java environment, and create a Jenkins job to compile and execute the application.


Each question folder = ready project + `*_ALL_IN_ONE.md` (question, aim, procedure, all code, xml/yml, commands, output).


## Viva-voce

- **What is Jenkins?** Open-source automation server for CI/CD, default port 8080.
- **Freestyle vs Pipeline job?** Freestyle = UI-configured steps; Pipeline = code (Jenkinsfile, Groovy).
- **Where is initial admin password?** /var/lib/jenkins/secrets/initialAdminPassword
- **Why open port 8080 in security group?** To reach Jenkins web UI from browser.
- **JDK vs JRE?** JDK has javac (compiler); JRE only runs programs.
