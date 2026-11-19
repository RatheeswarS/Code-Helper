# Q11. Palindrome Number - Jenkins CI

**Experiment:** Jenkins CI pipeline with GitHub checkout

## Question
Develop a Java application to determine whether a given integer reads the same forwards and backwards. Configure a Jenkins Continuous Integration (CI) pipeline to retrieve the source code from GitHub, compile the program, and execute it.

## Aim
To develop a Java application and configure a Jenkins Continuous Integration pipeline that retrieves the source code from GitHub, compiles and executes it.

## Procedure
1. Write the Java program and a `Jenkinsfile` with Checkout, Compile and Execute stages.
2. Push both files to a GitHub repository.
3. Create a Pipeline job in Jenkins using Pipeline script from SCM.
4. Build the job and verify the stages and console output.

## Project structure
```text
Q11_Palindrome/
  Palindrome.java
  Jenkinsfile
```

## Files

### `Palindrome.java`
```java
import java.util.Scanner;

public class Palindrome {

    public static boolean isPalindrome(int num) {
        if (num < 0) {
            return false;
        }
        int original = num;
        int reversed = 0;
        while (num > 0) {
            reversed = reversed * 10 + num % 10;
            num = num / 10;
        }
        return original == reversed;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter an integer: ");
        int n = sc.nextInt();
        if (isPalindrome(n)) {
            System.out.println(n + " is a PALINDROME");
        } else {
            System.out.println(n + " is NOT a palindrome");
        }
        sc.close();
    }
}
```

### `Jenkinsfile`
```groovy
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/<your-username>/palindrome-ci.git'
            }
        }
        stage('Compile') {
            steps {
                sh 'javac Palindrome.java'
            }
        }
        stage('Execute') {
            steps {
                sh 'echo "12321" | java Palindrome'
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
javac Palindrome.java
java Palindrome
```

### 3. Push source + Jenkinsfile to GitHub
Create an empty public repo `palindrome-ci` on github.com, then in this folder:
```bash
git init
git add Palindrome.java Jenkinsfile
git commit -m "Add Palindrome program and Jenkinsfile"
git branch -M main
git remote add origin https://github.com/<your-username>/palindrome-ci.git
git push -u origin main
```
Edit `Jenkinsfile` first: replace `<your-username>` with your GitHub username.

### 4. Create Jenkins pipeline job
1. Dashboard -> **New Item** -> name `palindrome-ci` -> **Pipeline** -> OK.
2. **Pipeline** section -> Definition: **Pipeline script from SCM**
   - SCM: **Git**, Repository URL: `https://github.com/<your-username>/palindrome-ci.git`
   - Branch Specifier: `*/main`, Script Path: `Jenkinsfile`
3. **Save** -> **Build Now**.
   (Or Definition: **Pipeline script** and paste the Jenkinsfile content directly.)

### 5. Verify
Build -> **Console Output** (or Stages view): Checkout -> Compile -> Execute all green, program output printed, `Finished: SUCCESS`.

### Errors
| Error | Fix |
|---|---|
| `javac: not found` | `sudo apt install -y openjdk-21-jdk` then `sudo systemctl restart jenkins` |
| `Couldn't find any revision to build` | branch is `master` not `main` -> change `branch:` in Jenkinsfile / Branch Specifier |
| Jenkins on Windows (not WSL) | replace every `sh '...'` with `bat '...'` |
| Private repo auth fail | Add credentials (GitHub username + token) and use `git branch: 'main', credentialsId: 'github', url: '...'` |


## Sample output (input: `12321`)
```text
Enter an integer: 12321 is a PALINDROME
```


## Result
The experiment was executed successfully and the output was verified.
