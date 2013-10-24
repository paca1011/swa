-- ===============================================================================
-- Jede SQL-Anweisung muss in genau 1 Zeile
-- Kommentare durch -- am Zeilenanfang
-- ===============================================================================

--
-- kunde
--
INSERT INTO kunde (id, version, nachname, vorname, geschlecht, email, passwort, erzeugt, aktualisiert) VALUES (101, 0, 'Admin','Admin','m','admin@web.de','admin','01.08.2006 00:00:00','01.08.2006 00:00:00');
INSERT INTO kunde (id, version, nachname, vorname, geschlecht, email, passwort, erzeugt, aktualisiert) VALUES (102, 0, 'MÃ¼ller','Hans','m','hans@web.de','broetchen','02.08.2006 00:00:00','02.08.2006 00:00:00');
INSERT INTO kunde (id, version, nachname, vorname, geschlecht, email, passwort, erzeugt, aktualisiert) VALUES (103, 0, 'Reinhard','Elisabeth','w','elli@web.de','prinzpi','03.08.2006 00:00:00','03.08.2006 00:00:00');
INSERT INTO kunde (id, version, nachname, vorname, geschlecht, email, passwort, erzeugt, aktualisiert) VALUES (104, 0, 'Maier','Elisabeth','w','elli2@web.de','prinzpi','04.08.2006 00:00:00','04.08.2006 00:00:00');
INSERT INTO kunde (id, version, nachname, vorname, geschlecht, email, passwort, erzeugt, aktualisiert) VALUES (105, 0, 'Frei','Elisabeth','w','elli3@web.de','prinzpi','05.08.2006 00:00:00','05.08.2006 00:00:00');

--
-- file_tbl
--
-- Die eigene Stored Procedure "insert_file_kunde" fuegt in die Tabelle file_tbl eine Zeile bzw. einen Datensatz ein,
-- der u.a. eine Datei enthaelt 
-- CALL insert_file_kunde(101,1,0,'image.png','Privatkunde_101.png','png','I','01.01.2007 01:00:00','01.01.2007 01:00:00');
-- CALL insert_file_kunde(102,2,0,'video.mp4','Privatkunde_102.mp4','mp4','V','01.01.2007 01:00:00','01.01.2007 01:00:00');

--
-- kunde_rolle
--
-- INSERT INTO kunde_rolle (kunde_fk, rolle) VALUES (1,'admin');
-- INSERT INTO kunde_rolle (kunde_fk, rolle) VALUES (1,'mitarbeiter');
-- INSERT INTO kunde_rolle (kunde_fk, rolle) VALUES (1,'abteilungsleiter');
-- INSERT INTO kunde_rolle (kunde_fk, rolle) VALUES (1,'kunde');
-- INSERT INTO kunde_rolle (kunde_fk, rolle) VALUES (101,'admin');
-- INSERT INTO kunde_rolle (kunde_fk, rolle) VALUES (101,'mitarbeiter');
-- INSERT INTO kunde_rolle (kunde_fk, rolle) VALUES (101,'kunde');
-- INSERT INTO kunde_rolle (kunde_fk, rolle) VALUES (102,'mitarbeiter');
-- INSERT INTO kunde_rolle (kunde_fk, rolle) VALUES (102,'kunde');
-- INSERT INTO kunde_rolle (kunde_fk, rolle) VALUES (103,'mitarbeiter');
-- INSERT INTO kunde_rolle (kunde_fk, rolle) VALUES (103,'kunde');
-- INSERT INTO kunde_rolle (kunde_fk, rolle) VALUES (104,'kunde');

--
-- adresse
--
INSERT INTO adresse (id, plz, stadt, strasse, hausnum, kunde_fk, lieferant_fk, erzeugt, aktualisiert) VALUES (200,'76133','Karlsruhe','Moltkestrasse','30',101,null,'01.08.2006 00:00:00','01.08.2006 00:00:00');
INSERT INTO adresse (id, plz, stadt, strasse, hausnum, kunde_fk, lieferant_fk, erzeugt, aktualisiert) VALUES (201,'76133','Karlsruhe','Moltkestrasse','31',102,null,'02.08.2006 00:00:00','02.08.2006 00:00:00');
INSERT INTO adresse (id, plz, stadt, strasse, hausnum, kunde_fk, lieferant_fk, erzeugt, aktualisiert) VALUES (202,'69115','Heidelberg','Belfortstrasse','15',103,null,'03.08.2006 00:00:00','03.08.2006 00:00:00');
INSERT INTO adresse (id, plz, stadt, strasse, hausnum, kunde_fk, lieferant_fk, erzeugt, aktualisiert) VALUES (203,'69115','Heidelberg','Belfortstrasse','15',104,null,'03.08.2006 00:00:00','03.08.2006 00:00:00');
INSERT INTO adresse (id, plz, stadt, strasse, hausnum, kunde_fk, lieferant_fk, erzeugt, aktualisiert) VALUES (204,'69115','Heidelberg','Belfortstrasse','15',105,null,'03.08.2006 00:00:00','03.08.2006 00:00:00');
INSERT INTO adresse (id, plz, stadt, strasse, hausnum, kunde_fk, lieferant_fk, erzeugt, aktualisiert) VALUES (205,'69117','lieferantenstadt','lieferstrasse','15',null,600,'03.08.2006 00:00:00','03.08.2006 00:00:00');
INSERT INTO adresse (id, plz, stadt, strasse, hausnum, kunde_fk, lieferant_fk, erzeugt, aktualisiert) VALUES (206,'69117','lieferantenstadt','lieferstrasse','16',null,601,'03.08.2006 00:00:00','03.08.2006 00:00:00');
INSERT INTO adresse (id, plz, stadt, strasse, hausnum, kunde_fk, lieferant_fk, erzeugt, aktualisiert) VALUES (207,'69117','lieferantenstadt','lieferstrasse','17',null,602,'03.08.2006 00:00:00','03.08.2006 00:00:00');

--
-- wartungsvertrag
--
-- INSERT INTO wartungsvertrag (nr, datum, version, inhalt, kunde_fk, idx, erzeugt, aktualisiert) VALUES (1,'31.01.2005',0,'Wartungsvertrag_1_K1',101,0,'01.01.2007 01:00:00','01.01.2007 01:00:00');
-- INSERT INTO wartungsvertrag (nr, datum, version, inhalt, kunde_fk, idx, erzeugt, aktualisiert) VALUES (2,'31.01.2006',0,'Wartungsvertrag_2_K1',101,1,'01.01.2007 02:00:00','01.01.2007 02:00:00');
-- INSERT INTO wartungsvertrag (nr, datum, version, inhalt, kunde_fk, idx, erzeugt, aktualisiert) VALUES (1,'30.06.2006',0,'Wartungsvertrag_1_K2',102,0,'01.01.2007 03:00:00','01.01.2007 03:00:00');

--
-- artikel
--
INSERT INTO artikel (id, bezeichnung, farbe, preisKunde, preisLieferant, bestand, erzeugt, aktualisiert) VALUES (300,'Tisch ''Oval''','Rot',80,70,5,'01.08.2006 00:00:00','01.08.2006 00:00:00');
INSERT INTO artikel (id, bezeichnung, farbe, preisKunde, preisLieferant, bestand, erzeugt, aktualisiert) VALUES (301,'Stuhl ''Sitz bequem''','Weiss',10,8,1,'02.08.2006 00:00:00','02.08.2006 00:00:00');
INSERT INTO artikel (id, bezeichnung, farbe, preisKunde, preisLieferant, bestand, erzeugt, aktualisiert) VALUES (302,'Tür ''Hoch und breit''','Braun',300,250,1,'03.08.2006 00:00:00','03.08.2006 00:00:00');
INSERT INTO artikel (id, bezeichnung, farbe, preisKunde, preisLieferant, bestand, erzeugt, aktualisiert) VALUES (303,'Fenster ''Glasklar''','Weiss',150,100,2,'04.08.2006 00:00:00','04.08.2006 00:00:00');
INSERT INTO artikel (id, bezeichnung, farbe, preisKunde, preisLieferant, bestand, erzeugt, aktualisiert) VALUES (304,'Spiegel ''Mach mich schöner''','Silber',60,40,1,'05.08.2006 00:00:00','05.08.2006 00:00:00');
INSERT INTO artikel (id, bezeichnung, farbe, preisKunde, preisLieferant, bestand, erzeugt, aktualisiert) VALUES (305,'Kleiderschrank ''Viel Platz''','Braun',500,400,1,'06.08.2006 00:00:00','06.08.2006 00:00:00');
--
-- bestellung
--
INSERT INTO bestellung (id, version, status, gesamtpreis, ausgeliefert, kunde_fk, lieferant_fk, idx, erzeugt, aktualisiert) VALUES (400, 0, 'in_bearbeitung',10,0,101, 600,0,'01.08.2006 00:00:00','01.08.2006 00:00:00');
INSERT INTO bestellung (id, version, status, gesamtpreis, ausgeliefert, kunde_fk, lieferant_fk, idx, erzeugt, aktualisiert) VALUES (401, 0, 'in_bearbeitung',10,0,102, 600, 1,'02.08.2006 00:00:00','02.08.2006 00:00:00');
INSERT INTO bestellung (id, version, status, gesamtpreis, ausgeliefert, kunde_fk, lieferant_fk, idx, erzeugt, aktualisiert) VALUES (402, 0, 'in_bearbeitung',10,0,103, 601, 0,'03.08.2006 00:00:00','03.08.2006 00:00:00');
INSERT INTO bestellung (id, version, status, gesamtpreis, ausgeliefert, kunde_fk, lieferant_fk, idx, erzeugt, aktualisiert) VALUES (403, 0, 'in_bearbeitung',10,0,104, 601, 1,'04.08.2006 00:00:00','04.08.2006 00:00:00');
INSERT INTO bestellung (id, version, status, gesamtpreis, ausgeliefert, kunde_fk, lieferant_fk, idx, erzeugt, aktualisiert) VALUES (404, 0, 'in_bearbeitung',10,0,105, 602, 0,'05.08.2006 00:00:00','05.08.2006 00:00:00');

--
-- bestellposition
--
INSERT INTO posten (id, verson, bestellung_fk, artikel_fk, anzahl, idx) VALUES (500, 0, 400,300,1,0);
INSERT INTO posten (id, verson, bestellung_fk, artikel_fk, anzahl, idx) VALUES (501, 0, 400,301,4,1);
INSERT INTO posten (id, verson, bestellung_fk, artikel_fk, anzahl, idx) VALUES (502, 0, 401,302,5,0);
INSERT INTO posten (id, verson, bestellung_fk, artikel_fk, anzahl, idx) VALUES (503, 0, 402,303,3,0);
INSERT INTO posten (id, verson, bestellung_fk, artikel_fk, anzahl, idx) VALUES (504, 0, 402,304,2,1);
INSERT INTO posten (id, verson, bestellung_fk, artikel_fk, anzahl, idx) VALUES (505, 0, 403,305,1,0);
INSERT INTO posten (id, verson, bestellung_fk, artikel_fk, anzahl, idx) VALUES (506, 0, 404,300,5,0);
INSERT INTO posten (id, verson, bestellung_fk, artikel_fk, anzahl, idx) VALUES (507, 0, 404,301,2,1);
INSERT INTO posten (id, verson, bestellung_fk, artikel_fk, anzahl, idx) VALUES (508, 0, 404,302,8,2);

--
-- lieferant
--

INSERT INTO lieferant (id, version, name, telefonnum, erzeugt, aktualisiert) VALUES (600, 0, 'DDS',94345346,'01.08.2006 00:00:00','01.08.2006 00:00:00');
INSERT INTO lieferant (id, version, name, telefonnum, erzeugt, aktualisiert) VALUES (601, 0, 'POS',34535455,'01.08.2006 00:00:00','01.08.2006 00:00:00');
INSERT INTO lieferant (id, version, name, telefonnum, erzeugt, aktualisiert) VALUES (602, 0, 'GGH',45656566,'01.08.2006 00:00:00','01.08.2006 00:00:00');
--
-- bestellung_lieferung

-- INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (400,600);
-- INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (401,600);
-- INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (402,601);
-- INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (402,602);
-- INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (403,602);
-- INSERT INTO bestellung_lieferung (bestellung_fk, lieferung_fk) VALUES (410,603);
