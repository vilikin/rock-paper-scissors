package in.vilik.kps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by vili on 23/03/2017.
 */

public class MyDialog {
    private boolean chosen;
    private int choice;

    public MyDialog(Context context, String title, String[] options) {
        chosen = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        choice = which;
                        chosen = true;
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean isChosen() {
        return chosen;
    }

    public int getChoice() {
        if (chosen) {
            return choice;
        } else {
            throw new RuntimeException("Choice has not been made yet!");
        }
    }
}
