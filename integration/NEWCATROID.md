# Integração Kix Engine → NewCatroid

Guia prático para ter os blocos Kix **usáveis no APK**.
Baseado no fluxo oficial do NewCatroid (`AGENTS.md`): Brick → BrickInfo → XstreamSerializer → CategoryBricksFactory.

## 0. Preparar o repo host

```bash
git clone https://github.com/Danveyd/NewCatroid.git
cd NewCatroid
git checkout -b feature/kix-engine

# Opção A: submodule
git submodule add https://github.com/sa827472ra-bot/Kix-Engine.git kix-engine-src

# Opção B: copiar fontes
# cp -r ../Kix-Engine/src/main/kotlin/org/catrobat/catroid/kix \
#   catroid/src/main/java/org/catrobat/catroid/
```

**Não copie** a pasta de stubs:
`kix-engine/src/main/kotlin/org/catrobat/catroid/content`
`kix-engine/src/main/kotlin/org/catrobat/catroid/formulaeditor`

Use as classes reais do NewCatroid.

## 1. settings.gradle

```gradle
include ':catroid'
include ':lunoscript-processor'
include ':lunoscript-annotations'
include ':vncclient'
// Kix:
include ':kix-engine'
project(':kix-engine').projectDir = new File(rootDir, 'kix-engine-src/kix-engine')
```

Se preferir fontes dentro do `catroid` (sem módulo), pule este passo e só copie o package `kix`.

## 2. catroid/build.gradle

```gradle
dependencies {
    // ...
    implementation project(':kix-engine')
}
```

No `kix-engine/build.gradle.kts` do Kix:

```kotlin
dependencies {
    compileOnly(project(":catroid"))
    // REMOVA stubs ao compilar junto com NewCatroid
}
```

## 3. Cores

Copie `res/values/kix_colors.xml` para:

`catroid/src/main/res/values/kix_colors.xml`

## 4. CategoryBricksFactory.kt

Arquivo:
`catroid/src/main/java/org/catrobat/catroid/ui/fragment/CategoryBricksFactory.kt`

Adicione imports e uma lista (ou reutilize motion/looks). Exemplo mínimo — criar categoria via string já existente ou adicionar em `BrickCategoryListBuilder`.

Trecho para colar (ex.: no final de um método que monta listas, ou criar `setupKixCategory()`):

```kotlin
import org.catrobat.catroid.kix.bricks.layers.*
import org.catrobat.catroid.kix.bricks.camera.*
import org.catrobat.catroid.kix.bricks.joystick.*
import org.catrobat.catroid.kix.bricks.network.*
import org.catrobat.catroid.kix.bricks.collision.*
import org.catrobat.catroid.kix.bricks.bot.*
import org.catrobat.catroid.kix.bricks.text.SuperTextBrick

fun kixBrickList(): MutableList<Brick> = mutableListOf(
    // Layers
    ShowLayerBrick(0),
    HideLayerBrick(0),
    ToggleLayerVisibilityBrick(0),
    SetLayerNameBrick(0, "Layer"),
    SetActorLayerBrick(0),
    GetActorLayerBrick(),
    BringLayerToFrontBrick(0),
    SendLayerToBackBrick(0),
    // Camera
    CameraFollowPlayerBrick(),
    CameraFixedPositionBrick(0f, 0f),
    CameraZoomBrick(1f),
    CameraRotateBrick(0f),
    CameraShakeBrick(5f, 300),
    CameraFadeInBrick(300),
    CameraFadeOutBrick(300),
    CameraBoundsBrick(0f, 1000f, 0f, 1000f),
    CameraLerpBrick(0.1f),
    CameraDepthOfFieldBrick(0f, 100f),
    CameraIsometricBrick(true),
    CameraCinematicCutBrick(0f, 0f, 1f, 0f),
    // Joystick
    JoystickDPadBrick(true),
    JoystickAnalogBrick(true),
    JoystickDualStickBrick(true),
    GetJoystickInputBrick(),
    // Network
    NetworkUDPConnectBrick("127.0.0.1", 7777),
    NetworkUDPSendBrick(""),
    NetworkUDPReceiveBrick(),
    NetworkUDPDisconnectBrick(),
    NetworkTCPConnectBrick("127.0.0.1", 7777),
    NetworkTCPSendBrick(""),
    NetworkTCPReceiveBrick(),
    NetworkTCPDisconnectBrick(),
    NetworkBroadcastBrick("msg"),
    NetworkListenBrick(),
    // Collision
    CollisionDetectionBrick(true),
    CollisionGroupFilterBrick("default", "default"),
    OnCollisionBrick(),
    // Bot
    BotPatrolBrick("0,0;100,0"),
    BotFollowTargetBrick(),
    BotPathfindBrick(0f, 0f),
    BotAIBehaviorBrick("wander"),
    // Text
    SuperTextBrick("title")
)
```

Ligue essa lista ao switch de categorias em `CategoryBricksFactory` / `BrickCategoryListBuilder` (mesma forma que as outras categorias do NewCatroid).

## 5. XstreamSerializer

Em `XstreamSerializer.java` (ou `.kt`), registre aliases:

```java
// Kix Engine
xstream.alias("brick", org.catrobat.catroid.kix.bricks.layers.ShowLayerBrick.class);
xstream.alias("brick", org.catrobat.catroid.kix.bricks.layers.HideLayerBrick.class);
xstream.alias("brick", org.catrobat.catroid.kix.bricks.layers.ToggleLayerVisibilityBrick.class);
xstream.alias("brick", org.catrobat.catroid.kix.bricks.layers.SetLayerNameBrick.class);
xstream.alias("brick", org.catrobat.catroid.kix.bricks.layers.SetActorLayerBrick.class);
xstream.alias("brick", org.catrobat.catroid.kix.bricks.layers.GetActorLayerBrick.class);
xstream.alias("brick", org.catrobat.catroid.kix.bricks.layers.BringLayerToFrontBrick.class);
xstream.alias("brick", org.catrobat.catroid.kix.bricks.layers.SendLayerToBackBrick.class);
// ... repita para todos os bricks camera/joystick/network/collision/bot/text
// (lista completa em integration/xstream_aliases.txt)
```

No NewCatroid o padrão é `xstream.alias("brick", SuaClasse.class);` para cada brick.

## 6. BrickInfo (descrições)

Onde o NewCatroid registra `BrickInfo` / tooltips:

```java
add(ShowLayerBrick.class, "Shows a visual layer (0-9)");
add(CameraFollowPlayerBrick.class, "Camera follows the current actor");
// ... (ver integration/brickinfo_entries.txt)
```

## 7. Stage tick

No loop do stage (Fast2D / StageListener / onde o frame é atualizado):

```kotlin
import org.catrobat.catroid.kix.KixEngine

// onCreate / first stage:
KixEngine.init(registerBricks = false) // bricks já via CategoryBricksFactory

// cada frame:
KixEngine.tick((deltaSeconds * 1000).toLong())
```

## 8. Layouts (mínimo)

Os bricks Kix usam `android.R.layout.simple_list_item_1` como placeholder.
Para UI nativa, crie `res/layout/brick_kix_*.xml` no estilo dos outros bricks e sobrescreva `getViewResource()`.

Enquanto isso, os bricks **já executam** a lógica mesmo com layout placeholder.

## 9. Build

```bash
./gradlew :catroid:assembleDebug
# APK em catroid/build/outputs/apk/
```

## Checklist

- [ ] Package `org.catrobat.catroid.kix` no source set
- [ ] Sem stubs Catroid do Kix-Engine
- [ ] `kix_colors.xml`
- [ ] CategoryBricksFactory lista Kix
- [ ] Xstream aliases
- [ ] BrickInfo (opcional mas recomendado)
- [ ] `KixEngine.tick` no stage
- [ ] APK instalado e categoria visível no editor

## Limitações honestas

- Integração **manual** no monorepo NewCatroid (não dá para publicar um APK só com o repo Kix-Engine).
- Fork completo do NewCatroid nesta conta do bot não foi criado automaticamente (repo grande + permissões); use o clone local acima.
- Render visual de SuperText/Layers/Camera no stage precisa que o renderer leia `CameraManager.getRenderOffset()`, layers e `SuperTextManager.visibleStyles()` — o tick já atualiza o estado.
