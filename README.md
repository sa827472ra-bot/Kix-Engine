# Kix Engine

**Kix Engine** is an advanced mod for Pocket Code / Catroid focused on professional game development features.

## Theme
Dark Gray + Emerald Green color palette.

## Features (in progress)
- Strict 10-layer visual management system
- Camera system (follow, zoom, rotate, shake, fade, bounds, lerp, DoF, isometric, cinematic)
- 26+ preset actions
- Custom blocks infrastructure (`CustomBlockFactory`, `CustomBlockBrick`, `BlockRegistry`)
- 8 Layer Bricks + 12 Camera Bricks
- Joystick / Network / Collision / Bot / SuperTextBrick (planned)

## Structure

```
res/values/kix_colors.xml

src/main/kotlin/org/catrobat/catroid/kix/
├── layers/LayerManager.kt
├── camera/CameraManager.kt
├── actions/KixPresetActions.kt
├── blocks/
│   ├── CustomBlockBrick.kt
│   ├── CustomBlockFactory.kt
│   └── BlockRegistry.kt
└── bricks/
    ├── layers/   (8 bricks)
    └── camera/   (12 bricks)
```

## Part Status

| Part | Content | Status |
|------|---------|--------|
| 1 | Colors, LayerManager, KixPresetActions | ✅ |
| 2 | CustomBlock* + BlockRegistry + Layer Bricks | ✅ |
| 3 | CameraManager + Camera Bricks | ✅ |
| 4+ | Joystick, Network, Collision, Bot, SuperText, Tests | Pending |

## When will the blocks be actually usable?

This repository is a **mod package** (source code). The bricks become usable inside Pocket Code only after integration into a real Catroid / NewCatroid build:

1. **Copy** the `org.catrobat.catroid.kix` package into the Catroid/NewCatroid source tree.
2. **Register** bricks in the host `CategoryBricksFactory` / brick category UI so they appear in the editor.
3. **Layouts + strings** – create `res/layout/brick_*.xml` and string resources (currently using placeholder `android.R.layout`).
4. **XstreamSerializer** – register the new brick classes for project save/load.
5. **Stage / render loop** – call `CameraManager.update(delta)` and apply `getRenderOffset()` / zoom / rotation in the stage renderer (or NewCatroid Fast2D).
6. **ActionFactory** (optional) – wire dedicated actions if you prefer not to call `KixPresetActions` directly from bricks.
7. Build & install the APK.

Until those steps are done, the logic is complete and testable in unit tests, but the blocks will **not** appear in the Pocket Code UI or affect the stage.

Recommended path: fork [Danveyd/NewCatroid](https://github.com/Danveyd/NewCatroid) (or official Catroid), merge this package, then do the 7 steps above.

## License
Based on Catrobat / Catroid (AGPL-3.0).
Additional Kix Engine contributions under the same license.
