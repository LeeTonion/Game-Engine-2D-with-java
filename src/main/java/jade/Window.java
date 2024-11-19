package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11C.*;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    public float r,g,b,a;
    private static Window window = null;

    private  static int currentSceneIndex = -1;
    private  static Scene currentScene = null;
    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
        r=0;
        g=0;
        b=0;
        a=0;

    }
    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
                default:
                    assert  false :"unknown scene"+ newScene;
                        break;
        }
    }
    public static Window get() {
        if (window == null) {
            window = new Window();
        }
        return window;
    }
    public static Scene getScene() {
        return get().currentScene;
    }
    public void run() {
        System.out.println("Hello, LWJGL " + Version.getVersion() + "!");
        init();
        loop();

        GLFW.glfwDestroyWindow(glfwWindow);
        GLFW.glfwTerminate();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        // Khởi tạo GLFW
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Không thể khởi tạo GLFW!");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        // Tạo cửa sổ
        glfwWindow = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (glfwWindow == 0) {
            throw new RuntimeException("Không thể tạo cửa sổ GLFW!");
        }

        GLFW.glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        GLFW.glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        GLFW.glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        GLFW.glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Thiết lập ngữ cảnh OpenGL
        GLFW.glfwMakeContextCurrent(glfwWindow);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(glfwWindow);
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE,GL_ONE_MINUS_SRC_ALPHA);

        Window.changeScene(0);
    }

    private void loop() {
        float beginTime = (float)glfwGetTime();
        float endTime ;
        float dt = -1.0f;
        while (!GLFW.glfwWindowShouldClose(glfwWindow)) {
            GL11.glClearColor(r, g, b, a);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            if (dt >=0){
               currentScene.Update(dt);
            }

            GLFW.glfwSwapBuffers(glfwWindow);
            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
            GLFW.glfwPollEvents();
        }
    }
}
