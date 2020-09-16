package com.tchy.syh.userAccount.account_management.realcert;

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
import android.widget.ImageView;

import com.linchaolong.android.imagepicker.ImagePicker;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.common.entity.CommonDataListResp;
import com.tchy.syh.databinding.AccountRealCertPicFragBinding;
import com.tchy.syh.userAccount.AccountApiService;
import com.tchy.syh.userAccount.AccountCommonVm;
import com.tchy.syh.userAccount.ResultFragment;
import com.tchy.syh.utils.FileRealPathUtil;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.base.ContainerActivity;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by goldze on 2017/7/17.
 * 详情界面
 */

public class RealCertPictureFragment extends BaseFragment<AccountRealCertPicFragBinding, AccountCommonVm> {


    private ImagePicker imagePicker = new ImagePicker();

    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.account_real_cert_pic_frag;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public AccountCommonVm initViewModel() {
        return new AccountCommonVm(this.getContext());
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 设置标题
        imagePicker.setTitle("上传图片");
        // 设置是否裁剪图片
        imagePicker.setCropImage(false);
        binding.picFront.setOnClickListener(v -> {
            startCameraOrGallery((ImageView) v);
        });
        binding.picBack.setOnClickListener(v -> {
            startCameraOrGallery((ImageView) v);
        });
        binding.picTake.setOnClickListener(v -> {
            startCameraOrGallery((ImageView) v);
        });

        binding.button3.setOnClickListener(v -> {

            if (picFrontUri == null) {
                ToastUtil.toastBottom("请上传正面照片");
                return;
            }
            if (picBackUri == null) {
                ToastUtil.toastBottom("请上传背面照片");
                return;
            }
            if (picTakeUri == null) {
                ToastUtil.toastBottom("请上传手持照片");
                return;
            }
            File fileFront = null;
            File fileBack = null;
            File fileTake = null;
            fileFront = new File(picFrontUri);
            fileBack = new File(picBackUri);
            fileTake = new File(picTakeUri);


            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part bodyFront =
                    MultipartBody.Part.createFormData("card_face", fileFront.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), fileFront));
            MultipartBody.Part bodyBack =
                    MultipartBody.Part.createFormData("card_back", fileBack.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), fileBack));
            MultipartBody.Part bodyTake =
                    MultipartBody.Part.createFormData("card_hand", fileTake.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), fileTake));


            HashMap<String, Object> params = new HashMap();
            params.put("card_no", getArguments().getString("id"));
            params.put("truename", getArguments().getString("name"));
            params.put("access_token", SPUtils.getInstance().getString("token"));
            Observable<CommonDataListResp> observable = RetrofitClient.getInstance().create(AccountApiService.class)
                    .upload_idcard(FormdataRequestUtil.getFormDataRequestParams(params), bodyFront, bodyBack, bodyTake)
                    .compose(RxUtils.bindToLifecycle(getContext())) //请求与View周期同步
                    .compose(RxUtils.schedulersTransformer()) //线程调度
                    .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                    .doOnSubscribe(disposable -> {
                    });

            observable.subscribe(res -> {


                if(res.getStatus()!=1){
                    ToastUtil.toastBottom(res.getInfo());
                    return;
                }
                ToastUtil.toastBottom("提交成功");
                Intent intent=new Intent();
                intent.setClass(getContext(),ContainerActivity.class );
                intent.putExtra("fragment", ResultFragment.class.getCanonicalName());
                Bundle bundle=new Bundle();
                bundle.putInt("icon",R.mipmap.group2 );
                bundle.putString("title", "实名认证");
                bundle.putString("msg", "认证已提交");
                bundle.putString("btnText", "确定");
                intent.putExtra("bundle", bundle);
                startActivity(intent );
                getActivity().finish();

            }, e -> {
                e.printStackTrace();
            });
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

    URI picFrontUri;
    URI picBackUri;
    URI picTakeUri;

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
                                        String path = FileRealPathUtil.getRealPathFromUri(getContext(), imageUri);
                                        Bitmap b = bitmapCompress(path);
                                        URI imagePath = saveBitmap(b);
                                        v.setImageBitmap(b);

//                                        v.setImageURI(imageUri);
                                        v.setBackground(null);

                                        switch (v.getId()) {
                                            case R.id.pic_front:
                                                picFrontUri = imagePath;
                                                break;
                                            case R.id.pic_back:
                                                picBackUri = imagePath;
                                                break;
                                            case R.id.pic_take:
                                                picTakeUri = imagePath;
                                                break;
                                        }
                                    }

                                    @Override
                                    public void onCropImage(Uri imageUri) {

                                    }
                                };
                                if (which == 0) {
                                    // 从相册中选取图片

                                    imagePicker.startGallery(RealCertPictureFragment.this, callback);
                                } else {
                                    // 拍照
                                    imagePicker.startCamera(RealCertPictureFragment.this, callback);
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


}
