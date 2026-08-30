# Kix Engine

Android Library mod for Pocket Code / Catroid / NewCatroid.
Theme: Dark Gray + Emerald Green.

## Build & test

```bash
git clone https://github.com/sa827472ra-bot/Kix-Engine.git
cd Kix-Engine
gradle wrapper --gradle-version 8.7
./gradlew :kix-engine:assembleDebug
./gradlew :kix-engine:testDebugUnitTest
```

## Parts

| Part | Content | Status |
|------|---------|--------|
| 1 | Colors, LayerManager, KixPresetActions | ✅ |
| 2 | CustomBlock* + Layer Bricks (8) | ✅ |
| 3 | CameraManager + Camera Bricks (12) | ✅ |
| 4 | Joystick / Network / Collision / Bot | ✅ |
| 5 | SuperTextBrick + 73 tests + KixEngine facade | ✅ |

## Optimizations (Part 5)

- **`KixEngine.tick(deltaMs)`** — single entry for stage loop (camera → collision → supertext)
- **`KixEngine.init()` / `resetAll()`** — one-shot setup and teardown
- **SuperTextManager** — only iterates entries with active animators
- **CollisionManager** — group filters + early exit when detection disabled
- **Joystick / Network** — concurrent structures, non-blocking queues
- **CameraManager** — clamped zoom, bounds applied once on position set

## SuperTextBrick (Fluent API)

```kotlin
SuperTextBrick("title")
  .text("Kix Engine")
  .color(0xFF00C853.toInt())
  .size(48f)
  .align(SuperTextStyle.Align.CENTER)
  .outline(0xFF000000.toInt(), 2f)
  .animate(SuperTextAnimation.TYPEWRITER, 1200)
  .applyToManager()
```

Animations: Fade, Slide, Typewriter, Bounce, Rotate, Pulse, Shake, Rainbow, Glitch, Wave.

## Host integration (still required for UI)

1. Delete Catroid stubs under `kix-engine/src/main/kotlin/org/catrobat/catroid/`
2. `implementation project(':kix-engine')` + `compileOnly project(':catroid')`
3. `KixEngine.init()` on app start; `KixEngine.tick(dt)` each frame
4. CategoryBricksFactory + XstreamSerializer + brick layouts

## License
AGPL-3.0
