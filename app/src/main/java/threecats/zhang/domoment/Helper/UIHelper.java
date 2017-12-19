package threecats.zhang.domoment.Helper;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import threecats.zhang.domoment.App;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/11/29.
 */

public class UIHelper {

    public static AlertDialog.Builder getYNDialog(Context context, String notice){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View dialogView = layoutInflater.inflate(R.layout.dialog_information_yesorno, null, false);
        View contentView = layoutInflater.inflate(R.layout.dialog_information_yesorno,dialogView.findViewById(R.id.dialoglayout));
        TextView noticeView = dialogView.findViewById(R.id.notice);//imageView
        ImageView imageView = dialogView.findViewById(R.id.imageView);
        noticeView.setText(notice);
        imageView.setImageResource(R.drawable.ic_action_warning);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(contentView);
        return dialog;
    }

    public static void Toast(String message){
        Context context = App.getContext();
        android.widget.Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showSoftKeyboard(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        new Handler().postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.showSoftInput(taskTitle, 0);// 显示输入法,InputMethodManager.SHOW_FORCED
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        },0);
    }

    public static void closeSoftKeyboard(EditText editText){
        new Handler().postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        },0);
    }

}
