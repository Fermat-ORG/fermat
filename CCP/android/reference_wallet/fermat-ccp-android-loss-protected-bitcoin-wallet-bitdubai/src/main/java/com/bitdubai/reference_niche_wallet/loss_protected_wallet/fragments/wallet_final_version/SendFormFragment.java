package com.bitdubai.reference_niche_wallet.loss_protected_wallet.fragments.wallet_final_version;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bitdubai.android_fermat_ccp_loss_protected_wallet_bitcoin.R;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatButton;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.transformation.CircleTransform;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_api.layer.all_definition.common.system.enums.NetworkStatus;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantGetCommunicationNetworkStatusException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrencyVault;
import com.bitdubai.fermat_api.layer.all_definition.enums.Engine;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.ReferenceWallet;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.enums.VaultType;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.CantPersistSettingsException;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.BitcoinNetworkConfiguration;
import com.bitdubai.fermat_bch_api.layer.definition.crypto_fee.BitcoinFee;
import com.bitdubai.fermat_bch_api.layer.definition.crypto_fee.FeeOrigin;
import com.bitdubai.fermat_ccp_api.all_definition.util.BitcoinConverter;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.enums.BalanceType;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.BitcoinWalletSettings;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.exceptions.ContactNameAlreadyExistsException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.LossProtectedWalletSettings;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantCreateLossProtectedWalletContactException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantFindLossProtectedWalletContactException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantGetAllLossProtectedWalletContactsException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantGetLossProtectedBalanceException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantRequestLossProtectedAddressException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedWallet;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedWalletContact;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.bar_code_scanner.IntentIntegrator;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.contacts_list_adapter.WalletContact;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.contacts_list_adapter.WalletContactListAdapter;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.enums.ShowMoneyType;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.popup.ConfirmSendDialog_feeCase;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.popup.Confirm_send_dialog;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.popup.ErrorConnectingFermatNetworkDialog;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.popup.ErrorExchangeRateConnectionDialog;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.utils.BitmapWorkerTask;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.utils.DecimalDigitsInputFilter;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.utils.WalletUtils;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.session.SessionConstant;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.widget.Toast.makeText;
import static com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.utils.WalletUtils.showMessage;

/**
 * Created by Matias Furszyfer on 2015.11.05..
 * updated by Andres Abreu aabreu1 2016/07/28
 */
public class SendFormFragment extends AbstractFermatFragment<ReferenceAppFermatSession<LossProtectedWallet>,ResourceProviderManager> implements View.OnClickListener{


    /**
     * Plaform reference
     */


    private LossProtectedWallet lossProtectedWalletManager;

    private ReferenceAppFermatSession<LossProtectedWallet> lossProtectedWalletSession;

    /**
     * UI
     */
    private View rootView;
    private AutoCompleteTextView contactName;
    private EditText editTextAmount;
    private EditText editFeedamount;
    private ImageView imageView_contact;
    private FermatButton send_button;
    private TextView txt_notes;
    private BitcoinConverter bitcoinConverter;
    private TextView txt_balance;
    private String feeLevel = "";
    private String feeOrigin = "";
    private LinearLayout advances_btn;

    private FermatWorker fermatWorker;

    /**
     * Adapters
     */
    private WalletContactListAdapter contactsAdapter;

    /**
     * User selected
     */
    private LossProtectedWalletContact lossProtectedWalletContact;

    private WalletContact walletContact;
    private boolean connectionDialogIsShow;
    private boolean onFocus;
    private Spinner spinner;
    private FermatTextView txt_type;
    private ImageView spinnerArrow;

    private LossProtectedWalletSettings lossProtectedWalletSettings;
    private BlockchainNetworkType blockchainNetworkType;

    boolean lossProtectedEnabled;
    private List<WalletContact> walletContactList = new ArrayList<>();

    private ExecutorService _executor;
    private CheckBox feed_Substract;
    private LinearLayout layoutAdvances;
    private RadioGroup feeGroup;
    private RadioButton fee_low_btn;
    private RadioButton fee_medium_btn;
    private RadioButton fee_high_btn;
    private long Balance = 0;

    private BitcoinWalletSettings bitcoinWalletSettings = null;

    public static SendFormFragment newInstance() {
        return new SendFormFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bitcoinConverter = new BitcoinConverter();
        setHasOptionsMenu(true);
        try {

            _executor = Executors.newFixedThreadPool(2);
            lossProtectedWalletSession = appSession;

            lossProtectedWalletManager = appSession.getModuleManager();


            if(appSession.getData(SessionConstant.BLOCKCHANIN_TYPE) != null)
                blockchainNetworkType = (BlockchainNetworkType)appSession.getData(SessionConstant.BLOCKCHANIN_TYPE);
            else
                blockchainNetworkType = BlockchainNetworkType.getDefaultBlockchainNetworkType();

            if(appSession.getData(SessionConstant.FEE_LEVEL) != null)
                feeLevel = (String)appSession.getData(SessionConstant.FEE_LEVEL);
            else
                feeLevel = BitcoinFee.NORMAL.toString();


            if(appSession.getData(SessionConstant.LOSS_PROTECTED_ENABLED) != null)
                lossProtectedEnabled = (boolean)appSession.getData(SessionConstant.LOSS_PROTECTED_ENABLED);
            else
                lossProtectedEnabled = true;

        } catch (Exception e) {
            appSession.getErrorManager().reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            showMessage(getActivity(), "CantGetCryptoWalletException- " + e.getMessage());

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        try {
            rootView = inflater.inflate(R.layout.loss_send_form_base, container, false);
            NetworkStatus networkStatus = getFermatNetworkStatus();
            if (networkStatus!= null) {
                switch (networkStatus) {
                    case CONNECTED:
                        setUpUI();
                        contactName.setText("");
                        setUpActions();
                        setUpUIData();

                        break;
                    case DISCONNECTED:
                        showErrorConnectionDialog();
                        setUpUI();
                        contactName.setText("");
                        setUpActions();
                        setUpUIData();

                        break;
                }
            }else {
                setUpUI();
                contactName.setText("");
                setUpActions();
                setUpUIData();

            }


            setUpContactAddapter();

            //(Hide keyboard)
            final InputMethodManager imm;
            imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(contactName.getWindowToken(), 0);

            return rootView;
        } catch (Exception e) {
            Toast.makeText(getActivity(), getResources().getString(R.string.Whooops_text), Toast.LENGTH_SHORT).show();
            appSession.getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.CRASH, e);

        }

        return null;
    }

    private void showErrorConnectionDialog() {
        final ErrorConnectingFermatNetworkDialog errorConnectingFermatNetworkDialog = new ErrorConnectingFermatNetworkDialog(getActivity(), appSession, null);
        errorConnectingFermatNetworkDialog.setLeftButton("CANCEL", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorConnectingFermatNetworkDialog.dismiss();
                getActivity().onBackPressed();
            }
        });
        errorConnectingFermatNetworkDialog.setRightButton("CONNECT", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorConnectingFermatNetworkDialog.dismiss();
                try {
                    if (getFermatNetworkStatus() == NetworkStatus.DISCONNECTED) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.Wait_a_minute), Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                } catch (CantGetCommunicationNetworkStatusException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        errorConnectingFermatNetworkDialog.show();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            super.onActivityCreated(savedInstanceState);

            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getView().getWindowToken(), 0);

        } catch (Exception e){
            makeText(getActivity(), getResources().getString(R.string.Whooops_text), Toast.LENGTH_SHORT).show();
            appSession.getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.CRASH, e);
        }
    }
    private void setUpUI() {

        try{
        contactName = (AutoCompleteTextView) rootView.findViewById(R.id.contact_name);
        spinnerArrow = (ImageView) rootView.findViewById(R.id.spinner_open);
        txt_notes = (TextView) rootView.findViewById(R.id.notes);
        editTextAmount = (EditText) rootView.findViewById(R.id.amount);
        imageView_contact = (ImageView) rootView.findViewById(R.id.profile_Image);
        send_button = (FermatButton) rootView.findViewById(R.id.send_button);
        txt_type = (FermatTextView) rootView.findViewById(R.id.txt_type);
        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        txt_balance = (TextView) rootView.findViewById(R.id.balance);
        editFeedamount = (EditText) rootView.findViewById(R.id.feed_amount);
        feed_Substract= (CheckBox) rootView.findViewById(R.id.checkBoxSubstract);


        advances_btn = (LinearLayout) rootView.findViewById(R.id.advances_btn);
        layoutAdvances = (LinearLayout) rootView.findViewById(R.id.feed_advances);
        feeGroup = (RadioGroup) rootView.findViewById(R.id.feeGroup);
        fee_low_btn = (RadioButton) rootView.findViewById(R.id.fee_low);
        fee_medium_btn = (RadioButton) rootView.findViewById(R.id.fee_Medium);
        fee_high_btn = (RadioButton) rootView.findViewById(R.id.fee_High);

        advances_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutAdvances.getVisibility() == View.GONE) {
                    layoutAdvances.setVisibility(View.VISIBLE);
                } else {
                    layoutAdvances.setVisibility(View.GONE);
                }

            }
        });

        if (feeLevel.equals(String.valueOf(BitcoinFee.SLOW))) {
            fee_low_btn.setChecked(true);
        }else if(feeLevel.equals(String.valueOf(BitcoinFee.NORMAL))) {
            fee_medium_btn.setChecked(true);
        }else if(feeLevel.equals(String.valueOf(BitcoinFee.FAST))) {
            fee_high_btn.setChecked(true);
        }

        editFeedamount.setText(bitcoinConverter.getBTC(String.valueOf(BitcoinFee.valueOf(feeLevel).getFee())));


        feeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                if (checkedId == R.id.fee_low) {
                    editFeedamount.setText(bitcoinConverter.getBTC(String.valueOf(BitcoinFee.SLOW.getFee())));
                    feeLevel = String.valueOf(BitcoinFee.SLOW);
                } else if (checkedId == R.id.fee_Medium) {
                    editFeedamount.setText(bitcoinConverter.getBTC(String.valueOf(BitcoinFee.NORMAL.getFee())));
                    feeLevel = String.valueOf(BitcoinFee.NORMAL);
                } else if (checkedId == R.id.fee_High) {
                    editFeedamount.setText(bitcoinConverter.getBTC(String.valueOf(BitcoinFee.FAST.getFee())));
                    feeLevel = String.valueOf(BitcoinFee.FAST);
                }

                bitcoinWalletSettings.setFeedLevel(feeLevel);

                    lossProtectedWalletManager.persistSettings(appSession.getAppPublicKey(), lossProtectedWalletSettings);
                } catch (CantPersistSettingsException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Balance = lossProtectedWalletManager.getBalance(BalanceType.AVAILABLE, lossProtectedWalletSession.getAppPublicKey(), blockchainNetworkType, String.valueOf(lossProtectedWalletSession.getData(SessionConstant.ACTUAL_EXCHANGE_RATE)));
            txt_balance.setText(WalletUtils.formatBalanceString(Balance,ShowMoneyType.BITCOIN.getCode())+ " BTC");

        } catch (CantGetLossProtectedBalanceException e) {
            e.printStackTrace();
        }

        try {

            List<String> list = new ArrayList<String>();
            list.add("BTC");
            list.add("Bits");
            list.add("Satoshis");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                    R.layout.list_item_spinner, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String text = "";
                    String txtType = txt_type.getText().toString();
                    String amount = editTextAmount.getText().toString();
                    String newAmount = "";
                    switch (position) {
                        case 0:
                            text = "[btc]";
                            if (!amount.equals("") && amount != null) {
                                if (txtType.equals("[bits]")) {
                                    newAmount = bitcoinConverter.getBitcoinsFromBits(amount);
                                } else if (txtType.equals("[satoshis]")) {
                                    newAmount = bitcoinConverter.getBTC(amount);
                                } else {
                                    newAmount = amount;
                                }
                            } else {
                                newAmount = amount;
                            }


                            break;
                        case 1:
                            text = "[bits]";
                            if (!amount.equals("") && amount != null) {
                                if (txtType.equals("[btc]")) {
                                    newAmount = bitcoinConverter.getBitsFromBTC(amount);
                                } else if (txtType.equals("[satoshis]")) {
                                    newAmount = bitcoinConverter.getBits(amount);
                                } else {
                                    newAmount = amount;
                                }
                            } else {
                                newAmount = amount;
                            }

                            break;
                        case 2:
                            text = "[satoshis]";
                            if (!amount.equals("") && amount != null) {
                                if (txtType.equals("[bits]")) {
                                    newAmount = bitcoinConverter.getSathoshisFromBits(amount);
                                } else if (txtType.equals("[btc]")) {
                                    newAmount = bitcoinConverter.getSathoshisFromBTC(amount);
                                } else {
                                    newAmount = amount;
                                }
                            } else {
                                newAmount = amount;
                            }
                            break;
                    }
                    AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.4, 1);
                    alphaAnimation.setDuration(300);
                    final String finalText = text;
                    if (newAmount.equals("0"))
                        newAmount = "";

                    final String finalAmount = newAmount;
                    alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            txt_type.setText(finalText);
                            editTextAmount.setText(finalAmount);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    txt_type.startAnimation(alphaAnimation);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinnerArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spinner.performClick();
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpActions() {
        try {
            /**
             * Listeners
             */

            imageView_contact.setOnClickListener(this);
            send_button.setOnClickListener(this);
            rootView.findViewById(R.id.scan_qr).setOnClickListener(this);

            contactName.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                public boolean onEditorAction(TextView v, int actionId,
                                              KeyEvent event) {
                    if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        // in.hideSoftInputFromWindow(autoEditText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        //Commented line is for hide keyboard. Just make above code as comment and test your requirement
                        //It will work for your need. I just putted that line for your understanding only
                        //You can use own requirement here also.

                   /* if (!connectionDialogIsShow) {
                        ConnectionWithCommunityDialog connectionWithCommunityDialog = new ConnectionWithCommunityDialog(getActivity(), appSession, appSession.getResourceProviderManager());
                        connectionWithCommunityDialog.show();
                        connectionWithCommunityDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                contactName.setText("");
                                connectionDialogIsShow = false;
                            }
                        });
                        connectionDialogIsShow = true;
                    }*/
                        return true;
                    }
                    return false;
                }
            });
       /* contactName.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(contactName, 0);
            }
        }, 50);*/
            contactName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    onFocus = hasFocus;
                    if (!onFocus) {
                        if (walletContact == null) {
                            contactName.setText("");
                        }
                    }
                }
            });
            /**
             *  Amount observer
             */
        /*editTextAmount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        */

            editTextAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(11, 8)});


            /**
             * Selector
             */
            //send_button.selector(R.drawable.bg_home_accept_normal,R.drawable.bg_home_accept_active, R.drawable.bg_home_accept_normal );

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setUpUIData() {
        if(lossProtectedWalletContact==null) {
            lossProtectedWalletContact = (LossProtectedWalletContact) appSession.getData(SessionConstant.LAST_SELECTED_CONTACT);
        }
        if (lossProtectedWalletContact != null) {
            try {

                if (lossProtectedWalletContact.getProfilePicture() != null) {

                    BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(imageView_contact, getResources(), true);
                    bitmapWorkerTask.execute(lossProtectedWalletContact.getProfilePicture());
                } else
                    Picasso.with(getActivity()).load(R.drawable.ic_profile_male).transform(new CircleTransform()).into(imageView_contact);

            } catch (Exception e) {
                Picasso.with(getActivity()).load(R.drawable.ic_profile_male).transform(new CircleTransform()).into(imageView_contact);
            }
            contactName.setText(lossProtectedWalletContact.getActorName());

        } else {
            Picasso.with(getActivity()).load(R.drawable.ic_profile_male).transform(new CircleTransform()).into(imageView_contact);
        }

    }

    private void setUpContactAddapter() {

         fermatWorker = new FermatWorker(getActivity()) {
            @Override
            protected Object doInBackground()  {
                try{

                    walletContactList = getWalletContactList();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return walletContactList;
            }
        };

        fermatWorker.setCallBack(new FermatWorkerCallBack() {
            @Override
            public void onPostExecute(Object... result) {
                if (result != null && result.length > 0) {


                    contactsAdapter = new WalletContactListAdapter(getActivity(), R.layout.loss_fragment_contacts_list_item,  (List<WalletContact>)result[0]);

                    contactName.setAdapter(contactsAdapter);
                    //autocompleteContacts.setTypeface(tf);
                    contactName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                            walletContact = (WalletContact) arg0.getItemAtPosition(position);

                            //add connection like a wallet contact
                            try {
                                if (walletContact.isConnection) {
                                    lossProtectedWalletContact = lossProtectedWalletManager.convertConnectionToContact(
                                            walletContact.name,
                                            Actors.INTRA_USER,
                                            walletContact.actorPublicKey,
                                            walletContact.profileImage,
                                            Actors.INTRA_USER,
                                            lossProtectedWalletManager.getSelectedActorIdentity().getPublicKey(),
                                            appSession.getAppPublicKey(),
                                            CryptoCurrency.BITCOIN,
                                            blockchainNetworkType);
                                } else {
                                    try {
                                        lossProtectedWalletContact = lossProtectedWalletManager.findWalletContactById(walletContact.contactId, lossProtectedWalletManager.getSelectedActorIdentity().getPublicKey());
                                    } catch (CantFindLossProtectedWalletContactException e) {
                                        e.printStackTrace();
                                    } catch (com.bitdubai.fermat_ccp_api.layer.middleware.wallet_contacts.exceptions.WalletContactNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }

                                walletContact.name = lossProtectedWalletContact.getActorName();
                                walletContact.actorPublicKey = lossProtectedWalletContact.getActorPublicKey();
                                if (lossProtectedWalletContact.getReceivedCryptoAddress().get(blockchainNetworkType) == null) {
                                    lossProtectedWalletManager.requestAddressToKnownUser(
                                            lossProtectedWalletManager.getSelectedActorIdentity().getPublicKey(),
                                            Actors.INTRA_USER,
                                            lossProtectedWalletContact.getActorPublicKey(),
                                            lossProtectedWalletContact.getActorType(),
                                            Platforms.CRYPTO_CURRENCY_PLATFORM,
                                            VaultType.CRYPTO_CURRENCY_VAULT,
                                            CryptoCurrencyVault.BITCOIN_VAULT.getCode(),
                                            appSession.getAppPublicKey(),
                                            ReferenceWallet.BASIC_WALLET_LOSS_PROTECTED_WALLET,
                                            blockchainNetworkType
                                    );

                                    Toast.makeText(getActivity(),  getResources().getString(R.string.Contact_without_net_address) + " " + blockchainNetworkType.getCode() + "\nplease wait 2 minutes.", Toast.LENGTH_LONG).show();

                                } else {

                                    walletContact.address = lossProtectedWalletContact.getReceivedCryptoAddress().get(blockchainNetworkType).getAddress();

                                    if (lossProtectedWalletContact != null) {
                                        walletContact.name = lossProtectedWalletContact.getActorName();
                                        walletContact.actorPublicKey = lossProtectedWalletContact.getActorPublicKey();

                                        lossProtectedWalletManager.requestAddressToKnownUser(
                                                lossProtectedWalletManager.getSelectedActorIdentity().getPublicKey(),
                                                Actors.INTRA_USER,
                                                lossProtectedWalletContact.getActorPublicKey(),
                                                lossProtectedWalletContact.getActorType(),
                                                Platforms.CRYPTO_CURRENCY_PLATFORM,
                                                VaultType.CRYPTO_CURRENCY_VAULT,
                                                CryptoCurrencyVault.BITCOIN_VAULT.getCode(),
                                                appSession.getAppPublicKey(),
                                                ReferenceWallet.BASIC_WALLET_LOSS_PROTECTED_WALLET,
                                                blockchainNetworkType
                                        );

                                        walletContact.contactId = lossProtectedWalletContact.getContactId();
                                        walletContact.profileImage = lossProtectedWalletContact.getProfilePicture();
                                        walletContact.isConnection = lossProtectedWalletContact.isConnection();
                                    }

                                    setUpUIData();

                                }
                            }

                            catch(CantCreateLossProtectedWalletContactException e)
                            {
                                appSession.getErrorManager().reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                                showMessage(getActivity(), "CantCreateWalletContactException- " + e.getMessage());

                            }
                            catch(ContactNameAlreadyExistsException e )
                            {
                                appSession.getErrorManager().reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                                showMessage(getActivity(), "ContactNameAlreadyExistsException- " + e.getMessage());
                            }
                            catch(CantRequestLossProtectedAddressException e)
                            {
                                e.printStackTrace();
                            }
                            catch (CantGetSelectedActorIdentityException e) {
                                e.printStackTrace();
                            } catch (ActorIdentityNotSelectedException e) {
                                e.printStackTrace();
                            }

                        }
                    });


                    contactName.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            Picasso.with(getActivity()).load(R.drawable.ic_profile_male).transform(new CircleTransform()).into(imageView_contact);
                        }
                    });
                }
            }

            @Override
            public void onErrorOccurred(Exception ex) {

                ErrorManager errorManager = appSession.getErrorManager();
                if (errorManager != null)
                    errorManager.reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI,
                            UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, ex);
                else
                    Log.e("getWalletContactList", ex.getMessage(), ex);

            }
        });

        fermatWorker.execute();
    }


    @Override
    public void onClick(View v) {
        try {
            int id = v.getId();

            if (id == R.id.scan_qr) {
                IntentIntegrator integrator = new IntentIntegrator(getActivity(), (EditText) rootView.findViewById(R.id.address));
                integrator.initiateScan();
            } else if (id == R.id.send_button) {
                InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getActivity().getCurrentFocus() != null && im.isActive(getActivity().getCurrentFocus())) {
                    im.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
                if (lossProtectedWalletContact != null) {
                    sendCrypto();
                } else
                    Toast.makeText(getActivity(), getResources().getString(R.string.Contact_not_found), Toast.LENGTH_LONG).show();
            } else if (id == R.id.imageView_contact) {
                // if user press the profile image
            } else if (id == R.id.btn_expand_send_form) {
                Object[] objects = new Object[1];
                objects[0] = walletContact;
                changeApp(Engine.BITCOIN_WALLET_CALL_INTRA_USER_COMMUNITY, objects);
            }
        }catch(Exception e) {
         e.printStackTrace();
        }
    }
    //TODO: Check if the setting for protecting the user from spending btc is working
    private void sendCrypto() {
        try {
            //first check if have exchange rate info
            if(appSession.getData(SessionConstant.ACTUAL_EXCHANGE_RATE)
                    != 0){
                if (lossProtectedWalletContact.getReceivedCryptoAddress().get(blockchainNetworkType) != null) {
                    CryptoAddress validAddress = WalletUtils.validateAddress(lossProtectedWalletContact.getReceivedCryptoAddress().get(blockchainNetworkType).getAddress(), lossProtectedWalletManager,blockchainNetworkType);
                    if (validAddress != null) {
                        EditText txtAmount = (EditText) rootView.findViewById(R.id.amount);
                        String amount = txtAmount.getText().toString();

                        EditText txtFee= (EditText) rootView.findViewById(R.id.feed_amount);
                        String fee = txtFee.getText().toString();

                        if(feed_Substract.isChecked())
                            feeOrigin = FeeOrigin.SUBSTRACT_FEE_FROM_AMOUNT.getCode();
                        else
                            feeOrigin = FeeOrigin.SUBSTRACT_FEE_FROM_FUNDS.getCode();

                        BigDecimal money;

                        if (amount.equals(""))
                            money = new BigDecimal("0");
                        else
                            money = new BigDecimal(amount);
                        if(!amount.equals("") && !money.equals(new BigDecimal("0"))) {
                            try {
                                String notes = "";
                                if (txt_notes.getText().toString().length() != 0) {
                                    notes = txt_notes.getText().toString();
                                }

                                String txtType = txt_type.getText().toString();
                                String newAmount = "";
                                String newFee = "";
                                String msg = "";

                                if (txtType.equals("[btc]")) {
                                    newAmount = bitcoinConverter.getSathoshisFromBTC(amount);
                                    newFee = bitcoinConverter.getSathoshisFromBTC(fee);
                                     msg       = bitcoinConverter.getBTC(String.valueOf(BitcoinNetworkConfiguration.MIN_ALLOWED_SATOSHIS_ON_SEND))+" BTC.";

                                   // newAmount = String.valueOf(Integer.valueOf(newAmount)); //without decimal .00000
                                } else if (txtType.equals("[satoshis]")) {
                                    newAmount = amount;
                                    newFee = fee;
                                    msg       = String.valueOf(BitcoinNetworkConfiguration.MIN_ALLOWED_SATOSHIS_ON_SEND)+" SATOSHIS.";
                                } else if (txtType.equals("[bits]")) {
                                    newAmount = bitcoinConverter.getSathoshisFromBits(amount);
                                    newFee = bitcoinConverter.getSathoshisFromBits(fee);
                                    msg       = bitcoinConverter.getBits(String.valueOf(BitcoinNetworkConfiguration.MIN_ALLOWED_SATOSHIS_ON_SEND))+" BITS.";
                                }

                                BigDecimal decimalFeed = new BigDecimal(newFee);
                                long minSatoshis = BitcoinNetworkConfiguration.MIN_ALLOWED_SATOSHIS_ON_SEND;
                                BigDecimal amountDecimal = new BigDecimal(newAmount);


                                BigDecimal operator = new BigDecimal(newAmount);

                                if (amountDecimal.longValueExact() > minSatoshis) {

                                    long availableBalance = lossProtectedWalletManager.getBalance(BalanceType.AVAILABLE, appSession.getAppPublicKey(), blockchainNetworkType, String.valueOf(appSession.getData(SessionConstant.ACTUAL_EXCHANGE_RATE)));

                                    if(amountDecimal.compareTo(new BigDecimal(availableBalance)) == 1) //Balance value is greater than send amount
                                    {
                                        if (!lossProtectedEnabled) {
                                            Confirm_send_dialog confirm_send_dialog = new Confirm_send_dialog(getActivity(),lossProtectedWalletManager,amountDecimal.longValueExact(),
                                                    validAddress,
                                                    notes,
                                                    appSession.getAppPublicKey(),
                                                    lossProtectedWalletManager.getActiveIdentities().get(0).getPublicKey(),
                                                    Actors.INTRA_USER,
                                                    lossProtectedWalletContact.getActorPublicKey(),
                                                    lossProtectedWalletContact.getActorType(),
                                                    ReferenceWallet.BASIC_WALLET_LOSS_PROTECTED_WALLET,
                                                    blockchainNetworkType,
                                                    appSession);
                                            confirm_send_dialog.show();

                                            confirm_send_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    Toast.makeText(getActivity(), getResources().getString(R.string.Sending_text), Toast.LENGTH_SHORT).show();
                                                    onBack(null);
                                                }
                                            });
                                        }
                                        else
                                        {
                                            //setting protected eneabled can't send
                                            Toast.makeText(getActivity(), getResources().getString(R.string.Action_not_allowed_lpw), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    else{
                                        try {

                                            //check amount + fee less than balance
                                            long total = 0;
                                            if(feeOrigin.equals(FeeOrigin.SUBSTRACT_FEE_FROM_FUNDS.getCode()))
                                                total =  operator.longValueExact() +  decimalFeed.longValueExact();
                                               // total = operator + decimalFeed;
                                            else{
                                                total =  operator.longValueExact() -  decimalFeed.longValueExact();
                                               // total = operator - decimalFeed;

                                            }


                                            if(total < Balance)
                                            {
                                                ConfirmSendDialog_feeCase sendConfirmDialog = new ConfirmSendDialog_feeCase(getActivity(),
                                                        lossProtectedWalletManager,
                                                      //  operator,
                                                     //   decimalFeed,
                                                        operator.longValueExact(),
                                                        decimalFeed.longValueExact(),
                                                        total,
                                                        FeeOrigin.getByCode(feeOrigin),
                                                        validAddress,
                                                        notes,
                                                        lossProtectedWalletContact.getActorPublicKey(),
                                                        lossProtectedWalletContact.getActorType(),
                                                        blockchainNetworkType,
                                                        appSession);

                                                sendConfirmDialog.show();
                                                sendConfirmDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {

                                                        onBack(null);
                                                    }
                                                });
                                            }
                                            else{
                                                Toast.makeText(getActivity(), getResources().getString(R.string.Insufficient_funds), Toast.LENGTH_LONG).show();
                                            }


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(getActivity(), getResources().getString(R.string.Send_error_text) + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }

                                } else {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.Invalid_Amount_small)+ " " + msg, Toast.LENGTH_LONG).show();
                                }


                            } catch (Exception e) {
                                appSession.getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.UNSTABLE, e);
                                Toast.makeText(getActivity(), getResources().getString(R.string.Whooops_text2), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();

                            }
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.Invalid_Amount), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.Contact_without_address), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.Contact_without_net_address)+" "+ blockchainNetworkType.getCode() + "\nplease wait 2 minutes", Toast.LENGTH_LONG).show();
                }

            }
            else
            {
                ErrorExchangeRateConnectionDialog errorExchangeRateConnectionDialog = new ErrorExchangeRateConnectionDialog(getActivity());
                errorExchangeRateConnectionDialog.show();
               // Toast.makeText(getActivity(), "Action not allowed.Could not retrieve the dollar exchange rate.\nCheck your internet connection.. ", Toast.LENGTH_LONG).show();

            }


        } catch (Exception e) {
            Toast.makeText(getActivity(), getResources().getString(R.string.Whooops_text2), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    /**
     * Obtain the wallet contacts from the cryptoWallet
     *
     * @return
     */
    private List<WalletContact> getWalletContactList() {
        List<WalletContact> contacts = new ArrayList<>();
        try {
            List<LossProtectedWalletContact> walletContactRecords = lossProtectedWalletManager.listAllActorContactsAndConnections(appSession.getAppPublicKey(), lossProtectedWalletManager.getSelectedActorIdentity().getPublicKey());
            for (LossProtectedWalletContact wcr : walletContactRecords) {

                String contactAddress = "";
                if (wcr.getReceivedCryptoAddress().get(blockchainNetworkType) != null)
                    contactAddress = wcr.getReceivedCryptoAddress().get(blockchainNetworkType).getAddress();
                contacts.add(new WalletContact(wcr.getContactId(), wcr.getActorPublicKey(), wcr.getActorName(), contactAddress, wcr.isConnection(), wcr.getProfilePicture()));
            }

        } catch (CantGetSelectedActorIdentityException e) {
            appSession.getErrorManager().reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            showMessage(getActivity(), "CantGetAllWalletContactsException- " + e.getMessage());;

            e.printStackTrace();
        } catch (ActorIdentityNotSelectedException e) {
            appSession.getErrorManager().reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            showMessage(getActivity(), "CantGetAllWalletContactsException- " + e.getMessage());;

            e.printStackTrace();
        } catch (CantGetAllLossProtectedWalletContactsException e) {
            e.printStackTrace();
        }
        return contacts;
    }


    @Override
    public void onDestroy() {
        contactsAdapter = null;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }



    @Override
    public void onBackPressed() {

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

       /* imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getView().getWindowToken(), 0);*/

    }

    @Override
    public void onStop() {

        if(fermatWorker != null)
            fermatWorker.shutdownNow();
        super.onStop();
    }
}