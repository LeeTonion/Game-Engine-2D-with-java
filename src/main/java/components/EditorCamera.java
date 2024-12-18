package components;
import jade.Camera;
import jade.KeyListener;
import jade.MouseListener;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class EditorCamera extends Component {
    private float dragDebounce = 0.032f; // Thời gian chờ để tránh xử lý sự kiện kéo quá nhanh
    private Camera levelEditorCamera;   // Lấy camera
    private Vector2f clickOrigin;       // Vị trí chuột khi bắt đầu kéo
    private boolean reset = false;      // kiểm tra trạng thái reset camera
    private float lerpTime = 0.0f;      // Thời gian để reset camera
    private float dragSensitivity = 30.0f; // Độ nhạy khi kéo camera
    private float scrollSensitivity = 0.1f; // Độ nhạy khi thu phóng camera

    // Constructor: khởi tạo camera và gán clickOrigin
    public EditorCamera(Camera levelEditorCamera) {
        this.levelEditorCamera = levelEditorCamera; // Gán camera được điều khiển
        this.clickOrigin = new Vector2f();          // Khởi tạo vector vị trí chuột
    }

    @Override
    public void editorUpdate(float dt) {
        // Nếu nhấn chuột phải và dragDebounce còn > 0
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT) && dragDebounce > 0) {
            this.clickOrigin = MouseListener.getWorld(); // Gán vị trí bắt đầu kéo
//            dragDebounce -= dt;                         // Giảm thời gian debounce theo thời gian frame
            return;                                     // Kết thúc xử lý tại đây
        }
        // Nếu tiếp tục nhấn chuột phải (hết thời gian chờ, thực hiện kéo camera
        else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT)) {
            Vector2f mousePos = MouseListener.getWorld();                 // Lấy vị trí chuột hiện tại
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin); // Tính khoảng cách di chuyển chuột
            levelEditorCamera.position.sub(delta.mul(dt).mul(dragSensitivity)); // Cập nhật vị trí camera
            this.clickOrigin.lerp(mousePos, dt);                           // Làm mịn vị trí chuột ban đầu
        }

        // Reset lại thời gian debounce nếu chuột phải không được nhấn
        if (dragDebounce <= 0.0f && !MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT)) {
            dragDebounce = 0.1f;
        }

        // Kiểm tra nếu có cuộn chuột để zoom
        if (MouseListener.getScrollY() != 0.0f) {
            float addValue = (float)Math.pow(Math.abs(MouseListener.getScrollY() * scrollSensitivity),
                    1 / levelEditorCamera.getZoom()); // Tính giá trị zoom
            addValue *= -Math.signum(MouseListener.getScrollY());             // Điều chỉnh zoom theo hướng cuộn
            levelEditorCamera.addZoom(addValue);                              // Thay đổi mức độ zoom của camera
        }

        // Nếu phím P được nhấn, bật cờ reset
        if (KeyListener.isKeyPressed(GLFW_KEY_P)) {
            reset = true;
        }

        // Nếu đang trong trạng thái reset
        if (reset) {
            levelEditorCamera.position.lerp(new Vector2f(), lerpTime);       // Nội suy vị trí camera về gốc
            levelEditorCamera.setZoom(this.levelEditorCamera.getZoom() +
                    ((1.0f - levelEditorCamera.getZoom()) * lerpTime));      // Nội suy zoom về mức 1.0
            this.lerpTime += 0.1f * dt;                                     // Tăng thời gian nội suy

            // Kiểm tra nếu camera đã gần về vị trí ban đầu
            if (Math.abs(levelEditorCamera.position.x) <= 5.0f &&
                    Math.abs(levelEditorCamera.position.y) <= 5.0f) {
                this.lerpTime = 0.0f;                                        // Đặt lại thời gian nội suy
                levelEditorCamera.position.set(0f, 0f);                      // Đặt vị trí camera về (0, 0)
                this.levelEditorCamera.setZoom(1.0f);                        // Đặt zoom về mức 1.0
                reset = false;                                               // Tắt trạng thái reset
            }
        }
    }
}
