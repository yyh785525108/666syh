package com.tchy.syh.common;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tchy.syh.R;
import com.tchy.syh.home.HomeActivity;

public class CommonImageFragment extends Fragment {
    public CommonImageFragment(){

    }
    boolean clickable;
    public static CommonImageFragment getInstance(int imageid,boolean clickable){
        CommonImageFragment f=new CommonImageFragment();
        Bundle bundle =new Bundle();
        bundle.putInt("image", imageid);
        bundle.putBoolean("clickable", clickable);
        f.setArguments(bundle);
        return f;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.common_image_fragment, null);
        ImageView image=v.findViewById(R.id.iv_item);
        image.setImageResource(getArguments().getInt("image"));
        if(getArguments().getBoolean("clickable")){
            image.setOnClickListener(o->{
                Intent intent =new Intent();
                intent.setClass(getContext(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            });
        }
        return v;
    }

}
