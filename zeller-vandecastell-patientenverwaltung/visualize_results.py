#!/usr/bin/env python3
"""
MongoDB Performance Test Results Visualization
Basierend auf den durchgeführten Tests
"""

def format_time(ms):
    """Formatiert Zeit in ms"""
    if ms < 1:
        return f"{ms:.2f}ms"
    else:
        return f"{ms:.0f}ms"

def print_table():
    """Druckt die Performance-Ergebnisse in Tabellenform"""

    # MongoDB Ergebnisse aus den durchgeführten Tests
    results = [
        ("Create 10", 28),
        ("Create 100", 48),
        ("Create 1000", 126),
        ("Update", 122),
        ("Read Damage Filter With Projection", 80),
        ("Read Healthpoints Filter With Projection", 13),
        ("Read Fireresistance Filter With Projection", 15),
        ("Read Flying Filter With Projection", 2),
        ("Read Location Filter With Projection", 12),
        ("Read Reward Filter With Projection", 2),
        ("Read With Projection", 27),
        ("Aggregate Sort", 4),
        ("Aggregate Group By", 179),
        ("Aggregate Match", 31),
        ("Delete", 513),
    ]

    print("\n" + "=" * 70)
    print("MONGODB PERFORMANCE TEST RESULTS")
    print("=" * 70)
    print()

    separator = "+" + "-" * 47 + "+" + "-" * 20 + "+"
    header = f"| {'':45} | {'MongoDB':18} |"

    print(separator)
    print(header)
    print(separator)

    for operation, ms in results:
        time_str = format_time(ms)
        row = f"| {operation:45} | {time_str:18} |"
        print(row)
        print(separator)

    print()

if __name__ == "__main__":
    print_table()

