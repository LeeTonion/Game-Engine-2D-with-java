package Scenes;

import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import jade.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import renderer.DebugDraw;
import physics2dtmp.PhysicsSystem2D;
import physics2dtmp.rigidbody.Rigidbody2D;
import util.AssetPool;

public class LevelEditorScene extends Scene {
    private GameObject obj1;
    private GameObject obj2;
    private SpriteRenderer obj1Sprite;
    private Spritesheet sprites ;
    GameObject levelEditorStuff = this.createGameObject("LevelEditor");

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();
        sprites = AssetPool.getSpritesheet("asset/images/tileset.png");
        Spritesheet gizmos = AssetPool.getSpritesheet("asset/images/gizmos.png");
        this.camera = new Camera(new Vector2f(-250,0));
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(this.camera));
        levelEditorStuff.addComponent(new GizmoSystem(gizmos));
        levelEditorStuff.start();






       /* obj1 = new GameObject("Object 1",new Transform(new Vector2f(200,100),new Vector2f(256,256)),3);
         obj1Sprite = new SpriteRenderer();
        obj1Sprite.setColor(new Vector4f(1,0,0,1));
        obj1.addComponent(obj1Sprite);
        obj1.addComponent(new Rigidbody());
        this.addGameObjectToScene(obj1);



        obj2 = new GameObject("Object 2",new Transform(new Vector2f(400,100),new Vector2f(256,256)),1);
        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();
        obj2Sprite.setTexture(AssetPool.getTexture("asset/images/green.png"));
        obj2SpriteRenderer.setSprite(obj2Sprite);
        obj2.addComponent(obj2SpriteRenderer);
        this.addGameObjectToScene(obj2);
*/



    }
    private void loadResources() {
        AssetPool.getShader("asset/shaders/default.glsl");
        AssetPool.addSpritesheet("asset/images/tileset.png",new Spritesheet(AssetPool.getTexture("asset/images/tileset.png"),16,16,242,0));
        AssetPool.addSpritesheet("asset/images/gizmos.png",
                new Spritesheet(AssetPool.getTexture("asset/images/gizmos.png"),
                        24, 48, 3, 2));

        for (GameObject g : gameObjects) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }
        }
    }

    @Override
    public void update(float dt) {
        /*DebugDraw.addBox2D(new Vector2f(400,200),new Vector2f(64,32),10,new Vector3f(0,1,0),1);
        DebugDraw.addCircle(new Vector2f(100,100),10f);*/
        levelEditorStuff.update(dt);
        this.camera.adjustProjection();
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }


    }

    @Override
    public void render() {
            this.renderer.render();
    }

    @Override
    public void imgui(){
        ImGui.begin("Level Editor Stuff");
        levelEditorStuff.imgui();
        ImGui.end();
        ImGui.begin("Test window");

        ImVec2 windowPos= new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize= new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x+windowSize.x;
        for (int i=0;i<sprites.size();i++){

            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() *4;
            float spriteHeight = sprite.getHeight() *4;
            int id = sprite.getTexId();
            Vector2f[] texCoords =sprite.getTexCoords();
            ImGui.pushID(i);
            if (ImGui.imageButton(id,spriteWidth,spriteHeight,texCoords[2].x,texCoords[0].y,texCoords[0].x,texCoords[2].y)){
                GameObject object= Prefabs.generateSpriteObject(sprite,32,32);
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();
            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 +itemSpacing.x+spriteWidth;
            if (i+1 < sprites.size() &&  nextButtonX2 < windowX2){
                ImGui.sameLine();
            }

        }

        ImGui.end();
    }
}
