package com.tchy.syh.custom.edittext;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.TintTypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tchy.syh.R;
import com.tchy.syh.databinding.CustomEditTextBinding;

@SuppressLint("RestrictedApi")
public class CustomEditText extends RelativeLayout {
    public static final String INPUT_TYPE_NUMBER="number";
    public static final String INPUT_TYPE_TEXT="text";
    public static final String INPUT_TYPE_PASSWORD="password";
    public CustomEditTextBinding binding;
    private LayoutInflater mInflater;
    RelativeLayout mView;
    private Button mSub;
    private ImageView mIcon;
    private TextView mLabel;
    private EditText mEdit;
    private ImageView mClear;
    private ImageView mVisible;
    private boolean mIsSupportClear;


    public CustomEditText(Context context) {
        this(context, null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }



    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

        if (attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CustomEditText, defStyleAttr, 0);


            boolean isSupportClear = a.getBoolean(R.styleable.CustomEditText_isSupportClear, true);
            boolean isSupportSub = a.getBoolean(R.styleable.CustomEditText_isSupportSub, false);
            boolean isSupportVisible = a.getBoolean(R.styleable.CustomEditText_isSupportVisible, false);
            final Drawable icon = a.getDrawable(R.styleable.CustomEditText_icon);
            CharSequence label = a.getText(R.styleable.CustomEditText_label);
            CharSequence text = a.getText(R.styleable.CustomEditText_text);
            CharSequence hint = a.getText(R.styleable.CustomEditText_hint);
            CharSequence subName = a.getText(R.styleable.CustomEditText_subName);
            int maxLength = a.getInteger(R.styleable.CustomEditText_maxLength, 11);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(maxLength);
            mEdit.setFilters(filterArray);
            String inputType = a.getString(R.styleable.CustomEditText_inputType);
            switch (inputType){
                case INPUT_TYPE_NUMBER:
                    this.mEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case INPUT_TYPE_PASSWORD:
                    this.mEdit.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    InputFilter[] filters = {new InputFilter.LengthFilter(16)};
                    mEdit.setFilters(filters);


                    break;
                default: INPUT_TYPE_TEXT:
                    this.mEdit.setInputType(InputType.TYPE_CLASS_TEXT);

            }
            mIsSupportClear = isSupportClear;
            if (isSupportClear) {
                mClear.setVisibility(VISIBLE);
            } else {
                mClear.setVisibility(INVISIBLE);

            }
            if (isSupportSub) {
                mSub.setVisibility(VISIBLE);
            } else {
                mSub.setVisibility(GONE);

            }
            if (isSupportVisible) {
                mVisible.setVisibility(VISIBLE);
            } else {
                mVisible.setVisibility(GONE);
            }
            if (!TextUtils.isEmpty(label)) {
                mLabel.setText(label);
                mLabel.setVisibility(VISIBLE);
            } else {
                mLabel.setVisibility(GONE);
            }
            if (!TextUtils.isEmpty(text)) {
                mEdit.setText(text);
            }
            if (!TextUtils.isEmpty(hint)) {
                mEdit.setHint(hint);
            }
            if (!TextUtils.isEmpty(subName)) {
                mSub.setText(subName);
            }
            if (icon != null) {
                mIcon.setImageDrawable(icon);
                mIcon.setVisibility(VISIBLE);
            } else {
                mIcon.setVisibility(GONE);
            }

            a.recycle();


        }
        binding.setVm(new CustomEditTextVM(getContext(), mIsSupportClear));

        binding.getVm().uc.pSwitchObservable.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable sender, int propertyId) {
                        if (binding.getVm().uc.pSwitchObservable.get()) {
                            //密码可见
                            binding.visible.setImageResource(R.mipmap.invisible_password);
                            binding.et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            binding.et.setSelection(binding.et.length());
                        } else {
                            //密码不可见
                            binding.visible.setImageResource(R.mipmap.visible_password);
                            binding.et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            binding.et.setSelection(binding.et.length());


                        }
                    }
                }
        );
    }


    private void initView() {

        if (mView == null) {


            mInflater = LayoutInflater.from(getContext());
            binding = DataBindingUtil.inflate(mInflater, R.layout.custom_edit_text, this, true);


            mClear = binding.clear;
            mVisible = binding.visible;
            mIcon = binding.icon;
            mEdit = binding.et;
            mLabel = binding.label;
            mSub = binding.subBtn;

            mClear.setVisibility(VISIBLE);
            mLabel.setVisibility(GONE);
            mIcon.setImageDrawable(getResources().getDrawable(R.mipmap.account));
//            addView(binding.getRoot(),new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        }

    }
    public void setParam(String s){
        binding.getVm().param.set(s);
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d("sort", "onDetachedFromWindow: ");
    }
}
