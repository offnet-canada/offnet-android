package ca.offnet.offnetnews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class ErrorDialog {
    private Context context;
    private String code;
    private String message;
    private String title;
    private AlertDialog.Builder builder;
    private AlertDialog popup;

    public ErrorDialog(Context c, String co, String mess) {
        context = c;
        code = co;
        message = mess;
        if (code.equals("000")) {
            title = "Trial over/Out of credits";
        } else {
            title = "An Error Occurred";
        }
        build();
        display();
    }

    private void build() {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);

        if (code.equals("000")) {
            builder.setPositiveButton(
                    "Buy Credits",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://app.offnet.ca"));
                            context.startActivity(browserIntent);
                        }
                    });

            builder.setNegativeButton(
                    "Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        } else {
            builder.setPositiveButton(
                    "Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }
        popup = builder.create();
    }

    public void display() {
        popup.show();
    }
}
