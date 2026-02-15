# 🎮 UXINTACE – 2D színes kockás arcade játék

**Fejlesztő:** Fáber Sándor
**Verzió:** 1.0 
**Licenc:** MIT​
**Nyelv: Kotlin 1.9+**
​**SDK: Target SDK 35 (Android 16 kompatibilis) ​**
**Build System: Gradle 8.10.2 (Kotlin DSL - build.gradle.kts) ​CI/CD: GitHub Actions (Automatikus APK build minden commit után)**

---

## 📱 Játék leírása

Az **UXINTACE** egy egyedi 2D-s, Tetris-szerű játék, ahol a játékos egy kurzor segítségével irányíthatja a színes kockákat egy 60×60-as rácson.

### 🎯 Játékmenet

- A kockák **balról jobbra** haladnak időzítve.
- A játékos egy **kurzorral** (szürke keret) mozoghat a rácson.
- **🤚 FOG** gomb: megfoghatsz egy kockát, és áthelyezheted máshova (cserélheted vagy üres helyre rakhatod).
- **🔫 SHOT** gomb: 20 pontért kilőhetsz egy felesleges kockát.
- **4 azonos színű kocka** vízszintesen vagy függőlegesen **felrobban**, helyük üres lesz, és **+1 pont** jár érte.
- Ha egy kocka eléri a jobb szélső oszlopot és a következő lépésnél tovább kellene lépnie, **Game Over**.
- **6 szint** van, a sebesség nő, minden **10.000 pont** után szintlépés, **60.000 pontnál** visszaáll az 1. szintre.

### 🎨 Színek

- 5 különböző színű kocka
- Háttér: sötétszürke (elkülönül a kockáktól)
- Rács: rikító sárga
- Kurzor: vastag szürke keret

---

## 🕹️ Kezelés

A képernyő alján található gombokkal:

| Gomb | Funkció |
|------|---------|
| ⬆️ ⬇️ ⬅️ ➡️ | Kurzor mozgatása |
| 🤚 FOG | Kocka megfogása / elengedése |
| 🔫 SHOT | Kocka kilövése (20 pont) |
| ⏸️ | Szünet / folytatás |

---

## 📦 Telepítés

1. Töltsd le a legfrissebb APK-t a **GitHub Actions** buildjéből:  
   [Actions fül](https://github.com/FaberSandor/Uxintace/actions) → legutóbbi build → **Artifacts** → `app-debug.zip`
2. Csomagold ki, telepítsd az APK-t a telefonodra.
3. Engedélyezd az ismeretlen forrásból származó alkalmazások telepítését, ha szükséges.

---

## 🔧 Fordítás forráskódból

```bash
git clone https://github.com/FaberSandor/Uxintace.git
cd Uxintace
./gradlew assembleDebug

