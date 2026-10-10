@echo off
echo ==================================================
echo Building and Starting RasheedhaMart Capstone...
echo ==================================================

echo Freeing port 8080 if occupied by background tasks...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080 ^| findstr LISTENING') do taskkill /f /pid %%a >nul 2>&1

call mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven build failed!
    exit /b %ERRORLEVEL%
)

echo.
echo [SUCCESS] Build completed. Starting Embedded Server on http://localhost:8080 ...
call mvn exec:java
