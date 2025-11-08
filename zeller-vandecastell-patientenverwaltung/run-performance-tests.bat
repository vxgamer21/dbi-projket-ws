@echo off
REM Performance Test Runner für MongoDB
REM Führt verschiedene Performance-Tests aus

echo ================================================================================
echo MongoDB Performance Test Runner
echo ================================================================================
echo.

:menu
echo Bitte wählen Sie einen Test:
echo.
echo [1] Alle Performance-Tests ausführen
echo [2] Nur CRUD-Operations Tests
echo [3] Nur Index-Performance Tests
echo [4] MongoDB starten (Docker)
echo [5] MongoDB stoppen (Docker)
echo [6] Test-Ergebnisse anzeigen
echo [7] Datenbank leeren
echo [0] Beenden
echo.
set /p choice="Ihre Wahl: "

if "%choice%"=="1" goto all_tests
if "%choice%"=="2" goto crud_tests
if "%choice%"=="3" goto index_tests
if "%choice%"=="4" goto start_mongo
if "%choice%"=="5" goto stop_mongo
if "%choice%"=="6" goto show_results
if "%choice%"=="7" goto clean_db
if "%choice%"=="0" goto end
goto menu

:all_tests
echo.
echo ================================================================================
echo Führe ALLE Performance-Tests aus...
echo ================================================================================
echo.
call mvn test -Dtest=MongoDBPerformanceTest,MongoDBIndexPerformanceTest
goto menu

:crud_tests
echo.
echo ================================================================================
echo Führe CRUD-Operations Tests aus...
echo ================================================================================
echo.
call mvn test -Dtest=MongoDBPerformanceTest
goto menu

:index_tests
echo.
echo ================================================================================
echo Führe Index-Performance Tests aus...
echo ================================================================================
echo.
call mvn test -Dtest=MongoDBIndexPerformanceTest
goto menu

:start_mongo
echo.
echo ================================================================================
echo Starte MongoDB mit Docker Compose...
echo ================================================================================
echo.
call docker-compose up -d mongo
echo.
echo MongoDB läuft auf: mongodb://localhost:27017/patientenverwaltungdb
echo Mongo Express UI: http://localhost:8081
echo.
pause
goto menu

:stop_mongo
echo.
echo ================================================================================
echo Stoppe MongoDB...
echo ================================================================================
echo.
call docker-compose down
echo.
pause
goto menu

:show_results
echo.
echo ================================================================================
echo Öffne Test-Ergebnisse...
echo ================================================================================
echo.
if exist "target\surefire-reports\" (
    start target\surefire-reports\
) else (
    echo Keine Test-Ergebnisse gefunden. Bitte führen Sie zuerst Tests aus.
)
pause
goto menu

:clean_db
echo.
echo ================================================================================
echo Leere MongoDB Datenbank...
echo ================================================================================
echo.
echo Warnung: Dies löscht alle Daten in der Datenbank!
set /p confirm="Sind Sie sicher? (j/n): "
if /i "%confirm%"=="j" (
    echo db.dropDatabase() | mongosh patientenverwaltungdb
    echo Datenbank wurde geleert.
) else (
    echo Abgebrochen.
)
pause
goto menu

:end
echo.
echo Auf Wiedersehen!
exit /b 0

