# Q01. Student Grade Calculator

**Experiment:** Maven project + Azure Pipeline (compile and generate JAR)

## Question
Write a Java program to read marks in three subjects, calculate the average, and display the grade according to the following criteria: A (90 and above), B (75-89), C (50-74), and F (below 50). Create a Maven project and configure an Azure Pipeline to compile the application and generate the JAR file.

## Aim
To develop a Java application as a Maven project and configure an Azure DevOps pipeline that compiles the source code and generates the JAR artifact.

## Procedure
1. Write the Java program in `src/main/java/com/example/`.
2. Configure `pom.xml` (Java 17, jar plugin with Main-Class).
3. Build locally with `mvn clean package` and run the JAR.
4. Push the project to GitHub.
5. Create `azure-pipelines.yml` with Maven compile + package, copy and publish artifact tasks.
6. Create the pipeline in Azure DevOps, run it and download the JAR artifact.

## Project structure
```text
Q01_StudentGrade/
  src/main/java/com/example/StudentGrade.java
  pom.xml
  azure-pipelines.yml
  .gitignore
```

## Files

### `src/main/java/com/example/StudentGrade.java`
```java
package com.example;

import java.util.Scanner;

public class StudentGrade {

    public static double average(double m1, double m2, double m3) {
        return (m1 + m2 + m3) / 3.0;
    }

    public static char grade(double avg) {
        if (avg >= 90) {
            return 'A';
        } else if (avg >= 75) {
            return 'B';
        } else if (avg >= 50) {
            return 'C';
        } else {
            return 'F';
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter marks of subject 1: ");
        double m1 = sc.nextDouble();
        System.out.print("Enter marks of subject 2: ");
        double m2 = sc.nextDouble();
        System.out.print("Enter marks of subject 3: ");
        double m3 = sc.nextDouble();

        double avg = average(m1, m2, m3);
        System.out.println();
        System.out.printf("Average = %.2f%n", avg);
        System.out.println("Grade   = " + grade(avg));
        sc.close();
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
    <artifactId>student-grade</artifactId>
    <version>1.0</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
            </plugin>

            <!-- adds Main-Class to the JAR so "java -jar" works -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.4.1</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>com.example.StudentGrade</mainClass>
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
# Azure Pipeline: compile the Maven project and generate the JAR artifact
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
  displayName: 'Package JAR'
  inputs:
    mavenPomFile: 'pom.xml'
    goals: 'package'
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
mvn archetype:generate -DgroupId=com.example -DartifactId=student-grade -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
cd student-grade
```
Now:
- delete the generated `src/main/java/com/example/App.java` and `src/test/java/com/example/AppTest.java`
- create `src/main/java/com/example/StudentGrade.java` (code above)
- replace `pom.xml` with the one above
- create `azure-pipelines.yml` in the project root (code above)

(Or simply copy this ready-made question folder - it already has this exact layout.)

### 3. Build & run locally
```bash
mvn clean compile                # compile only
mvn package                      # creates target/student-grade-1.0.jar
java -jar target/student-grade-1.0.jar
```
Sample input: `85 92 78`

### 4. Push code to GitHub
```bash
git init
git add .
git commit -m "StudentGrade Maven project with Azure pipeline"
git branch -M main
git remote add origin https://github.com/<your-username>/student-grade.git
git push -u origin main
```
(First create an empty repo named `student-grade` on github.com. Password prompt = GitHub Personal Access Token.)

### 5. Azure DevOps pipeline
1. Go to https://dev.azure.com -> **New project** (name: `student-grade`, Private) -> Create.
2. Left menu **Pipelines** -> **Create Pipeline** -> **GitHub (YAML)** -> authorize -> select the repo.
3. Choose **Existing Azure Pipelines YAML file** -> Branch `main`, Path `/azure-pipelines.yml` -> Continue -> **Run**.
4. Open the job -> logs show `BUILD SUCCESS` for compile and package steps.
5. Run summary -> **1 published** -> `drop` -> `student-grade-1.0.jar` (download = JAR artifact).

(Without GitHub: Azure **Repos** -> Files -> Initialize/clone, push code there with
`git remote add origin https://dev.azure.com/<org>/student-grade/_git/student-grade`, then pick **Azure Repos Git** in step 2.)

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


## Sample output (input: `85 92 78`)
```text
Enter marks of subject 1: Enter marks of subject 2: Enter marks of subject 3: 
Average = 85.00
Grade   = B
```


## Result
The experiment was executed successfully and the output was verified.
