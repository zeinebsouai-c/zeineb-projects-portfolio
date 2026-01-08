# Fernueberwachung_Patienten
Dieses Repository enthält das Projekt Patientenüberwachungssystem, das im Rahmen des Studiengangs Informatik in Kultur und Gesundheit an der HTW Berlin im Kurs Medizinische Informationssysteme entwickelt wird. Das Projekt wird von einem Team aus drei Studierenden umgesetzt.
## Ziel des Projekts
Ziel des Projekts ist die Entwicklung eines Systems zur Patientenüberwachung, das speziell für den Einsatz in Krankenhäusern konzipiert ist. Das System soll Pflegekräften die Möglichkeit bieten, medizinische Daten von Patienten zentral einzusehen und persönliche Informationen sowie detaillierte medizinische Statusdaten in einer Einzelansicht abzurufen.

## Technische Umsetzung
- Hauptprogrammiersprache: Java
- Geplanter Einsatzbereich: Krankenhäuser und Pflegeeinrichtungen

## Projektabhängigkeiten

Dieses Projekt ist modular aufgebaut und benötigt mehrere interne und externe Abhängigkeiten. Um die **JavaFX-GUI** und andere Komponenten erfolgreich auszuführen, stellen Sie bitte sicher, dass Sie Folgendes installiert und korrekt in Ihrer Entwicklungsumgebung eingebunden haben.

---

### **Erforderliche JavaFX-Module (für die GUI-Funktionalität)**

Installieren Sie OpenJFX 19 zusammen mit Java SDK 19 und binden Sie die folgenden JavaFX-Module zur Laufzeit und beim Build ein:

* `javafx.base`
* `javafx.controls`
* `javafx.fxml`
* `javafx.graphics`

JavaFX SDK herunterladen: [https://openjfx.io/](https://openjfx.io/)

---

### **Projektweite Module**

Diese internen Module werden im gesamten Projekt verwendet und müssen eingebunden werden:

* `corelogic`
* `eventsystem`
* `helperfunctions`
* `services`

---

### **Modulspezifische Abhängigkeiten**

#### 1. `GUI`-Modul

**Benötigte Abhängigkeiten:**

* `openjfx.javafx.base`
* `openjfx.javafx.controls`
* `openjfx.javafx.fxml`
* `openjfx.javafx.graphics`
* `lib` (lokale Bibliotheken, falls vorhanden)
* `corelogic`
* `services`

#### 2. `corelogic`-Modul

**Benötigte Abhängigkeiten:**

* `helperfunctions`
* `services`

#### 3. `eventsystem`-Modul

**Benötigte Abhängigkeiten:**

* `corelogic`
* `helperfunctions`
* `services`

#### 4. `Fernueberwachung_Patienten`-Modul

**Benötigte Abhängigkeiten:**

* `openjfx.javafx.controls`
* `openjfx.javafx.fxml`
* `openjfx.javafx.graphics`
* `openjfx.javafx.base`

#### 5. `helperfunctions`-Modul

**Benötigte Abhängigkeiten:**

* `corelogic`
* `eventsystem`
* `Fernueberwachung_Patienten`

#### 6. `services`-Modul

**Benötigte Abhängigkeiten:**

* `corelogic`
* `helperfunctions`
* `eventsystem`

#### 7. `src`-Modul (Root-Modul)

**Benötigte Abhängigkeiten:**

* `corelogic`
* `eventsystem`
* `Fernueberwachung_Patienten`
* `helperfunctions`
* `services`

---

