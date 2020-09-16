package com.tchy.syh.common;


import android.content.Context;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.tchy.syh.R;
import com.tchy.syh.custom.CustomDivider;
import com.tchy.syh.custom.CustomLineManagers;
import com.tchy.syh.custom.CustomRoundCornerImageView;
import com.tchy.syh.custom.CustomRoundCornerTextView;
import com.tchy.syh.custom.CustomRoundTopCornerImageView;
import com.tchy.syh.custom.CustomWrapContentLayoutManager;
import com.tchy.syh.custom.toolbar.CustomToolbar;
import com.tchy.syh.custom.edittext.CustomEditText;
import com.rd.PageIndicatorView;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import me.goldze.mvvmhabit.binding.command.BindingCommand;
import q.rorbin.badgeview.QBadgeView;


public class CommonViewAdapter {


    @BindingAdapter(
            value = {"tintColor", "drawSize"},
            requireAll = false
    )
    public static void tintColor(TextView v, int tintColor, int size) {
        if (size <= 0) {
            size = 20;
        }
        Drawable left = v.getCompoundDrawables()[0];
        if (left != null) {
            left.mutate().setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
            left.setBounds(0, 0, dip2px(v.getContext(), size), dip2px(v.getContext(), size));

        }


        Drawable top = v.getCompoundDrawables()[1];
        if (top != null) {
            top.mutate().setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
            top.setBounds(0, 0, dip2px(v.getContext(), size), dip2px(v.getContext(), size));


        }


        Drawable right = v.getCompoundDrawables()[2];
        if (right != null) {
            right.mutate().setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
            right.setBounds(0, 0, dip2px(v.getContext(), size), dip2px(v.getContext(), size));

        }


        Drawable bottom = v.getCompoundDrawables()[3];
        if (bottom != null) {
            bottom.mutate().setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
            bottom.setBounds(0, 0, dip2px(v.getContext(), size), dip2px(v.getContext(), size));

        }

        v.setCompoundDrawables(left, top, right, bottom);

    }

    @BindingAdapter(
            value = {"url", "placeholderRes"},
            requireAll = false
    )
    public static void setImageUri(ImageView imageView, String url, int placeholderRes) {

        if (!TextUtils.isEmpty(url)) {

            DrawableTransitionOptions transitionOptions = new DrawableTransitionOptions()
                    .crossFade(500);
            Glide.with(imageView.getContext()).asDrawable().load(url).apply((new RequestOptions()).placeholder(new ColorDrawable(Color.parseColor("#eeeeee"))).encodeQuality(5).diskCacheStrategy(DiskCacheStrategy.NONE)).transition(transitionOptions).into(imageView);
        }


    }
    @BindingAdapter(
            value = {"urlCache", "placeholderRes"},
            requireAll = false
    )
    public static void urlCache(ImageView imageView, String url, int placeholderRes) {

        if (!TextUtils.isEmpty(url)) {

            DrawableTransitionOptions transitionOptions = new DrawableTransitionOptions()
                    .crossFade(500);
            Glide.with(imageView.getContext()).asDrawable().load(url).apply((new RequestOptions()).placeholder(new ColorDrawable(Color.parseColor("#eeeeee"))).encodeQuality(50).diskCacheStrategy(DiskCacheStrategy.NONE)).transition(transitionOptions).into(imageView);
        }


    }
    @BindingAdapter(
            value = {"divider"},
            requireAll = false
    )
    public static void divider(RecyclerView rv, boolean divider) {
        if (divider)
            rv.addItemDecoration(new CustomDivider(rv.getContext()));

    }
    @BindingAdapter({"lineManager"})
    public static void setLineManager(RecyclerView recyclerView, CustomLineManagers.LineManagerFactory lineManagerFactory) {
        recyclerView.addItemDecoration(lineManagerFactory.create(recyclerView));
    }
    @BindingAdapter(
            value = {"topCornerUrl"},
            requireAll = false
    )
    public static void topCornerUrl(CustomRoundTopCornerImageView imageView, String url) {

        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片

            DrawableTransitionOptions transitionOptions = new DrawableTransitionOptions()
                    .crossFade(500);
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(new RequestOptions().encodeQuality(10).skipMemoryCache(true).placeholder(new ColorDrawable(Color.parseColor("#eeeeee"))).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(imageView);
        }
    }

    @BindingAdapter(
            value = {"cornerUrl"},
            requireAll = false
    )
    public static void cornerUrl(CustomRoundCornerImageView imageView, String url) {

        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(new RequestOptions().encodeQuality(50))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            imageView.setDrawable(resource);
                        }
                    });

        }
    }

    @BindingAdapter(
            value = {"cImgSrc", "placeHolder"},
            requireAll = false
    )
    public static void cImgSrc(ImageView imageView, String url, int id) {
        if (!TextUtils.isEmpty(url)) {
            DrawableTransitionOptions transitionOptions = new DrawableTransitionOptions()
                    .crossFade(500);
            Glide.with(imageView.getContext()).asDrawable().load(url).apply((new RequestOptions()).circleCrop().fallback(id).encodeQuality(50).diskCacheStrategy(DiskCacheStrategy.NONE)).transition(transitionOptions).into(imageView);
        }

    }

    @BindingAdapter(
            value = {"cornerImgSrc", "placeHolder"},
            requireAll = false
    )
    public static void cornerImgSrc(ImageView imageView, String url, int id) {
        if (!TextUtils.isEmpty(url)) {
            DrawableTransitionOptions transitionOptions = new DrawableTransitionOptions()
                    .crossFade(500);
            Glide.with(imageView.getContext()).asDrawable().load(url).apply((new RequestOptions()).transform(new RoundedCorners(10)).fallback(id).encodeQuality(50).diskCacheStrategy(DiskCacheStrategy.NONE)).transition(transitionOptions).into(imageView);
        }

    }

    @BindingAdapter(
            value = {"urlNoAnim"},
            requireAll = false
    )
    public static void urlNoAnim(ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {

            Glide.with(imageView.getContext()).asDrawable().load(url).apply((new RequestOptions()).placeholder(new ColorDrawable(Color.parseColor("#eeeeee"))).encodeQuality(50).diskCacheStrategy(DiskCacheStrategy.NONE)).into(imageView);
        }

    }

    @BindingAdapter(
            value = {"danger"},
            requireAll = false
    )
    public static void danger(TextView v, boolean b) {
        if (b) {
            v.setTextColor(v.getContext().getResources().getColor(R.color.colorAccentThemeLight));
        }

    }

    @BindingAdapter(
            value = {"icon"},
            requireAll = false
    )
    public static void setImageUri(ImageView imageView, int resid, int placeholderRes) {
        if (resid != 0) {
            imageView.setImageResource(resid);
        }

    }

    @BindingAdapter(
            value = {"onCheckedChangedCommand"},
            requireAll = false
    )
    public static void onCheckedChangedCommand(RadioGroup radioGroup, final BindingCommand<Integer> bindingCommand) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                bindingCommand.execute(group.indexOfChild(radioButton));
            }
        });
    }

    @BindingAdapter(
            value = {"cChecked", "direction"},
            requireAll = false
    )
    public static void onCheckedChangedCommand(TextView box, boolean cVal, int direction) {
        //direction: 0-left 1-top 2-right 3-bottom
        Drawable drawable;
        if (cVal) {
            drawable = box.getContext().getResources().getDrawable(R.drawable.radio_checked);
        } else {
            drawable = box.getContext().getResources().getDrawable(R.drawable.radio_default);
        }


        switch (direction) {
            case 0:
                box.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                break;
            case 1:
                box.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

                break;
            case 2:
                box.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

                break;
            case 3:
                box.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);

                break;

        }
    }


    @BindingAdapter(value = {"imgSrc"}, requireAll = false)
    public static void imgSrc(ImageView iv, final boolean value) {
        Log.d("sort", "call: " + value);
        if (value) {
            iv.setImageDrawable(iv.getContext().getResources().getDrawable(R.mipmap.visible_password));
        } else {
            iv.setImageDrawable(iv.getContext().getResources().getDrawable(R.mipmap.invisible_password));
        }
    }


    //    @BindingAdapter(value = {"inputType"}, requireAll = false)
//    public static void inputType(View view, final int value) {
//
//        if(view instanceof ViewGroup){
//            for(int i=0;i<((ViewGroup) view).getChildCount();i++){
//                View child=((ViewGroup) view).getChildAt(i);
//                if(child instanceof EditText){
//                    ((EditText) child).setInputType(value);
//                }
//            }
//        }
//
//    }
    @BindingAdapter(value = {"text"}, requireAll = false)
    public static void text(CustomEditText view, final ObservableField<String> observable) {
        if (observable != null) {
            view.binding.getVm().text = observable;
        }
    }

    @BindingAdapter(value = {"html"}, requireAll = false)
    public static void text(HtmlTextView view, final String s) {
        if (s != null) {
            HtmlHttpImageGetter htmlHttpImageGetter = new HtmlHttpImageGetter(view);
            htmlHttpImageGetter.enableCompressImage(true);
            view.setHtml(s, htmlHttpImageGetter);
        }
    }

    @BindingAdapter(value = {"do"}, requireAll = false)
    public static void subObserver(CustomEditText view, final BindingCommand bindingCommand) {
        if (bindingCommand != null) {
            view.binding.getVm().SubObserve = bindingCommand;
        }

    }

    @BindingAdapter(value = {"playState"}, requireAll = false)
    public static void playState(ImageView view, boolean value) {
        if (value)
            view.setImageResource(R.mipmap.pause);
        else
            view.setImageResource(R.mipmap.play_red_bg);


    }

    @BindingAdapter(value = {"hasNotify"}, requireAll = false)
    public static void hasNotify(CustomToolbar view, boolean value) {
        if (value) {
            new QBadgeView(view.getContext())
                    .bindTarget(view.getRightButton())
                    .setBadgeText("")
                    .setBadgeGravity(Gravity.END | Gravity.TOP)
                    .setBadgePadding(6, true)
                    .setGravityOffset(5, 5, true);

        }

    }

    @BindingAdapter(value = {"searchText"}, requireAll = false)
    public static void searchText(CustomToolbar view, final ObservableField<String> observable) {
        if (observable != null) {
        }

    }

    @BindingAdapter(value = {"bannerIndex"}, requireAll = false)
    public static void bannerIndex(ViewPager view, int count) {
        view.setCurrentItem(count);

    }

    @BindingAdapter(value = {"pageCount"}, requireAll = false)
    public static void pageCount(PageIndicatorView view, int count) {
        view.setCount(count);

    }

    @BindingAdapter(value = {"onCheckedChange"}, requireAll = false)
    public static void onCheckedChange(RadioButton view, int id) {
        Drawable drawable = view.getContext().getDrawable(id);
        view.setOnCheckedChangeListener((v, b) -> {
            if (b) {
                v.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
            } else {
                v.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        });

    }

    @BindingAdapter("wrapLayoutManager")
    public static void wrapLayoutManager(RecyclerView recyclerView, boolean flag) {
        if (flag)
            recyclerView.setLayoutManager(new CustomWrapContentLayoutManager(recyclerView.getContext(), LinearLayout.VERTICAL, false));


    }

    @BindingAdapter("loadMoreFinish")
    public static void loadFinish(TwinklingRefreshLayout wrapper, boolean flag) {
        if (flag)
            wrapper.finishLoadmore();


    }


    @BindingAdapter("refreshFinish")
    public static void refreshFinish(TwinklingRefreshLayout wrapper, boolean flag) {
        if (flag) {
            wrapper.finishRefreshing();
        }


    }

    @BindingAdapter(value = {"drawableid"}, requireAll = false)
    public static void drawableid(ImageView imageView, int id) {

        imageView.setImageResource(id);
    }

    @BindingAdapter(value = {"imgBg"}, requireAll = false)
    public static void imgBg(CustomRoundCornerTextView imageView, String url) {

        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(new RequestOptions().encodeQuality(50))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            imageView.setDrawable(resource);
                        }
                    });

        } else {
            imageView.setDrawable(imageView.getResources().getColor(R.color.colorAccentThemeLight));
        }
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @BindingAdapter(value = {"drawTop", "imgSize"}, requireAll = false)
    public static void drawableTop(TextView v, int id, int size) {
        Drawable drawable = v.getContext().getDrawable(id);
        if (size > 0) {
            drawable.setBounds(0, 0, dip2px(v.getContext(), size), dip2px(v.getContext(), size));
        }
        v.setCompoundDrawables(null, drawable, null, null);
    }

    @BindingAdapter(value = {"drawLeft", "imgSize"}, requireAll = false)
    public static void drawLeft(TextView v, int id, int size) {
        Drawable drawable = v.getContext().getDrawable(id);
        if (size > 0) {
            drawable.setBounds(0, 0, dip2px(v.getContext(), size), dip2px(v.getContext(), size));
        }

        v.setCompoundDrawables(drawable, v.getCompoundDrawables()[1], v.getCompoundDrawables()[2], v.getCompoundDrawables()[3]);
    }

    @BindingAdapter(value = {"onRadioChecked"}, requireAll = false)
    public static void onRadioChecked(RadioButton v, boolean isc) {

        v.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    v.setCompoundDrawables(v.getCompoundDrawables()[0], v.getCompoundDrawables()[1], v.getContext().getResources().getDrawable(R.drawable.radio_checked), v.getCompoundDrawables()[3]);
                else
                    v.setCompoundDrawables(v.getCompoundDrawables()[0], v.getCompoundDrawables()[1], v.getContext().getResources().getDrawable(R.drawable.radio_default), v.getCompoundDrawables()[3]);


            }
        });
    }

    @BindingAdapter(value = {"selectCenter"}, requireAll = false)
    public static void setSelected(ViewPager view, int count) {
        if (count > 1) {
            view.setCurrentItem(count/2);
        }

    }

    @BindingAdapter(value = {"topImage"}, requireAll = false)
    public static void topImage(RadioButton view, int id) {
        view.setTextColor(view.getResources().getColor(R.color.btnTextColorThemeLight));
        Drawable drawable = view.getResources().getDrawable(id);
        drawable.setBounds(0, 0, (int) view.getResources().getDimension(R.dimen.bottom_icon_size), (int) view.getResources().getDimension(R.dimen.bottom_icon_size));
        if (view.isChecked())
            drawable.setTint(view.getResources().getColor(R.color.colorAccentThemeLight));
        else
            drawable.setTint(view.getResources().getColor(R.color.btnTextColorThemeLight));

        view.setCompoundDrawables(null, drawable, null, null);
        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    drawable.setTint(view.getResources().getColor(R.color.colorAccentThemeLight));
                    view.setCompoundDrawables(null, drawable, null, null);
                } else {
                    drawable.setTint(view.getResources().getColor(R.color.btnTextColorThemeLight));
                    view.setCompoundDrawables(null, drawable, null, null);
                }
            }
        });
    }


}
