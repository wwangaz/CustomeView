package com.example.wangweimin.customerview.view;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wangweimin.customerview.R;

/**
 * Created by wangweimin on 16/5/11.
 */
public class PromoteBottomSheetDialogView {

    final private int MEDIATE_HEIGHT = 600;

    public PromoteBottomSheetDialogView(final Activity activity, List<Promote> list) {
        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        final View view = LayoutInflater.from(activity).inflate(R.layout.product_promote_bottome_view, null);

        ListRecyclerView mPromoteListView = (ListRecyclerView) view.findViewById(R.id.product_promote_list);
        TextView mPromoteBtn = (TextView) view.findViewById(R.id.product_promote_btn);

        final PromoteBottomSheetAdapter adapter = new PromoteBottomSheetAdapter();

        mPromoteListView.setAdapter(adapter);

        adapter.refreshViewByReplaceData(list);

        adapter.setOnRecyclerViewItemClickListener(new BaseRecyclerListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Promote promote = adapter.getData().get(position);
                AppUtil.processAction(activity, promote.directUrl);
                dialog.dismiss();
            }
        });

        mPromoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        View parentView = (View) view.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parentView);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams.height < MEDIATE_HEIGHT) {
            layoutParams.height = MEDIATE_HEIGHT;
            view.setLayoutParams(layoutParams);
        }

        behavior.setPeekHeight(MEDIATE_HEIGHT);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parentView.getLayoutParams();
        params.gravity = Gravity.TOP;
        parentView.setLayoutParams(params);
        dialog.show();

    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView mPromoteItemContent;

        public ViewHolder(View itemView) {
            super(itemView);
            mPromoteItemContent = (TextView) itemView.findViewById(R.id.promote_item_content);
        }
    }

    private class PromoteBottomSheetAdapter extends BaseRecyclerListAdapter<Promote, ViewHolder> {

        @Override
        protected ViewHolder onCreateItemViewHolder(ViewGroup viewGroup) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.promote_list_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        protected void onBindItemViewHolder(ViewHolder viewHolder, int position) {
            final Promote mPromote = getData().get(position);
            viewHolder.mPromoteItemContent.setText(mPromote.title);
        }
    }
}

