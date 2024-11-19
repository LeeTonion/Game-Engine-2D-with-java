package jade;

import org.lwjgl.glfw.GLFW;

public class KeyListener {
    private static KeyListener instance;
    private final boolean[] keyPressed = new boolean[350];

    private KeyListener() {
    }

    public static KeyListener get() {
        if (instance == null) {
            instance = new KeyListener();
        }
        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key < 0 || key >= get().keyPressed.length) return;

        if (action == GLFW.GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW.GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        if (keyCode < 0 || keyCode >= get().keyPressed.length) {
            return false;
        }
        return get().keyPressed[keyCode];
    }
}
