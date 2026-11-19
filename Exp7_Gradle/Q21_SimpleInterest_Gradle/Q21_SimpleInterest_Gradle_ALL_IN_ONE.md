# Q21. Simple Interest - Gradle

**Experiment:** Gradle Java project: build and JAR

## Question
Develop a Java application to calculate simple interest using the principal amount, rate of interest, and time period provided as input. Create a Gradle Java project, configure the build.gradle file, and build the application to generate the required build artifacts.

## Aim
To develop a Java application, create a Gradle Java project, configure build.gradle and build it to generate the JAR / build artifacts.

## Procedure
1. Install JDK and Gradle.
2. Create project structure `src/main/java/com/example`.
3. Write `settings.gradle` and `build.gradle` (java + application plugins, Main-Class manifest).
4. Run `gradle clean build` and check `build/libs`.
5. Run the JAR with `java -jar`.

## Project structure
```text
Q21_SimpleInterest_Gradle/
  src/main/java/com/example/SimpleInterest.java
  settings.gradle
  build.gradle
  .gitignore
```

## Files

### `src/main/java/com/example/SimpleInterest.java`
```java
package com.example;

import java.util.Scanner;

public class SimpleInterest {

    public static double calculateSI(double principal, double rate, double time) {
        return (principal * rate * time) / 100;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter principal amount : ");
        double p = sc.nextDouble();
        System.out.print("Enter rate of interest : ");
        double r = sc.nextDouble();
        System.out.print("Enter time (years)     : ");
        double t = sc.nextDouble();

        double si = calculateSI(p, r, t);
        System.out.println();
        System.out.printf("Simple Interest = %.2f%n", si);
        System.out.printf("Total Amount    = %.2f%n", p + si);
        sc.close();
    }
}
```

### `settings.gradle`
```groovy
rootProject.name = 'simple-interest'
```

### `build.gradle`
```groovy
plugins {
    id 'java'
    id 'application'
}

group = 'com.example'
version = '1.0'

repositories {
    mavenCentral()
}

dependencies {
    testImplementation 'junit:junit:4.13.2'
}

application {
    mainClass = 'com.example.SimpleInterest'
}

// Main-Class in the manifest -> "java -jar" works
jar {
    manifest {
        attributes 'Main-Class': 'com.example.SimpleInterest'
    }
}

// lets "gradle run" read keyboard input (Scanner)
run {
    standardInput = System.in
}
```

## Commands / Steps

### 1. Install JDK + Gradle (one time)
Ubuntu / WSL:
```bash
sudo apt update
sudo apt install -y openjdk-17-jdk unzip wget
wget https://services.gradle.org/distributions/gradle-8.14.3-bin.zip
sudo unzip -d /opt/gradle gradle-8.14.3-bin.zip
echo 'export PATH=$PATH:/opt/gradle/gradle-8.14.3/bin' >> ~/.bashrc
source ~/.bashrc
gradle -v
```
(Do NOT use `apt install gradle` - it gives very old Gradle 4.4.)
Windows: download the same zip, extract to `C:\gradle`, add `C:\gradle\gradle-8.14.3\bin` to PATH, `gradle -v`.

### 2. Create the Gradle Java project
**Option A - manual (matches this folder):**
```bash
mkdir simple-interest && cd simple-interest
mkdir -p src/main/java/com/example
# create settings.gradle, build.gradle and src/main/java/com/example/SimpleInterest.java (code above)
```
**Option B - gradle init:**
```bash
mkdir simple-interest && cd simple-interest
gradle init --type java-application --dsl groovy
#  answers: Java version -> 17, project name -> simple-interest, structure -> Single application project,
#  test framework -> JUnit 4, package -> com.example
# then put SimpleInterest.java in app/src/main/java/com/example/, delete the generated App.java + AppTest.java,
# and in app/build.gradle set mainClass = 'com.example.SimpleInterest' and add the jar { manifest ... } + run { ... } blocks
```

### 3. Build
```bash
gradle tasks                  # list available tasks
gradle clean build            # compile + test + package
ls build/libs                 # -> simple-interest-1.0.jar   (Option B: app/build/libs/app.jar)
ls build/classes/java/main/com/example
```

### 4. Run
```bash
java -jar build/libs/simple-interest-1.0.jar
# or
gradle run -q --console=plain
```
Sample input: `10000 5 2`

### Build artifacts generated
| Path | What |
|---|---|
| `build/classes/java/main/` | compiled `.class` files |
| `build/libs/simple-interest-1.0.jar` | runnable JAR |
| `build/distributions/simple-interest-1.0.zip / .tar` | distribution with start scripts (`application` plugin) |
| `build/scripts/simple-interest` | launch scripts |

### Optional: Gradle wrapper (no Gradle install needed on other PCs)
```bash
gradle wrapper
./gradlew clean build          # Windows: gradlew.bat clean build
```

### Errors
| Error | Fix |
|---|---|
| `no main manifest attribute` | `jar { manifest { attributes 'Main-Class': ... } }` missing in build.gradle |
| `NoSuchElementException` on `gradle run` | `run { standardInput = System.in }` missing |
| `Could not find or load main class` | package line in .java must match `com.example` and folder `src/main/java/com/example` |
| Unsupported class file major version | Gradle too old for JDK -> use Gradle 8.14 with JDK 17/21 |


## Sample output (input: `10000 5 2`)
```text
Enter principal amount : Enter rate of interest : Enter time (years)     : 
Simple Interest = 1000.00
Total Amount    = 11000.00
```


## Result
The experiment was executed successfully and the output was verified.
