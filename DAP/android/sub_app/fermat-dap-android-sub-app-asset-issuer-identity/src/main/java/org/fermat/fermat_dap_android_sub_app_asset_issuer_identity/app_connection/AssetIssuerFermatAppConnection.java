package org.fermat.fermat_dap_android_sub_app_asset_issuer_identity.app_connection;

import android.content.Context;

import com.bitdubai.fermat_android_api.core.ResourceSearcher;
import com.bitdubai.fermat_android_api.engine.FermatFragmentFactory;
import com.bitdubai.fermat_android_api.engine.FooterViewPainter;
import com.bitdubai.fermat_android_api.engine.HeaderViewPainter;
import com.bitdubai.fermat_android_api.engine.NavigationViewPainter;
import com.bitdubai.fermat_android_api.layer.definition.wallet.abstracts.AbstractReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.AppConnections;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Developers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_dap_android_sub_app_asset_issuer_identity_bitdubai.R;

import org.fermat.fermat_dap_android_sub_app_asset_issuer_identity.fragmentFactory.IssuerIdentityFragmentFactory;
import org.fermat.fermat_dap_android_sub_app_asset_issuer_identity.session.IssuerIdentitySubAppSessionReferenceApp;
import org.fermat.fermat_dap_api.layer.dap_sub_app_module.asset_issuer_identity.interfaces.AssetIssuerIdentityModuleManager;

/**
 * Created by Matias Furszyfer on 2015.12.09..
 */
public class AssetIssuerFermatAppConnection extends AppConnections<ReferenceAppFermatSession<AssetIssuerIdentityModuleManager>> {

    public AssetIssuerFermatAppConnection(Context activity) {
        super(activity);
    }

    @Override
    public FermatFragmentFactory getFragmentFactory() {
        return new IssuerIdentityFragmentFactory();
    }

    @Override
    public PluginVersionReference[] getPluginVersionReference() {
        return new PluginVersionReference[]{new PluginVersionReference(
                Platforms.DIGITAL_ASSET_PLATFORM,
                Layers.SUB_APP_MODULE,
                Plugins.BITDUBAI_DAP_ASSET_ISSUER_IDENTITY,
                Developers.BITDUBAI,
                new Version()
        )};
    }

    @Override
    public AbstractReferenceAppFermatSession getSession() {
        return new IssuerIdentitySubAppSessionReferenceApp();
    }


    @Override
    public NavigationViewPainter getNavigationViewPainter() {
        return null;
    }

    @Override
    public HeaderViewPainter getHeaderViewPainter() {
        return null;
    }

    @Override
    public FooterViewPainter getFooterViewPainter() {
        return null;
    }

    @Override
    public ResourceSearcher getResourceSearcher() {
        return new AssetIssuerSearcher();
    }
}
