package com.yyh.read666;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.yyh.read666.okhttp.UrlConstant;
import com.yyh.read666.tab4.NoticeListActivity;
import com.yyh.read666.tab5.DailiActivity;
import com.yyh.read666.tab5.FedbackActivity;
import com.yyh.read666.tab5.GuanyuActivity;
import com.yyh.read666.tab5.SettingActivity;
import com.yyh.read666.utils.DateUtil;
import com.yyh.read666.utils.SharedPreferencesUtil;
import com.yyh.read666.vo.User;

public class Fragment5 extends Fragment implements View.OnClickListener {

    private Button setBtn,xiaoxiBtn;
    private ImageView headImg;
    private TextView userNameTv,dushuHaoTv,yaoqingRenTv;
    private RelativeLayout dailiLay,xuanzhangLay,shangchengLay,yijianLay,helpLay,duihuanLay,guanyuLay;

    private LinearLayout shengjiLay,shujiaLay,zhuyeLay;

    private TextView phoneTv;
    private TextView vipTv;
    private ImageView vipImg;

    private TextView kaiTongTv,xufeiTv;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_5,container,false);
        initView(view);
        initData();

        return view;
    }

    private void initView(View view){
        vipImg=view.findViewById(R.id.vipImg);
        kaiTongTv=view.findViewById(R.id.kaiTongTv);
        xufeiTv=view.findViewById(R.id.xufeiTv);
        phoneTv=view.findViewById(R.id.phoneTv);
        setBtn=view.findViewById(R.id.setBtn);
        xiaoxiBtn=view.findViewById(R.id.xiaoxiBtn);
        headImg=view.findViewById(R.id.headImg);
        userNameTv=view.findViewById(R.id.userNameTv);
        dushuHaoTv=view.findViewById(R.id.dushuHaoTv);
        yaoqingRenTv=view.findViewById(R.id.yaoqingRenTv);
        dailiLay=view.findViewById(R.id.dailiLay);
        xuanzhangLay=view.findViewById(R.id.xuanzhangLay);
        shangchengLay=view.findViewById(R.id.shangchengLay);
        yijianLay=view.findViewById(R.id.yijianLay);
        helpLay=view.findViewById(R.id.helpLay);
        duihuanLay=view.findViewById(R.id.duihuanLay);
        guanyuLay=view.findViewById(R.id.guanyuLay);

        shengjiLay=view.findViewById(R.id.shengjiLay);
        shujiaLay=view.findViewById(R.id.shujiaLay);
        zhuyeLay=view.findViewById(R.id.zhuyeLay);
        vipTv=view.findViewById(R.id.vipTv);

        String str1=String.format("客服电话 <font color=\"#999999\">%s", "(9:00—19:00)");
        phoneTv.setText(Html.fromHtml(str1));



        setBtn.setOnClickListener(this);
        xiaoxiBtn.setOnClickListener(this);
        dailiLay.setOnClickListener(this);
        xuanzhangLay.setOnClickListener(this);
        shangchengLay.setOnClickListener(this);
        yijianLay.setOnClickListener(this);
        helpLay.setOnClickListener(this);
        duihuanLay.setOnClickListener(this);

        shengjiLay.setOnClickListener(this);
        shujiaLay.setOnClickListener(this);
        zhuyeLay.setOnClickListener(this);
        guanyuLay.setOnClickListener(this);

    }
    private void initData(){
        User user= SharedPreferencesUtil.getLoginUser(getContext());
        if (user!=null){
            userNameTv.setText(user.getNickname());
            dushuHaoTv.setText("读书号："+user.getUid());
            yaoqingRenTv.setText("邀请人:"+user.getParent_name());
            Glide.with(getActivity()).load(user.getAvatar()).into(headImg);
            try {
                int level=Integer.parseInt(user.getLevel()) ;
                String deadline=user.getDeadline();
                if (level==0){
                    vipTv.setText("读书改变命运");
                    kaiTongTv.setText("开通VIP会员");
                    xufeiTv.setText("开通");
                    vipImg.setVisibility(View.GONE);
                }else {
                    vipImg.setVisibility(View.VISIBLE);
                    if ((deadline!=null&&deadline.equals("0"))||(level>=3)){
                        vipTv.setText("永久");
                        kaiTongTv.setText("VIP会员");
                        xufeiTv.setText("升级");
                    }else {
                        kaiTongTv.setText("续费VIP会员");
                        vipTv.setText("到期时间："+ DateUtil.timeStamp2Date(deadline,"yyyy-MM-dd"));
                        xufeiTv.setText("续费");
                    }
                }
            }catch (NumberFormatException e){
                vipTv.setText("读书改变命运");
                kaiTongTv.setText("开通VIP会员");
                xufeiTv.setText("开通");
                vipImg.setVisibility(View.GONE);
            }


        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view==setBtn){
            intent=new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        }else if (view==xiaoxiBtn){
            intent=new Intent(getActivity(), NoticeListActivity.class);
            startActivity(intent);
        }else if (view==dailiLay){
            intent=new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", UrlConstant.DAILI_URL);
            startActivity(intent);
        }else if (view==xuanzhangLay){
            intent=new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", UrlConstant.XUANZHANG_URL);
            startActivity(intent);
        }else if (view==shangchengLay){
            intent=new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", UrlConstant.DINGDAN_URL);
            startActivity(intent);
        }else if (view==yijianLay){
            intent=new Intent(getActivity(), FedbackActivity.class);
            startActivity(intent);
        }else if (view==helpLay){
             intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + "4006711319");
            intent.setData(data);
            startActivity(intent);

        }else if (view==duihuanLay){
            intent=new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", UrlConstant.DUIHUANMA_URL);
            startActivity(intent);
        }else if (view==shengjiLay){
            intent=new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", UrlConstant.SHENGJI_URL);
            startActivity(intent);
        }else if (view==shujiaLay){
            intent=new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", UrlConstant.SHUJIA_URL);
            startActivity(intent);
        }else if (view==zhuyeLay){
            intent=new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", UrlConstant.ZHUYE_URL);
            startActivity(intent);
        }else if (view==guanyuLay){
            intent=new Intent(getActivity(), GuanyuActivity.class);
            startActivity(intent);
        }




    }
}
