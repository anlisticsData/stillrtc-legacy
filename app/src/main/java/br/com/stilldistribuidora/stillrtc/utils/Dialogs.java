package br.com.stilldistribuidora.stillrtc.utils;

/**
 * Created by Still Technology and Development Team on 21/04/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import br.com.stilldistribuidora.stillrtc.R;

public class Dialogs {

    private static final String TAG = Dialogs.class.getSimpleName();

    private Context context;

    public Dialogs(Context context) {
        this.context = context;
    }

    public void showDialog(String[] array, final DialogResultListener listener) {

        String title = array[0];
        String content = array[1];
        String btnCancel = array[2];
        String btnDone = array[3];

        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);

        if (content.length() > 0) {
            builder.setMessage(content);
        }

        builder.setPositiveButton(btnDone, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onResult(true);
            }
        });
        builder.setNegativeButton(btnCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onResult(false);
            }
        });

        builder.show();
    }

//    public void showDialog(String[] array, final DialogNeutralResultListener listener) {
//
//        String title = array[0];
//        final String content = array[1];
//        String btnCancel = array[2];
//        String btnDone = array[3];
//
//        String btnNeutral = null;
//        if(array.length>4){
//            btnNeutral = array[4];
//        }
//
//        AlertDialog.Builder builder =
//                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
//        builder.setTitle(title);
//
//        if (content.length() > 0) {
//            builder.setMessage(content);
//        }
//
//        if(array.length>4) {
//            builder.setNeutralButton(btnNeutral, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    listener.onNeutral(2);
//                }
//            });
//        }
//
//        builder.setPositiveButton(btnDone, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                listener.onNeutral(1);
//            }
//        });
//        builder.setNegativeButton(btnCancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                listener.onNeutral(0);
//            }
//        });
//
//        builder.show();
//    }


//    public void showDialogTextLong(String title, String text) {
//
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View dialog_layout = inflater.inflate(R.layout.dialog_text_long, null);
//        AlertDialog.Builder db = new AlertDialog.Builder(context);
//        db.setView(dialog_layout);
//
//        db.setTitle(title);
//        db.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
//
//        TextView thumbImage = (TextView) dialog_layout.findViewById(R.id.thumbImage);
//        thumbImage.setText(Utils.fromHtml(text));
//        AlertDialog dialog = db.show();
//    }
}