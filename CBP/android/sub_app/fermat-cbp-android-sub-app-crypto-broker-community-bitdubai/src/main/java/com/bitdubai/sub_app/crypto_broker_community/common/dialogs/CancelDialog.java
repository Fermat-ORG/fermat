package com.bitdubai.sub_app.crypto_broker_community.common.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatButton;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.dialogs.FermatDialog;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_community.exceptions.ConnectionRequestNotFoundException;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_community.exceptions.CryptoBrokerCancellingFailedException;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_community.interfaces.CryptoBrokerCommunityInformation;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_community.interfaces.CryptoBrokerCommunitySelectableIdentity;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_community.interfaces.CryptoBrokerCommunitySubAppModuleManager;
import com.bitdubai.fermat_pip_api.layer.network_service.subapp_resources.SubAppResourcesProviderManager;
import com.bitdubai.sub_app.crypto_broker_community.R;


/**
 * Created by Alejandro Bicelis on 15/02/2016
 */
public class CancelDialog extends FermatDialog<ReferenceAppFermatSession<CryptoBrokerCommunitySubAppModuleManager>, SubAppResourcesProviderManager>
        implements View.OnClickListener {

    /**
     * UI components
     */
    FermatButton positiveBtn;
    FermatButton negativeBtn;
    FermatTextView mDescription;
    FermatTextView subTitle;
    FermatTextView mTitle;
    CharSequence description;
    CharSequence username;
    CharSequence title;

    CryptoBrokerCommunityInformation cryptoBrokerCommunityInformation;

    CryptoBrokerCommunitySelectableIdentity identity;


    public CancelDialog(Activity activity,
                        ReferenceAppFermatSession<CryptoBrokerCommunitySubAppModuleManager> session,
                        SubAppResourcesProviderManager subAppResources,
                        CryptoBrokerCommunityInformation cryptoBrokerCommunityInformation,
                        CryptoBrokerCommunitySelectableIdentity identity) {

        super(activity, session, subAppResources);

        this.cryptoBrokerCommunityInformation = cryptoBrokerCommunityInformation;
        this.identity = identity;
    }


    @Override
    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDescription = (FermatTextView) findViewById(R.id.cbc_description);
        subTitle = (FermatTextView) findViewById(R.id.cbc_sub_title);
        mTitle = (FermatTextView) findViewById(R.id.cbc_title);
        positiveBtn = (FermatButton) findViewById(R.id.positive_button);
        negativeBtn = (FermatButton) findViewById(R.id.negative_button);

        positiveBtn.setOnClickListener(this);
        negativeBtn.setOnClickListener(this);
        mDescription.setText(description != null ? description : "");
        subTitle.setText(username != null ? username : "");
        mTitle.setText(title != null ? title : "");

    }

    public void setDescription(CharSequence description) {
        this.description = description;
    }

    public void setUsername(CharSequence username) {
        this.username = username;
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.cbc_dialog_generic_use;
    }

    @Override
    protected int setWindowFeature() {
        return Window.FEATURE_NO_TITLE;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.positive_button) {
            try {
                if (cryptoBrokerCommunityInformation != null && identity != null) {

                    getSession().getModuleManager().cancelCryptoBroker(cryptoBrokerCommunityInformation.getConnectionId());
                    Toast.makeText(getContext(), R.string.cancelled_successfully, Toast.LENGTH_SHORT).show();

                    //set flag so that the preceding fragment reads it on dismiss()
                    getSession().setData("connectionresult", 1);

                } else {
                    Toast.makeText(getContext(), R.string.error_try, Toast.LENGTH_SHORT).show();
                }
                dismiss();
            } catch (CryptoBrokerCancellingFailedException e) {
                getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.UNSTABLE, e);
                Toast.makeText(getContext(), R.string.error_try2, Toast.LENGTH_SHORT).show();
            } catch (ConnectionRequestNotFoundException e) {
                getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.UNSTABLE, e);
                Toast.makeText(getContext(), R.string.error_cancel, Toast.LENGTH_SHORT).show();
            }

            dismiss();
        } else if (i == R.id.negative_button) {
            dismiss();
        }
    }


}
