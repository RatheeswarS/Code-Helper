# Q17. Largest of Three - Jenkins CD

**Experiment:** Jenkins CD pipeline: build, JAR, deploy to local directory, execute

## Question
Develop a Java application to read three integers and display the largest number. Create a Jenkins CD pipeline with stages for building, packaging, and deploying the application to a separate deployment directory on the local machine, and verify its execution.

## Aim
To develop a Java application and configure a Jenkins Continuous Delivery pipeline that builds the application, packages it as a JAR, deploys it to a separate deployment directory on the local machine and executes / verifies it.

## Procedure
1. Write the Java program and a `Jenkinsfile` with Checkout, Build, Package, Deploy and Execute & Verify stages.
2. Push to GitHub (or use the no-GitHub inline pipeline).
3. Create a Pipeline job in Jenkins and build it.
4. Check the deployed JAR in the deployment directory and verify the output.

## Project structure
```text
Q17_LargestOfThree/
  LargestOfThree.java
  Jenkinsfile
  Jenkinsfile-no-github
```

## Files

### `LargestOfThree.java`
```java
import java.util.Scanner;

public class LargestOfThree {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter three integers: ");
        int a = sc.nextInt();
        int b = sc.nextInt();
        int c = sc.nextInt();

        int largest;
        if (a >= b && a >= c) {
            largest = a;
        } else if (b >= a && b >= c) {
            largest = b;
        } else {
            largest = c;
        }
        System.out.println("Largest number is: " + largest);
        sc.close();
    }
}
```

### `Jenkinsfile`
```groovy
pipeline {
    agent any

    environment {
        APP_NAME   = 'LargestOfThree'
        JAR_NAME   = 'LargestOfThree.jar'
        DEPLOY_DIR = '/var/lib/jenkins/deployments/largest-of-three'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/<your-username>/largest-of-three.git'
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
                sh 'cd ${DEPLOY_DIR} && echo "12 45 30" | java -jar ${JAR_NAME} | tee output.txt'
                sh 'grep -q "Largest number is: 45" ${DEPLOY_DIR}/output.txt && echo "VERIFIED: deployed application ran correctly"'
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
        APP_NAME   = 'LargestOfThree'
        JAR_NAME   = 'LargestOfThree.jar'
        DEPLOY_DIR = '/var/lib/jenkins/deployments/largest-of-three'
    }

    stages {
        stage('Get Source') {
            steps {
                writeFile file: 'LargestOfThree.java', text: '''import java.util.Scanner;

public class LargestOfThree {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter three integers: ");
        int a = sc.nextInt();
        int b = sc.nextInt();
        int c = sc.nextInt();

        int largest;
        if (a >= b && a >= c) {
            largest = a;
        } else if (b >= a && b >= c) {
            largest = b;
        } else {
            largest = c;
        }
        System.out.println("Largest number is: " + largest);
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
                sh 'cd ${DEPLOY_DIR} && echo "12 45 30" | java -jar ${JAR_NAME} | tee output.txt'
                sh 'grep -q "Largest number is: 45" ${DEPLOY_DIR}/output.txt && echo "VERIFIED: deployed application ran correctly"'
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
javac -d build LargestOfThree.java
jar cfe LargestOfThree.jar LargestOfThree -C build .
mkdir -p ~/deploy/largest-of-three
cp LargestOfThree.jar ~/deploy/largest-of-three/
cd ~/deploy/largest-of-three
java -jar LargestOfThree.jar
```

### 3. Push code to GitHub
Create empty repo `largest-of-three` on github.com. Replace `<your-username>` in `Jenkinsfile`, then:
```bash
git init
git add LargestOfThree.java Jenkinsfile
git commit -m "LargestOfThree CD pipeline"
git branch -M main
git remote add origin https://github.com/<your-username>/largest-of-three.git
git push -u origin main
```

### 4. Create the Jenkins pipeline
1. **New Item** -> `largest-of-three-cd` -> **Pipeline** -> OK.
2. Definition **Pipeline script from SCM** -> Git -> repo URL -> Branch `*/main` -> Script Path `Jenkinsfile` -> Save.
3. **Build Now**.

**No GitHub / no internet?** Definition **Pipeline script** -> paste `Jenkinsfile-no-github` (source code is written by the pipeline itself) -> Save -> Build Now.

### 5. Verify deployment
- Stage view: Checkout -> Build -> Package -> Deploy -> Execute & Verify all green.
- Console output shows the program output and `VERIFIED: deployed application ran correctly`.
- Build page -> **Build Artifacts** -> `LargestOfThree.jar`.
- On the machine:
```bash
sudo ls -l /var/lib/jenkins/deployments/largest-of-three/
sudo -u jenkins java -jar /var/lib/jenkins/deployments/largest-of-three/LargestOfThree.jar
```

### Errors
| Error | Fix |
|---|---|
| `javac: not found` / `jar: not found` | JRE only -> `sudo apt install -y openjdk-21-jdk`, `sudo systemctl restart jenkins` |
| `Permission denied` on deploy dir | Keep DEPLOY_DIR under `/var/lib/jenkins/...` (owned by jenkins) or `sudo mkdir -p /opt/deploy && sudo chown jenkins:jenkins /opt/deploy` |
| `no main manifest attribute` | jar made without `e` flag -> use `jar cfe <jar> <MainClass> -C build .` |
| Jenkins on Windows | replace `sh` with `bat`, DEPLOY_DIR like `C:\\deploy\\largest-of-three`, `copy` instead of `cp` |


## Sample output (input: `12 45 30`)
```text
Enter three integers: Largest number is: 45
```


## Result
The experiment was executed successfully and the output was verified.
