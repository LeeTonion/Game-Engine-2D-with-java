package jade;

import org.lwjgl.glfw.GLFW;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean[] mouseButtonPressed = new boolean[3]; // Biến kiểm tra nút bấm
    private boolean isDragging; // Biến kiểm tra kéo chuột

    // Constructor riêng tư để ngăn việc tạo nhiều instance
    private MouseListener() {
        this.scrollX = 0;
        this.scrollY = 0;
        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0;
        this.lastY = 0;
    }

    // Hàm get singleton instance
    public static MouseListener get() {
        if (instance == null) {
            instance = new MouseListener();
        }
        return instance;
    }

    // Hàm callback khi di chuyển chuột
    public static void mousePosCallback(long window, double xpos, double ypos) {
        // Lấy instance của MouseListener trực tiếp khi cần
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {

        if (button < get().mouseButtonPressed.length) {
            if (action == GLFW.GLFW_PRESS) {
                get().mouseButtonPressed[button] = true;
            } else if (action == GLFW.GLFW_RELEASE) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;  // Không còn kéo khi thả chuột
            }
        }
    }

    // Hàm callback khi cuộn chuột
    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        // Lấy instance của trực tiếp khi cần
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    // Hàm gọi mỗi lần kết thúc một frame
    public static void endFrame() {
        // Lấy instance của trực tiếp khi cần
     get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    // Getter cho tọa độ X chuột
    public static float getX() {
        return (float) get().xPos;
    }

    // Getter cho tọa độ Y chuột
    public static float getY() {
        return (float) get().yPos;
    }

    public static float getDx() {
        return (float) (get().lastX - get().xPos);
    }


    public static float getDy() {
        return (float) (get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }


    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }


    public static boolean MouseButtonDown(int button) {
        return button < get().mouseButtonPressed.length && get().mouseButtonPressed[button];
    }
}
