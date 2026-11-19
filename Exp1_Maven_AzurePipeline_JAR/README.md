# Exp1 Maven AzurePipeline JAR

Maven project + Azure Pipeline (compile and generate JAR)

## Questions

- **Q01** `Q01_StudentGrade/` - Write a Java program to read marks in three subjects, calculate the average, and display the grade according to the following criteria: A (90 and above), B (75-89), C (50-74), and F (below 50). Create a Maven project and configure an Azure Pipeline to compile the application and generate the JAR file.

- **Q02** `Q02_ElectricityBill/` - Develop a Java application to calculate an electricity bill based on the units consumed, using the following rates: Rs.1.50 per unit for the first 100 units, Rs.2.50 per unit for the next 100 units, and Rs.4.00 per unit for units exceeding 200. Build the application as a Maven project and configure an Azure Pipeline to compile the source code and generate the JAR artifact.

- **Q03** `Q03_SimpleInterest/` - Develop a Java application to calculate simple interest based on the principal amount, rate of interest, and time period provided as input. Build the application as a Maven project and configure an Azure Pipeline to compile the source code and generate the JAR artifact.

- **Q04** `Q04_CircleArea/` - Develop a Java application to calculate the area of a circle based on its radius provided as input. Build the application as a Maven project and configure an Azure Pipeline to compile the source code and generate the JAR artifact.


Each question folder = ready project + `*_ALL_IN_ONE.md` (question, aim, procedure, all code, xml/yml, commands, output).


## Viva-voce

- **What is Maven?** Build automation / dependency management tool for Java, driven by pom.xml.
- **What is pom.xml?** Project Object Model - groupId, artifactId, version, dependencies, plugins.
- **Maven lifecycle phases?** validate, compile, test, package, verify, install, deploy.
- **What is an artifact?** Output of a build (JAR) stored/published by the pipeline.
- **What is Azure Pipelines?** Azure DevOps CI/CD service; pipeline defined in azure-pipelines.yml.
- **trigger / pool / steps?** trigger = branch that starts run; pool = agent machine; steps = tasks run in order.
- **Hosted vs self-hosted agent?** Hosted = Microsoft VM; self-hosted = your own machine running the agent.
