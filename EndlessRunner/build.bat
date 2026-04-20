@echo off
REM Endless Runner - Simple Build Script
REM This is a minimal version with basic error checking

echo.
echo Building Endless Runner Game...
echo.

REM Check current directory
if not exist "src" (
    echo ERROR: You must run this from the EndlessRunner folder
    pause
    exit /b 1
)

REM Create bin directory
if not exist "bin" mkdir bin

REM Compile
echo Compiling Java files...
cd src
javac -d ../bin Main.java GamePanel.java Player.java Obstacle.java Scoreboard.java ImageLoader.java
if errorlevel 1 (
    echo Compilation FAILED
    cd ..
    pause
    exit /b 1
)
cd ..

REM Create manifest directory
if not exist "bin\META-INF" mkdir bin\META-INF

REM Create manifest file
(
    echo Manifest-Version: 1.0
    echo Main-Class: Main
) > bin\META-INF\MANIFEST.MF

REM Build JAR
cd bin
jar cfm ../EndlessRunner.jar META-INF\MANIFEST.MF *.class
if errorlevel 1 (
    echo JAR creation FAILED
    cd ..
    pause
    exit /b 1
)
cd ..

echo.
echo SUCCESS! EndlessRunner.jar created
echo.
echo To play: java -jar EndlessRunner.jar
echo.
pause
