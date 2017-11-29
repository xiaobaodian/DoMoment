package threecats.zhang.domoment.Helper;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/11/29.
 */

public class MaskDialog {

    private PopupWindow popupWindow;
    private Button doBottonOK;
    private Button doBottonNo;
    private ConstraintLayout root;

    public MaskDialog(Context context, String notice){
        BuildWindow(context, "", notice);
    }

    public MaskDialog(Context context, String title, String notice){
        BuildWindow(context, title, notice);
    }

    private void BuildWindow(Context context, String title, String notice){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View contentView = layoutInflater.inflate(R.layout.popupwindow_dialog, null, false);
        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        //进入退出的动画
        popupWindow.setAnimationStyle(R.style.PopupDialog);

        root = contentView.findViewById(R.id.Root);
        root.setOnClickListener(v -> {
            popupWindow.dismiss();
        });
        popupWindow.setOnDismissListener(() -> {
            setBackgroundAlpha(1f);
        });

        doBottonOK = new Button(context);
        doBottonNo = new Button(context);
        Button btnOK = contentView.findViewById(R.id.YesButton);
        btnOK.setOnClickListener(view -> {
            doBottonOK.performClick();
            popupWindow.dismiss();
        });
        Button btnNo = contentView.findViewById(R.id.NoButton);
        btnNo.setOnClickListener(view -> {
            doBottonNo.performClick();
            popupWindow.dismiss();
        });

        TextView tvTitle = contentView.findViewById(R.id.Title);
        TextView tvNotice = contentView.findViewById(R.id.Notice);

        if (title.length() == 0) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        tvNotice.setText(notice);
    }

    public void setBtnOKOnClickListener(View.OnClickListener onClickListener){
        doBottonOK.setOnClickListener(onClickListener);
    }
    public void setBtnCancelOnClickListener(View.OnClickListener onClickListener){
        doBottonNo.setOnClickListener(onClickListener);
    }

    public void showAtLocation(View parent){
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, root.getTop());
    }

    public void dismiss(){
        popupWindow.dismiss();
    }

    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams layoutParams = DoMoment.getCurrentActivity().getWindow().getAttributes();
        layoutParams.alpha = alpha;
        DoMoment.getCurrentActivity().getWindow().setAttributes(layoutParams);
    }
}
