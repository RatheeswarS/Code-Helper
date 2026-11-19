# Q24. Rectangle Area & Perimeter - Gradle

**Experiment:** Gradle Java project: build and JAR

## Question
Develop a Java application to read the length and breadth of a rectangle and calculate its area and perimeter. Configure a Gradle Java project to build the application and generate the JAR file.

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
Q24_Rectangle_Gradle/
  src/main/java/com/example/Rectangle.java
  settings.gradle
  build.gradle
  .gitignore
```

## Files

### `src/main/java/com/example/Rectangle.java`
```java
package com.example;

import java.util.Scanner;

public class Rectangle {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter length : ");
        double length = sc.nextDouble();
        System.out.print("Enter breadth: ");
        double breadth = sc.nextDouble();

        double area = length * breadth;
        double perimeter = 2 * (length + breadth);
        System.out.printf("Area      = %.2f%n", area);
        System.out.printf("Perimeter = %.2f%n", perimeter);
        sc.close();
    }
}
```

### `settings.gradle`
```groovy
rootProject.name = 'rectangle'
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
    mainClass = 'com.example.Rectangle'
}

// Main-Class in the manifest -> "java -jar" works
jar {
    manifest {
        attributes 'Main-Class': 'com.example.Rectangle'
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
mkdir rectangle && cd rectangle
mkdir -p src/main/java/com/example
# create settings.gradle, build.gradle and src/main/java/com/example/Rectangle.java (code above)
```
**Option B - gradle init:**
```bash
mkdir rectangle && cd rectangle
gradle init --type java-application --dsl groovy
#  answers: Java version -> 17, project name -> rectangle, structure -> Single application project,
#  test framework -> JUnit 4, package -> com.example
# then put Rectangle.java in app/src/main/java/com/example/, delete the generated App.java + AppTest.java,
# and in app/build.gradle set mainClass = 'com.example.Rectangle' and add the jar { manifest ... } + run { ... } blocks
```

### 3. Build
```bash
gradle tasks                  # list available tasks
gradle clean build            # compile + test + package
ls build/libs                 # -> rectangle-1.0.jar   (Option B: app/build/libs/app.jar)
ls build/classes/java/main/com/example
```

### 4. Run
```bash
java -jar build/libs/rectangle-1.0.jar
# or
gradle run -q --console=plain
```
Sample input: `12 8`

### Build artifacts generated
| Path | What |
|---|---|
| `build/classes/java/main/` | compiled `.class` files |
| `build/libs/rectangle-1.0.jar` | runnable JAR |
| `build/distributions/rectangle-1.0.zip / .tar` | distribution with start scripts (`application` plugin) |
| `build/scripts/rectangle` | launch scripts |

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


## Sample output (input: `12 8`)
```text
Enter length : Enter breadth: Area      = 96.00
Perimeter = 40.00
```


## Result
The experiment was executed successfully and the output was verified.
