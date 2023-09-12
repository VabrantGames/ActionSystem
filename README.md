# ActionSystem 

An inheritance based action system for [libGDX](https://libgdx.com/).

---

[![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/com.vabrantgames.actionsystem/actionsystem?color=green&nexusVersion=2&server=https%3A%2F%2Fs01.oss.sonatype.org&label=release)](https://central.sonatype.com/artifact/com.vabrantgames.actionsystem/actionsystem/0.6.0)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/com.vabrantgames.actionsystem/actionsystem?label=snapshot&color=green&server=https%3A%2F%2Fs01.oss.sonatype.org)](https://s01.oss.sonatype.org/#nexus-search;gav~com.vabrantgames.actionsystem~actionsystem~~~)




## How to add to project
To use in your project add the following repositories in your build.gradle. Then this project as a dependency with 
a specified version.

```groovy
repositories {
    maven { url 'https://s01.oss.sonatype.org/content/repositories/snapshots/' }
    maven { url 'https://s01.oss.sonatype.org/content/repositories/releases/' }
}
```

```groovy
dependency {
    implementation 'com.vabrantgames.actionsystem:actionsystem:version'
}
```
---

## Usage

### 1. Implement Interface
Any class that is to be actioned should implement at least one interface to add functionality.

- [Movable](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/Movable.java)
- [Colorable](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/Colorable.java)
- [Rotatable](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/Rotatable.java)
- [Scalable](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/Scalable.java)
- [Shakable](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/Shakable.java)
- [Zoomable](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/Zoomable.java)

```java
public class Square implements Movable {
    
    private float x;
    private float y;
    
    @Override
    public void setX(float x) {
        this.x = x;
    }
    
    @Override
    public void setY(float y) {
        this.y = y;
    }
    
    @Override
    public float getX() {
        return x;
    }
    
    @Override
    public float getY() {
        return y;
    }
}
```

**_Note:_** *Depending on which interfaces are implemented determines what actions can be used. A class that only 
implements 
[Movable](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/Movable.java) can not be used for actions that reply on 
[Colorable](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/Colorable.java).*

### 2. Create ActionManager
Create an [ActionManager](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/ActionManager.java) in a top level class like your screen or main game class. 

`ActionManager actionManager = new ActionManager();`

Then in your render method or wherever you update your objects, update the [ActionManager](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/ActionManager.java) by passing in the delta 
time.
`actionManager.update(delta);`

**_Note:_** *You can get the delta at* `Gdx.graphics.getDeltaTime();`

### 3. Create An Action
Actions can easily be created from static helpers located in the action you are trying to use.

`MoveAction action = MoveAction.moxeXBy(square, amount, duration, interpolation);`

### 4. Add Action To ActionManager
`actionManager.add(action);`

---

## Actions
- [ColorAction](actionsystem/src/main/java/com/vabrant/actionsystem/actions/coloraction/ColorAction.java)
- [CountDownAction](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/CountDownAction.java)
- [DelayAction](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/DelayAction.java)
- [GroupAction](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/GroupAction.java)
- [MoveAction](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/MoveAction.java)
- [RepeatAction](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/RepeatAction.java)
- [RotateAction](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/RotateAction.java)
- [RunnableAction](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/RunnableAction.java)
- [ScaleAction](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/ScaleAction.java)
- [ShakeAction](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/ShakeAction.java)
- [ZoomAction](https://github.com/VabrantGames/ActionSystem/blob/master/actionsystem/src/main/java/com/vabrant/actionsystem/actions/ZoomAction.java)







