package com.tchy.syh.userAccount.account_management.mod_info;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tchy.syh.R;
import com.tchy.syh.BR;

import com.tchy.syh.databinding.AccountModInfoBinding;
import com.tchy.syh.dialog.DatePickerDialog;
import com.tchy.syh.utils.FileRealPathUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.SPUtils;

public class ModInfoFragment extends BaseFragment<AccountModInfoBinding, ModInfoVm> {

    private ImagePicker imagePicker = new ImagePicker();

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.account_mod_info;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public ModInfoVm initViewModel() {
        return new ModInfoVm(getContext());
    }

    ActionSheetDialog genderDialog;
    DatePickerDialog datePickerDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imagePicker.setTitle("上传图片");
        // 设置是否裁剪图片
        imagePicker.setCropImage(true);
        binding.headImg.setOnClickListener(v -> {
            startCameraOrGallery(binding.iv);
        });
        me.goldze.mvvmhabit.bus.Messenger.getDefault().register(getContext(), "myRefresh", new BindingAction() {
            @Override
            public void call() {
                viewModel.birthday.set(SPUtils.getInstance().getString("birthday"));
                viewModel.gender.set(SPUtils.getInstance().getString("sex"));
                viewModel.nickname.set(SPUtils.getInstance().getString("nickname"));
//                viewModel.avatar.set(SPUtils.getInstance().getString("avatar"));
            }
        });
        Messenger.getDefault().register(getContext(), "dateDialog", DatePickerDialog.DateBean.class, new BindingConsumer<DatePickerDialog.DateBean>() {
            @Override
            public void call(DatePickerDialog.DateBean s) {
                viewModel.birthday.set(s.dateStr);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                viewModel.birthdayVal.set(String.valueOf( sdf.format(s.date)));
//                viewModel.birthdayVal.set(String.valueOf(s.date.getTime() / 1000));
                viewModel.modBirthday.execute();

            }
        });

        binding.tvBirthday.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog();
            Bundle bundle = new Bundle();
            String date = viewModel.birthday.get();
            if (date.equals("0000-00-00")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.format(Calendar.getInstance().getTime());

            }
            bundle.putString("date", date);
            datePickerDialog.setArguments(bundle);
            datePickerDialog.show(getChildFragmentManager(), "");

        });
        binding.tvGender.setOnClickListener(v -> {
            genderDialog = new ActionSheetDialog(getContext(), new String[]{"保密", "男", "女"}, null);
            genderDialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            viewModel.gender.set("保密");
                            break;
                        case 1:
                            viewModel.gender.set("男");
                            break;
                        case 2:
                            viewModel.gender.set("女");
                            break;
                    }
                    genderDialog.cancel();
                    viewModel.modGender.execute();
                }
            });
            genderDialog.show();
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
                                    public void onCropImage(Uri imageUri) {
                                        String path = FileRealPathUtil.getRealPathFromUri(getContext(), imageUri);
                                        Bitmap b = bitmapCompress(path);
                                        URI imagePath = saveBitmap(b);
                                        v.setBackground(null);
                                        DrawableTransitionOptions transitionOptions = new DrawableTransitionOptions()
                                                .crossFade(500);
                                        Glide.with(getContext()).asDrawable().load(b).apply((new RequestOptions()).circleCrop().encodeQuality(50).diskCacheStrategy(DiskCacheStrategy.NONE)).transition(transitionOptions).into(v);
//                                        v.setImageBitmap(b);

                                        viewModel.picFrontUri = imagePath;
                                        viewModel.modAvatar.execute();
                                    }
                                };
                                if (which == 0) {
                                    // 从相册中选取图片

                                    imagePicker.startGallery(ModInfoFragment.this, callback);
                                } else {
                                    // 拍照
                                    imagePicker.startCamera(ModInfoFragment.this, callback);
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
    public void onDestroy() {
        super.onDestroy();
        datePickerDialog = null;
    }
}
