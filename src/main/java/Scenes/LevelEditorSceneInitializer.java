package Scenes;

import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import jade.*;
import org.joml.Vector2f;
import org.lwjgl.system.CallbackI;
import util.AssetPool;

import java.io.File;
import java.util.Collection;

public class LevelEditorSceneInitializer extends SceneInitializer {

    private Spritesheet sprites ;
    private GameObject levelEditorStuff;

    public LevelEditorSceneInitializer() {

    }

    @Override
    public void init(Scene scene) {
        sprites = AssetPool.getSpritesheet("asset/images/tileset.png");
        Spritesheet gizmos = AssetPool.getSpritesheet("asset/images/gizmos.png");

        levelEditorStuff = scene.createGameObject("LevelEditor");
        levelEditorStuff.setNoSerialize();
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(scene.camera()));
        levelEditorStuff.addComponent(new GizmoSystem(gizmos));
        scene.addGameObjectToScene(levelEditorStuff);
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
    public void imgui(){

        ImGui.begin("Level Editor Stuff");
        levelEditorStuff.imgui();
        ImGui.end();
        ImGui.begin("Objects");

        if(ImGui.beginTabBar("WindowTabBar")) {
            if(ImGui.beginTabItem("Blocks")) {
                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                for (int i = 0; i < sprites.size(); i++) {
                    Sprite sprite = sprites.getSprite(i);
                    float spriteWidth = sprite.getWidth() * 4;
                    float spriteHeight = sprite.getHeight() * 4;
                    int id = sprite.getTexId();
                    Vector2f[] texCoords = sprite.getTexCoords();
                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                        GameObject object = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);
                        levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                    }
                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                    if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }
                }
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Prefabs")) {
                Spritesheet playerSprites = AssetPool.getSpritesheet("asset/images/spritesheet.png");
                Sprite sprite = playerSprites.getSprite(0);
                float spriteWidth = sprite.getWidth() * 4;
                float spriteHeight = sprite.getHeight() * 4;
                int id = sprite.getTexId();
                Vector2f[] texCoords = sprite.getTexCoords();
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateMario();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.sameLine();

                Spritesheet items = AssetPool.getSpritesheet("asset/images/items.png");
                sprite = items.getSprite(0);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();

                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateQuestionBlock();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.sameLine();

                ImGui.endTabItem();
            }
            if(ImGui.beginTabItem("Sounds")){
                Collection<Sound> sounds = AssetPool.getAllSounds();
                for(Sound sound: sounds){
                    File tmp = new File(sound.getFilepath());
                    if(ImGui.button(tmp.getName())){
                        if(!sound.isPlaying()){
                            sound.play();
                        } else{
                            sound.stop();
                        }
                    }

                    if(ImGui.getContentRegionAvailX() > 100){
                        ImGui.sameLine();
                    }
                }

                ImGui.endTabItem();
            }
            ImGui.endTabBar();
        }
        ImGui.end();
    }
}
