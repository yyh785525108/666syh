package com.tchy.syh.settings;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.R;
import com.tchy.syh.databinding.SettingsBinding;
import com.tchy.syh.userAccount.account_management.mod_info.ModInfoFragment;
import com.tchy.syh.userAccount.userLogin.LoginActivity;
import com.tchy.syh.utils.ToastUtil;

import java.io.File;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.base.ContainerActivity;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.SPUtils;

public class SettingsFragment extends BaseFragment<SettingsBinding,BaseViewModel> {


    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.settings;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public BaseViewModel initViewModel() {
        return new BaseViewModel();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.info.setOnClickListener(v->{
            Intent intent1=new Intent();
            intent1.setClass(getContext(),ContainerActivity.class );
            intent1.putExtra("fragment", ModInfoFragment.class.getCanonicalName());
            startActivity(intent1);
        });
        binding.account.setOnClickListener(v -> {
            Intent intent1=new Intent();
            intent1.setClass(getContext(),ContainerActivity.class );
            intent1.putExtra("fragment", AccountSettingsFragment.class.getCanonicalName());
            startActivity(intent1);
        });
        binding.about.setOnClickListener(v -> {
            Intent intent1=new Intent();
            intent1.setClass(getContext(),ContainerActivity.class );
            intent1.putExtra("fragment", AboutFragment.class.getCanonicalName());
            startActivity(intent1);
        });
        binding.exit.setOnClickListener(v -> {
            Intent intent1=new Intent();
            intent1.putExtra("isExit", true);
            intent1.setClass(getContext(),LoginActivity.class );

            startActivity(intent1);
            SPUtils.getInstance().clear();
            SPUtils.getInstance().put("isFirstStartup", false);
            Messenger.getDefault().sendNoMsg("myRefresh");

        });
        binding.cacheClear.setOnClickListener(v->{

            ToastUtil.toastCenter(getContext(),R.mipmap.toast_success,R.string.toast_cache);

            if(size==0){
                return ;
            }
            clearCache();
            binding.tvCacheSize.setText(getCacheSize()/1024/1024+"M");
        });
        binding.tvCacheSize.setText(getCacheSize()/1024/1024+"M");
    }
    public void clearCache(){

        File file=this.getContext().getExternalCacheDir();
        deleteFileRescure(file);
    }
    public long getCacheSize(){
        size =0;
        File file=this.getContext().getExternalCacheDir();
        fileRescure(file);
        return size;
    }
    long size =0;
    public void fileRescure(File f ){

        if(f.isFile()){
            size+=f.length();
        }else if (f.isDirectory()){
            for(File child :f.listFiles()){
                fileRescure(child);
            }

        }
    }
    public void deleteFileRescure(File f ){
        if(f.isFile()){
            f.delete();
        }else if (f.isDirectory()){

            for(File child :f.listFiles()){
                deleteFileRescure(child);
            }
            //非尾递归 待优化
            f.delete();

        }
    }

}
