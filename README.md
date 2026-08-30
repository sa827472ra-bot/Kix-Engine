# Kix Engine

Android Library mod for Pocket Code / Catroid / NewCatroid.
Theme: Dark Gray + Emerald Green.

## Build

```bash
git clone https://github.com/sa827472ra-bot/Kix-Engine.git
cd Kix-Engine
gradle wrapper --gradle-version 8.7
./gradlew :kix-engine:assembleDebug
```

AAR: `kix-engine/build/outputs/aar/kix-engine-debug.aar`

Stubs under `kix-engine/src/main/kotlin/org/catrobat/catroid/` are for **standalone compile only**. Delete them when linking to real Catroid.

## Status

| Part | Content | Status |
|------|---------|--------|
| 1 | Colors, LayerManager, KixPresetActions | ✅ |
| 2 | CustomBlock* + BlockRegistry + Layer Bricks (8) | ✅ |
| 3 | CameraManager + Camera Bricks (12) | ✅ |
| 4 | Joystick, Network, Collision, Bot managers + bricks | ✅ |
| 5 | SuperTextBrick + unit tests + layouts | Pending |

### Part 4 details
- **Joystick** (4): DPad, Analog, DualStick, GetInput + `JoystickManager`
- **Network** (10): UDP/TCP connect/send/receive/disconnect, Broadcast, Listen + `NetworkManager`
- **Collision** (3): Detection toggle, Group filter, OnCollision + `CollisionManager`
- **Bot** (4): Patrol, Follow, Pathfind, AIBehavior + `BotManager`

## Still missing for real in-app use
- SuperTextBrick (10 animations)
- 73 unit tests
- Brick XML layouts + string resources
- Host integration: CategoryBricksFactory, XstreamSerializer, stage loop (`CameraManager.update`, `CollisionManager.update`, `BotManager.step`)

## License
AGPL-3.0
