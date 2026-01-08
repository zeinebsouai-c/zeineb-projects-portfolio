# Beleg PZR2 (94)
Checkboxen gemäß Bearbeitung befüllen und _kursiv_ gesetzten Text durch entsprechende Angaben ersetzten.
Bei keiner Angabe wird nur Entwurf, Tests (ohne Testabdeckung Rest), Fehlerfreiheit und Basisfunktionalität bewertet.
Die Zahl in der Klammer sind die jeweiligen Punkte für die Bewertung.
Die empfohlenen Realisierungen sind **fett** gesetzt und ergeben 50 Punkte.
Ergänzende Anmerkungen bitte immer _kursiv_ setzen. Andere Änderungen sind nicht zulässig.

## Vorrausetzungen für das Bestehen
- [ ] Quellen angegeben
- [x] zip Archiv mit dem Projekt im root
- [x] IntelliJ-Projekt (kein Gradle, Maven o.ä.)
- [x] keine weiteren Bibliotheken außer JUnit5, Mockito und JavaFX (und deren Abhängigkeiten)
- [x] keine Umlaute, Sonderzeichen, etc. in Datei- und Pfadnamen
- [x] Trennung zwischen Test- und Produktiv-Code
- [x] kompilierbar
- [x] geforderte main-Methoden nur im default package des module belegProg3
- [ ] keine vorgetäuschte Funktionalität (inkl. leere Tests)

## Entwurf (10)
- [x] **Benennung** (2)
- [x] **Zuständigkeit** (2)
- [x] **Paketierung** (2)
- [x] Schichtenaufteilung (via modules) (2)
- [ ] Architekturdiagramm (1)
- [ ] keine Duplikate (1)

## Tests (34)
- [ ] **Testqualität** (9)
- [ ] **Testabdeckung GL (inkl. Abhängigkeiten)** (9) _Abdeckung in Prozent angeben_
- [ ] Testabdeckung Rest (6)
  - [ ] Einfügen von Produzent*innen über das CLI _Tests angeben_
  - [ ] Anzeigen von Produzent*innen über das CLI _Tests angeben_
  - [ ] ein Beobachter _Tests angeben_
  - [ ] deterministische Funktionalität der Simulationen _Tests angeben_
  - [ ] Speichern via JOS oder JBP _Tests angeben_
  - [ ] Laden via JOS oder JBP _Tests angeben_
- [ ] **mindestens 5 Unittests, die Mockito verwenden** (5)
- [ ] mindestens 4 Spy- / Verhaltens-Tests (4)
- [ ] **keine unbeabsichtigt fehlschlagenden Test** (1)

## Fehlerfreiheit (10)
- [ ] **Kapselung** (5)
- [x] **keine Ablauffehler** (5)

## Basisfunktionalität (10)
- [x] **CRUD** (2)
- [x] **CLI** (2)
  * Syntax gemäß Anforderungen
- [x] **Simulation** (2)
  * ohne race conditions, ohne sleep
- [x] **I/O** (2)
- [] **Net** (2)

## Funktionalität (20)
- [x] vollständige GL (2)
- [x] threadsichere GL (1)
- [x] vollständiges CLI (2)
- [x] alternatives CLI (1)
  * _angeben welche Funktionalität im alternativen CLI deaktiviert_
- [x] ausdifferenziertes event-System mit mindestens 3 events (2)
- [x] observer (2)
- [x] angemessene Aufzählungstypen (2)
- [x] Simulation 2 (1)
- [x] Simulation 3 (1)
- [x] sowohl JBP als auch JOS (2)
- [ ] sowohl TCP als auch UDP (2)
- [ ] Server unterstützt konkurierende Clients für TCP oder UDP (2)

## zusätzliche Anforderungen GUI (5)
- [x] Basis-GUI (CRUD) (1)
- [x] skalierbare GUI (1)
- [x] vollständige GUI (1)
- [x] Sortierung in der GUI (1)
- [x] data binding verwendet (1)

## zusätzliche Anforderungen (5)
gemeinsame Produzentenverwaltung: _ja/nein_
- [ ] Unterstützung mehrerer Instanzen (2)
- [ ] Ansteuerung per CLI (2)
- [ ] An- und Abschalten zur Laufzeit (1)

