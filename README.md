# Uxintace
Type game android 16 Kotlin

1. app/src/main/java/com/example/uxintace/Cell.kt
2. app/src/main/java/com/example/uxintace/GameBoard.kt
3. app/src/main/java/com/example/uxintace/GameView.kt
4. app/src/main/java/com/example/uxintace/MainActivity.kt
5. app/src/main/res/layout/activity_main.xml
6. app/src/main/AndroidManifest.xml
7. app/build.gradle.kts
8. app/proguard-rules.pro
9. .github/workflows/build.yml (opcionális)

Uxintace/
├── .github/
│   └── workflows/
│       └── build.yml
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── example/
│   │   │   │           └── uxintace/
│   │   │   │               ├── Cell.kt
│   │   │   │               ├── GameBoard.kt
│   │   │   │               ├── GameView.kt
│   │   │   │               └── MainActivity.kt
│   │   │   ├── res/
│   │   │   │   └── layout/
│   │   │   │       └── activity_main.xml
│   │   │   └── AndroidManifest.xml
│   │   └── test/ (opcionális)
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle.kts (projekt szintű)
├── settings.gradle.kts
├── gradle.properties
├── local.properties (NE töltsd fel!)
└── .gitignore


.github/workflows/build.yml
app/src/main/java/com/example/uxintace/Cell.kt
app/src/main/java/com/example/uxintace/GameBoard.kt
app/src/main/java/com/example/uxintace/GameView.kt
app/src/main/java/com/example/uxintace/MainActivity.kt
app/src/main/res/layout/activity_main.xml
app/src/main/AndroidManifest.xml
app/build.gradle.kts
app/proguard-rules.pro
gradle/wrapper/gradle-wrapper.properties
build.gradle.kts
settings.gradle.kts
gradle.properties
.gitignore

# 🎮 UXINTACE – 2D színes kockás arcade játék

**Fejlesztő:** Fáber Sándor  
**Verzió:** 1.0  
**Platform:** Android (min. API 24)  
**Nyelv:** Kotlin  
**Licenc:** MIT  

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

