package jade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

import java.sql.SQLOutput;

public class LevelEditorScene extends Scene {
    private GameObject obj1;
    private GameObject obj2;
    private SpriteRenderer obj1Sprite;
    private Spritesheet sprites ;
    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-250,0));
        if(levelLoaded){
            return;
        }
        sprites = AssetPool.getSpritesheet("asset/images/WalkingCaveExplorer-Sheet.png");

        obj1 = new GameObject("Object 1",new Transform(new Vector2f(200,100),new Vector2f(256,256)),3);
         obj1Sprite = new SpriteRenderer();
        obj1Sprite.setColor(new Vector4f(1,0,0,1));
        obj1.addComponent(obj1Sprite);
        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;
        obj2 = new GameObject("Object 2",new Transform(new Vector2f(400,100),new Vector2f(256,256)),1);
        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();
        obj2Sprite.setTexture(AssetPool.getTexture("asset/images/2024-10-17 (1).png"));
        obj2SpriteRenderer.setSprite(obj2Sprite);
        obj2.addComponent(obj2SpriteRenderer);
        this.addGameObjectToScene(obj2);


    }
    private void loadResources() {
        AssetPool.getShader("asset/shaders/default.glsl");

        AssetPool.addSpritesheet("asset/images/WalkingCaveExplorer-Sheet.png",new Spritesheet(AssetPool.getTexture("asset/images/WalkingCaveExplorer-Sheet.png"),44,35,9,4));
    }

    @Override
    public void update(float dt) {
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();

    }
    @Override
    public void imgui(){
        ImGui.begin("Test window");
        ImGui.text("Some ramdom text");
        ImGui.end();
    }
}
