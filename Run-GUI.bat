@echo off
cd /d "%~dp0"
echo === Building SHAMS ===
call mvn -f pom.xml clean package -Dmaven.compiler.release=20
if %errorlevel% neq 0 (
  echo Build failed.
  pause
  exit /b %errorlevel%
)
echo === Running SHAMS ===
java -jar "target\shams-1.0.0.jar"
echo.
pause
