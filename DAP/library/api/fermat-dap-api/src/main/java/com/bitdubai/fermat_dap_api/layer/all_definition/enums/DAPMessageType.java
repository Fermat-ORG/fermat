package com.bitdubai.fermat_dap_api.layer.all_definition.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 25/11/15.
 */
public enum DAPMessageType implements FermatEnum {

    //ENUM DECLARATION

    ASSET_APPROPRIATION("ASAP");

    //VARIABLE DECLARATION

    private String code;

    //CONSTRUCTORS

    DAPMessageType(String code) {
        this.code = code;
    }

    //PUBLIC METHODS

    public static DAPMessageType getByCode(String code) throws InvalidParameterException {
        for (DAPMessageType fenum : values()) {
            if (fenum.getCode().equals(code)) return fenum;
        }
        throw new InvalidParameterException(InvalidParameterException.DEFAULT_MESSAGE, null, "Code Received: " + code, "This Code Is Not Valid for the DAPMessageType enum.");
    }

    //PRIVATE METHODS

    //GETTER AND SETTERS

    @Override
    public String getCode() {
        return code;
    }
}
