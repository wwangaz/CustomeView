package com.example.wangweimin.customerview.view;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.wangweimin.customerview.R;

/**
 * Created by wangweimin on 16/4/20.
 */
public class AdvanceMailCompleteTextView extends RelativeLayout {
    private final static int PADDING_RIGHT = 40;
    private final static int TEXT_EMS = 20;
    private final static String HINT_STR = "请输入邮箱地址";

    private Context mContext;
    private MailCompleteTextView mCompleteTextView;
    private ImageView mImageView;

    public AdvanceMailCompleteTextView(Context context) {
        super(context);
        mContext = context;
    }

    public AdvanceMailCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    private void initViews() {
        RelativeLayout.LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        mCompleteTextView = new MailCompleteTextView(mContext);
        mCompleteTextView.setLayoutParams(params);
        mCompleteTextView.setPadding(0, 0, PADDING_RIGHT, 0);
        mCompleteTextView.setSingleLine(true);
        mCompleteTextView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        mCompleteTextView.setFitsSystemWindows(true);
        mCompleteTextView.setEms(TEXT_EMS);
        mCompleteTextView.setHint(HINT_STR);
        mCompleteTextView.setImeOptions(EditorInfo.IME_ACTION_NEXT | EditorInfo.IME_FLAG_NO_FULLSCREEN | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mCompleteTextView.setDropDownHorizontalOffset(0);
        mCompleteTextView.setDropDownVerticalOffset(2);

        RelativeLayout.LayoutParams imageParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        imageParams.addRule(ALIGN_PARENT_RIGHT);
        imageParams.addRule(CENTER_VERTICAL);
        imageParams.rightMargin = 10;
        mImageView = new ImageView(mContext);
        mImageView.setLayoutParams(imageParams);
        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mImageView.setClickable(true);
        mImageView.setBackgroundResource(R.drawable.delete);
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCompleteTextView.setText("");
            }
        });

        this.addView(mCompleteTextView);
        this.addView(mImageView);

        mCompleteTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus && !mCompleteTextView.getText().toString().isEmpty()) {
                    //有焦点，文本不为空，显示删除按钮，关闭自动补全提示
                    mCompleteTextView.setShow(false);
                    mImageView.setVisibility(VISIBLE);
                } else if (hasFocus) {
                    //有焦点，文本为空，不显示删除按钮，开启自动补全提示
                    mCompleteTextView.setShow(true);
                    mImageView.setVisibility(GONE);
                } else {
                    //无焦点，不显示删除按钮，关闭自动补全提示，
                    mCompleteTextView.setShow(false);
                    mImageView.setVisibility(GONE);
                }
            }
        });

    }

}
