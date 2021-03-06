package org.fermat.fermat_dap_android_wallet_asset_issuer.v3.common.filters;

import android.widget.Filter;

import org.fermat.fermat_dap_android_wallet_asset_issuer.models.User;
import org.fermat.fermat_dap_android_wallet_asset_issuer.v3.common.adapters.DeliverUserAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank Contreras (contrerasfrank@gmail.com) on 4/27/16.
 */
public class DeliverUserAdapterFilter extends Filter {

    private List<User> data;
    private DeliverUserAdapter adapter;

    public DeliverUserAdapterFilter(List<User> data, DeliverUserAdapter adapter) {
        this.data = data;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        String filterString = constraint.toString().toLowerCase();

        FilterResults results = new FilterResults();

        final List<User> list = data;

        int count = list.size();
        final ArrayList<User> nlist = new ArrayList<>(count);

        String filterableString;
        User user;

        for (int i = 0; i < count; i++) {
            user = list.get(i);
            filterableString = user.getName();
            if (filterableString.toLowerCase().contains(filterString)) {
                nlist.add(list.get(i));
            }
        }

        results.values = nlist;
        results.count = nlist.size();

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults filterResults) {
        adapter.changeDataSet((List<User>) filterResults.values);
    }
}
