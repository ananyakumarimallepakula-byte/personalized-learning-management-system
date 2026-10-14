@echo off
title LearnPredict - Backend Server (Spring Boot Port 8080)
echo ========================================================
echo   Starting LearnPredict Spring Boot Backend...
echo   Port: 8080
echo ========================================================
set "JAVA_HOME=C:\Users\AJAY KUMAR\jdk\jdk-17.0.12+7"
set "PATH=%JAVA_HOME%\bin;C:\Users\AJAY KUMAR\maven\apache-maven-3.9.9\bin;%PATH%"

cd /d "d:\management system\backend"
mvn spring-boot:run
pause
