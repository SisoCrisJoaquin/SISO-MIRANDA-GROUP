@echo off
REM Endless Runner - Diagnostic Script
REM This script helps identify build issues

cls
echo.
echo ============================================================
echo        ENDLESS RUNNER - DIAGNOSTIC REPORT
echo ============================================================
echo.

echo CHECKING SYSTEM SETUP
echo ────────────────────────────────────────────────────────────
echo.

echo 1. Current Directory:
echo    %cd%
echo.

echo 2. Checking if we're in the right folder...
if exist "src\Main.java" (
    echo    ✓ Found src\Main.java
) else (
    echo    ✗ ERROR: Not in EndlessRunner folder or src folder missing
    echo    Please navigate to EndlessRunner folder and run again
    pause
    exit /b 1
)
echo.

echo 3. Checking Java installation...
java -version >nul 2>&1
if errorlevel 1 (
    echo    ✗ ERROR: Java is NOT installed or not in PATH
    echo.
    echo    Solution:
    echo    - Download Java from oracle.com/java
    echo    - Or use adoptium.net
    echo    - Install the JDK (not JRE)
    echo    - Restart your computer
    echo    - Try again
) else (
    echo    ✓ Java is installed
    for /f "tokens=*" %%i in ('java -version 2^>^&1') do (
        echo      %%i
    )
)
echo.

echo 4. Java compiler (javac) check...
javac -version >nul 2>&1
if errorlevel 1 (
    echo    ✗ ERROR: javac not found
    echo    Make sure you installed JDK and restarted
) else (
    echo    ✓ javac found
)
echo.

echo 5. JAR tool check...
jar >nul 2>&1
if errorlevel 1 (
    echo    ✗ ERROR: jar command not found
    echo    Java installation may be incomplete
) else (
    echo    ✓ jar tool found
)
echo.

echo 6. Checking source files...
for %%f in (src\Main.java src\GamePanel.java src\Player.java src\Obstacle.java src\Scoreboard.java src\ImageLoader.java) do (
    if exist "%%f" (
        echo    ✓ Found %%f
    ) else (
        echo    ✗ Missing %%f
    )
)
echo.

echo 7. Checking directories...
if exist "src" (
    echo    ✓ src directory exists
) else (
    echo    ✗ src directory missing
)
if exist "bin" (
    echo    ✓ bin directory exists
) else (
    echo    - bin directory will be created
)
if exist "resources" (
    echo    ✓ resources directory exists
) else (
    echo    - resources directory will be created
)
echo.

echo ============================================================
echo SUMMARY
echo ============================================================
echo.
echo If you see any ✗ errors above, that's the problem!
echo.
echo Most common issue: Java not installed
echo Solution: Download and install Java, then restart computer
echo.
echo Once all check marks appear, try running: build.bat or run.bat
echo.
pause
