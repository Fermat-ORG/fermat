package com.bitdubai.fermat_android_api.ui.expandableRecicler;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * RecyclerView.Adapter implementation that
 * adds the ability to expand and collapse list items.
 * <p/>
 * Changes should be notified through:
 * {@link #notifyParentItemInserted(int)}
 * {@link #notifyParentItemRemoved(int)}
 * {@link #notifyParentItemChanged(int)}
 * {@link #notifyParentItemRangeInserted(int, int)}
 * {@link #notifyChildItemInserted(int, int)}
 * {@link #notifyChildItemRemoved(int, int)}
 * {@link #notifyChildItemChanged(int, int)}
 * methods and not the notify methods of RecyclerView.Adapter.
 *
 * @author Ryan Brooks
 * @version 1.0
 * @since 5/27/2015
 */
public abstract class ExpandableRecyclerAdapter<PVH extends ParentViewHolder, CVH extends ChildViewHolder, PI extends ParentListItem, CI>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ParentViewHolder.ParentListItemExpandCollapseListener {

    private static final String EXPANDED_STATE_MAP = "ExpandableRecyclerAdapter.ExpandedStateMap";
    private static final int TYPE_PARENT = 0;
    private static final int TYPE_CHILD = 1;
    private static final int TYPE_CHILD_LAST = 2;
    private static final String TAG = "ExpandableRecyclerAdap";
    /**
     * A {@link List} of all currently expanded {@link ParentListItem} objects
     * and their children, in order. Changes to this list should be made through the add/remove methods
     * available in {@link ExpandableRecyclerAdapter}
     */
    protected List<Object> mItemList;
    protected FermatListItemListeners<CI> childItemEventListeners;

    private List<? extends ParentListItem> mParentItemList;
    private ExpandCollapseListener mExpandCollapseListener;
    private List<RecyclerView> mAttachedRecyclerViewPool;

    /**
     * Allows objects to register themselves as expand/collapse listeners to be
     * notified of change events.
     * <p/>
     * Implement this in your {@link Activity} or {@link android.app.Fragment}
     * to receive these callbacks.
     */
    public interface ExpandCollapseListener {

        /**
         * Called when a list item is expanded.
         *
         * @param position The index of the item in the list being expanded
         */
        void onListItemExpanded(int position);

        /**
         * Called when a list item is collapsed.
         *
         * @param position The index of the item in the list being collapsed
         */
        void onListItemCollapsed(int position);
    }

    /**
     * Primary constructor. Sets up {@link #mParentItemList} and {@link #mItemList}.
     * <p/>
     * Changes to {@link #mParentItemList} should be made through add/remove methods in
     * {@link ExpandableRecyclerAdapter}
     *
     * @param parentItemList List of all {@link ParentListItem} objects to be
     *                       displayed in the RecyclerView that this
     *                       adapter is linked to
     */
    public ExpandableRecyclerAdapter(@NonNull List<? extends ParentListItem> parentItemList) {
        super();
        mParentItemList = parentItemList;
        mItemList = ExpandableRecyclerAdapterHelper.generateParentChildItemList(parentItemList);
        mAttachedRecyclerViewPool = new ArrayList<>();
    }

    /**
     * Implementation of Adapter.onCreateViewHolder(ViewGroup, int)
     * that determines if the list item is a parent or a child and calls through
     * to the appropriate implementation of either {@link #onCreateParentViewHolder(ViewGroup)}
     * or {@link #onCreateChildViewHolder(ViewGroup)}.
     *
     * @param viewGroup The {@link ViewGroup} into which the new {@link android.view.View}
     *                  will be added after it is bound to an adapter position.
     * @param viewType  The view type of the new {@code android.view.View}.
     * @return A new RecyclerView.ViewHolder
     * that holds a {@code android.view.View} of the given view type.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_PARENT) {
            PVH pvh = onCreateParentViewHolder(viewGroup);
            pvh.setParentListItemExpandCollapseListener(this);
            return pvh;
        } else if (viewType == TYPE_CHILD) {
            return onCreateChildViewHolder(viewGroup);
        } else {
            throw new IllegalStateException("Incorrect ViewType found");
        }
    }


    /**
     * Implementation of Adapter.onBindViewHolder(RecyclerView.ViewHolder, int)
     * that determines if the list item is a parent or a child and calls through
     * to the appropriate implementation of either {@link #onBindParentViewHolder(ParentViewHolder, int, ParentListItem)}
     * or {@link #onBindChildViewHolder(ChildViewHolder, int, Object)}.
     *
     * @param holder   The RecyclerView.ViewHolder to bind data to
     * @param position The index in the list at which to bind
     * @throws IllegalStateException if the item in the list is either null or not of type {@link ParentListItem}
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Object listItem = getListItem(position);
        if (listItem instanceof ParentWrapper) {
            PVH parentViewHolder = (PVH) holder;

            if (parentViewHolder.shouldItemViewClickToggleExpansion()) {
                parentViewHolder.setMainItemClickToExpand();
            }

            ParentWrapper parentWrapper = (ParentWrapper) listItem;
            parentViewHolder.setExpanded(parentWrapper.isExpanded());
            onBindParentViewHolder(parentViewHolder, position, (PI) parentWrapper.getParentListItem());

        } else if (listItem == null) {
            throw new IllegalStateException("Incorrect ViewHolder found");
        } else {
            onBindChildViewHolder((CVH) holder, position, (CI) listItem);
            bindChildItemEventListeners(holder, position, (CI) listItem);
        }
    }

    /**
     * Bind a event listener to a child item
     *
     * @param holder   the view holder with the views fot this item
     * @param position the position of the item in the list
     * @param listItem the item itself
     */
    private void bindChildItemEventListeners(RecyclerView.ViewHolder holder, final int position, final CI listItem) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (childItemEventListeners != null) {
                    childItemEventListeners.onItemClickListener(listItem, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (childItemEventListeners != null) {
                    childItemEventListeners.onLongItemClickListener(listItem, position);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Callback called from {@link #onCreateViewHolder(ViewGroup, int)} when
     * the list item created is a parent.
     *
     * @param parentViewGroup The {@link ViewGroup} in the list for which a {@link PVH}
     *                        is being created
     * @return A {@code PVH} corresponding to the {@link ParentListItem} with
     * the {@code ViewGroup} parentViewGroup
     */
    public abstract PVH onCreateParentViewHolder(ViewGroup parentViewGroup);

    /**
     * Callback called from {@link #onCreateViewHolder(ViewGroup, int)} when
     * the list item created is a child.
     *
     * @param childViewGroup The {@link ViewGroup} in the list for which a {@link CVH}
     *                       is being created
     * @return A {@code CVH} corresponding to the child list item with the
     * {@code ViewGroup} childViewGroup
     */
    public abstract CVH onCreateChildViewHolder(ViewGroup childViewGroup);

    /**
     * Callback called from onBindViewHolder(RecyclerView.ViewHolder, int)
     * when the list item bound to is a parent.
     * <p/>
     * Bind data to the {@link PVH} here.
     *
     * @param parentViewHolder The {@code PVH} to bind data to
     * @param position         The index in the list at which to bind
     * @param parentListItem   The {@link ParentListItem} which holds the data to
     *                         be bound to the {@code PVH}
     */
    public abstract void onBindParentViewHolder(PVH parentViewHolder, int position, PI parentListItem);

    /**
     * Callback called from onBindViewHolder(RecyclerView.ViewHolder, int)
     * when the list item bound to is a child.
     * <p/>
     * Bind data to the {@link CVH} here.
     *
     * @param childViewHolder The {@code CVH} to bind data to
     * @param position        The index in the list at which to bind
     * @param childListItem   The child list item which holds that data to be
     *                        bound to the {@code CVH}
     */
    public abstract void onBindChildViewHolder(CVH childViewHolder, int position, CI childListItem);

    /**
     * Gets the number of parent and child objects currently expanded.
     *
     * @return The size of {@link #mItemList}
     */
    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    /**
     * Gets the view type of the item at the given position.
     *
     * @param position The index in the list to get the view type of
     * @return {@value #TYPE_PARENT} for {@link ParentListItem} and {@value #TYPE_CHILD}
     * for child list items
     * @throws IllegalStateException if the item at the given position in the list is null
     */
    @Override
    public int getItemViewType(int position) {
        Object listItem = getListItem(position);
        if (listItem instanceof ParentWrapper) {
            return TYPE_PARENT;
        } else if (listItem == null) {
            throw new IllegalStateException("Null object added");
        } else {
            return TYPE_CHILD;
        }
    }

    /**
     * Gets the list of ParentItems that is backing this adapter.
     * Changes can be made to the list and the adapter notified via the
     * {@link #notifyParentItemInserted(int)}
     * {@link #notifyParentItemRemoved(int)}
     * {@link #notifyParentItemChanged(int)}
     * {@link #notifyParentItemRangeInserted(int, int)}
     * {@link #notifyChildItemInserted(int, int)}
     * {@link #notifyChildItemRemoved(int, int)}
     * {@link #notifyChildItemChanged(int, int)}
     * methods.
     *
     * @return The list of ParentListItems that this adapter represents
     */
    public List<? extends ParentListItem> getParentItemList() {
        return mParentItemList;
    }



    /**
     * <p/>
     * Called when a {@link ParentListItem} is triggered to expand.
     *
     * @param position The index of the item in the list being expanded
     */
    @Override
    public void onParentListItemExpanded(int position) {
        Object listItem = getListItem(position);
        if (listItem instanceof ParentWrapper) {
            expandParentListItem((ParentWrapper) listItem, position, true);
        }
    }

    /**
     * <p/>
     * Called when a {@link ParentListItem} is triggered to collapse.
     *
     * @param position The index of the item in the list being collapsed
     */
    @Override
    public void onParentListItemCollapsed(int position) {
        Object listItem = getListItem(position);
        if (listItem instanceof ParentWrapper) {
            collapseParentListItem((ParentWrapper) listItem, position, true);
        }
    }

    /**
     * Implementation of Adapter#onAttachedToRecyclerView(RecyclerView).
     * <p/>
     * Called when this {@link ExpandableRecyclerAdapter} is attached to a RecyclerView.
     *
     * @param recyclerView The {@code RecyclerView} this {@code ExpandableRecyclerAdapter}
     *                     is being attached to
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAttachedRecyclerViewPool.add(recyclerView);
    }

    /**
     * Implementation of Adapter.onDetachedFromRecyclerView(RecyclerView)
     * <p/>
     * Called when this ExpandableRecyclerAdapter is detached from a RecyclerView.
     *
     * @param recyclerView The {@code RecyclerView} this {@code ExpandableRecyclerAdapter}
     *                     is being detached from
     */
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mAttachedRecyclerViewPool.remove(recyclerView);
    }

    public void setExpandCollapseListener(ExpandCollapseListener expandCollapseListener) {
        mExpandCollapseListener = expandCollapseListener;
    }

    // region Programmatic Expansion/Collapsing

    /**
     * Expands the parent with the specified index in the list of parents.
     *
     * @param parentIndex The index of the parent to expand
     */
    public void expandParent(int parentIndex) {
        int parentWrapperIndex = getParentWrapperIndex(parentIndex);

        Object listItem = getListItem(parentWrapperIndex);
        ParentWrapper parentWrapper;
        if (listItem instanceof ParentWrapper) {
            parentWrapper = (ParentWrapper) listItem;
        } else {
            return;
        }

        expandViews(parentWrapper, parentWrapperIndex);
    }

    /**
     * Expands the parent associated with a specified {@link ParentListItem} in
     * the list of parents.
     *
     * @param parentListItem The {@code ParentListItem} of the parent to expand
     */
    public void expandParent(ParentListItem parentListItem) {
        ParentWrapper parentWrapper = getParentWrapper(parentListItem);
        int parentWrapperIndex = mItemList.indexOf(parentWrapper);
        if (parentWrapperIndex == -1) {
            return;
        }

        expandViews(parentWrapper, parentWrapperIndex);
    }

    /**
     * Expands all parents in the list.
     */
    public void expandAllParents() {
        for (ParentListItem parentListItem : mParentItemList) {
            expandParent(parentListItem);
        }
    }

    /**
     * Collapses the parent with the specified index in the list of parents.
     *
     * @param parentIndex The index of the parent to collapse
     */
    public void collapseParent(int parentIndex) {
        int parentWrapperIndex = getParentWrapperIndex(parentIndex);

        Object listItem = getListItem(parentWrapperIndex);
        ParentWrapper parentWrapper;
        if (listItem instanceof ParentWrapper) {
            parentWrapper = (ParentWrapper) listItem;
        } else {
            return;
        }

        collapseViews(parentWrapper, parentWrapperIndex);
    }

    /**
     * Collapses the parent associated with a specified {@link ParentListItem} in
     * the list of parents.
     *
     * @param parentListItem The {@code ParentListItem} of the parent to collapse
     */
    public void collapseParent(ParentListItem parentListItem) {
        ParentWrapper parentWrapper = getParentWrapper(parentListItem);
        int parentWrapperIndex = mItemList.indexOf(parentWrapper);
        if (parentWrapperIndex == -1) {
            return;
        }

        collapseViews(parentWrapper, parentWrapperIndex);
    }

    /**
     * Collapses all parents in the list.
     */
    public void collapseAllParents() {
        for (ParentListItem parentListItem : mParentItemList) {
            collapseParent(parentListItem);
        }
    }

    /**
     * Stores the expanded state map across state loss.
     * <p/>
     * Should be called from {@link Activity#onSaveInstanceState(Bundle)} in
     * the {@link Activity} that hosts the RecyclerView that this
     * {@link ExpandableRecyclerAdapter} is attached to.
     * <p/>
     * This will make sure to add the expanded state map as an extra to the
     * instance state bundle to be used in {@link #onRestoreInstanceState(Bundle)}.
     *
     * @param savedInstanceState The {@code Bundle} into which to store the
     *                           expanded state map
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(EXPANDED_STATE_MAP, generateExpandedStateMap());
    }

    /**
     * Fetches the expandable state map from the saved instance state {@link Bundle}
     * and restores the expanded states of all of the list items.
     * <p/>
     * Should be called from {@link Activity#onRestoreInstanceState(Bundle)} in
     * the {@link Activity} that hosts the RecyclerView that this
     * {@link ExpandableRecyclerAdapter} is attached to.
     * <p/>
     * Assumes that the list of parent list items is the same as when the saved
     * instance state was stored.
     *
     * @param savedInstanceState The {@code Bundle} from which the expanded
     *                           state map is loaded
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(EXPANDED_STATE_MAP)) {
            return;
        }

        HashMap<Integer, Boolean> expandedStateMap = (HashMap<Integer, Boolean>) savedInstanceState.getSerializable(EXPANDED_STATE_MAP);
        int fullCount = 0;
        int childCount = 0;
        Object listItem;
        ParentWrapper parentWrapper;
        List<?> childItemList;
        int listItemCount = mItemList.size();
        while (fullCount < listItemCount) {
            listItem = getListItem(fullCount);

            if (listItem instanceof ParentWrapper) {
                parentWrapper = (ParentWrapper) listItem;

                if (expandedStateMap.containsKey(fullCount - childCount)) {
                    parentWrapper.setExpanded(expandedStateMap.get(fullCount - childCount));


                    if (parentWrapper.isExpanded() && !parentWrapper.isInitiallyExpanded()) {
                        childItemList = parentWrapper.getChildItemList();

                        if (childItemList != null) {
                            int childListItemCount = childItemList.size();
                            for (int j = 0; j < childListItemCount; j++) {
                                fullCount++;
                                childCount++;
                                mItemList.add(fullCount, childItemList.get(j));
                            }
                        }
                    } else if (!parentWrapper.isExpanded() && parentWrapper.isInitiallyExpanded()) {
                        childItemList = parentWrapper.getChildItemList();
                        int childListItemCount = childItemList.size();
                        for (int j = 0; j < childListItemCount; j++) {
                            mItemList.remove(fullCount + 1);
                        }
                    }
                }
            } else {
                childCount++;
            }

            fullCount++;
        }

        notifyDataSetChanged();
    }

    /**
     * Gets the list item held at the specified adapter position.
     *
     * @param position The index of the list item to return
     * @return The list item at the specified position
     */
    protected Object getListItem(int position) {
        return mItemList.get(position);
    }

    /**
     * Calls through to the ParentViewHolder to expand views for each
     * RecyclerView the specified parent is a child of.
     * <p/>
     * These calls to the ParentViewHolder are made so that animations can be
     * triggered at the ViewHolder level.
     *
     * @param parentIndex The index of the parent to expand
     */
    private void expandViews(ParentWrapper parentWrapper, int parentIndex) {
        PVH viewHolder;
        for (RecyclerView recyclerView : mAttachedRecyclerViewPool) {
            viewHolder = (PVH) recyclerView.findViewHolderForAdapterPosition(parentIndex);
            if (viewHolder != null && !viewHolder.isExpanded()) {
                viewHolder.setExpanded(true);
                viewHolder.onExpansionToggled(false);
            }

            expandParentListItem(parentWrapper, parentIndex, false);
        }
    }

    /**
     * Calls through to the ParentViewHolder to collapse views for each
     * RecyclerView a specified parent is a child of.
     * <p/>
     * These calls to the ParentViewHolder are made so that animations can be
     * triggered at the ViewHolder level.
     *
     * @param parentIndex The index of the parent to collapse
     */
    private void collapseViews(ParentWrapper parentWrapper, int parentIndex) {
        PVH viewHolder;
        for (RecyclerView recyclerView : mAttachedRecyclerViewPool) {
            viewHolder = (PVH) recyclerView.findViewHolderForAdapterPosition(parentIndex);
            if (viewHolder != null && viewHolder.isExpanded()) {
                viewHolder.setExpanded(false);
                viewHolder.onExpansionToggled(true);
            }

            collapseParentListItem(parentWrapper, parentIndex, false);
        }
    }

    /**
     * Expands a specified parent item. Calls through to the
     * ExpandCollapseListener and adds children of the specified parent to the
     * total list of items.
     *
     * @param parentWrapper                     The ParentWrapper of the parent to expand
     * @param parentIndex                       The index of the parent to expand
     * @param expansionTriggeredByListItemClick true if expansion was triggered
     *                                          by a click event, false otherwise.
     */
    private void expandParentListItem(ParentWrapper parentWrapper, int parentIndex, boolean expansionTriggeredByListItemClick) {
        if (!parentWrapper.isExpanded()) {
            parentWrapper.setExpanded(true);

            if (expansionTriggeredByListItemClick && mExpandCollapseListener != null) {
                int expandedCountBeforePosition = getExpandedItemCount(parentIndex);
                mExpandCollapseListener.onListItemExpanded(parentIndex - expandedCountBeforePosition);
            }

            List<?> childItemList = parentWrapper.getChildItemList();
            if (childItemList != null) {
                int childListItemCount = childItemList.size();
                for (int i = 0; i < childListItemCount; i++) {
                    try {
                        mItemList.add(parentIndex + i + 1, childItemList.get(i));
                        notifyItemInserted(parentIndex + i + 1);
                    }catch (Exception e){
                        Log.e(TAG,"Error: expandParentListItem.");
                       // e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Collapses a specified parent item. Calls through to the
     * ExpandCollapseListener and adds children of the specified parent to the
     * total list of items.
     *
     * @param parentWrapper                    The ParentWrapper of the parent to collapse
     * @param parentIndex                      The index of the parent to collapse
     * @param collapseTriggeredByListItemClick true if expansion was triggered
     *                                         by a click event, false otherwise.
     */
    private void collapseParentListItem(ParentWrapper parentWrapper, int parentIndex, boolean collapseTriggeredByListItemClick) {
        if (parentWrapper.isExpanded()) {
            parentWrapper.setExpanded(false);

            if (collapseTriggeredByListItemClick && mExpandCollapseListener != null) {
                int expandedCountBeforePosition = getExpandedItemCount(parentIndex);
                mExpandCollapseListener.onListItemCollapsed(parentIndex - expandedCountBeforePosition);
            }

            List<?> childItemList = parentWrapper.getChildItemList();
            if (childItemList != null) {
                for (int i = childItemList.size() - 1; i >= 0; i--) {
                    mItemList.remove(parentIndex + i + 1);
                    notifyItemRemoved(parentIndex + i + 1);
                }
            }
        }
    }

    /**
     * Gets the number of expanded child list items before the specified position.
     *
     * @param position The index before which to return the number of expanded
     *                 child list items
     * @return The number of expanded child list items before the specified position
     */
    private int getExpandedItemCount(int position) {
        if (position == 0) {
            return 0;
        }

        int expandedCount = 0;
        for (int i = 0; i < position; i++) {
            Object listItem = getListItem(i);
            if (!(listItem instanceof ParentWrapper)) {
                expandedCount++;
            }
        }
        return expandedCount;
    }

    // endregion

    // region Data Manipulation

    /**
     * Change the data set and notify the adapter about this
     *
     * @param parentItemList the list of parent items (with children) to be replace with
     */
    public void changeDataSet(@NonNull List<? extends ParentListItem> parentItemList) {
        onChangeDataSet();
        mParentItemList = parentItemList;
        mItemList = ExpandableRecyclerAdapterHelper.generateParentChildItemList(parentItemList);

        notifyDataSetChanged();
    }

    /**
     * Notify subClass when dataset is change to stop work if is running
     */
    protected void onChangeDataSet(){

    }

    /**
     * Notify any registered observers that the ParentListItem reflected at {@code parentPosition}
     * has been newly inserted. The ParentListItem previously at {@code parentPosition} is now at
     * position {@code parentPosition + 1}.
     * <p/>
     * This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their
     * positions may be altered.
     *
     * @param parentPosition Position of the newly inserted ParentListItem in the data set, relative
     *                       to list of ParentListItems only.
     * @see #notifyParentItemRangeInserted(int, int)
     */
    public void notifyParentItemInserted(int parentPosition) {
        ParentListItem parentListItem = mParentItemList.get(parentPosition);

        int wrapperIndex;
        if (parentPosition < mParentItemList.size() - 1) {
            wrapperIndex = getParentWrapperIndex(parentPosition);
        } else {
            wrapperIndex = mItemList.size();
        }

        int sizeChanged = addParentWrapper(wrapperIndex, parentListItem);
        notifyItemRangeInserted(wrapperIndex, sizeChanged);
    }

    /**
     * Notify any registered observers that the currently reflected {@code itemCount}
     * ParentListItems starting at {@code parentPositionStart} have been newly inserted.
     * The ParentListItems previously located at {@code parentPositionStart} and beyond
     * can now be found starting at position {@code parentPositionStart + itemCount}.
     * <p/>
     * This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their positions
     * may be altered.
     *
     * @param parentPositionStart Position of the first ParentListItem that was inserted, relative
     *                            to list of ParentListItems only.
     * @param itemCount           Number of items inserted
     * @see #notifyParentItemInserted(int)
     */
    public void notifyParentItemRangeInserted(int parentPositionStart, int itemCount) {
        int initialWrapperIndex;
        if (parentPositionStart < mParentItemList.size() - itemCount) {
            initialWrapperIndex = getParentWrapperIndex(parentPositionStart);
        } else {
            initialWrapperIndex = mItemList.size();
        }

        int sizeChanged = 0;
        int wrapperIndex = initialWrapperIndex;
        int changed;
        int parentPositionEnd = parentPositionStart + itemCount;
        for (int i = parentPositionStart; i < parentPositionEnd; i++) {
            ParentListItem parentListItem = mParentItemList.get(i);
            changed = addParentWrapper(wrapperIndex, parentListItem);
            wrapperIndex += changed;
            sizeChanged += changed;
        }

        notifyItemRangeInserted(initialWrapperIndex, sizeChanged);
    }

    /**
     * Notify any registered observers that the ParentListItem previously located at {@code parentPosition}
     * has been removed from the data set. The ParentListItems previously located at and after
     * {@code parentPosition} may now be found at {@code oldPosition - 1}.
     * <p/>
     * This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their positions
     * may be altered.
     *
     * @param parentPosition Position of the ParentListItem that has now been removed, relative
     *                       to list of ParentListItems only.
     */
    public void notifyParentItemRemoved(int parentPosition) {
        int sizeChanged = 1;
        int wrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.remove(wrapperIndex);
        if (parentWrapper.isExpanded()) {
            int childListSize = parentWrapper.getChildItemList().size();
            for (int i = 0; i < childListSize; i++) {
                mItemList.remove(wrapperIndex);
                sizeChanged++;
            }
        }

        notifyItemRangeRemoved(wrapperIndex, sizeChanged);
    }

    /**
     * Notify any registered observers that the ParentListItem at {@code parentPosition} has changed.
     * This will also trigger an item changed for children of the ParentList specified.
     * <p/>
     * This is an item change event, not a structural change event. It indicates that any
     * reflection of the data at {@code parentPosition} is out of date and should be updated.
     * The ParentListItem at {@code parentPosition} retains the same identity. This means
     * the number of children must stay the same.
     *
     * @param parentPosition Position of the item that has changed
     */
    public void notifyParentItemChanged(int parentPosition) {
        ParentListItem parentListItem = mParentItemList.get(parentPosition);
        int wrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(wrapperIndex);
        parentWrapper.setParentListItem(parentListItem);
        int sizeChanged = 1;
        if (parentWrapper.isExpanded()) {
            List<?> childItems = parentWrapper.getChildItemList();
            int childListSize = childItems.size();
            Object child;
            for (int i = 0; i < childListSize; i++) {
                child = childItems.get(i);
                mItemList.set(wrapperIndex + i + 1, child);
                sizeChanged++;
            }
        }

        notifyItemRangeChanged(wrapperIndex, sizeChanged);
    }

    /**
     * Notify any registered observers that the ParentListItem reflected at {@code parentPosition}
     * has a ChildItem that has been newly inserted at {@code childPosition}.
     * The ChildItem previously at {@code childPosition} is now at
     * position {@code childPosition + 1}.
     * <p/>
     * This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their
     * positions may be altered.
     *
     * @param parentPosition Position of the ParentListItem which has been added a child, relative
     *                       to list of ParentListItems only.
     * @param childPosition  Position of the child object that has been inserted, relative to children
     *                       of the ParentListItem specified by {@code parentPosition} only.
     */
    public void notifyChildItemInserted(int parentPosition, int childPosition) {
        ParentListItem parentListItem = mParentItemList.get(parentPosition);
        Object child = parentListItem.getChildItemList().get(childPosition);

        int parentWrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(parentWrapperIndex);
        if (parentWrapper.isExpanded()) {
            mItemList.add(parentWrapperIndex + childPosition + 1, child);
            notifyItemInserted(parentWrapperIndex + childPosition + 1);
        }
    }

    /**
     * Notify any registered observers that the ParentListItem located at {@code parentPosition}
     * has a ChildItem that has been removed from the data set, previously located at {@code childPosition}.
     * The ChildItem previously located at and after {@code childPosition} may
     * now be found at {@code childPosition - 1}.
     * <p/>
     * This is a structural change event. Representations of other existing items in the
     * data set are still considered up to date and will not be rebound, though their positions
     * may be altered.
     *
     * @param parentPosition Position of the ParentListItem which has a child removed from, relative
     *                       to list of ParentListItems only.
     * @param childPosition  Position of the child object that has been removed, relative to children
     *                       of the ParentListItem specified by {@code parentPosition} only.
     */
    public void notifyChildItemRemoved(int parentPosition, int childPosition) {
        int parentWrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(parentWrapperIndex);

        if (parentWrapper.isExpanded()) {
            mItemList.remove(parentWrapperIndex + childPosition + 1);
            notifyItemRemoved(parentWrapperIndex + childPosition + 1);
        }
    }

    /**
     * Notify any registered observers that the ParentListItem at {@code parentPosition} has
     * a child located at {@code childPosition} that has changed.
     * <p/>
     * This is an item change event, not a structural change event. It indicates that any
     * reflection of the data at {@code childPosition} is out of date and should be updated.
     * The ParentListItem at {@code childPosition} retains the same identity.
     *
     * @param parentPosition Position of the ParentListItem who has a child that has changed
     * @param childPosition  Position of the child that has changed
     */
    public void notifyChildItemChanged(int parentPosition, int childPosition) {
        ParentListItem parentListItem = mParentItemList.get(parentPosition);
        int parentWrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(parentWrapperIndex);
        parentWrapper.setParentListItem(parentListItem);
        if (parentWrapper.isExpanded()) {
            int listChildPosition = parentWrapperIndex + childPosition + 1;
            Object child = parentWrapper.getChildItemList().get(childPosition);
            mItemList.set(listChildPosition, child);
            notifyItemChanged(listChildPosition);
        }
    }

    /**
     * Set the event listener fot the child items
     *
     * @param eventListeners the object with the event listeners
     */
    public void setChildItemFermatEventListeners(FermatListItemListeners<CI> eventListeners) {
        this.childItemEventListeners = eventListeners;
    }

    private int addParentWrapper(int wrapperIndex, ParentListItem parentListItem) {
        int sizeChanged = 1;
        ParentWrapper parentWrapper = new ParentWrapper(parentListItem);
        mItemList.add(wrapperIndex, parentWrapper);
        if (parentWrapper.isInitiallyExpanded()) {
            parentWrapper.setExpanded(true);
            List<?> childItemList = parentWrapper.getChildItemList();
            mItemList.addAll(wrapperIndex + sizeChanged, childItemList);
            sizeChanged += childItemList.size();
        }
        return sizeChanged;
    }

    // endregion

    /**
     * Generates a HashMap used to store expanded state for items in the list
     * on configuration change or whenever onResume is called.
     *
     * @return A HashMap containing the expanded state of all parent list items
     */
    private HashMap<Integer, Boolean> generateExpandedStateMap() {
        HashMap<Integer, Boolean> parentListItemHashMap = new HashMap<>();
        int childCount = 0;

        Object listItem;
        ParentWrapper parentWrapper;
        int listItemCount = mItemList.size();
        for (int i = 0; i < listItemCount; i++) {
            if (mItemList.get(i) != null) {
                listItem = getListItem(i);
                if (listItem instanceof ParentWrapper) {
                    parentWrapper = (ParentWrapper) listItem;
                    parentListItemHashMap.put(i - childCount, parentWrapper.isExpanded());
                } else {
                    childCount++;
                }
            }
        }

        return parentListItemHashMap;
    }

    /**
     * Gets the index of a ParentWrapper within the helper item list based on
     * the index of the ParentWrapper.
     *
     * @param parentIndex The index of the parent in the list of parent items
     * @return The index of the parent in the list of all views in the adapter
     */
    private int getParentWrapperIndex(int parentIndex) {
        int parentCount = 0;
        int listItemCount = mItemList.size();
        for (int i = 0; i < listItemCount; i++) {
            if (mItemList.get(i) instanceof ParentWrapper) {
                parentCount++;

                if (parentCount > parentIndex) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * Gets the ParentWrapper for a specified ParentListItem from the list of
     * parents.
     *
     * @param parentListItem A ParentListItem in the list of parents
     * @return If the parent exists on the list, returns its ParentWrapper.
     * Otherwise, returns null.
     */
    private ParentWrapper getParentWrapper(ParentListItem parentListItem) {
        int listItemCount = mItemList.size();
        for (int i = 0; i < listItemCount; i++) {
            Object listItem = mItemList.get(i);
            if (listItem instanceof ParentWrapper) {
                if (((ParentWrapper) listItem).getParentListItem().equals(parentListItem)) {
                    return (ParentWrapper) listItem;
                }
            }
        }

        return null;
    }
}