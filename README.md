<div align="center">

<pre>
 ██████╗  █████╗  ██████╗    ███╗   ███╗ █████╗ ███╗   ██╗
 ██╔══██╗██╔══██╗██╔════╝    ████╗ ████║██╔══██╗████╗  ██║
 ██████╔╝███████║██║         ██╔████╔██║███████║██╔██╗ ██║
 ██╔═══╝ ██╔══██║██║         ██║╚██╔╝██║██╔══██║██║╚██╗██║
 ██║     ██║  ██║╚██████╗    ██║ ╚═╝ ██║██║  ██║██║ ╚████║
 ╚═╝     ╚═╝  ╚═╝ ╚═════╝   ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝
</pre>

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Playable-brightgreen?style=for-the-badge)
![Levels](https://img.shields.io/badge/Levels-3-yellow?style=for-the-badge)

**Three levels. Six ghosts. One final boss. Waka waka!**

[Get Started](#getting-started) · [How to Play](#how-to-play) · [Screenshots](#screenshots) · [Contribute](#contributing) · [Report a Bug](https://github.com/jawadashraf000/PacMan-Game-in-Java/issues)

</div>

---

## Overview

This is a complete, fully playable, multi-level recreation of the iconic **1980 Namco arcade game Pac-Man**, built entirely from scratch in Java using the Swing GUI framework. No external game engines. No third-party libraries. Just four Java classes, a tile-based maze, a sound system, and a game that genuinely escalates in challenge across three distinct levels.

What sets this apart from a typical student project is the depth of its implementation: a **fade-in start screen** with logo animation, a **full sound engine** with looping siren and positional audio, **bonus collectibles** (cherries and strawberries), **floating score popups**, **persistent high score storage**, and a **boss ghost** that breaks all the rules by tracking Pac-Man directly with vector-based movement — no grid, no randomness, just relentless pursuit.

It is built to be played and studied.

---

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Screenshots](#screenshots)
- [Getting Started](#getting-started)
- [How to Play](#how-to-play)
- [Level Progression](#level-progression)
- [Ghost Roster](#ghost-roster)
- [Scoring System](#scoring-system)
- [Sound System](#sound-system)
- [Game Mechanics](#game-mechanics)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [Author](#author)

---

## Features

```
  ╔══════════════════════════════════════════════════════════════╗
  ║  WHAT THIS GAME ACTUALLY DOES                                ║
  ╠══════════════════════════════════════════════════════════════╣
  ║  Animated fade-in start screen with logo and sound           ║
  ║  Three progressively harder levels on a 35x17 tile maze      ║
  ║  Six unique ghost enemies with distinct sprites              ║
  ║  A boss ghost (Final Ghost) with direct vector tracking      ║
  ║  Cherries (+50) and Strawberries (+100) as bonus items       ║
  ║  Floating "+50" / "+100" score popups on collection          ║
  ║  Full sound engine: waka, siren, death, start, bonus         ║
  ║  Looping ambient siren that reacts to game state             ║
  ║  Tunnel wrapping on the left and right maze edges            ║
  ║  Directional Pac-Man sprites (up, down, left, right)         ║
  ║  Queued input — buffer your next turn before the corner      ║
  ║  Pause / resume with Spacebar at any time                    ║
  ║  Persistent high score saved to highscore.txt                ║
  ║  Instant restart after game over with Enter key              ║
  ╚══════════════════════════════════════════════════════════════╝
```

---

## Architecture

The game is composed of exactly **4 classes**, each with a clear, single responsibility:

| Class | Role |
|---|---|
| `App.java` | Entry point. Defines board constants, creates the `JFrame`, launches `StartScreen`, then swaps in `PacMan` on completion. |
| `StartScreen.java` | Animated logo screen. Fades the logo image in and out using `AlphaComposite`, then invokes a callback to start the game. |
| `PacMan.java` | The entire game engine. Handles the tile map, all entities (`Block`), movement, collision, AI, scoring, sound coordination, level transitions, and rendering. |
| `Sound.java` | Static sound manager. Pre-loads all `.wav` clips into a `HashMap` and exposes `play()`, `loop()`, and `stop()` — called from anywhere in the codebase. |

---

## Tech Stack

| Layer | Technology | Purpose |
|---|---|---|
| Language | Java (JDK 8+) | Core application logic |
| GUI Framework | Java Swing | Window management and event handling |
| Rendering Engine | Java AWT Graphics2D | Custom 2D maze and sprite painting |
| Game Loop | javax.swing.Timer | Frame-by-frame update at 50ms tick |
| Audio | javax.sound.sampled | Clip-based WAV playback and looping |
| Input Handling | KeyListener / KeyAdapter | Real-time keyboard controls |
| Persistence | Scanner / PrintWriter | High score read/write via highscore.txt |
| Asset Loading | getClass().getResource() | Images and sounds loaded from classpath |

Zero external dependencies. Runs on any machine with Java 8 or higher.

---

## Project Structure

```
PacMan-Game-in-Java/
│
├── App.java                      ← Entry point, board constants, JFrame setup
├── StartScreen.java              ← Animated logo fade-in/out screen
├── PacMan.java                   ← Game engine, maze, entities, AI, rendering
├── Sound.java                    ← Static sound manager for all audio clips
│
├── wall.png                      ← Maze wall tile sprite
├── pacmanRight.png               ← Pac-Man facing right
├── pacmanLeft.png                ← Pac-Man facing left
├── pacmanUp.png                  ← Pac-Man facing up
├── pacmanDown.png                ← Pac-Man facing down
├── redGhost.png                  ← Red ghost sprite
├── blueGhost.png                 ← Blue ghost sprite
├── orangeGhost.png               ← Orange ghost sprite
├── pinkGhost.png                 ← Pink ghost sprite
├── greenGhost.png                ← Green ghost sprite (Level 3 only)
├── purpleGhost.png               ← Purple ghost sprite (Level 3 only)
├── finalGhost.png                ← Boss ghost sprite (Levels 2 & 3)
├── cherry.png                    ← Cherry bonus item (+50 pts)
├── strawberry.png                ← Strawberry bonus item (+100 pts)
├── logo.png                      ← Start screen logo
│
├── sounds/
│   ├── logo.wav                  ← Plays during start screen fade
│   ├── start.wav                 ← Plays at game/level/life start
│   ├── siren.wav                 ← Ambient loop during active gameplay
│   ├── waka.wav                  ← Plays on every pellet eaten
│   ├── death.wav                 ← Plays on Pac-Man death
│   └── bonus.wav                 ← Plays on cherry or strawberry pickup
│
└── highscore.txt                 ← Auto-generated on first high score save
```

---

## Screenshots


| Start Screen | Gameplay | Game Over |
|---|---|---|
| ![Start Screen](screenshots/startScreen.png) | ![Gameplay](screenshots/gamePlay.png) | ![Game Over](screenshots/gameOver.png) |

---

## Getting Started

### Prerequisites

You need **Java JDK 8 or higher** installed. Verify with:

```bash
java -version
```

Download the JDK at [adoptium.net](https://adoptium.net) if needed.

---

### Installation

```bash
git clone https://github.com/jawadashraf000/PacMan-Game-in-Java.git
cd PacMan-Game-in-Java
```

---

### Running the Game

**Option 1 — IDE (Recommended)**

1. Open the project in IntelliJ IDEA, Eclipse, or NetBeans
2. Run `App.java` — it is the entry point
3. The start screen appears immediately with the logo animation

**Option 2 — Terminal**

```bash
# Compile all four classes
javac *.java

# Run the game
java App
```

> All image and sound assets must be in the same directory as the compiled classes, with the `sounds/` folder preserved as a subdirectory.

---

## How to Play

```
  ┌───────────────────────────────────────────────────┐
  │                   CONTROLS                        │
  ├──────────────────────┬────────────────────────────┤
  │  Arrow Up            │  Move Pac-Man Up           │
  │  Arrow Down          │  Move Pac-Man Down         │
  │  Arrow Left          │  Move Pac-Man Left         │
  │  Arrow Right         │  Move Pac-Man Right        │
  │  Spacebar            │  Pause / Resume            │
  │  Enter               │  Restart after Game Over   │
  └──────────────────────┴────────────────────────────┘
```

**Pro tip — Queued Input:** You do not need to be perfectly aligned to turn. The game buffers your next direction input and applies it the moment Pac-Man reaches a valid turning tile. Tap your next direction early to take corners smoothly without losing speed.

**Objective:** Eat every white pellet on the board to advance to the next level. Avoid ghosts, collect bonus items for extra points, and survive all three levels.

---

## Level Progression

The game features **3 levels**, each loading the same maze layout but escalating the threat significantly.

```
  ┌────────────┬──────────────────────────────────────────────────┐
  │  Level 1   │  4 ghosts (red, blue, orange, pink)              │
  │            │  Standard ghost AI — random direction on walls   │
  ├────────────┼──────────────────────────────────────────────────┤
  │  Level 2   │  4 ghosts + Final Ghost introduced               │
  │            │  Final Ghost tracks Pac-Man at speed 4 px/tick   │
  │            │  No walls stop the Final Ghost — open pursuit    │
  ├────────────┼──────────────────────────────────────────────────┤
  │  Level 3   │  6 ghosts (green + purple added) + Final Ghost   │
  │            │  Final Ghost speed increases to 6 px/tick        │
  │            │  Final Ghost is larger than standard ghosts      │
  └────────────┴──────────────────────────────────────────────────┘
```

When a level is cleared, a **"Level X starts!"** message appears centered on screen, the siren pauses, and a brief delay gives you time to prepare before enemies begin moving again.

---

## Ghost Roster

```
  STANDARD GHOSTS — Grid-based random movement, wall-bouncing AI
  ═══════════════════════════════════════════════════════════════
  RED     — Present from Level 1. First ghost placed in the map.
  BLUE    — Present from Level 1. Placed beside red at start.
  ORANGE  — Present from Level 1. Placed beside blue at start.
  PINK    — Present from Level 1. Placed beside orange at start.
  GREEN   — Joins in Level 3. Starts at a fixed upper position.
  PURPLE  — Joins in Level 3. Starts beside green.

  FINAL GHOST — Vector-tracking boss, present from Level 2
  ═══════════════════════════════════════════════════════════════
  Larger sprite (44x44 px vs 32x32 for standard ghosts).
  Ignores walls entirely — moves through open space directly
  toward Pac-Man using normalized vector math each tick.
  Speed: 4 px/tick on Level 2, 6 px/tick on Level 3.
  Respects death and restart pauses — gives you a fair window.
```

All standard ghosts also respect the **side corridors** of the maze (rows 7–9), where they — and Pac-Man — can tunnel off one edge of the screen and reappear on the other.

---

## Scoring System

```
  ┌──────────────────────────────────┬───────────────┐
  │  Action                          │  Points       │
  ├──────────────────────────────────┼───────────────┤
  │  Eat a pellet                    │  +10          │
  │  Collect a Cherry                │  +50          │
  │  Collect a Strawberry            │  +100         │
  └──────────────────────────────────┴───────────────┘
```

Cherries and strawberries trigger a floating **popup text** (`+50` or `+100`) that appears at Pac-Man's position and fades after 500ms — so you always know exactly what you earned.

**High Score** is persisted automatically to `highscore.txt` in the project directory. It is read on startup and updated the moment your final score exceeds it at game over. Your best run is never lost.

---

## Sound System

The `Sound` class pre-loads all audio at startup via `Sound.loadAll()` and stores each `Clip` in a `HashMap<String, Clip>`. This avoids any loading delay during gameplay.

```
  ┌──────────────┬──────────────────────────────────────────────┐
  │  Sound Key   │  When It Plays                               │
  ├──────────────┼──────────────────────────────────────────────┤
  │  logo        │  During the start screen fade animation      │
  │  start       │  On game start, after death, on level clear  │
  │  siren       │  Loops continuously during active gameplay   │
  │  waka        │  Every time a pellet is eaten                │
  │  death       │  On ghost or Final Ghost collision           │
  │  bonus       │  On cherry or strawberry pickup              │
  └──────────────┴──────────────────────────────────────────────┘
```

The siren is **state-aware** — it stops during death sequences, level transitions, and starting pauses, then resumes automatically once gameplay is active again.

---

## Game Mechanics

**The Tile Map**
The maze is defined as a 17-row × 35-column `String[]` array directly in `PacMan.java`. Each character maps to a specific entity: `X` for walls, space for pellets, `r/b/o/p` for ghosts, `P` for Pac-Man's start, `c` for cherry, `C` for strawberry, and `O` for tunnel openings.

**Movement & Queued Input**
Pac-Man moves at `TILE_SIZE / 4` (8 px) per tick. Direction changes are buffered in `nextDirection` and only applied when Pac-Man is perfectly tile-aligned — so early inputs never cause missed turns.

**Collision Detection**
All collision uses AABB (axis-aligned bounding box) rectangle intersection. The same `collision()` method handles wall blocking, ghost contact, pellet eating, and bonus item collection uniformly across all entity types.

**Tunnel Wrapping**
Rows 7 through 9 (the middle horizontal corridor) are designated tunnel rows. Any entity — Pac-Man or ghost — that exits the left or right boundary in this zone instantly reappears on the opposite side.

**Death Sequence**
On collision, all entities freeze, the siren stops, the death sound plays, and a 2-second pause runs before resetting positions. A further 4.5-second restart pause follows, during which the start sound plays and entities remain still before movement resumes.

**High Score Persistence**
On game over, if `score > highScore`, the new value is written to `highscore.txt` using `PrintWriter`. On next launch, `App` reads it back with a `Scanner` — your record survives between sessions.

---

## Roadmap

Possible directions for future development:

- [ ] Power pellets — make ghosts vulnerable and edible
- [ ] Ghost eaten score multiplier (200 / 400 / 800 / 1600)
- [ ] Animated Pac-Man mouth open/close cycle
- [ ] Ghost respawn from a central ghost house
- [ ] Additional maze layouts beyond Level 3
- [ ] Global leaderboard with player name entry
- [ ] Difficulty selector on the start screen
- [ ] Joystick / gamepad controller support

---

## Contributing

Contributions are welcome — bug fixes, new features, balance improvements, or additional levels.

```bash
# Fork the repository, then:
git clone https://github.com/jawadashraf000/PacMan-Game-in-Java.git
git checkout -b feature/your-feature-name

# Make your changes, then:
git commit -m "Add description of change"
git push origin feature/your-feature-name
# Open a Pull Request on GitHub
```

Please keep commits focused, code commented, and pull requests scoped to a single concern.

---

## Author

**Jawad Ashraf**
GitHub: [@jawadashraf000](https://github.com/jawadashraf000)

If this project helped you learn or gave you something to build on, a star on the repository goes a long way.

---

<div align="center">

```
  · · · · · · · · · · · · · · · · · · · · · · · · · · · · · · </> · · · · · · · · · · · · · · · · · · · · · · · · · · · · · ·
```

*Built with Java. No engine. No shortcuts. Just code.*

**Waka waka!**

</div>
