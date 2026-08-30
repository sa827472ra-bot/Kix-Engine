# Kix Engine

**Kix Engine** — Android Library mod for Pocket Code / Catroid / NewCatroid.

Dark Gray + Emerald Green theme. Layers, Camera, custom blocks infrastructure.

## Build as Android Library (Option B)

```bash
git clone https://github.com/sa827472ra-bot/Kix-Engine.git
cd Kix-Engine

# Needs Android SDK + JDK 17
# Generate wrapper if missing:
gradle wrapper --gradle-version 8.7

./gradlew :kix-engine:assembleDebug
# AAR → kix-engine/build/outputs/aar/kix-engine-debug.aar
```

### Module layout

```
Kix-Engine/
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── res/values/kix_colors.xml          # shared resources
├── src/main/kotlin/.../kix/           # production sources
└── kix-engine/
    ├── build.gradle.kts
    ├── src/main/AndroidManifest.xml
    └── src/main/kotlin/.../           # Catroid STUBS (standalone only)
```

Stubs under `kix-engine/src/main/kotlin/org/catrobat/catroid/` exist **only** so the library compiles alone. They are **not** the real Catroid runtime.

### Use inside NewCatroid

1. Copy/clone this repo next to NewCatroid **or** add as git submodule.
2. In NewCatroid `settings.gradle`:
   ```gradle
   include ':kix-engine'
   project(':kix-engine').projectDir = new File(settingsDir, '../Kix-Engine/kix-engine')
   ```
3. In `catroid/build.gradle`:
   ```gradle
   implementation project(':kix-engine')
   ```
4. **Delete** the stub files inside `kix-engine/src/main/kotlin/org/catrobat/catroid/` (Sprite, Brick, Formula, …).
5. In `kix-engine/build.gradle.kts` add:
   ```kotlin
   compileOnly(project(":catroid"))
   ```
6. Register bricks in CategoryBricksFactory, XstreamSerializer, layouts, stage loop (`CameraManager.update`).

Without steps 4–6 you get a compilable AAR, but blocks still will not appear in the Pocket Code UI.

## Feature status

| Area | Status |
|------|--------|
| Colors (`kix_colors.xml`) | ✅ |
| LayerManager + 8 Layer Bricks | ✅ |
| CameraManager + 12 Camera Bricks | ✅ |
| CustomBlockFactory / Brick / BlockRegistry | ✅ |
| Android Library Gradle (Option B) | ✅ |
| Joystick Bricks (4) | ❌ missing |
| Network Bricks TCP/UDP (10) | ❌ missing |
| Collision Bricks (3) | ❌ missing |
| Bot Bricks (4) | ❌ missing |
| SuperTextBrick + 10 animations | ❌ missing |
| Unit tests (73) | ❌ missing |
| Brick XML layouts + strings | ❌ missing |
| CategoryBricksFactory / Xstream hooks | ❌ missing (host app) |
| Stage render integration | ❌ missing (host app) |

## License
AGPL-3.0 (Catrobat-compatible).
