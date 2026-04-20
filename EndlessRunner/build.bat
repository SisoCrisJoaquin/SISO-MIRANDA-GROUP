@echo off
REM Endless Runner - Simple Build Script

echo.
echo Building Endless Runner Game...
echo.

if not exist "src" (
    echo ERROR: You must run this from the EndlessRunner folder
    pause
    exit /b 1
)

if not exist "bin" mkdir bin

echo Compiling Java files...
cd src
javac -d ../bin *.java
if errorlevel 1 (
    echo Compilation FAILED
    cd ..
    pause
    exit /b 1
)
cd ..

echo.
echo Build Complete! Ready to play!
echo Run 'play.bat' to start the game.
echo.
pause

echo.
echo SUCCESS! EndlessRunner.jar created
echo.
echo To play: java -jar EndlessRunner.jar
echo.
pause
