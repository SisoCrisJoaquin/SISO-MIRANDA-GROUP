@echo off
REM Endless Runner - Run Game
REM This script runs the compiled Endless Runner game

echo Starting Endless Runner Game...
cd /d "%~dp0"

if exist "EndlessRunner.jar" (
    java -jar EndlessRunner.jar
) else if exist "bin" (
    java -cp bin Main
) else (
    echo ERROR: Game files not found!
    echo Please run 'run.bat' first to build the game.
    pause
    exit /b 1
)
