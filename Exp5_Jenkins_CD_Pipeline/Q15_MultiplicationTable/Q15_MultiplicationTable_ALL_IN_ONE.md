# Q15. Multiplication Table - Jenkins CD

**Experiment:** Jenkins CD pipeline: build, JAR, deploy to local directory, execute

## Question
Develop a Java application to read an integer and display its multiplication table from 1 to 10. Configure a Jenkins Continuous Delivery (CD) pipeline to build the application, package it as a JAR file, deploy the JAR to a separate deployment directory on the local machine, and execute it.

## Aim
To develop a Java application and configure a Jenkins Continuous Delivery pipeline that builds the application, packages it as a JAR, deploys it to a separate deployment directory on the local machine and executes / verifies it.

## Procedure
1. Write the Java program and a `Jenkinsfile` with Checkout, Build, Package, Deploy and Execute & Verify stages.
2. Push to GitHub (or use the no-GitHub inline pipeline).
3. Create a Pipeline job in Jenkins and build it.
4. Check the deployed JAR in the deployment directory and verify the output.

## Project structure
```text
Q15_MultiplicationTable/
  MultiplicationTable.java
  Jenkinsfile
  Jenkinsfile-no-github
```

## Files

### `MultiplicationTable.java`
```java
import java.util.Scanner;

public class MultiplicationTable {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int n = sc.nextInt();
        System.out.println("Multiplication table of " + n);
        for (int i = 1; i <= 10; i++) {
            System.out.println(n + " x " + i + " = " + (n * i));
        }
        sc.close();
    }
}
```

### `Jenkinsfile`
```groovy
pipeline {
    agent any

    environment {
        APP_NAME   = 'MultiplicationTable'
        JAR_NAME   = 'MultiplicationTable.jar'
        DEPLOY_DIR = '/var/lib/jenkins/deployments/multiplication-table'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/<your-username>/multiplication-table.git'
            }
        }
        stage('Build') {
            steps {
                sh 'rm -rf build && mkdir -p build'
                sh 'javac -d build ${APP_NAME}.java'
            }
        }
        stage('Package') {
            steps {
                // jar c=create f=file e=entry point(Main-Class)
                sh 'jar cfe ${JAR_NAME} ${APP_NAME} -C build .'
                archiveArtifacts artifacts: "${JAR_NAME}", fingerprint: true
            }
        }
        stage('Deploy') {
            steps {
                sh 'mkdir -p ${DEPLOY_DIR}'
                sh 'cp ${JAR_NAME} ${DEPLOY_DIR}/'
                sh 'ls -l ${DEPLOY_DIR}'
            }
        }
        stage('Execute & Verify') {
            steps {
                sh 'cd ${DEPLOY_DIR} && echo "7" | java -jar ${JAR_NAME} | tee output.txt'
                sh 'grep -q "7 x 10 = 70" ${DEPLOY_DIR}/output.txt && echo "VERIFIED: deployed application ran correctly"'
            }
        }
    }
    post {
        success {
            echo "Deployed to ${DEPLOY_DIR}/${JAR_NAME}"
        }
        failure {
            echo 'CD pipeline failed'
        }
    }
}
```

### `Jenkinsfile-no-github`
```groovy
pipeline {
    agent any

    environment {
        APP_NAME   = 'MultiplicationTable'
        JAR_NAME   = 'MultiplicationTable.jar'
        DEPLOY_DIR = '/var/lib/jenkins/deployments/multiplication-table'
    }

    stages {
        stage('Get Source') {
            steps {
                writeFile file: 'MultiplicationTable.java', text: '''import java.util.Scanner;

public class MultiplicationTable {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int n = sc.nextInt();
        System.out.println("Multiplication table of " + n);
        for (int i = 1; i <= 10; i++) {
            System.out.println(n + " x " + i + " = " + (n * i));
        }
        sc.close();
    }
}
'''
            }
        }
        stage('Build') {
            steps {
                sh 'rm -rf build && mkdir -p build'
                sh 'javac -d build ${APP_NAME}.java'
            }
        }
        stage('Package') {
            steps {
                // jar c=create f=file e=entry point(Main-Class)
                sh 'jar cfe ${JAR_NAME} ${APP_NAME} -C build .'
                archiveArtifacts artifacts: "${JAR_NAME}", fingerprint: true
            }
        }
        stage('Deploy') {
            steps {
                sh 'mkdir -p ${DEPLOY_DIR}'
                sh 'cp ${JAR_NAME} ${DEPLOY_DIR}/'
                sh 'ls -l ${DEPLOY_DIR}'
            }
        }
        stage('Execute & Verify') {
            steps {
                sh 'cd ${DEPLOY_DIR} && echo "7" | java -jar ${JAR_NAME} | tee output.txt'
                sh 'grep -q "7 x 10 = 70" ${DEPLOY_DIR}/output.txt && echo "VERIFIED: deployed application ran correctly"'
            }
        }
    }
    post {
        success {
            echo "Deployed to ${DEPLOY_DIR}/${JAR_NAME}"
        }
        failure {
            echo 'CD pipeline failed'
        }
    }
}
```

## Commands / Steps

### 1. Jenkins + JDK ready
(see `jenkins-install-commands.md`). `jar` and `javac` need the **JDK**:
```bash
sudo apt install -y openjdk-21-jdk git
javac -version && jar --version
```

### 2. Test build/package/deploy manually (same as pipeline does)
```bash
mkdir -p build
javac -d build MultiplicationTable.java
jar cfe MultiplicationTable.jar MultiplicationTable -C build .
mkdir -p ~/deploy/multiplication-table
cp MultiplicationTable.jar ~/deploy/multiplication-table/
cd ~/deploy/multiplication-table
java -jar MultiplicationTable.jar
```

### 3. Push code to GitHub
Create empty repo `multiplication-table` on github.com. Replace `<your-username>` in `Jenkinsfile`, then:
```bash
git init
git add MultiplicationTable.java Jenkinsfile
git commit -m "MultiplicationTable CD pipeline"
git branch -M main
git remote add origin https://github.com/<your-username>/multiplication-table.git
git push -u origin main
```

### 4. Create the Jenkins pipeline
1. **New Item** -> `multiplication-table-cd` -> **Pipeline** -> OK.
2. Definition **Pipeline script from SCM** -> Git -> repo URL -> Branch `*/main` -> Script Path `Jenkinsfile` -> Save.
3. **Build Now**.

**No GitHub / no internet?** Definition **Pipeline script** -> paste `Jenkinsfile-no-github` (source code is written by the pipeline itself) -> Save -> Build Now.

### 5. Verify deployment
- Stage view: Checkout -> Build -> Package -> Deploy -> Execute & Verify all green.
- Console output shows the program output and `VERIFIED: deployed application ran correctly`.
- Build page -> **Build Artifacts** -> `MultiplicationTable.jar`.
- On the machine:
```bash
sudo ls -l /var/lib/jenkins/deployments/multiplication-table/
sudo -u jenkins java -jar /var/lib/jenkins/deployments/multiplication-table/MultiplicationTable.jar
```

### Errors
| Error | Fix |
|---|---|
| `javac: not found` / `jar: not found` | JRE only -> `sudo apt install -y openjdk-21-jdk`, `sudo systemctl restart jenkins` |
| `Permission denied` on deploy dir | Keep DEPLOY_DIR under `/var/lib/jenkins/...` (owned by jenkins) or `sudo mkdir -p /opt/deploy && sudo chown jenkins:jenkins /opt/deploy` |
| `no main manifest attribute` | jar made without `e` flag -> use `jar cfe <jar> <MainClass> -C build .` |
| Jenkins on Windows | replace `sh` with `bat`, DEPLOY_DIR like `C:\\deploy\\multiplication-table`, `copy` instead of `cp` |


## Sample output (input: `7`)
```text
Enter a number: Multiplication table of 7
7 x 1 = 7
7 x 2 = 14
7 x 3 = 21
7 x 4 = 28
7 x 5 = 35
7 x 6 = 42
7 x 7 = 49
7 x 8 = 56
7 x 9 = 63
7 x 10 = 70
```


## Result
The experiment was executed successfully and the output was verified.
