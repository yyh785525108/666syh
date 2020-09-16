package com.tchy.syh.my.feedback;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.Observable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.FeedbackFragBinding;
import com.tchy.syh.utils.FileRealPathUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.ConvertUtils;


public class FeedBackFragment extends BaseFragment<FeedbackFragBinding, FeedBackVM> {
    private ImagePicker imagePicker = new ImagePicker();
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.feedback_frag;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public FeedBackVM initViewModel() {
        return new FeedBackVM(this.getContext());
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imagePicker.setTitle("上传图片");
        // 设置是否裁剪图片
        imagePicker.setCropImage(true);
        binding.add.setOnClickListener(v->{
             LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ConvertUtils.dp2px(70),ConvertUtils.dp2px(70) );
            lp.setMarginStart(ConvertUtils.dp2px(20));
            ImageView imageView=new ImageView(this.getContext());
            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setOnClickListener(s->{

                viewModel.imgList.remove( binding.imgList.indexOfChild(imageView));
                binding.imgList.removeView(imageView);

            });
            startCameraOrGallery(imageView);



        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(this, requestCode, resultCode, data);
    }


    private void startCameraOrGallery(final ImageView v) {
        RxPermissions rxPermissions = new RxPermissions(getActivity());

        rxPermissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe((per) -> {
            if (per.granted) {
                new AlertDialog.Builder(getActivity()).setTitle("上传图片")
                        .setItems(new String[]{"从相册中选取图片", "拍照"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 回调
                                ImagePicker.Callback callback = new ImagePicker.Callback() {
                                    @Override
                                    public void onPickImage(Uri imageUri) {

                                    }
                                    @Override
                                    public void cropConfig(CropImage.ActivityBuilder builder){
                                        // 默认配置
                                        builder.setMultiTouchEnabled(false)
                                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                                .setRequestedSize(640, 640)
                                                .setAspectRatio(5, 5);
                                    }
                                    @Override
                                    public void onCropImage(Uri imageUri) {
                                        String path = FileRealPathUtil.getRealPathFromUri(getContext(), imageUri);
                                        Bitmap b = bitmapCompress(path);
                                        URI imagePath = saveBitmap(b);
                                        v.setBackground(null);
                                        DrawableTransitionOptions transitionOptions = new DrawableTransitionOptions()
                                                .crossFade(500);
                                        Glide.with(getContext()).asDrawable().load(b).apply((new RequestOptions()).encodeQuality(50).diskCacheStrategy(DiskCacheStrategy.NONE)).transition(transitionOptions).into(v);
//                                        v.setImageBitmap(b);

                                      viewModel.imgList.add(imagePath);
                                        binding.imgList.addView(v,binding.imgList.getChildCount()-1 );
                                    }
                                };
                                if (which == 0) {
                                    // 从相册中选取图片

                                    imagePicker.startGallery(FeedBackFragment.this, callback);
                                } else {
                                    // 拍照
                                    imagePicker.startCamera(FeedBackFragment.this, callback);
                                }
                            }
                        })
                        .show()
                        .getWindow()
                        .setGravity(Gravity.BOTTOM);

            }

        });

    }

    public Bitmap bitmapCompress(String path) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
//        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        newOpts.inSampleSize = 2;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);
//        Matrix matrix = new Matrix();
//        if(bitmap.getWidth()<bitmap.getHeight()){
//            matrix.postRotate(-90);
//        }

//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }

    public URI saveBitmap(Bitmap bitmap) {
        String path = getContext().getCacheDir().getPath();

        File file = new File(path, System.currentTimeMillis() + ".jpg");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            System.out.println("___________保存的__sd___下_______________________");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.toURI();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
