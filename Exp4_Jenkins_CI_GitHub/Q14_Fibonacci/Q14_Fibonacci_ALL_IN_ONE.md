# Q14. Fibonacci - Jenkins CI auto build

**Experiment:** Jenkins CI pipeline with GitHub checkout

## Question
Develop a Java application to read an integer (n) and display the first (n) terms of the Fibonacci series. Configure a Jenkins CI pipeline to retrieve the source code from GitHub and automatically build the application.

## Aim
To develop a Java application and configure a Jenkins Continuous Integration pipeline that retrieves the source code from GitHub, compiles and executes it.

## Procedure
1. Write the Java program and a `Jenkinsfile` with Checkout, Compile and Execute stages.
2. Push both files to a GitHub repository.
3. Create a Pipeline job in Jenkins using Pipeline script from SCM.
4. Build the job and verify the stages and console output.

## Project structure
```text
Q14_Fibonacci/
  Fibonacci.java
  Jenkinsfile
```

## Files

### `Fibonacci.java`
```java
import java.util.Scanner;

public class Fibonacci {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of terms (n): ");
        int n = sc.nextInt();
        long a = 0, b = 1;
        System.out.print("First " + n + " terms of Fibonacci series: ");
        for (int i = 1; i <= n; i++) {
            System.out.print(a + " ");
            long next = a + b;
            a = b;
            b = next;
        }
        System.out.println();
        sc.close();
    }
}
```

### `Jenkinsfile`
```groovy
pipeline {
    agent any

    // Check GitHub every minute and build automatically on every new commit
    triggers {
        githubPush()
        pollSCM('* * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/<your-username>/fibonacci-ci.git'
            }
        }
        stage('Build') {
            steps {
                sh 'javac Fibonacci.java'
            }
        }
        stage('Run') {
            steps {
                sh 'echo "10" | java Fibonacci'
            }
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
javac Fibonacci.java
java Fibonacci
```

### 3. Push source + Jenkinsfile to GitHub
Create an empty public repo `fibonacci-ci` on github.com, then in this folder:
```bash
git init
git add Fibonacci.java Jenkinsfile
git commit -m "Add Fibonacci program and Jenkinsfile"
git branch -M main
git remote add origin https://github.com/<your-username>/fibonacci-ci.git
git push -u origin main
```
Edit `Jenkinsfile` first: replace `<your-username>` with your GitHub username.

### 4. Create Jenkins pipeline job
1. Dashboard -> **New Item** -> name `fibonacci-ci` -> **Pipeline** -> OK.
2. **Pipeline** section -> Definition: **Pipeline script from SCM**
   - SCM: **Git**, Repository URL: `https://github.com/<your-username>/fibonacci-ci.git`
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


## Sample output (input: `10`)
```text
Enter number of terms (n): First 10 terms of Fibonacci series: 0 1 1 2 3 5 8 13 21 34
```


## Result
The experiment was executed successfully and the output was verified.
