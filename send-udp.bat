@echo off
setlocal enabledelayedexpansion

REM Check if both arguments are provided
if "%~1"=="" (
    echo Usage: %~n0 ^<message^> ^<port^>
    echo Example: %~n0 "sensor_id=h1; value=30" 3344
    exit /b 1
)

if "%~2"=="" (
    echo Error: Port number required
    echo Usage: %~n0 ^<message^> ^<port^>
    echo Example: %~n0 "sensor_id=1; value=30" 3344
    exit /b 1
)

set MESSAGE=%~1
set PORT=%~2
set HOST=localhost

REM Send UDP message using PowerShell
powershell -Command "$m='%MESSAGE%'; $p=%PORT%; $h='%HOST%'; $c=New-Object System.Net.Sockets.UdpClient; try { $c.Send([Text.Encoding]::ASCII.GetBytes($m), $m.Length, $h, $p); Write-Host 'Sent: '$m' to '$h':'$p -ForegroundColor Green } catch { Write-Host 'Error: '$_ -ForegroundColor Red } finally { $c.Close() }"

endlocal