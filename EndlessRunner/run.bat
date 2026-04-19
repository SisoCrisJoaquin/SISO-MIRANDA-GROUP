@echo off
echo Compiling Endless Runner Game...
cd src
javac *.java
if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b 1
)
cd ..
echo.
echo Starting game...
cd src
java Main
cd ..
pause
