package com.kay.demo.fingerprint.fingerprintSix.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kay.demo.fingerprint.R;


/**
 * Created by kaylau on 2017/2/24.
 */
public class CommonDialog extends Dialog {

    private static final int DIALOG_MARGIN = 80;

    public CommonDialog(Context context) {
        this(context, R.style.custom_dialog);
    }

    public CommonDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {

        private Context mContext;
        private boolean mCancelable;
        private boolean messageIsCenter = true;
        private String mTitle;
        private String mMessage;
        private CharSequence mNegativeButtonText;
        private CharSequence mPositiveButtonText;
        private OnClickListener mNegativeButtonClickListener;
        private OnClickListener mPositiveButtonClickListener;

        private View ll_dialog_confirm, ll_dialog_cancel;
        private TextView mNegativeButton, mPositiveButton;
        private ScrollView sv_dialog;

        private int margin;

        public Builder(Context context) {
            this.mContext = context;
            this.setCancelable(false);
            this.setMargin(DIALOG_MARGIN);
        }

        public Builder setMargin(int margin) {
            this.margin = margin;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setMessageCenter(boolean messageIsCenter) {
            this.messageIsCenter = messageIsCenter;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.mNegativeButtonText = negativeButtonText;
            this.mNegativeButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.mPositiveButtonText = positiveButtonText;
            this.mPositiveButtonClickListener = listener;
            return this;
        }

        public CommonDialog show() {
            CommonDialog dialog = onCreateDialog();
            if (dialog != null) {
                dialog.setCancelable(false);
                dialog.setOwnerActivity((Activity) mContext);
                dialog.setCanceledOnTouchOutside(false);
//        	dialog.getWindow().setLayout(300, 200); //4.0以下的设备需要在 show之前 加上这个才起作用
                Activity mActivity = dialog.getOwnerActivity();
                if (mActivity != null && !mActivity.isFinishing()) {
                    dialog.show();
                    return dialog;
                }
            }
            return null;
        }

        private CommonDialog onCreateDialog() {
            final CommonDialog dialog = new CommonDialog(mContext);
            View layout = LayoutInflater.from(mContext).inflate(R.layout.layout_dialog_common, null);
            dialog.setContentView(layout);

            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();

            int dialogWidth = getDialogWidth((Activity) mContext, margin);
            layoutParams.width = dialogWidth; // 宽度

            sv_dialog = layout.findViewById(R.id.sv_dialog);
            sv_dialog.setVerticalScrollBarEnabled(false);

            TextView tv_dialog_single_btn = layout.findViewById(R.id.tv_dialog_single_btn);
            LinearLayout ll_dialog_double_btn = layout.findViewById(R.id.ll_dialog_double_btn);


            if (!TextUtils.isEmpty(mPositiveButtonText) && !TextUtils.isEmpty(mNegativeButtonText)) {
                // 双按钮
                ll_dialog_double_btn.setVisibility(View.VISIBLE);
                tv_dialog_single_btn.setVisibility(View.GONE);
                // set the confirm button
                initRightBtn(dialog, layout, null);

                // set the cancel button
                initLeftBtn(dialog, layout, null);
            } else {
                if (!TextUtils.isEmpty(mPositiveButtonText)) {
                    // 单按钮
                    ll_dialog_double_btn.setVisibility(View.GONE);
                    tv_dialog_single_btn.setVisibility(View.VISIBLE);
                    initRightBtn(dialog, layout, tv_dialog_single_btn);

                } else if (!TextUtils.isEmpty(mNegativeButtonText)) {
                    // 单按钮
                    ll_dialog_double_btn.setVisibility(View.GONE);
                    tv_dialog_single_btn.setVisibility(View.VISIBLE);
                    initLeftBtn(dialog, layout, tv_dialog_single_btn);

                } else {
                    ll_dialog_double_btn.setVisibility(View.GONE);
                    tv_dialog_single_btn.setVisibility(View.GONE);
                }
            }

            TextView textView = layout.findViewById(R.id.tv_dialog_msg);
            TextView tv_title = layout.findViewById(R.id.tv_title);
            if (messageIsCenter) {
                textView.setGravity(Gravity.CENTER);
            } else {
                textView.setGravity(Gravity.LEFT);
            }
            if (!TextUtils.isEmpty(mMessage)) {
//                textView.setText(Html.fromHtml(mMessage));
                textView.setText(mMessage);
            }
            if (!TextUtils.isEmpty(mTitle)) {
                tv_title.setText(Html.fromHtml(mTitle));
                tv_title.setVisibility(View.VISIBLE);
            } else {
                tv_title.setVisibility(View.GONE);
            }
            return dialog;
        }

        private int getDialogWidth(Activity activity, int margin) {
            Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            defaultDisplay.getMetrics(outMetrics);
            int widthPixels = outMetrics.widthPixels;
            return widthPixels - dip2px(activity, margin);
        }

        public int dip2px(Context context, float dpValue) {
            float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        private void initLeftBtn(CommonDialog dialog, View layout, TextView tv_dialog_single_btn) {
            if (tv_dialog_single_btn != null) {
                ll_dialog_cancel = layout.findViewById(R.id.ll_dialog_single_btn);
                mNegativeButton = tv_dialog_single_btn;

            } else {
                ll_dialog_cancel = layout.findViewById(R.id.ll_dialog_cancel);
                mNegativeButton = layout.findViewById(R.id.tv_dialog_cancel);
            }
            mNegativeButton.setText(mNegativeButtonText);
            ll_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mNegativeButtonClickListener != null) {
                        mNegativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                }
            });
        }

        private void initRightBtn(CommonDialog dialog, View layout, TextView tv_dialog_single_btn) {
            if (tv_dialog_single_btn != null) {
                ll_dialog_confirm = layout.findViewById(R.id.ll_dialog_single_btn);
                mPositiveButton = tv_dialog_single_btn;

            } else {
                ll_dialog_confirm = layout.findViewById(R.id.ll_dialog_confirm);
                mPositiveButton = layout.findViewById(R.id.tv_dialog_confirm);
            }
            mPositiveButton.setText(mPositiveButtonText);
            ll_dialog_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPositiveButtonClickListener != null) {
                        mPositiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                }
            });
        }
    }
}