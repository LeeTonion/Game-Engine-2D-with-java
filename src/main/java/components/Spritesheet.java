package components;

import org.joml.Vector2f;
import renderer.Texture;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {
    private Texture texture;
    private List<Sprite> sprites;
    public Spritesheet(Texture texture, int width, int height,int numSprite,int spacing) {
        this.sprites = new ArrayList<Sprite>();
        this.texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight()-height;
        for(int i=0;i<numSprite;i++) {
            float topY =(currentY +height)/(float)texture.getHeight();
            float rightX =(currentX+width)/(float)texture.getWidth();
            float leftX = currentX/(float)texture.getWidth();
            float bottomY =currentY/(float)texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX,topY),
                    new Vector2f(rightX,bottomY),
                    new Vector2f(leftX,bottomY),
                    new Vector2f(leftX,topY)
            };
            Sprite sprite =new Sprite(this.texture,texCoords);
            this.sprites.add(sprite);
            currentX+=width+spacing;
            if(currentX>=texture.getWidth()) {
                currentX=0;
                currentY -= height+spacing;
            }
        }
    }
    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }
}