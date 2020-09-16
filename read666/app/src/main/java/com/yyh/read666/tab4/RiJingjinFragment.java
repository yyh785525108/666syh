package com.yyh.read666.tab4;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yyh.read666.Fragment2;
import com.yyh.read666.R;

import org.json.JSONArray;
import org.json.JSONException;

public class RiJingjinFragment extends Fragment {
    private ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_rijingjin,container,false);
        listView=view.findViewById(R.id.listView);
        listView.setAdapter(new MyAdapter(getActivity(),null));

        return view;
    }

    public class MyAdapter extends BaseAdapter {

        private JSONArray cards;
        private Context ctx;
        private LayoutInflater mInflater;

        public MyAdapter(Context context, JSONArray cards) {
            ctx = context;
            this.cards = cards;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 10;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            try {
                return cards.getJSONObject(position);
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
                convertView = mInflater.inflate(R.layout.item_rijinjing, null);
                viewHolder=new ViewHolder();


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


//            viewHolder.name.setText(lxr.getName());
            return convertView;
        }

        class ViewHolder {
            public TextView kayuTv;
            public ImageView logoImg;
            public TextView shangjiaNameTv;
            public TextView kaNameTv;
            private ImageView desImg;
        }


    }
}
