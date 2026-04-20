# Simple Build Script for Endless Runner
Write-Host "Building Endless Runner Game..." -ForegroundColor Green

# Check if src folder exists
if (-not (Test-Path "src")) {
    Write-Host "ERROR: You must run this from the EndlessRunner folder" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Create bin directory
if (-not (Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

# Compile Java files
Write-Host "Compiling Java files..." -ForegroundColor Yellow
cd src
javac -d ../bin *.java
if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation FAILED" -ForegroundColor Red
    cd ..
    Read-Host "Press Enter to exit"
    exit 1
}
cd ..

# Create META-INF folder
$metaDir = "bin\META-INF"
if (-not (Test-Path $metaDir)) {
    New-Item -ItemType Directory -Path $metaDir | Out-Null
}

# Create manifest file
$manifest = @"
Manifest-Version: 1.0
Main-Class: Main
"@
$manifest | Out-File -FilePath "bin\META-INF\MANIFEST.MF" -Encoding ASCII

# Find jar command
$jarCmd = "jar"
$javaPath = (Get-Command java -ErrorAction SilentlyContinue).Source
if ($javaPath) {
    $javaDir = Split-Path $javaPath
    $jarCmd = Join-Path $javaDir "jar.exe"
}

# Create JAR file
Write-Host "Creating JAR file..." -ForegroundColor Yellow
cd bin
& $jarCmd cfm ../EndlessRunner.jar META-INF\MANIFEST.MF *.class
if ($LASTEXITCODE -ne 0) {
    Write-Host "JAR creation FAILED" -ForegroundColor Red
    cd ..
    Read-Host "Press Enter to exit"
    exit 1
}
cd ..

Write-Host "Build Complete! EndlessRunner.jar created successfully!" -ForegroundColor Green
Write-Host "Run 'play.bat' to start the game" -ForegroundColor Cyan
Read-Host "Press Enter to continue"
