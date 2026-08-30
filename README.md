# Kix Engine

**Kix Engine** is an advanced mod for Pocket Code / Catroid focused on professional game development features.

## Theme
Dark Gray + Emerald Green color palette.

## Features (in progress)
- Strict 10-layer visual management system
- 26 preset actions (Layers, Camera, Joystick, Network, Collision, Bot)
- Custom blocks infrastructure
- SuperTextBrick with advanced text rendering and animations
- Full set of Camera, Joystick, Network, Collision, Bot and Layer bricks

## Structure

```
res/values/kix_colors.xml
src/main/kotlin/org/catrobat/catroid/kix/
├── layers/
│   └── LayerManager.kt
└── actions/
    └── KixPresetActions.kt
```

## License
Based on Catrobat / Catroid (AGPL-3.0).
Additional Kix Engine contributions under the same license.

## Status
Part 1 complete: Colors, LayerManager and KixPresetActions base structure.
