@echo off
REM ====================================================================
REM SQL Performance Test Runner
REM ====================================================================

echo.
echo ====================================================================
echo   SQL Performance Tests - Relationale Datenbank
echo ====================================================================
echo.

REM Überprüfe ob PostgreSQL läuft
echo [1/3] Überprüfe Datenbankverbindung...
timeout /t 2 /nobreak >nul

REM Führe die Tests aus
echo [2/3] Führe Performance Tests aus...
echo.

call mvnw.cmd clean test -Dtest=SQLPerformanceTest -DfailIfNoTests=false

echo.
echo [3/3] Tests abgeschlossen!
echo.

REM Prüfe Exit Code
if %ERRORLEVEL% EQU 0 (
    echo ====================================================================
    echo   ERFOLG: Alle Tests bestanden!
    echo ====================================================================
) else (
    echo ====================================================================
    echo   FEHLER: Tests fehlgeschlagen! Bitte Logs prüfen.
    echo ====================================================================
)

echo.
echo Test-Reports: target\surefire-reports\
echo.

pause

