@echo off
echo ====================================
echo MongoDB Datenbank ansehen
echo ====================================
echo.

echo Verfuegbare Optionen:
echo 1. Alle Aerzte anzeigen
echo 2. Alle Patienten anzeigen
echo 3. Alle Behandlungen anzeigen
echo 4. Anzahl der Dokumente pro Collection
echo 5. MongoDB Shell oeffnen
echo.

set /p choice="Waehlen Sie eine Option (1-5): "

if "%choice%"=="1" (
    docker exec -it mongo mongosh patientenverwaltungdb --eval "db.aerzte.find().pretty()"
) else if "%choice%"=="2" (
    docker exec -it mongo mongosh patientenverwaltungdb --eval "db.patienten.find().pretty()"
) else if "%choice%"=="3" (
    docker exec -it mongo mongosh patientenverwaltungdb --eval "db.behandlungen.find().pretty()"
) else if "%choice%"=="4" (
    echo.
    echo Anzahl der Dokumente:
    echo ---------------------
    docker exec -it mongo mongosh patientenverwaltungdb --eval "print('Aerzte: ' + db.aerzte.countDocuments()); print('Patienten: ' + db.patienten.countDocuments()); print('Behandlungen: ' + db.behandlungen.countDocuments());"
) else if "%choice%"=="5" (
    echo Oeffne MongoDB Shell...
    echo Tipp: Verwenden Sie 'db.aerzte.find().pretty()' um Aerzte anzuzeigen
    docker exec -it mongo mongosh patientenverwaltungdb
) else (
    echo Ungueltige Auswahl!
)

pause

