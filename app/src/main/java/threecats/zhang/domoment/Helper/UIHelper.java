package threecats.zhang.domoment.Helper;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import threecats.zhang.domoment.DataStructures.CategoryBase;
import threecats.zhang.domoment.DataStructures.CustomCategory;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.ENUM.EditorMode;
import threecats.zhang.domoment.R;
import threecats.zhang.domoment.adapter.CategoryBackgroundAdapter;

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
        Context context = DoMoment.getContext();
        android.widget.Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
