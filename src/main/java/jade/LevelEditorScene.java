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
    private Spritesheet sprites ;
    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-250,0));

        sprites = AssetPool.getSpritesheet("asset/images/WalkingCaveExplorer-Sheet.png");
        obj1 = new GameObject("Object 1",new Transform(new Vector2f(100,100),new Vector2f(256,256)));
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2",new Transform(new Vector2f(400,100),new Vector2f(256,256)));
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(1)));
        this.addGameObjectToScene(obj2);


    }
    private void loadResources() {
        AssetPool.getShader("asset/shaders/default.glsl");

        AssetPool.addSpritesheet("asset/images/WalkingCaveExplorer-Sheet.png",new Spritesheet(AssetPool.getTexture("asset/images/WalkingCaveExplorer-Sheet.png"),44,35,9,4));
    }
    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft=0;

    @Override
    public void Update(float dt) {
        spriteFlipTimeLeft -=dt;
        if(spriteFlipTimeLeft <=0){
             spriteFlipTimeLeft=spriteFlipTime;
             spriteIndex++;
             if(spriteIndex>8){
                 spriteIndex=0;
             }
             obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
        }
        System.out.println("FPS: " +(1.0/dt));
        // camera.position.x -= dt * 50.0f;

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}
