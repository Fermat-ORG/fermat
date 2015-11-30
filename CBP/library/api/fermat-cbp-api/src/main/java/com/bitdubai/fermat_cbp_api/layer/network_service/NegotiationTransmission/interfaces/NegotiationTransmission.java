package com.bitdubai.fermat_cbp_api.layer.network_service.NegotiationTransmission.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationStatus;
import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationTransactionType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationTransmissionState;

import java.util.UUID;

/**
 * Created by Yordin Alayn on 29.11.15.
 */
public interface NegotiationTransmission {

    UUID getTransactionId();

    UUID getNegotiationId();

    NegotiationTransactionType getNegotiationTansactionType();

    String getPublicKeyActorSend();

    String getActorSendType();

    String getPublicKeyActorReceive();

    PlatformComponentType getActorReceiveType();

    NegotiationTransmissionState getTransmissionState();

    long getTimestamp();

}
