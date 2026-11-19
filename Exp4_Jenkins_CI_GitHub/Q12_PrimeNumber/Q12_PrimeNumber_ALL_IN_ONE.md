# Q12. Prime Number - Jenkins CI with GitHub trigger

**Experiment:** Jenkins CI pipeline with GitHub checkout

## Question
Develop a Java application to determine whether a given integer is a prime number. Configure a Jenkins CI pipeline with source code checkout and build stages, and trigger a new build whenever the source code is updated in GitHub.

## Aim
To develop a Java application and configure a Jenkins Continuous Integration pipeline that retrieves the source code from GitHub, compiles and executes it.

## Procedure
1. Write the Java program and a `Jenkinsfile` with Checkout, Compile and Execute stages.
2. Push both files to a GitHub repository.
3. Create a Pipeline job in Jenkins using Pipeline script from SCM.
4. Build the job and verify the stages and console output.

## Project structure
```text
Q12_PrimeNumber/
  PrimeCheck.java
  Jenkinsfile
```

## Files

### `PrimeCheck.java`
```java
import java.util.Scanner;

public class PrimeCheck {

    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter an integer: ");
        int n = sc.nextInt();
        if (isPrime(n)) {
            System.out.println(n + " is a PRIME number");
        } else {
            System.out.println(n + " is NOT a prime number");
        }
        sc.close();
    }
}
```

### `Jenkinsfile`
```groovy
pipeline {
    agent any

    // Build automatically when code is pushed to GitHub (webhook),
    // pollSCM is the backup if the webhook cannot reach Jenkins
    triggers {
        githubPush()
        pollSCM('* * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/<your-username>/prime-ci.git'
            }
        }
        stage('Compile') {
            steps {
                sh 'javac PrimeCheck.java'
            }
        }
        stage('Execute') {
            steps {
                sh 'echo "29" | java PrimeCheck'
            }
        }
    }
    post {
        success {
            echo 'Build and execution successful'
        }
        failure {
            echo 'Build failed'
        }
    }
}
```

## Commands / Steps

### 1. Jenkins + Java must be installed
(see `jenkins-install-commands.md` in the jenk folder, or Exp3 commands). Need JDK for `javac`:
```bash
sudo apt install -y openjdk-21-jdk git
javac -version
```
Jenkins at `http://localhost:8080`. Plugins needed: **Git**, **Pipeline**, **GitHub** (come with suggested plugins).

### 2. Test the program locally
```bash
javac PrimeCheck.java
java PrimeCheck
```

### 3. Push source + Jenkinsfile to GitHub
Create an empty public repo `prime-ci` on github.com, then in this folder:
```bash
git init
git add PrimeCheck.java Jenkinsfile
git commit -m "Add PrimeCheck program and Jenkinsfile"
git branch -M main
git remote add origin https://github.com/<your-username>/prime-ci.git
git push -u origin main
```
Edit `Jenkinsfile` first: replace `<your-username>` with your GitHub username.

### 4. Create Jenkins pipeline job
1. Dashboard -> **New Item** -> name `prime-ci` -> **Pipeline** -> OK.
2. **Pipeline** section -> Definition: **Pipeline script from SCM**
   - SCM: **Git**, Repository URL: `https://github.com/<your-username>/prime-ci.git`
   - Branch Specifier: `*/main`, Script Path: `Jenkinsfile`
3. **Save** -> **Build Now**.
   (Or Definition: **Pipeline script** and paste the Jenkinsfile content directly.)

### 5. Verify
Build -> **Console Output** (or Stages view): Checkout -> Compile -> Execute all green, program output printed, `Finished: SUCCESS`.

### 6. Auto-trigger build on GitHub update
**Option A - GitHub webhook (instant):**
1. Jenkins job -> Configure -> **Build Triggers** -> tick **GitHub hook trigger for GITScm polling** -> Save
   (the `triggers { githubPush() }` in the Jenkinsfile also sets this after the first build).
2. GitHub repo -> **Settings** -> **Webhooks** -> **Add webhook**
   - Payload URL: `http://<JENKINS-PUBLIC-IP>:8080/github-webhook/`  (trailing `/` required)
   - Content type: `application/json`, event: **Just the push event** -> Add webhook (green tick = OK).
3. Jenkins on localhost/WSL is not reachable by GitHub -> expose it:
```bash
ngrok http 8080        # use the https://xxxx.ngrok-free.app URL + /github-webhook/ as Payload URL
```
**Option B - Poll SCM (works on localhost, no webhook):** Build Triggers -> **Poll SCM** -> Schedule `* * * * *`
(already in Jenkinsfile as `pollSCM('* * * * *')`).

**Test it:**
```bash
# edit the code (e.g. change a message) then:
git add .
git commit -m "update to trigger Jenkins"
git push
```
Within ~1 minute a new build starts automatically -> build cause says "Started by GitHub push" or "Started by an SCM change".

### Errors
| Error | Fix |
|---|---|
| `javac: not found` | `sudo apt install -y openjdk-21-jdk` then `sudo systemctl restart jenkins` |
| `Couldn't find any revision to build` | branch is `master` not `main` -> change `branch:` in Jenkinsfile / Branch Specifier |
| Jenkins on Windows (not WSL) | replace every `sh '...'` with `bat '...'` |
| Private repo auth fail | Add credentials (GitHub username + token) and use `git branch: 'main', credentialsId: 'github', url: '...'` |


## Sample output (input: `29`)
```text
Enter an integer: 29 is a PRIME number
```


## Result
The experiment was executed successfully and the output was verified.
