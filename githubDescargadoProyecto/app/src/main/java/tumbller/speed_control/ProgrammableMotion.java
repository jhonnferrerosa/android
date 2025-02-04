package tumbller.speed_control;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.*;
import java.io.*;



public class ProgrammableMotion {
    static ProgrammableMotion instance = null;
    BufferedReader reader;
    ArrayAdapter<String> log;
    Activity activity;
    ListView logView;
    Button btnCancel;
    TextView txtCarSpeed;
    TextView txtTurnSpeed;
    boolean shouldRun;
    Thread thread;

    public static void start(String filename, Context context) {
        instance = new ProgrammableMotion(filename, context);
    }

    public static void cancel() {
        assert instance != null;
        instance.shouldRun = false;
        instance.thread.interrupt();
    }

    ProgrammableMotion(String filename, Context context) {
        try {
            File file = new File(context.getExternalFilesDir(null), filename);
            reader = new BufferedReader(new FileReader(file));
            log = new ArrayAdapter<>(context, R.layout.list_item);
            log.add("");
            log.add("");

            activity = (Activity) context;
            logView = activity.findViewById(R.id.listView);
            logView.setAdapter(log);
            logView.setVisibility(View.VISIBLE);
            btnCancel = activity.findViewById(R.id.btnCancel);
            btnCancel.setVisibility(Button.VISIBLE);
            txtCarSpeed = activity.findViewById(R.id.txtCarSpeed);
            txtTurnSpeed = activity.findViewById(R.id.txtTurnSpeed);

            shouldRun = true;
            thread = new Thread(this::doMotion);
            thread.start();

        } catch (Throwable t) {
            t.printStackTrace();
            cleanup();
        }
    }

    void doMotion() {
        try {
            while (shouldRun) {
                String line = reader.readLine();
                if (line == null)
                    break;

                String[] item = line.split(",");
                if (item.length == 3) {
                    int wait = Integer.parseInt(item[0]);
                    byte carSpeed = Byte.parseByte(item[1]);
                    byte turnSpeed = Byte.parseByte(item[2]);

                    String logstr = String.format("wait:%3d, car:%3d, turn:%3d", wait, carSpeed, turnSpeed);
                    logView.post(() -> {
                        log.add(logstr);
                        logView.setSelection(log.getCount() - 1);
                    });
                    Thread.sleep(wait * 100L);
                    sendSpeed(carSpeed, turnSpeed);
                }
            }
        } catch (InterruptedException ignored) {
        } catch (Throwable t) {
            t.printStackTrace();
        }
        sendSpeed((byte) 0, (byte) 0);
        activity.runOnUiThread(this::cleanup);
    }
    void sendSpeed(byte carSpeed, byte turnSpeed) {
        Bluetooth.sendControlValue(carSpeed, turnSpeed);
        activity.runOnUiThread(() -> {
            txtCarSpeed.setText(Integer.toString(carSpeed));
            txtTurnSpeed.setText(Integer.toString(turnSpeed));
        });
    }

    void cleanup() {
        if (btnCancel != null)
            btnCancel.setVisibility(Button.INVISIBLE);
        if (logView != null)
            logView.setVisibility(View.INVISIBLE);
        instance = null;
    }

}
