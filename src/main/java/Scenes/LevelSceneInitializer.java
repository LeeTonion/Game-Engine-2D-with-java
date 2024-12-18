package Scenes;

import Scenes.Scene;
import Scenes.SceneInitializer;
import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import jade.*;
import org.joml.Vector2f;
import physics2d.components.Box2DCollider;
import physics2d.components.Rigidbody2D;
import physics2d.enums.BodyType;
import util.AssetPool;
import java.io.File;
import java.util.Collection;

public class LevelSceneInitializer extends SceneInitializer {

    public LevelSceneInitializer() {
    }

    @Override
    public void init(Scene scene) {
        Spritesheet sprites = AssetPool.getSpritesheet("asset/images/spritesheets/decorationsAndBlocks.png");
        GameObject cameraObject = scene.createGameObject("GameCamera");
        cameraObject.addComponent(new GameCamera(scene.camera()));
        cameraObject.start();
        scene.addGameObjectToScene(cameraObject);
    }
    @Override
    public void loadResources(Scene scene) {
        AssetPool.getShader("asset/shaders/default.glsl");
        AssetPool.addSpritesheet("asset/images/tileset.png",
                new Spritesheet(AssetPool.getTexture("asset/images/tileset.png"),
                        16,16,81,0));
        AssetPool.addSpritesheet("asset/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("asset/images/spritesheet.png"),
                        16,16,26,0));
        AssetPool.addSpritesheet("asset/images/turtle.png",
                new Spritesheet(AssetPool.getTexture("asset/images/turtle.png"),
                        16, 24, 4, 0));
        AssetPool.addSpritesheet("asset/images/bigSpritesheet.png",
                new Spritesheet(AssetPool.getTexture("asset/images/bigSpritesheet.png"),
                        16, 32, 42, 0));
        AssetPool.addSpritesheet("asset/images/pipes.png",
                new Spritesheet(AssetPool.getTexture("asset/images/pipes.png"),
                        32, 32, 4, 0));
        AssetPool.addSpritesheet("asset/images/items.png",
                new Spritesheet(AssetPool.getTexture("asset/images/items.png"),
                        16,16,43,0));
        AssetPool.addSpritesheet("asset/images/gizmos.png",
                new Spritesheet(AssetPool.getTexture("asset/images/gizmos.png"),
                        24, 48, 3, 2));

        AssetPool.addSound("asset/sounds/main-theme-overworld.ogg", true);
        AssetPool.addSound("asset/sounds/flagpole.ogg", false);
        AssetPool.addSound("asset/sounds/break_block.ogg", false);
        AssetPool.addSound("asset/sounds/bump.ogg", false);
        AssetPool.addSound("asset/sounds/coin.ogg", false);
        AssetPool.addSound("asset/sounds/gameover.ogg", false);
        AssetPool.addSound("asset/sounds/jump-small.ogg", false);
        AssetPool.addSound("asset/sounds/mario_die.ogg", false);
        AssetPool.addSound("asset/sounds/pipe.ogg", false);
        AssetPool.addSound("asset/sounds/powerup.ogg", false);
        AssetPool.addSound("asset/sounds/powerup_appears.ogg", false);
        AssetPool.addSound("asset/sounds/stage_clear.ogg", false);
        AssetPool.addSound("asset/sounds/stomp.ogg", false);
        AssetPool.addSound("asset/sounds/kick.ogg", false);
        AssetPool.addSound("asset/sounds/invincible.ogg", false);
        for (GameObject g : scene.getGameObjects()) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }
            if (g.getComponent(StateMachine.class) != null) {
                StateMachine stateMachine = g.getComponent(StateMachine.class);
                stateMachine.refreshTextures();
            }
        }
    }
    @Override
    public void imgui() {
    }
}