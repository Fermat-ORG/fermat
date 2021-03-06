package com.bitdubai.reference_wallet.crypto_broker_wallet.app_connection;

import android.content.Context;

import com.bitdubai.fermat_android_api.core.ResourceSearcher;
import com.bitdubai.fermat_android_api.engine.FermatFragmentFactory;
import com.bitdubai.fermat_android_api.engine.FooterViewPainter;
import com.bitdubai.fermat_android_api.engine.HeaderViewPainter;
import com.bitdubai.fermat_android_api.engine.NavigationViewPainter;
import com.bitdubai.fermat_android_api.engine.NotificationPainter;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.AppConnections;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Developers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.FermatBundle;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.NotificationBundleConstants;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_broker.interfaces.CryptoBrokerWalletModuleManager;
import com.bitdubai.reference_wallet.crypto_broker_wallet.R;
import com.bitdubai.reference_wallet.crypto_broker_wallet.common.footer.CryptoBrokerWalletFooterPainter;
import com.bitdubai.reference_wallet.crypto_broker_wallet.common.header.CryptoBrokerWalletHeaderPainter;
import com.bitdubai.reference_wallet.crypto_broker_wallet.common.navigationDrawer.CryptoBrokerNavigationViewPainter;
import com.bitdubai.reference_wallet.crypto_broker_wallet.common.notifications.CryptoBrokerNotificationPainter;
import com.bitdubai.reference_wallet.crypto_broker_wallet.fragmentFactory.CryptoBrokerWalletFragmentFactory;

import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_CANCEL_NEGOTIATION_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_CONTRACT_CANCELLED_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_CONTRACT_COMPLETED_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_CONTRACT_CUSTOMER_SUBMITTED_PAYMENT_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_CONTRACT_EXPIRATION_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_NEW_CONTRACT_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_NEW_NEGOTIATION_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_WAITING_FOR_BROKER_NOTIFICATION;


/**
 * Created by Nelson Ramirez
 *
 * @since 2015.12.17
 */
public class CryptoBrokerWalletFermatAppConnection extends AppConnections<ReferenceAppFermatSession<CryptoBrokerWalletModuleManager>> {

    private CryptoBrokerWalletResourceSearcher resourceSearcher;


    public CryptoBrokerWalletFermatAppConnection(Context activity) {
        super(activity);
    }

    @Override
    public FermatFragmentFactory getFragmentFactory() {
        return new CryptoBrokerWalletFragmentFactory();
    }

    @Override
    public PluginVersionReference[] getPluginVersionReference() {
        return new PluginVersionReference[]{new PluginVersionReference(
                Platforms.CRYPTO_BROKER_PLATFORM,
                Layers.WALLET_MODULE,
                Plugins.CRYPTO_BROKER,
                Developers.BITDUBAI,
                new Version()
        )};

    }

    @Override
    protected ReferenceAppFermatSession<CryptoBrokerWalletModuleManager> getSession() {
        return getFullyLoadedSession();
    }

    @Override
    public NavigationViewPainter getNavigationViewPainter() {
        return new CryptoBrokerNavigationViewPainter(getContext(), getFullyLoadedSession(), getApplicationManager());
    }

    @Override
    public HeaderViewPainter getHeaderViewPainter() {
        return new CryptoBrokerWalletHeaderPainter(getContext(), getFullyLoadedSession());
    }

    @Override
    public FooterViewPainter getFooterViewPainter() {
        return new CryptoBrokerWalletFooterPainter(getContext(), getFullyLoadedSession());
    }

    @Override
    public NotificationPainter getNotificationPainter(FermatBundle fermatBundle) {
        int notificationID = fermatBundle.getInt(NotificationBundleConstants.NOTIFICATION_ID);

        switch (notificationID) {
            case CBW_NEW_NEGOTIATION_NOTIFICATION:
                return new CryptoBrokerNotificationPainter(getContext().getResources().getString(R.string.new_negotiation), getContext().getResources().getString(R.string.have_negotiation_message), "");
            case CBW_WAITING_FOR_BROKER_NOTIFICATION:
                return new CryptoBrokerNotificationPainter(getContext().getResources().getString(R.string.negotiation_update), getContext().getResources().getString(R.string.negotiation_update_message), "");
            case CBW_CANCEL_NEGOTIATION_NOTIFICATION:
                return new CryptoBrokerNotificationPainter(getContext().getResources().getString(R.string.negotiation_cancelled2), getContext().getResources().getString(R.string.negotiation_cancelled_message), "");
            case CBW_NEW_CONTRACT_NOTIFICATION:
                return new CryptoBrokerNotificationPainter(getContext().getResources().getString(R.string.new_contract), getContext().getResources().getString(R.string.new_contract_message), "");
            case CBW_CONTRACT_CUSTOMER_SUBMITTED_PAYMENT_NOTIFICATION:
                return new CryptoBrokerNotificationPainter(getContext().getResources().getString(R.string.contract_update), getContext().getResources().getString(R.string.contract_update_message1), "");
            case CBW_CONTRACT_COMPLETED_NOTIFICATION:
                return new CryptoBrokerNotificationPainter(getContext().getResources().getString(R.string.contract_update), getContext().getResources().getString(R.string.contract_update_message2), "");
            case CBW_CONTRACT_CANCELLED_NOTIFICATION:
                return new CryptoBrokerNotificationPainter(getContext().getResources().getString(R.string.contract_update), getContext().getResources().getString(R.string.contract_update_message3), "");
            case CBW_CONTRACT_EXPIRATION_NOTIFICATION:
                return new CryptoBrokerNotificationPainter(getContext().getResources().getString(R.string.expiring_contract), getContext().getResources().getString(R.string.expiring_contract_message), "");
            default:
                return super.getNotificationPainter(fermatBundle);
        }
    }

    @Override
    public ResourceSearcher getResourceSearcher() {
        if (resourceSearcher == null)
            resourceSearcher = new CryptoBrokerWalletResourceSearcher();
        return resourceSearcher;
    }
}
