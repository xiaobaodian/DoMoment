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
    private Button doOK;
    private Button doNo;
    View.OnClickListener onOKClickListener = null;
    View.OnClickListener onNoClickListener = null;
    ConstraintLayout root;

    public MaskDialog(Context context, String title, String notice){

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View contentView = layoutInflater.inflate(R.layout.popupwindow_dialog, null, false);
        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失，这里因为PopupWindow填充了整个窗口，所以这句代码就没用了
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        //进入退出的动画
        popupWindow.setAnimationStyle(R.style.PopupDialog);
        //popupWindow.setAnimationStyle(R.style.MyPopWindowAnim);

        root = contentView.findViewById(R.id.Root);
        root.setOnClickListener(v -> {
            popupWindow.dismiss();
        });
        popupWindow.setOnDismissListener(() -> {
            setBackgroundAlpha(1f);
        });

        ConstraintLayout dialog = contentView.findViewById(R.id.Dialog);
        //dialog.setAnimationStyle(R.style.PopupDialog);

        doOK = new Button(context);
        doNo = new Button(context);
        Button btnOK = contentView.findViewById(R.id.YesButton);
        btnOK.setOnClickListener(view -> {
            doOK.performClick();
            popupWindow.dismiss();
        });
        Button btnNo = contentView.findViewById(R.id.NoButton);
        btnNo.setOnClickListener(view -> {
            doNo.performClick();
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

    public void setOnOKClickListener(View.OnClickListener onClickListener){
        doOK.setOnClickListener(onClickListener);
    }
    public void setOnNoClickListener(View.OnClickListener onClickListener){
        onNoClickListener = onClickListener;
        doNo.setOnClickListener(onClickListener);
    }

    public void showAtLocation(View parent){
        setBackgroundAlpha(0.5f);
        //popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        int h = root.getTop();
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, h);
    }

    public void dismiss(){
        popupWindow.dismiss();
    }

    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = DoMoment.getMainActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        DoMoment.getMainActivity().getWindow().setAttributes(lp);
    }
}
