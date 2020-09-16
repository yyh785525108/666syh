package com.yyh.read666.tab2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yyh.read666.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YinpingPinglunFragment extends Fragment {
    public ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_yinping,container,false);
        listView=view.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),YinpingDetailActivity.class);startActivity(intent);
            }
        });

        return view;
    }

    public void refreshListView(JSONArray list){
        listView.setAdapter(new MyAdapter(getActivity(),list));

    }

    public class MyAdapter extends BaseAdapter {

        private JSONArray list;
        private Context ctx;
        private LayoutInflater mInflater;

        public MyAdapter(Context context, JSONArray list) {
            ctx = context;
            this.list = list;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
//            return list.length();
            return 30;

        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            try {
                return list.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
           ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_yinping_pinglun, null);
                viewHolder=new ViewHolder();
                viewHolder.nameTv=convertView.findViewById(R.id.nameTv);
                viewHolder.countTv=convertView.findViewById(R.id.countTv);
                viewHolder.dateTv=convertView.findViewById(R.id.dateTv);
                viewHolder.timeTv=convertView.findViewById(R.id.timeTv);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            try {
//                JSONObject data=list.getJSONObject(position);
//                int id=data.getInt("id");
//                String title=data.getString("title");
//                String duration=data.getString("duration");
//                String play_num=data.getString("play_num");
//
//                viewHolder.nameTv.setText(title);
//                viewHolder.countTv.setText(play_num);
//                viewHolder.timeTv.setText(duration);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


//            viewHolder.name.setText(lxr.getName());
            return convertView;
        }

        class ViewHolder {
            public TextView nameTv;
            public TextView countTv;
            public TextView dateTv;
            public TextView timeTv;
        }


    }
}
