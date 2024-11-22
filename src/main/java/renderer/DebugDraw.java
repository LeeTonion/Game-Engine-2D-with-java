package renderer;

import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.AssetPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class DebugDraw {
    private  static  int  Max_LINES =500;
    private static List<Line2D> Lines = new ArrayList<>();
    private static float[] vertexArray = new float[Max_LINES*6*2];
    private static Shader shader = AssetPool.getShader("asset/shaders/debugLine2D.glsl");
    private static int vaoID;
    private static int vboID;

    private static boolean started =false;

    public static void start(){
        vaoID =glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID=glGenVertexArrays();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6*Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6*Float.BYTES, 3*Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(4);
    }
    public static void beginFrame(){
        if (! started){
            start();
            started=true;
        }

        for(int i=0;i< Lines.size();i++){
            if(Lines.get(i).beginFrame() <0){
                Lines.remove(i);
                i--;

            }
        }
    }
    public static void draw(){
        if(Lines.size() <=0){return;}
        int index =0;
        for (Line2D line : Lines){
            for(int i =0;i<2;i++){
                Vector2f position = i ==0 ? line.getFrom() : line.getTo();
                Vector3f color = line.getColor();

                vertexArray[index] = position.x;
                vertexArray[index+1] = position.y;
                vertexArray[index+2] = -10.0f;

                vertexArray[index+3] = color.x;
                vertexArray[index+4] = color.y;
                vertexArray[index+5] = color.z;
                index+=6;

            }
        }
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray,0,Lines.size()*6*2));

        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawArrays(GL_LINES , 0, Lines.size());

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0) ;

        shader.detach();
    }



    public static void addline2D(Vector2f from, Vector2f to){
        addLine2D(from,to,new Vector3f(0,1,0),1 );
    }
    public static void addLine2D(Vector2f from, Vector2f to,Vector3f color){
        addLine2D(from,to,color,1 );
    }
    public static void addLine2D(Vector2f from, Vector2f to,Vector3f color,int lifetime){
        if(Lines.size() >= Max_LINES){return;}
        DebugDraw.Lines.add( new Line2D(from,to,color,lifetime));
    }
}
