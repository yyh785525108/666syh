package com.tchy.syh.custom.toolbar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tchy.syh.R;
import com.tchy.syh.databinding.CustomToolBarBinding;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

@SuppressLint("RestrictedApi")
public class CustomToolbar extends Toolbar {
    CustomToolBarBinding binding;
    private LayoutInflater mInflater;

    private View mView;
    private ImageView mLeftButton;
    private TextView mTextTitle;
    private TextView mSearchView;
    private ImageView mRightButton;

    public void setRightClickCommand(BindingCommand rightClickCommand) {
        this.rightClickCommand = rightClickCommand;
        binding.getVm().setRightObserve(rightClickCommand);
    }

    BindingCommand rightClickCommand;

    public void setLeftClickCommand(BindingCommand leftClickCommand) {
        this.leftClickCommand = leftClickCommand;
        binding.getVm().setLeftObserve(leftClickCommand);

    }

    BindingCommand leftClickCommand;

    public CustomToolbar(Context context) {
        this(context, null);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackground(null);
        initView();
        //设置toolbar的边距

        if (attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CustomToolbar, defStyleAttr, 0);
            type = a.getInteger(R.styleable.CustomToolbar_searchType, 0);


            final Drawable rightIcon = a.getDrawable(R.styleable.CustomToolbar_rightButtonIcon);
            //一定要在这里进行条件判断
            if (rightIcon != null) {
                //setNavigationIcon(navIcon);
                setRightButtonIcon(rightIcon);
                showRightButton(true);

            }

            final Drawable leftIcon = a.getDrawable(R.styleable.CustomToolbar_leftButtonIcon);
            //一定要在这里进行条件判断
            if (leftIcon != null) {
                setLeftButtonIcon(leftIcon);
                showLeftButton(true);
            }

            //默认false
            boolean isShowSearchView = a.getBoolean(R.styleable.CustomToolbar_isShowSearchView, false);
            //如果isShowSearchView为true，把Title隐藏
            if (isShowSearchView) {
                showSearchView();
                hideTitleView();

            }
            boolean inverseTitleColor = a.getBoolean(R.styleable.CustomToolbar_inverseTitleColor, true);
            //如果isShowSearchView为true，把Title隐藏
            if (inverseTitleColor) {
               this.mTextTitle.setTextColor(getResources().getColor(R.color.titleColor));
               if(this.mLeftButton.getDrawable()!=null)
                    this.mLeftButton.getDrawable().setTint(Color.BLACK);
               this.binding.root.setBackgroundResource(R.drawable.line_bottom_grey_bg);
            }else{
                this.mTextTitle.setTextColor(getResources().getColor(R.color.white));
                if(this.mLeftButton.getDrawable()!=null)
                    this.mLeftButton.getDrawable().setTint(Color.WHITE);
                this.binding.root.setBackgroundResource(R.drawable.filled_gradient_bg);
            }
            CharSequence toolbarTitle = a.getText(R.styleable.CustomToolbar_toolbarTitle);
            if (toolbarTitle != null) {
                setTitle(toolbarTitle);
            }

            a.recycle();
        }
        CustomToolbarVM vm=    new CustomToolbarVM(this.getContext(),type);

        binding.setVm(vm);
    }

    private void initView() {

        if (mView == null) {

            mInflater = LayoutInflater.from(getContext());
            binding= DataBindingUtil.inflate(mInflater,R.layout.custom_tool_bar,this,true);
            mTextTitle = binding.toolbarTitle;
            mSearchView = binding.toolbarSearchview;

            mRightButton = binding.toolbarRightButton;
            mLeftButton=binding.toolbarLeftButton;




        }

    }
    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    //对右边的Button进行Background设置
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setRightButtonIcon(Drawable icon) {

        if (mRightButton != null) {

            mRightButton.setImageDrawable(icon);
            mRightButton.setVisibility(VISIBLE);
        }

    }

    public void setRightButtonIcon(int icon) {

        setRightButtonIcon(getResources().getDrawable(icon));
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setLeftButtonIcon(Drawable icon) {

        if (mLeftButton != null) {

            mLeftButton.setImageDrawable(icon);
            mLeftButton.setVisibility(VISIBLE);
        }

    }
    public void setLeftButtonIcon(int icon) {

        setLeftButtonIcon(getResources().getDrawable(icon));
    }


    public void setRightButtonText(CharSequence text) {
//        mRightButton.setText(text);
        mRightButton.setVisibility(VISIBLE);
    }


    public void setRightButtonText(int id) {
        setRightButtonText(getResources().getString(id));
    }


    public ImageView getRightButton() {

        return this.mRightButton;
    }

    //设置标题
    @Override
    public void setTitle(int resId) {

        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {

        if (mTextTitle != null) {
            mTextTitle.setText(title);
            showTitleView();
            hideSearchView();
        }

    }
    public void showLeftButton(boolean flag) {

        if (mLeftButton != null)
            mLeftButton.setVisibility(flag?VISIBLE:GONE);

    }
    public void showRightButton(boolean flag) {

        if (mRightButton != null)
            mRightButton.setVisibility(flag?VISIBLE:GONE);

    }

    //提供外接方法，实现EditView和TextView的转换
    public void showSearchView() {

        if (mSearchView != null)
            mSearchView.setVisibility(VISIBLE);

    }

    public void hideSearchView() {
        if (mSearchView != null)
            mSearchView.setVisibility(GONE);
    }

    public void showTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(VISIBLE);
    }


    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);

    }
    public void setToolbarTitle(String s){
        if(this.mTextTitle!=null)
            setTitle(s);
    }


    public int type =0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
