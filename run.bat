@echo off
echo ==================================================
echo Building and Starting RasheedhaMart Capstone...
echo ==================================================

call mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven build failed!
    exit /b %ERRORLEVEL%
)

echo.
echo [SUCCESS] Build completed. Starting Embedded Server on http://localhost:8080 ...
call mvn exec:java
