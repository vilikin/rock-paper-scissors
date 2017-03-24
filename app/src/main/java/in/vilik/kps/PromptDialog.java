package in.vilik.kps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.widget.EditText;

/**
 * Created by vili on 23/03/2017.
 */

public class PromptDialog {
    private PromptDialog() {}

    public static void open(Context context, String title, final Listener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        final EditText input = new EditText(context);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onComplete(input.getText().toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                listener.onComplete(null);
            }
        });

        builder.show();
    }

    public interface Listener {
        void onComplete(@Nullable String text);
    }
}
