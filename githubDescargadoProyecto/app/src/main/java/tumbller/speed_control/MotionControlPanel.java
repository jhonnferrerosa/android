package tumbller.speed_control;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.widget.TextView;
import androidx.core.math.MathUtils;
public class MotionControlPanel {
    public static final int MAX_SPEED = 80;
    private final Point maxPos;
    private final PointF factor;
    private byte prevCarSpeed = 0;
    private byte prevTurnSpeed = 0;
    private final TextView txtCarSpeed;
    private final TextView txtTurnSpeed;

    public MotionControlPanel(int width, int height, Context context) {
        maxPos = new Point(width / 2, height / 2);
        factor = new PointF((float)MAX_SPEED / maxPos.x, (float)MAX_SPEED / maxPos.y);

        Activity act = (Activity) context;
        txtCarSpeed = act.findViewById(R.id.txtCarSpeed);
        txtTurnSpeed = act.findViewById(R.id.txtTurnSpeed);
    }
    public void resetSpot() {
        sendSpeed((byte) 0, (byte) 0);
    }

    public void moveSpot(Point pos) {
        byte carSpeed = calcSpeed(pos.y, maxPos.y, factor.y);
        byte turnSpeed = calcSpeed(pos.x, maxPos.x, factor.x);
        if (carSpeed >= 0)
            turnSpeed = (byte) -turnSpeed;

        sendSpeed(carSpeed, turnSpeed);
    }

    byte calcSpeed(int pos, int max, float factor) {
        pos = MathUtils.clamp(pos, -max, max);
        float speed = pos * factor;
        speed = speed * Math.abs(speed) / MAX_SPEED;

        return (byte) speed;
    }

    void sendSpeed(byte carSpeed, byte turnSpeed) {
        if (prevCarSpeed != carSpeed ||
                prevTurnSpeed != turnSpeed) {

            Bluetooth.sendControlValue(carSpeed, turnSpeed);

            txtCarSpeed.setText(Integer.toString(carSpeed));
            txtTurnSpeed.setText(Integer.toString(turnSpeed));

            prevCarSpeed = carSpeed;
            prevTurnSpeed = turnSpeed;
        }
    }


}
