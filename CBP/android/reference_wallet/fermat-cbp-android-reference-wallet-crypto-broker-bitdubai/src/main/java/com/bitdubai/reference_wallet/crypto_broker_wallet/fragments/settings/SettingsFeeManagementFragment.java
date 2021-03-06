package com.bitdubai.reference_wallet.crypto_broker_wallet.fragments.settings;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_bch_api.layer.definition.crypto_fee.BitcoinFee;
import com.bitdubai.fermat_bch_api.layer.definition.crypto_fee.FeeOrigin;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_broker.CryptoBrokerWalletPreferenceSettings;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_broker.interfaces.CryptoBrokerWalletModuleManager;
import com.bitdubai.reference_wallet.crypto_broker_wallet.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT;
import static com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT;


/**
 * Created by Miguel Payarez (miguel_payarez@hotmail.com) on 05/07/16.
 * Updated by Nelson Ramirez (nelsonalfo@gmail.com) on 06/07/2016
 */
public class SettingsFeeManagementFragment
        extends AbstractFermatFragment<ReferenceAppFermatSession<CryptoBrokerWalletModuleManager>, ResourceProviderManager> {

    // Constants
    private static final String TAG = "SettingsFeeManagement";

    //

    private long bitcoinFee = 0;
    private DecimalFormat df = new DecimalFormat("0.00000000");
    CryptoBrokerWalletPreferenceSettings feeSettings;

    //UI
    private RadioButton radioButtonSlow;
    private RadioButton radioButtonNormal;
    private RadioButton radioButtonFast;
    private FermatTextView feeMinerAmount;
    private Spinner feeOriginSpinner;
    private Spinner blockchainNetworkSpinner;

    // Fermat Managers
    private CryptoBrokerWalletModuleManager moduleManager;
    private ErrorManager errorManager;


    public static SettingsFeeManagementFragment newInstance() {
        return new SettingsFeeManagementFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get managers
        try {
            moduleManager = appSession.getModuleManager();
            errorManager = appSession.getErrorManager();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_BROKER_WALLET, DISABLES_THIS_FRAGMENT, ex);
        }

        try {
            feeSettings = moduleManager.loadAndGetSettings(appSession.getAppPublicKey());
        } catch (Exception e) {
            feeSettings = new CryptoBrokerWalletPreferenceSettings();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        configureToolbar();
        View layout = inflater.inflate(R.layout.cbw_settings_fee_management, container, false);


        final RadioGroup radioButtonGroup = (RadioGroup) layout.findViewById(R.id.cbw_radio_button_group);
        feeMinerAmount = (FermatTextView) layout.findViewById(R.id.cbw_fee_miner_amount);
        feeOriginSpinner = (Spinner) layout.findViewById(R.id.cbw_fee_origin_spinner);
        blockchainNetworkSpinner = (Spinner) layout.findViewById(R.id.cbw_blockchain_network_type);

        radioButtonSlow = (RadioButton) layout.findViewById(R.id.cbw_radio_button_slow);
        radioButtonNormal = (RadioButton) layout.findViewById(R.id.cbw_radio_button_normal);
        radioButtonFast = (RadioButton) layout.findViewById(R.id.cbw_radio_button_fast);

        feeMinerAmount.setText(satoshiToBtcFormat(BitcoinFee.SLOW.getFee()) + " " + getResources().getString(R.string.btc));
        bitcoinFee = BitcoinFee.SLOW.getFee();

        radioButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.cbw_radio_button_slow) {
                    feeMinerAmount.setText(" " + satoshiToBtcFormat(BitcoinFee.SLOW.getFee()) + " " + getResources().getString(R.string.btc));
                    bitcoinFee = BitcoinFee.SLOW.getFee();
                } else if (checkedId == R.id.cbw_radio_button_normal) {
                    feeMinerAmount.setText(" " + satoshiToBtcFormat(BitcoinFee.NORMAL.getFee()) + " " + getResources().getString(R.string.btc));
                    bitcoinFee = BitcoinFee.NORMAL.getFee();
                } else if (checkedId == R.id.cbw_radio_button_fast) {
                    feeMinerAmount.setText(" " + satoshiToBtcFormat(BitcoinFee.FAST.getFee()) + " " + getResources().getString(R.string.btc));
                    bitcoinFee = BitcoinFee.FAST.getFee();
                }
            }
        });


        List<String> feeOriginValues = new ArrayList<>();
        feeOriginValues.add(FeeOrigin.SUBSTRACT_FEE_FROM_AMOUNT.getCode());
        feeOriginValues.add(FeeOrigin.SUBSTRACT_FEE_FROM_FUNDS.getCode());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.cbw_spinner_item, feeOriginValues);
        dataAdapter.setDropDownViewResource(R.layout.cbw_simple_spinner_dropdown_item);
        feeOriginSpinner.setAdapter(dataAdapter);


        List<String> blockchainNetworkType = new ArrayList<>();
        blockchainNetworkType.add(BlockchainNetworkType.REG_TEST.getCode());
        blockchainNetworkType.add(BlockchainNetworkType.TEST_NET.getCode());
        blockchainNetworkType.add(BlockchainNetworkType.PRODUCTION.getCode());

        dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.cbw_spinner_item, blockchainNetworkType);
        dataAdapter.setDropDownViewResource(R.layout.cbw_simple_spinner_dropdown_item);
        blockchainNetworkSpinner.setAdapter(dataAdapter);


        final View nextStepButton = layout.findViewById(R.id.cbw_save_fee_button);
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettingsAndGoBack();
                changeActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SETTINGS, appSession.getAppPublicKey());
            }
        });


        if (feeSettings != null) {
            feeOriginSpinner.setSelection(dataAdapter.getPosition(feeSettings.getFeeOrigin().getCode()));
            blockchainNetworkSpinner.setSelection(dataAdapter.getPosition(feeSettings.getBlockchainNetworkType().getCode()));
            feeMinerAmount.setText(satoshiToBtcFormat(feeSettings.getBitcoinFee().getFee()) + " " + getResources().getString(R.string.btc));
            selectRadioButtonsValue(feeSettings.getBitcoinFee().getFee());
        }


        return layout;
    }

    private void selectRadioButtonsValue(long bitCoinFee) {

        if (bitCoinFee == BitcoinFee.SLOW.getFee()) {
            radioButtonSlow.setChecked(true);
            radioButtonNormal.setChecked(false);
            radioButtonFast.setChecked(false);
        } else if (bitCoinFee == BitcoinFee.NORMAL.getFee()) {
            radioButtonSlow.setChecked(false);
            radioButtonNormal.setChecked(true);
            radioButtonFast.setChecked(false);
        } else if (bitCoinFee == BitcoinFee.FAST.getFee()) {
            radioButtonSlow.setChecked(false);
            radioButtonNormal.setChecked(false);
            radioButtonFast.setChecked(true);
        }
    }


    private String satoshiToBtcFormat(Long satoshi) {
        return df.format(Double.valueOf(satoshi) / 100000000);

    }

    @SuppressWarnings("deprecation")
    private void configureToolbar() {
        Toolbar toolbar = getToolbar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            toolbar.setBackground(getResources().getDrawable(R.drawable.cbw_action_bar_gradient_colors, null));
        else
            toolbar.setBackground(getResources().getDrawable(R.drawable.cbw_action_bar_gradient_colors));

        toolbar.setTitleTextColor(Color.WHITE);
        if (toolbar.getMenu() != null) toolbar.getMenu().clear();
    }

    private void saveSettingsAndGoBack() {
        try {
            feeSettings.setBitcoinFee(BitcoinFee.getByFee(bitcoinFee));
            feeSettings.setFeeOrigin(FeeOrigin.getByCode(feeOriginSpinner.getSelectedItem().toString()));
            feeSettings.setBlockchainNetworkType(BlockchainNetworkType.getByCode(blockchainNetworkSpinner.getSelectedItem().toString()));

            moduleManager.persistSettings(appSession.getAppPublicKey(), feeSettings);

        } catch (FermatException ex) {
            Toast.makeText(SettingsFeeManagementFragment.this.getActivity(), getResources().getString(R.string.error_settings), Toast.LENGTH_SHORT).show();

            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_BROKER_WALLET,
                        DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, ex);
            else
                Log.e(TAG, ex.getMessage(), ex);
        }

        changeActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SETTINGS, appSession.getAppPublicKey());
    }
}

