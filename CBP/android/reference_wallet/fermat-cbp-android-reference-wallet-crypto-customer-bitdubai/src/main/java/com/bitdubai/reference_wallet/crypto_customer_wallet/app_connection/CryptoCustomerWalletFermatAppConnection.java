package com.bitdubai.reference_wallet.crypto_customer_wallet.app_connection;

import android.content.Context;
import android.content.res.Resources;

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
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_customer.interfaces.CryptoCustomerWalletModuleManager;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.header.CryptoCustomerWalletHeaderPainter;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.navigationDrawer.CustomerNavigationViewPainter;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.notifications.CryptoCustomerNotificationPainter;
import com.bitdubai.reference_wallet.crypto_customer_wallet.fragmentFactory.CryptoCustomerWalletFragmentFactory;

import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CCW_CANCEL_NEGOTIATION_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CCW_CONTRACT_BROKER_ACK_PAYMENT_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CCW_CONTRACT_BROKER_SUBMITED_MERCHANDISE;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CCW_CONTRACT_CANCELLED_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CCW_CONTRACT_COMPLETED_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CCW_CONTRACT_EXPIRATION_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CCW_NEW_CONTRACT_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CCW_WAITING_FOR_CUSTOMER_NOTIFICATION;


/**
 * Created by Nelson Ramirez
 *
 * @since 2015.12.17
 */
public class CryptoCustomerWalletFermatAppConnection extends AppConnections<ReferenceAppFermatSession<CryptoCustomerWalletModuleManager>> {
    private final String LANGUAGE = Resources.getSystem().getConfiguration().locale.getLanguage();
    
    private CryptoCustomerWalletResourceSearcher resourceSearcher;


    public CryptoCustomerWalletFermatAppConnection(Context activity) {
        super(activity);
    }

    @Override
    public FermatFragmentFactory getFragmentFactory() {
        return new CryptoCustomerWalletFragmentFactory();
    }

    @Override
    public PluginVersionReference[] getPluginVersionReference() {
        return new PluginVersionReference[]{new PluginVersionReference(
                Platforms.CRYPTO_BROKER_PLATFORM,
                Layers.WALLET_MODULE,
                Plugins.CRYPTO_CUSTOMER,
                Developers.BITDUBAI,
                new Version()
        )};
    }

    @Override
    protected ReferenceAppFermatSession<CryptoCustomerWalletModuleManager> getSession() {
        return getFullyLoadedSession();
    }


    @Override
    public NavigationViewPainter getNavigationViewPainter() {
        return new CustomerNavigationViewPainter(getContext(), getFullyLoadedSession(), getApplicationManager());
    }

    @Override
    public HeaderViewPainter getHeaderViewPainter() {
        return new CryptoCustomerWalletHeaderPainter(getContext(), getFullyLoadedSession());
    }

    @Override
    public FooterViewPainter getFooterViewPainter() {
        return null;
    }

    @Override
    public NotificationPainter getNotificationPainter(FermatBundle fermatBundle) {
        int notificationID = fermatBundle.getInt(NotificationBundleConstants.NOTIFICATION_ID);
        String title, textBody;

        switch (notificationID) {
            case CCW_WAITING_FOR_CUSTOMER_NOTIFICATION:
                title = !LANGUAGE.equalsIgnoreCase("es") ? "Negotiation Update" : "Negociación Actualizada";

                textBody = !LANGUAGE.equalsIgnoreCase("es") ?
                        "You have received an update, check your Customer Wallet." :
                        "Recibiste una actualización, revisa tu Customer Wallet.";
                break;

            case CCW_CANCEL_NEGOTIATION_NOTIFICATION:
                title = !LANGUAGE.equalsIgnoreCase("es") ? "Negotiation Canceled" : "Negociación Cancelada";

                textBody = !LANGUAGE.equalsIgnoreCase("es") ?
                        "Check the Contract History, a broker has canceled a negotiation." :
                        "Verifica tu Historial de Contratos, una negociacion a sido cancelada.";
                break;

            case CCW_NEW_CONTRACT_NOTIFICATION:
                title = !LANGUAGE.equalsIgnoreCase("es") ? "New contract." : "Nuevo Contrato";

                textBody = !LANGUAGE.equalsIgnoreCase("es") ?"A new Contract has been created, check your wallet." :
                        "Un nuevo Contrato ha sido creado, revisa tu Customer Wallet.";
                break;

            case CCW_CONTRACT_BROKER_ACK_PAYMENT_NOTIFICATION:
                title = !LANGUAGE.equalsIgnoreCase("es") ? "Contract Update" : "Contrato Actualizado";
                textBody = !LANGUAGE.equalsIgnoreCase("es") ? "A broker has acknowledged your payment." : "Un broker a confirmado tu pago.";
                break;

            case CCW_CONTRACT_BROKER_SUBMITED_MERCHANDISE:
                title = !LANGUAGE.equalsIgnoreCase("es") ? "Contract Update" : "Contrato Actualizado";
                textBody = !LANGUAGE.equalsIgnoreCase("es") ? "A Merchandise has been submitted." : "Una Mercancia a sido recibida.";
                break;

            case CCW_CONTRACT_COMPLETED_NOTIFICATION:
                title = !LANGUAGE.equalsIgnoreCase("es") ? "Contract Update" : "Contrato Actualizado";
                textBody = !LANGUAGE.equalsIgnoreCase("es") ? "The contract has been completed." : "El contrato ha sido completado.";
                break;

            case CCW_CONTRACT_CANCELLED_NOTIFICATION:
                title = !LANGUAGE.equalsIgnoreCase("es") ? "Contract Update" : "Contrato Actualizado";

                textBody = !LANGUAGE.equalsIgnoreCase("es") ?
                        "The contract has been cancelled, check the Contract History." :
                        "El contrato a sido cancelado, revisa el Historial de Contratos.";
                break;

            case CCW_CONTRACT_EXPIRATION_NOTIFICATION:
                title = !LANGUAGE.equalsIgnoreCase("es") ? "Expiring contract." : "Expiración de contrato";

                textBody = !LANGUAGE.equalsIgnoreCase("es") ?
                        "A contract is about to expire, check your Customer Wallet." :
                        "Un contrato esta por expirar, revisa tu Customer Wallet.";
                break;

            default:
                return super.getNotificationPainter(fermatBundle);
        }

        return new CryptoCustomerNotificationPainter(title, textBody, "");
    }

    @Override
    public ResourceSearcher getResourceSearcher() {
        if (resourceSearcher == null)
            resourceSearcher = new CryptoCustomerWalletResourceSearcher();
        return resourceSearcher;
    }
}
