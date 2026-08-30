# Kix Engine

**Kix Engine** is an advanced mod for Pocket Code / Catroid focused on professional game development features.

## Theme
Dark Gray + Emerald Green color palette.

## Features (in progress)
- Strict 10-layer visual management system
- 26 preset actions (Layers, Camera, Joystick, Network, Collision, Bot)
- Custom blocks infrastructure (`CustomBlockFactory`, `CustomBlockBrick`, `BlockRegistry`)
- 8 Layer Bricks fully implemented
- SuperTextBrick with advanced text rendering and animations (planned)
- Full set of Camera, Joystick, Network, Collision and Bot bricks (planned)

## Structure

```
res/values/
└── kix_colors.xml

src/main/kotlin/org/catrobat/catroid/kix/
├── layers/
│   └── LayerManager.kt
├── actions/
│   └── KixPresetActions.kt
├── blocks/
│   ├── CustomBlockBrick.kt
│   ├── CustomBlockFactory.kt
│   └── BlockRegistry.kt
└── bricks/
    └── layers/
        ├── ShowLayerBrick.kt
        ├── HideLayerBrick.kt
        ├── ToggleLayerVisibilityBrick.kt
        ├── SetLayerNameBrick.kt
        ├── SetActorLayerBrick.kt
        ├── GetActorLayerBrick.kt
        ├── BringLayerToFrontBrick.kt
        └── SendLayerToBackBrick.kt
```

## Part Status

| Part | Content | Status |
|------|---------|--------|
| 1 | Colors, LayerManager, KixPresetActions | ✅ Done |
| 2 | CustomBlockFactory, CustomBlockBrick, BlockRegistry + 8 Layer Bricks | ✅ Done |
| 3 | Camera Bricks / Joystick / Network / ... | Pending |

## License
Based on Catrobat / Catroid (AGPL-3.0).
Additional Kix Engine contributions under the same license.
