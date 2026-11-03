package miuix.recyclerview.card;

import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import miuix.recyclerview.card.base.ViewOutlineHelper;

/* JADX INFO: loaded from: classes2.dex */
public abstract class CardGroupAdapter extends RecyclerView.Adapter {
    private long mAnimatorDuration;
    private float mCardRadius;
    private RecyclerView mRecyclerView;
    private final SparseIntArray mTypeMap = new SparseIntArray(64);
    private boolean isNeedItemPressEffect = true;
    private int mRemovedGroupId = -1;
    private int mClickPosition = -1;
    private final RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() { // from class: miuix.recyclerview.card.CardGroupAdapter.1
        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onChanged() {
            super.onChanged();
            CardGroupAdapter.this.updateGroupInfo();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int i, int i2) {
            super.onItemRangeChanged(i, i2);
            CardGroupAdapter.this.updateGroupInfo();
        }
    };

    public abstract int getItemViewGroup(int i);

    public abstract void setHasStableIds();

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
        this.mCardRadius = recyclerView.getContext().getResources().getDimensionPixelSize(R$dimen.miuix_recyclerview_card_group_radius);
        registerAdapterDataObserver(this.mObserver);
        RecyclerView.ItemAnimator itemAnimator = this.mRecyclerView.getItemAnimator();
        if (itemAnimator != null) {
            this.mAnimatorDuration = itemAnimator.getAddDuration();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        unregisterAdapterDataObserver(this.mObserver);
        this.mRecyclerView = null;
    }

    protected CardGroupAdapter() {
        setHasStableIds();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ViewOutlineHelper.setItemCardOutline(viewHolder, getItemViewGroupType(i), isSupportLayoutManager() ? this.mCardRadius : 0.0f, i == this.mClickPosition, this.mAnimatorDuration);
        if (isSupportLayoutManager()) {
            setFirstItemMargin(viewHolder, i);
        }
        if (this.isNeedItemPressEffect) {
            Drawable foreground = viewHolder.itemView.getForeground();
            View view = viewHolder.itemView;
            if (foreground == null) {
                TypedArray typedArrayObtainStyledAttributes = view.getContext().getTheme().obtainStyledAttributes(new int[]{R$attr.cardGroupItemForegroundEffect});
                viewHolder.itemView.setForeground(typedArrayObtainStyledAttributes.getDrawable(0));
                typedArrayObtainStyledAttributes.recycle();
            } else {
                view.getForeground();
                getItemViewGroupType(i);
            }
        }
    }

    private boolean isSupportLayoutManager() {
        if (this.mRecyclerView.getItemDecorationCount() > 0) {
            RecyclerView.ItemDecoration itemDecorationAt = this.mRecyclerView.getItemDecorationAt(0);
            if (itemDecorationAt instanceof CardItemDecoration) {
                return ((CardItemDecoration) itemDecorationAt).isSupportLayoutManager(this.mRecyclerView.getLayoutManager());
            }
        }
        return false;
    }

    private void setFirstItemMargin(RecyclerView.ViewHolder viewHolder, int i) {
        ViewGroup.MarginLayoutParams marginLayoutParams;
        if (i == 0) {
            if (this.mRecyclerView.getItemDecorationCount() > 0) {
                RecyclerView.ItemDecoration itemDecorationAt = this.mRecyclerView.getItemDecorationAt(0);
                if (itemDecorationAt instanceof CardItemDecoration) {
                    Rect rectOffsetsRect = ((CardItemDecoration) itemDecorationAt).offsetsRect(this, i);
                    ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
                    if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                        marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    } else if (layoutParams != null) {
                        marginLayoutParams = new ViewGroup.MarginLayoutParams(layoutParams);
                    } else {
                        marginLayoutParams = new ViewGroup.MarginLayoutParams(new ViewGroup.LayoutParams(-2, -2));
                    }
                    marginLayoutParams.topMargin = rectOffsetsRect.top;
                    marginLayoutParams.bottomMargin = rectOffsetsRect.bottom;
                    viewHolder.itemView.setLayoutParams(marginLayoutParams);
                    return;
                }
                return;
            }
            return;
        }
        ViewGroup.LayoutParams layoutParams2 = viewHolder.itemView.getLayoutParams();
        if (layoutParams2 instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) layoutParams2;
            marginLayoutParams2.topMargin = 0;
            marginLayoutParams2.bottomMargin = 0;
            viewHolder.itemView.setLayoutParams(marginLayoutParams2);
        }
    }

    public int getItemViewGroupType(int i) {
        return this.mTypeMap.get(i);
    }

    public void updateGroupInfo() {
        int itemCount = getItemCount();
        int i = Integer.MIN_VALUE;
        int i2 = 0;
        while (i2 < itemCount) {
            int itemViewGroup = getItemViewGroup(i2);
            SparseIntArray sparseIntArray = this.mTypeMap;
            if (itemViewGroup != i) {
                sparseIntArray.put(i2, 2);
                int i3 = i2 - 1;
                if (i3 >= 0) {
                    int i4 = this.mTypeMap.get(i3);
                    if (i4 == 2) {
                        this.mTypeMap.put(i3, 1);
                    } else if (i4 == 3) {
                        this.mTypeMap.put(i3, 4);
                    }
                }
            } else {
                sparseIntArray.put(i2, 3);
            }
            if (itemViewGroup == Integer.MIN_VALUE) {
                this.mTypeMap.put(i2, 0);
            }
            i2++;
            i = itemViewGroup;
        }
        int itemCount2 = getItemCount() - 1;
        int i5 = this.mTypeMap.get(itemCount2);
        if (i5 == 2) {
            this.mTypeMap.put(itemCount2, 1);
        } else if (i5 == 3) {
            this.mTypeMap.put(itemCount2, 4);
        }
    }

    public int getRemovedGroupId() {
        return this.mRemovedGroupId;
    }
}
