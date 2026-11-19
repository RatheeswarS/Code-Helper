# Exp5 Jenkins CD Pipeline

Jenkins CD pipeline: build, JAR, deploy to local directory, execute

## Questions

- **Q15** `Q15_MultiplicationTable/` - Develop a Java application to read an integer and display its multiplication table from 1 to 10. Configure a Jenkins Continuous Delivery (CD) pipeline to build the application, package it as a JAR file, deploy the JAR to a separate deployment directory on the local machine, and execute it.

- **Q16** `Q16_ArraySum/` - Develop a Java application to read five integers into an array and calculate their sum. Configure a Jenkins CD pipeline to build the application, generate the JAR file, deploy it to a separate deployment directory on the local machine, and execute the deployed application.

- **Q17** `Q17_LargestOfThree/` - Develop a Java application to read three integers and display the largest number. Create a Jenkins CD pipeline with stages for building, packaging, and deploying the application to a separate deployment directory on the local machine, and verify its execution.

- **Q18** `Q18_CelsiusToFahrenheit/` - Develop a Java application to convert a temperature from Celsius to Fahrenheit using the formula F = (C x 9/5) + 32. Configure a Jenkins CD pipeline to build the application, package it as a JAR file, deploy it to a separate deployment directory on the local machine, and verify its execution.


Each question folder = ready project + `*_ALL_IN_ONE.md` (question, aim, procedure, all code, xml/yml, commands, output).


## Viva-voce

- **CI vs CD?** CI builds/tests; Continuous Delivery also packages and deploys automatically.
- **jar cfe?** c=create, f=file name, e=entry point (Main-Class).
- **archiveArtifacts?** Saves build output in Jenkins for download.
- **Why separate deployment directory?** Simulates deploying the release away from the build workspace.
- **Continuous Delivery vs Deployment?** Delivery = ready to deploy (may need approval); Deployment = auto to production.
