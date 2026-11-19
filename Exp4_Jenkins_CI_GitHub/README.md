# Exp4 Jenkins CI GitHub

Jenkins CI pipeline with GitHub checkout

## Questions

- **Q11** `Q11_Palindrome/` - Develop a Java application to determine whether a given integer reads the same forwards and backwards. Configure a Jenkins Continuous Integration (CI) pipeline to retrieve the source code from GitHub, compile the program, and execute it.

- **Q12** `Q12_PrimeNumber/` - Develop a Java application to determine whether a given integer is a prime number. Configure a Jenkins CI pipeline with source code checkout and build stages, and trigger a new build whenever the source code is updated in GitHub.

- **Q13** `Q13_Factorial/` - Develop a Java application to calculate the factorial of a non-negative integer using a loop. Create a Jenkins CI pipeline with stages for checking out the source code from GitHub, compiling the program, and executing it.

- **Q14** `Q14_Fibonacci/` - Develop a Java application to read an integer (n) and display the first (n) terms of the Fibonacci series. Configure a Jenkins CI pipeline to retrieve the source code from GitHub and automatically build the application.


Each question folder = ready project + `*_ALL_IN_ONE.md` (question, aim, procedure, all code, xml/yml, commands, output).


## Viva-voce

- **What is CI?** Continuous Integration - every commit is automatically built and tested.
- **What is a Jenkinsfile?** Pipeline as code stored in the repo; declarative syntax pipeline { stages { } }.
- **Webhook vs Poll SCM?** Webhook: GitHub pushes event to Jenkins. Poll SCM: Jenkins checks repo on a schedule.
- **Cron '* * * * *'?** Every minute (min hour day month weekday).
- **Stage vs step?** Stage = logical phase; step = single command in a stage.
