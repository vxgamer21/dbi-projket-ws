@echo off
echo ====================================
echo MongoDB Datenbank - Schnellansicht
echo ====================================
echo.

echo Anzahl der Dokumente:
echo ---------------------
docker exec mongo mongosh patientenverwaltungdb --quiet --eval "print('Aerzte: ' + db.aerzte.countDocuments()); print('Patienten: ' + db.patienten.countDocuments()); print('Behandlungen: ' + db.behandlungen.countDocuments());"
echo.

echo Beispiel-Arzt (erster Eintrag):
echo --------------------------------
docker exec mongo mongosh patientenverwaltungdb --quiet --eval "db.aerzte.findOne()"
echo.

echo.
echo Moechten Sie alle Aerzte sehen? (Dies kann viel Output erzeugen bei 100 Aerzten)
set /p showAll="Alle Aerzte anzeigen? (j/n): "

if /i "%showAll%"=="j" (
    docker exec mongo mongosh patientenverwaltungdb --quiet --eval "db.aerzte.find().forEach(printjson)"
)

pause

