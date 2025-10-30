INSERT INTO arzt(id, art, fachgebiet, geb_datum, lkennzahl, okennzahl, plz, haus_nr, svnr, rufnummer, stadt, strasse, email, name)
VALUES (nextval('arzt_seq'), 'M', 'A', '1970-01-01', '123', '1234', '1234', '1234', 1234567890, '1234567890', 'Wien', 'Teststraße 1', 'test@test.at', 'Testarzt');

INSERT INTO arztpraxen(id, art, ist_kassenarzt, lkennzahl, okennzahl, plz, haus_nr, rufnummer, stadt, strasse, name, arzt_id)
VALUES (nextval('arztpraxen_seq'), 'A', true, '123', '1234', '1234', '1234', '1234567890', 'Wien', 'Teststraße 1', 'Testpraxis', currval('arzt_seq'));