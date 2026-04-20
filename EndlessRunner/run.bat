@echo off
REM Endless Runner Game - Build and Run Script
REM This is a robust build system with detailed error checking

setlocal enabledelayedexpansion
cls

echo.
echo ============================================================
echo            ENDLESS RUNNER GAME - BUILD SYSTEM
echo ============================================================
echo.
echo Current Directory: %cd%
echo.

REM Check if we're in the right directory
if not exist "src" (
    echo ERROR: Cannot find 'src' folder!
    echo.
    echo This script must be run from the EndlessRunner directory.
    echo Current directory: %cd%
    echo.
    echo Please navigate to the EndlessRunner folder and try again.
    echo.
    pause
    exit /b 1
)

echo Checking system requirements...
echo.

REM Check if Java is installed
echo Checking for Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo.
    echo ERROR: Java is NOT installed or not in your PATH!
    echo.
    echo Solution:
    echo 1. Download Java from: oracle.com/java or adoptium.net
    echo 2. Install Java Development Kit (JDK)
    echo 3. After installation, restart your computer
    echo 4. Try running this script again
    echo.
    pause
    exit /b 1
)

for /f "tokens=*" %%i in ('java -version 2^>^&1') do (
    echo ✓ Java found: %%i
)
echo.

REM Check if source files exist
echo Checking source files...
if not exist "src\Main.java" (
    echo ERROR: Main.java not found!
    pause
    exit /b 1
)
if not exist "src\GamePanel.java" (
    echo ERROR: GamePanel.java not found!
    pause
    exit /b 1
)
if not exist "src\Player.java" (
    echo ERROR: Player.java not found!
    pause
    exit /b 1
)
if not exist "src\Obstacle.java" (
    echo ERROR: Obstacle.java not found!
    pause
    exit /b 1
)
if not exist "src\Scoreboard.java" (
    echo ERROR: Scoreboard.java not found!
    pause
    exit /b 1
)
if not exist "src\ImageLoader.java" (
    echo ERROR: ImageLoader.java not found!
    pause
    exit /b 1
)
echo ✓ All source files found
echo.

REM Create necessary directories
echo Creating directories...
if not exist "bin" (
    mkdir bin
    echo ✓ Created 'bin' directory
)
if not exist "resources" (
    mkdir resources
    echo ✓ Created 'resources' directory
)
echo.

REM Compile Java files
echo ============================================================
echo [1/4] Compiling Java source files...
echo ============================================================
cd src

echo Compiling...
for %%f in (*.java) do (
    echo   - Compiling %%f
)
echo.

javac -d ../bin *.java 2>compile_error.log

if errorlevel 1 (
    echo.
    echo ERROR: Compilation failed!
    echo.
    echo Error details:
    type compile_error.log
    echo.
    cd ..
    pause
    exit /b 1
) else (
    echo ✓ Compilation successful!
)

cd ..
echo.

REM Create META-INF directory
echo ============================================================
echo [2/4] Creating META-INF directory...
echo ============================================================
if not exist "bin\META-INF" (
    mkdir bin\META-INF
    echo ✓ Created META-INF directory
) else (
    echo ✓ META-INF directory already exists
)
echo.

REM Create MANIFEST.MF file
echo ============================================================
echo [3/4] Creating MANIFEST.MF...
echo ============================================================
(
    echo Manifest-Version: 1.0
    echo Created-By: Endless Runner Build System
    echo Main-Class: Main
    echo Class-Path: .
) > bin\META-INF\MANIFEST.MF

if exist "bin\META-INF\MANIFEST.MF" (
    echo ✓ MANIFEST.MF created successfully
) else (
    echo ERROR: Failed to create MANIFEST.MF!
    pause
    exit /b 1
)
echo.

REM Build JAR file
echo ============================================================
echo [4/4] Building JAR file...
echo ============================================================

cd bin

if not exist "*.class" (
    echo ERROR: No .class files found!
    echo Compilation must have failed.
    cd ..
    pause
    exit /b 1
)

echo Building EndlessRunner.jar...
jar cfm ../EndlessRunner.jar META-INF\MANIFEST.MF *.class 2>jar_error.log

if errorlevel 1 (
    echo.
    echo ERROR: JAR creation failed!
    echo.
    echo Error details:
    type jar_error.log
    echo.
    cd ..
    pause
    exit /b 1
) else (
    echo ✓ JAR file created successfully!
)

cd ..
echo.

REM Verify JAR was created
if exist "EndlessRunner.jar" (
    for /f "usebackq" %%A in ('EndlessRunner.jar') do set size=%%~zA
    echo ✓ EndlessRunner.jar created successfully
    echo   File size: !size! bytes
) else (
    echo ERROR: EndlessRunner.jar was not created!
    pause
    exit /b 1
)

echo.
echo ============================================================
echo              BUILD COMPLETE - SUCCESS!
echo ============================================================
echo.
echo ✓ Your game has been built successfully!
echo.
echo Next steps:
echo.
echo 1. RUN THE GAME:
echo    - Double-click: play.bat
echo    - Or: java -jar EndlessRunner.jar
echo.
echo 2. (OPTIONAL) ADD CUSTOM IMAGES:
echo    - Place PNG files in the 'resources' folder:
echo      • player.png (30x40 pixels)
echo      • obstacle.png (40x50 pixels)
echo      • background.png (800x500+ pixels)
echo    - Run this script again to rebuild with your images
echo.
echo Location of game: %cd%\EndlessRunner.jar
echo.
echo ============================================================
echo.
pause
exit /b 0 

