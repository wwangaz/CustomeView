package com.example.wangweimin.customerview.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.wangweimin.customerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangweimin on 16/4/19.
 * <p/>
 * a TextView extends to AutoCompleteTextView
 * add a delete button to clear text and show or hide automatically,
 * customize complete layout and data adapter
 */
public class MailCompleteTextView extends AutoCompleteTextView {
    private Context mContext;
    private boolean isTextWatch;

    private final static String[] emailSuffix = {
            "@qq.com", "@163.com", "@126.com", "@gmail.com", "@sina.com", "@hotmail.com",
            "@yahoo.com", "@sohu.com", "@foxmail.com", "@139.com", "@yeah.net", "@vip.qq.com",
            "@vip.sina.com"
    };

    public MailCompleteTextView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MailCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public void init() {
        final MyAdapter adapter = new MyAdapter(mContext);
        setAdapter(adapter);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isTextWatch) {
                    String input = editable.toString();
                    String prefix = input;
                    //使用“@”前字符串生成带筛选列表
                    if (input.contains("@")) prefix = input.substring(0, input.indexOf("@"));

                    adapter.clearList();

                    if (prefix.length() > 0) {
                        for (String suffix : emailSuffix) {
                            adapter.addListData(prefix + suffix);
                        }
                    }

                    adapter.getFilter().filter(input);
                    adapter.notifyDataSetChanged();
                    showDropDown();
                }
            }
        });
        setThreshold(1);
    }

    //控制是否显示补全
    public void setShow(boolean textWatch) {
        isTextWatch = textWatch;
    }


    class MyAdapter extends BaseAdapter implements Filterable {
        private List<String> mList = new ArrayList<>();
        private Context mContext;
        private MyFilter mFilter;

        public MyAdapter(Context context) {
            mContext = context;
        }

        public void clearList() {
            mList.clear();
        }

        public void addListData(String strData) {
            mList.add(strData);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view;
            ViewHolder viewHolder;

            if (convertView == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.activity_mailcomplete_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.mTextView = (TextView) view.findViewById(R.id.tv_autocomplete);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.mTextView.setText(mList.get(position));

            return view;
        }

        @Override
        public Filter getFilter() {
            //每次均使用新对象，以更新List，后期考虑优化
            mFilter = new MyFilter(mList);
            return mFilter;
        }

        class MyFilter extends Filter {
            private List<String> original;

            public MyFilter(List<String> list) {
                original = list;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //新的工作线程，不能直接使用mList，会造成并发访问错误
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = original;
                    results.count = original.size();
                } else {
                    List<String> filteredList = new ArrayList<>();
                    for (String str : original) {
                        if (str.toUpperCase().startsWith(constraint.toString().toUpperCase()))
                            filteredList.add(str);
                    }
                    results.values = filteredList;
                    results.count = filteredList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mList = (List<String>) filterResults.values;
                notifyDataSetChanged();
            }
        }

        private class ViewHolder {
            TextView mTextView;
        }
    }


}
