#!/bin/bash
# MongoDB Performance Test Runner (Unix/Linux/Mac)

echo "================================================================================"
echo "MongoDB Performance Test Runner"
echo "================================================================================"
echo ""

show_menu() {
    echo "Bitte wählen Sie einen Test:"
    echo ""
    echo "[1] Alle Performance-Tests ausführen"
    echo "[2] Nur CRUD-Operations Tests"
    echo "[3] Nur Index-Performance Tests"
    echo "[4] MongoDB starten (Docker)"
    echo "[5] MongoDB stoppen (Docker)"
    echo "[6] Test-Ergebnisse anzeigen"
    echo "[7] Datenbank leeren"
    echo "[0] Beenden"
    echo ""
    read -p "Ihre Wahl: " choice
}

run_all_tests() {
    echo ""
    echo "================================================================================"
    echo "Führe ALLE Performance-Tests aus..."
    echo "================================================================================"
    echo ""
    ./mvnw test -Dtest=MongoDBPerformanceTest,MongoDBIndexPerformanceTest
}

run_crud_tests() {
    echo ""
    echo "================================================================================"
    echo "Führe CRUD-Operations Tests aus..."
    echo "================================================================================"
    echo ""
    ./mvnw test -Dtest=MongoDBPerformanceTest
}

run_index_tests() {
    echo ""
    echo "================================================================================"
    echo "Führe Index-Performance Tests aus..."
    echo "================================================================================"
    echo ""
    ./mvnw test -Dtest=MongoDBIndexPerformanceTest
}

start_mongo() {
    echo ""
    echo "================================================================================"
    echo "Starte MongoDB mit Docker Compose..."
    echo "================================================================================"
    echo ""
    docker-compose up -d mongo
    echo ""
    echo "MongoDB läuft auf: mongodb://localhost:27017/patientenverwaltungdb"
    echo "Mongo Express UI: http://localhost:8081"
    echo ""
    read -p "Drücken Sie Enter zum Fortfahren..."
}

stop_mongo() {
    echo ""
    echo "================================================================================"
    echo "Stoppe MongoDB..."
    echo "================================================================================"
    echo ""
    docker-compose down
    echo ""
    read -p "Drücken Sie Enter zum Fortfahren..."
}

show_results() {
    echo ""
    echo "================================================================================"
    echo "Öffne Test-Ergebnisse..."
    echo "================================================================================"
    echo ""
    if [ -d "target/surefire-reports" ]; then
        ls -lh target/surefire-reports/
        echo ""
        echo "Test-Reports befinden sich in: target/surefire-reports/"
    else
        echo "Keine Test-Ergebnisse gefunden. Bitte führen Sie zuerst Tests aus."
    fi
    echo ""
    read -p "Drücken Sie Enter zum Fortfahren..."
}

clean_db() {
    echo ""
    echo "================================================================================"
    echo "Leere MongoDB Datenbank..."
    echo "================================================================================"
    echo ""
    echo "Warnung: Dies löscht alle Daten in der Datenbank!"
    read -p "Sind Sie sicher? (j/n): " confirm
    if [ "$confirm" = "j" ] || [ "$confirm" = "J" ]; then
        mongosh patientenverwaltungdb --eval "db.dropDatabase()"
        echo "Datenbank wurde geleert."
    else
        echo "Abgebrochen."
    fi
    echo ""
    read -p "Drücken Sie Enter zum Fortfahren..."
}

# Main loop
while true; do
    show_menu

    case $choice in
        1) run_all_tests ;;
        2) run_crud_tests ;;
        3) run_index_tests ;;
        4) start_mongo ;;
        5) stop_mongo ;;
        6) show_results ;;
        7) clean_db ;;
        0) echo ""; echo "Auf Wiedersehen!"; exit 0 ;;
        *) echo "Ungültige Auswahl. Bitte versuchen Sie es erneut." ;;
    esac
done

