package android.support.v7.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by SensYang on 2017/7/19 0019.
 */

public class CatchLinearLayoutManager extends LinearLayoutManager {
    public CatchLinearLayoutManager(Context context) {
        super(context);
    }

    public CatchLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CatchLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
