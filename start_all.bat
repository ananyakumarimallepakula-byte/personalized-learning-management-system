@echo off
title LearnPredict - Full Stack Launcher
echo ========================================================
echo   Launching Frontend and Backend in Separate Terminals
echo ========================================================

echo [1/2] Launching Backend Command Prompt...
start "LearnPredict Backend Server (Spring Boot)" cmd /k "call \"d:\management system\start_backend.bat\""

timeout /t 2 /nobreak >nul

echo [2/2] Launching Frontend Command Prompt...
start "LearnPredict Frontend Server (Node.js)" cmd /k "call \"d:\management system\start_frontend.bat\""

echo.
echo ========================================================
echo   Both services are now running in separate windows!
echo   - Backend:  http://localhost:8080/api
echo   - Frontend: http://localhost:3000
echo ========================================================
echo.
pause
