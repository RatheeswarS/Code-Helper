# Q08. Purchase Discount with JUnit

**Experiment:** Maven + JUnit tests + Azure Pipeline (build and regression tests)

## Question
Develop a Java application to calculate the final purchase amount by applying a 10% discount when the purchase amount is Rs.1,000 or more; otherwise, no discount is applied. Write JUnit test cases for purchase amounts below, equal to, and above Rs.1,000, and configure an Azure Pipeline using Maven to build the application and run the regression tests.

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
Q08_Discount/
  src/main/java/com/example/Discount.java
  src/test/java/com/example/DiscountTest.java
  pom.xml
  azure-pipelines.yml
  .gitignore
```

## Files

### `src/main/java/com/example/Discount.java`
```java
package com.example;

import java.util.Scanner;

public class Discount {

    // 10% discount when purchase amount >= Rs.1000, otherwise no discount
    public static double finalAmount(double amount) {
        if (amount >= 1000) {
            return amount - (amount * 10 / 100);
        }
        return amount;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter purchase amount: ");
        double amount = sc.nextDouble();
        double fin = finalAmount(amount);
        System.out.println();
        System.out.printf("Purchase amount : Rs. %.2f%n", amount);
        System.out.printf("Discount        : Rs. %.2f%n", amount - fin);
        System.out.printf("Final amount    : Rs. %.2f%n", fin);
        sc.close();
    }
}
```

### `src/test/java/com/example/DiscountTest.java`
```java
package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DiscountTest {

    @Test
    void belowThousandNoDiscount() {
        assertEquals(999.0, Discount.finalAmount(999), 0.001);
    }

    @Test
    void exactlyThousandGetsDiscount() {
        assertEquals(900.0, Discount.finalAmount(1000), 0.001);
    }

    @Test
    void aboveThousandGetsDiscount() {
        assertEquals(1350.0, Discount.finalAmount(1500), 0.001);
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
    <artifactId>discount</artifactId>
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
                            <mainClass>com.example.Discount</mainClass>
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
mvn archetype:generate -DgroupId=com.example -DartifactId=discount -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
cd discount
```
Now:
- delete the generated `src/main/java/com/example/App.java` and `src/test/java/com/example/AppTest.java`
- create `src/main/java/com/example/Discount.java` (code above)
- create the test file(s) under `src/test/java/com/example/` (code above)
- replace `pom.xml` with the one above
- create `azure-pipelines.yml` in the project root (code above)

(Or simply copy this ready-made question folder - it already has this exact layout.)

### 3. Build & run locally
```bash
mvn clean compile                # compile only
mvn test                         # runs JUnit tests -> "Tests run: X, Failures: 0, Errors: 0"
mvn package                      # creates target/discount-1.0.jar
java -jar target/discount-1.0.jar
```
Sample input: `1500`

### 4. Push code to GitHub
```bash
git init
git add .
git commit -m "Discount Maven project with Azure pipeline"
git branch -M main
git remote add origin https://github.com/<your-username>/discount.git
git push -u origin main
```
(First create an empty repo named `discount` on github.com. Password prompt = GitHub Personal Access Token.)

### 5. Azure DevOps pipeline
1. Go to https://dev.azure.com -> **New project** (name: `discount`, Private) -> Create.
2. Left menu **Pipelines** -> **Create Pipeline** -> **GitHub (YAML)** -> authorize -> select the repo.
3. Choose **Existing Azure Pipelines YAML file** -> Branch `main`, Path `/azure-pipelines.yml` -> Continue -> **Run**.
4. Open the job -> logs show `BUILD SUCCESS` for compile and package steps.
5. Run summary -> **1 published** -> `drop` -> `discount-1.0.jar` (download = JAR artifact).
6. Open the **Tests** tab of the run -> all JUnit tests show **Passed** (regression tests).

(Without GitHub: Azure **Repos** -> Files -> Initialize/clone, push code there with
`git remote add origin https://dev.azure.com/<org>/discount/_git/discount`, then pick **Azure Repos Git** in step 2.)

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


## Sample output (input: `1500`)
```text
Enter purchase amount: 
Purchase amount : Rs. 1500.00
Discount        : Rs. 150.00
Final amount    : Rs. 1350.00
```


Expected test result:
```text
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.DiscountTest
[INFO] Tests run: N, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```


## Result
The experiment was executed successfully and the output was verified.
