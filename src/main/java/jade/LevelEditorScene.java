package jade;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

import java.sql.SQLOutput;

public class LevelEditorScene extends Scene {
    private GameObject obj1;
    private GameObject obj2;
    private Spritesheet sprites ;
    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-250,0));

        sprites = AssetPool.getSpritesheet("asset/images/WalkingCaveExplorer-Sheet.png");

        obj1 = new GameObject("Object 1",new Transform(new Vector2f(200,100),new Vector2f(256,256)),3);
        obj1.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture("asset/images/green.png")
        )));
        this.addGameObjectToScene(obj1);
        obj2 = new GameObject("Object 2",new Transform(new Vector2f(400,100),new Vector2f(256,256)),1);
        obj2.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture("asset/images/2024-10-17 (1).png")
        )));
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
}
