package com.example.commonlibary.pickerview.takephoto.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.commonlibary.R;
import com.example.commonlibary.pickerview.takephoto.util.Util;

import java.util.List;



/**
 * 底部列表弹窗选择
 * @param <T>
 */
public class BottomSelectDialog<T> extends Dialog  {

    public static final String TAG = "AlertPhoneDialog";

    public List<T> list;  //数据源

    public List<TextModel> textList;  //展示对象

    public BottomSelectAdapter adapter;

    public ChooseItemListener listener;

    private Context context;

    LinearLayout llCancel;

    ListView lvBottemSelect;
    View rootView;


    public BottomSelectDialog(Context context, int theme) {
        super(context, theme);
    }

    public BottomSelectDialog(Context context, ChooseItemListener listener) {
        super(context, R.style.member_type_dialog);
        setOwnerActivity((Activity) context);
        this.context = context;
        this.listener = listener;
        rootView = LayoutInflater.from(context).inflate(R.layout.bottem_select_dialog, null);
        setContentView(rootView);
    }

    public void bindData(List<T> list, List<TextModel> textList) {
        this.list = list;
        this.textList = textList;
        initData();
    }

    public void initData() {
        lvBottemSelect = (ListView) rootView.findViewById(R.id.lv_bottem_select);
        View footView = LayoutInflater.from(getContext()).inflate(R.layout.bottem_select_foot, null);
        llCancel = (LinearLayout) footView.findViewById(R.id.ll_cancel);
        lvBottemSelect.addFooterView(footView);
        adapter = new BottomSelectAdapter(getContext(), R.layout.bottem_select_item, textList);
        lvBottemSelect.setAdapter(adapter);
        lvBottemSelect.setSelector(R.color.transparent);
        lvBottemSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.chooseItem(list.get(i));
            }
        });
        findViewById( R.id.ll_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    public void show() {
        getWindow().setGravity(Gravity.BOTTOM);//dialog显示在底部
        //FullScreen();//dialog全屏显示
        super.show();
        Util.FullScreen((Activity) context, this);
    }





    public interface ChooseItemListener<T> {
        void chooseItem(T object);
    }

    public class TextModel {
        public String text;
        public int color;

        public TextModel(String text) {
            this.text = text;
            this.color = ContextCompat.getColor(context, R.color.color_303233);
        }

        public TextModel(String text, int color) {
            this.text = text;
            this.color = color;
        }

    }

    /**
     * 点卡弹出框
     */
    class BottomSelectAdapter extends ArrayAdapter<TextModel> {

        private int sourceId;

        public BottomSelectAdapter(Context context, int resource, List<TextModel> objects) {
            super(context, resource, objects);
            sourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextModel pci = getItem(position);
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(sourceId, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_value = (TextView) view.findViewById(R.id.tv_value);
                viewHolder.v_line = view.findViewById(R.id.v_line);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_value.setText(pci.text);
            viewHolder.tv_value.setTextColor(pci.color);
            if (position == textList.size() - 1) {
                viewHolder.v_line.setVisibility(View.GONE);
            } else {
                viewHolder.v_line.setVisibility(View.VISIBLE);
            }
            return view;
        }

        class ViewHolder {
            TextView tv_value;
            View v_line;
        }
    }
}
