# Q05. Calculator with JUnit

**Experiment:** Maven + JUnit tests + Azure Pipeline (build and regression tests)

## Question
Develop a Java calculator application to perform addition, subtraction, multiplication, and division, including division-by-zero handling. Write JUnit test cases for all arithmetic operations and configure an Azure Pipeline using Maven to build the application and run the regression tests.

## Aim
To develop a Java application with JUnit test cases as a Maven project and configure an Azure DevOps pipeline that builds the application and runs the regression tests.

## Procedure
1. Write the Java program in `src/main/java/com/example/`.
2. Write JUnit 5 test cases in `src/test/java/com/example/`.
3. Configure `pom.xml` with junit-jupiter dependency and surefire plugin.
4. Run `mvn test` and `mvn package` locally.
5. Push to GitHub and create `azure-pipelines.yml` (compile, test with published results, package).
6. Run pipeline in Azure DevOps and check the Tests tab.

## Project structure
```text
Q05_Calculator/
  src/main/java/com/example/Calculator.java
  src/test/java/com/example/CalculatorTest.java
  pom.xml
  azure-pipelines.yml
  .gitignore
```

## Files

### `src/main/java/com/example/Calculator.java`
```java
package com.example;

import java.util.Scanner;

public class Calculator {

    public double add(double a, double b) {
        return a + b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero is not allowed");
        }
        return a / b;
    }

    public static void main(String[] args) {
        Calculator calc = new Calculator();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter first number : ");
        double a = sc.nextDouble();
        System.out.print("Enter second number: ");
        double b = sc.nextDouble();

        System.out.println();
        System.out.println("Addition       : " + calc.add(a, b));
        System.out.println("Subtraction    : " + calc.subtract(a, b));
        System.out.println("Multiplication : " + calc.multiply(a, b));
        try {
            System.out.println("Division       : " + calc.divide(a, b));
        } catch (ArithmeticException e) {
            System.out.println("Division       : Error - " + e.getMessage());
        }
        sc.close();
    }
}
```

### `src/test/java/com/example/CalculatorTest.java`
```java
package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CalculatorTest {

    private final Calculator calc = new Calculator();

    @Test
    void testAddition() {
        assertEquals(15.0, calc.add(10, 5), 0.0001);
    }

    @Test
    void testSubtraction() {
        assertEquals(5.0, calc.subtract(10, 5), 0.0001);
    }

    @Test
    void testMultiplication() {
        assertEquals(50.0, calc.multiply(10, 5), 0.0001);
    }

    @Test
    void testDivision() {
        assertEquals(2.0, calc.divide(10, 5), 0.0001);
    }

    @Test
    void testDivisionByZero() {
        ArithmeticException ex = assertThrows(ArithmeticException.class,
                () -> calc.divide(10, 0));
        assertEquals("Division by zero is not allowed", ex.getMessage());
    }
}
```

### `pom.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>calculator</artifactId>
    <version>1.0</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
            </plugin>

            <!-- runs the JUnit 5 tests during "mvn test" / "mvn package" -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
            </plugin>

            <!-- adds Main-Class to the JAR so "java -jar" works -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.4.1</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>com.example.Calculator</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### `azure-pipelines.yml`
```yaml
# Azure Pipeline: build with Maven, run JUnit regression tests, publish JAR
trigger:
- main

pool:
  vmImage: 'ubuntu-latest'

steps:
- task: Maven@4
  displayName: 'Compile source code'
  inputs:
    mavenPomFile: 'pom.xml'
    goals: 'clean compile'
    javaHomeOption: 'JDKVersion'
    jdkVersionOption: '1.17'
    publishJUnitResults: false

- task: Maven@4
  displayName: 'Run JUnit regression tests'
  inputs:
    mavenPomFile: 'pom.xml'
    goals: 'test'
    javaHomeOption: 'JDKVersion'
    jdkVersionOption: '1.17'
    publishJUnitResults: true
    testResultsFiles: '**/surefire-reports/TEST-*.xml'
    testRunTitle: 'Regression Tests'

- task: Maven@4
  displayName: 'Package JAR'
  inputs:
    mavenPomFile: 'pom.xml'
    goals: 'package'
    options: '-DskipTests'
    javaHomeOption: 'JDKVersion'
    jdkVersionOption: '1.17'
    publishJUnitResults: false

- task: CopyFiles@2
  displayName: 'Copy JAR to staging directory'
  inputs:
    SourceFolder: '$(System.DefaultWorkingDirectory)/target'
    Contents: '*.jar'
    TargetFolder: '$(Build.ArtifactStagingDirectory)'

- task: PublishBuildArtifacts@1
  displayName: 'Publish JAR artifact'
  inputs:
    PathtoPublish: '$(Build.ArtifactStagingDirectory)'
    ArtifactName: 'drop'
```

## Commands / Steps

### 1. Install JDK 17 + Maven (one time)
Ubuntu / WSL:
```bash
sudo apt update
sudo apt install -y openjdk-17-jdk maven git
java -version
mvn -v
```
Windows: install JDK 17, download `apache-maven-3.9.x-bin.zip` from maven.apache.org, extract to `C:\maven`,
add `C:\maven\bin` to PATH, set `JAVA_HOME` to the JDK folder, then `mvn -v`.

### 2. Create the Maven project
```bash
mvn archetype:generate -DgroupId=com.example -DartifactId=calculator -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
cd calculator
```
Now:
- delete the generated `src/main/java/com/example/App.java` and `src/test/java/com/example/AppTest.java`
- create `src/main/java/com/example/Calculator.java` (code above)
- create the test file(s) under `src/test/java/com/example/` (code above)
- replace `pom.xml` with the one above
- create `azure-pipelines.yml` in the project root (code above)

(Or simply copy this ready-made question folder - it already has this exact layout.)

### 3. Build & run locally
```bash
mvn clean compile                # compile only
mvn test                         # runs JUnit tests -> "Tests run: X, Failures: 0, Errors: 0"
mvn package                      # creates target/calculator-1.0.jar
java -jar target/calculator-1.0.jar
```
Sample input: `10 0`

### 4. Push code to GitHub
```bash
git init
git add .
git commit -m "Calculator Maven project with Azure pipeline"
git branch -M main
git remote add origin https://github.com/<your-username>/calculator.git
git push -u origin main
```
(First create an empty repo named `calculator` on github.com. Password prompt = GitHub Personal Access Token.)

### 5. Azure DevOps pipeline
1. Go to https://dev.azure.com -> **New project** (name: `calculator`, Private) -> Create.
2. Left menu **Pipelines** -> **Create Pipeline** -> **GitHub (YAML)** -> authorize -> select the repo.
3. Choose **Existing Azure Pipelines YAML file** -> Branch `main`, Path `/azure-pipelines.yml` -> Continue -> **Run**.
4. Open the job -> logs show `BUILD SUCCESS` for compile and package steps.
5. Run summary -> **1 published** -> `drop` -> `calculator-1.0.jar` (download = JAR artifact).
6. Open the **Tests** tab of the run -> all JUnit tests show **Passed** (regression tests).

(Without GitHub: Azure **Repos** -> Files -> Initialize/clone, push code there with
`git remote add origin https://dev.azure.com/<org>/calculator/_git/calculator`, then pick **Azure Repos Git** in step 2.)

### Error fix: "No hosted parallelism has been purchased or granted"
Free Microsoft-hosted agents need a grant (form: https://aka.ms/azpipelines-parallelism-request). Quick fix = self-hosted agent on your own PC:
1. User settings (top right) -> **Personal access tokens** -> New token -> scope **Agent Pools (Read & manage)** -> copy it.
2. Project settings -> **Agent pools** -> **Default** -> **New agent** -> Windows -> Download.
3. PowerShell:
```powershell
mkdir C:\agent ; cd C:\agent
Expand-Archive $HOME\Downloads\vsts-agent-win-x64-*.zip -DestinationPath .
.\config.cmd      # URL: https://dev.azure.com/<your-org>  auth: PAT  pool: Default  (Enter for rest)
.\run.cmd         # leave this window open -> "Listening for Jobs"
```
4. In `azure-pipelines.yml` replace the pool block with:
```yaml
pool:
  name: Default
```
and in every Maven@4 task change `javaHomeOption: 'JDKVersion'` + `jdkVersionOption: '1.17'` to:
```yaml
    javaHomeOption: 'Path'
    mavenVersionOption: 'Path'
    mavenDirectory: 'C:\maven'
```
(PC needs JDK + Maven installed and JAVA_HOME set; restart run.cmd after setting env vars.) Commit, push, run again.


## Sample output (input: `10 0`)
```text
Enter first number : Enter second number: 
Addition       : 10.0
Subtraction    : 10.0
Multiplication : 0.0
Division       : Error - Division by zero is not allowed
```


Expected test result:
```text
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.CalculatorTest
[INFO] Tests run: N, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```


## Result
The experiment was executed successfully and the output was verified.
