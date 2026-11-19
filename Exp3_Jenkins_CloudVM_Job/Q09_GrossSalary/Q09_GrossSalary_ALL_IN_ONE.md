# Q09. Gross Salary - Jenkins job

**Experiment:** Jenkins on cloud VM + Jenkins job to compile & execute Java

## Question
Develop a Java application to calculate and display the gross salary of an employee based on the basic salary, House Rent Allowance (HRA), and Dearness Allowance (DA) provided as input, using the formula Gross Salary = Basic Salary + HRA + DA. Install and configure Jenkins on a cloud virtual machine, configure Java, and create a Jenkins job to compile and execute the application.

## Aim
To install and configure Jenkins on a cloud virtual machine, configure Java, and create a Jenkins job to compile and execute a Java application.

## Procedure
1. Launch an Ubuntu VM on the cloud and open port 8080.
2. Install OpenJDK and Jenkins on the VM.
3. Unlock Jenkins and install suggested plugins.
4. Configure JDK under Manage Jenkins -> Tools.
5. Create a Freestyle job with an Execute shell build step to compile and run the program.
6. Build the job and check the console output.

## Project structure
```text
Q09_GrossSalary/
  GrossSalary.java
  build-step.sh
  Jenkinsfile
```

## Files

### `GrossSalary.java`
```java
import java.util.Scanner;

public class GrossSalary {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Basic Salary: ");
        double basic = sc.nextDouble();
        System.out.print("Enter HRA: ");
        double hra = sc.nextDouble();
        System.out.print("Enter DA: ");
        double da = sc.nextDouble();

        double gross = basic + hra + da;
        System.out.println();
        System.out.printf("Basic Salary : %.2f%n", basic);
        System.out.printf("HRA          : %.2f%n", hra);
        System.out.printf("DA           : %.2f%n", da);
        System.out.printf("Gross Salary : %.2f%n", gross);
        sc.close();
    }
}
```

### `build-step.sh`
```bash
echo "===== Java version ====="
java -version

echo "===== Creating GrossSalary.java ====="
cat > GrossSalary.java <<'EOF'
import java.util.Scanner;

public class GrossSalary {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Basic Salary: ");
        double basic = sc.nextDouble();
        System.out.print("Enter HRA: ");
        double hra = sc.nextDouble();
        System.out.print("Enter DA: ");
        double da = sc.nextDouble();

        double gross = basic + hra + da;
        System.out.println();
        System.out.printf("Basic Salary : %.2f%n", basic);
        System.out.printf("HRA          : %.2f%n", hra);
        System.out.printf("DA           : %.2f%n", da);
        System.out.printf("Gross Salary : %.2f%n", gross);
        sc.close();
    }
}
EOF

echo "===== Compiling ====="
javac GrossSalary.java

echo "===== Executing ====="
echo "30000 6000 4000" | java GrossSalary
```

### `Jenkinsfile`
```groovy
// Optional: same job as a Pipeline (New Item -> Pipeline -> paste this)
pipeline {
    agent any
    tools {
        jdk 'JDK21'        // name given in Manage Jenkins -> Tools -> JDK installations
    }
    stages {
        stage('Create Source') {
            steps {
                writeFile file: 'GrossSalary.java', text: '''import java.util.Scanner;

public class GrossSalary {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Basic Salary: ");
        double basic = sc.nextDouble();
        System.out.print("Enter HRA: ");
        double hra = sc.nextDouble();
        System.out.print("Enter DA: ");
        double da = sc.nextDouble();

        double gross = basic + hra + da;
        System.out.println();
        System.out.printf("Basic Salary : %.2f%n", basic);
        System.out.printf("HRA          : %.2f%n", hra);
        System.out.printf("DA           : %.2f%n", da);
        System.out.printf("Gross Salary : %.2f%n", gross);
        sc.close();
    }
}
'''
            }
        }
        stage('Compile') {
            steps {
                sh 'java -version'
                sh 'javac GrossSalary.java'
            }
        }
        stage('Execute') {
            steps {
                sh 'echo "30000 6000 4000" | java GrossSalary'
            }
        }
    }
}
```

## Commands / Steps


### 1. Create cloud VM (AWS EC2 example; Azure VM is same idea)
1. AWS console -> EC2 -> **Launch instance** -> Name `jenkins-server`, AMI **Ubuntu Server 22.04/24.04**, type `t2.medium` (t2.micro works but slow).
2. Key pair -> create `jenkins-key` (.pem) -> download.
3. Network settings -> Security group -> allow **SSH (22)** and add **Custom TCP 8080** from Anywhere (0.0.0.0/0).
4. Launch -> copy the **Public IPv4 address**.

(Azure: Virtual machines -> Create -> Ubuntu 22.04 -> SSH key -> after creation: Networking -> Add inbound port rule 8080.)

### 2. Connect to the VM
```bash
chmod 400 jenkins-key.pem                     # Windows PowerShell: skip this line
ssh -i jenkins-key.pem ubuntu@<PUBLIC-IP>
```

### 3. Install Java (JDK, needed for javac) + Jenkins
```bash
sudo apt update
sudo apt install -y fontconfig openjdk-21-jdk
java -version
javac -version

sudo mkdir -p /etc/apt/keyrings
sudo wget -O /etc/apt/keyrings/jenkins-keyring.asc https://pkg.jenkins.io/debian-stable/jenkins.io-2026.key
echo "deb [signed-by=/etc/apt/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/" | sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt update
sudo apt install -y jenkins
sudo systemctl enable jenkins
sudo systemctl start jenkins
sudo systemctl status jenkins                 # active (running)  -> press q
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```
If key/GPG error (`NO_PUBKEY`): check current key name on https://pkg.jenkins.io/debian-stable/ and replace the `.key` file name.

### 4. Unlock Jenkins
Browser -> `http://<PUBLIC-IP>:8080` -> paste the password -> **Install suggested plugins** -> create admin user -> Save and Finish.

### 5. Configure Java in Jenkins
1. **Manage Jenkins** -> **Tools** -> **JDK installations** -> **Add JDK**.
2. Name: `JDK21`, uncheck **Install automatically**, JAVA_HOME: `/usr/lib/jvm/java-21-openjdk-amd64`
   (find it with `readlink -f $(which javac) | sed 's:/bin/javac::'`).
3. **Save**.

### 6. Create the Jenkins job (Freestyle)
1. Dashboard -> **New Item** -> name `GrossSalary-job` -> **Freestyle project** -> OK.
2. (Optional) General -> **JDK** dropdown -> `JDK21` (shows only when a JDK is configured in Tools).
3. **Build Steps** -> **Add build step** -> **Execute shell** -> paste `build-step.sh` (below / in this folder).
4. **Save** -> **Build Now** -> click build `#1` -> **Console Output**.

Build step script (`build-step.sh`) - creates the Java file, compiles and executes it:
```bash
echo "===== Java version ====="
java -version

echo "===== Creating GrossSalary.java ====="
cat > GrossSalary.java <<'EOF'
import java.util.Scanner;

public class GrossSalary {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Basic Salary: ");
        double basic = sc.nextDouble();
        System.out.print("Enter HRA: ");
        double hra = sc.nextDouble();
        System.out.print("Enter DA: ");
        double da = sc.nextDouble();

        double gross = basic + hra + da;
        System.out.println();
        System.out.printf("Basic Salary : %.2f%n", basic);
        System.out.printf("HRA          : %.2f%n", hra);
        System.out.printf("DA           : %.2f%n", da);
        System.out.printf("Gross Salary : %.2f%n", gross);
        sc.close();
    }
}
EOF

echo "===== Compiling ====="
javac GrossSalary.java

echo "===== Executing ====="
echo "30000 6000 4000" | java GrossSalary
```

Alternative: put `GrossSalary.java` on the VM and use only the last 3 lines:
```bash
scp -i jenkins-key.pem GrossSalary.java ubuntu@<PUBLIC-IP>:/tmp/
# build step:
cp /tmp/GrossSalary.java .
javac GrossSalary.java
echo "30000 6000 4000" | java GrossSalary
```

### Run the program manually on the VM (to show output)
```bash
javac GrossSalary.java
java GrossSalary
```


## Sample output (input: `30000 6000 4000`)
```text
Enter Basic Salary: Enter HRA: Enter DA: 
Basic Salary : 30000.00
HRA          : 6000.00
DA           : 4000.00
Gross Salary : 40000.00
```


## Result
The experiment was executed successfully and the output was verified.
