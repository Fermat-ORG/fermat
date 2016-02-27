/*
 * @#OutgoingMessageDataAccessObject.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_dap_plugin.layer.network.service.asset.transmission.developer.bitdubai.version_2.database.communications;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOperator;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilter;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_dap_api.layer.all_definition.network_service_message.DAPMessage;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.exceptions.RecordsNotFoundException;
import com.bitdubai.fermat_dap_plugin.layer.network.service.asset.transmission.developer.bitdubai.version_2.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_dap_plugin.layer.network.service.asset.transmission.developer.bitdubai.version_2.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_dap_plugin.layer.network.service.asset.transmission.developer.bitdubai.version_2.exceptions.CantUpdateRecordDataBaseException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.contents.FermatMessageCommunication;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.MessageStatus;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.contents.FermatMessage;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.FermatMessageContentType;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.FermatMessagesStatus;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The Class <code>com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.user.developer.bitdubai.version_1.database.communications.OutgoingMessageDao</code> is
 * throw when error occurred updating new record in a table of the data base
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 07/10/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class OutgoingMessageDao {

    /**
     * Represent the dataBase
     */
    private Database dataBase;
    private DAPMessageDAO dapMessageDAO;

    /**
     * Constructor with parameters
     *
     * @param dataBase
     */
    public OutgoingMessageDao(Database dataBase, DAPMessageDAO dapMessageDAO) {
        super();
        this.dataBase = dataBase;
        this.dapMessageDAO = dapMessageDAO;
    }

    /**
     * Return the Database
     *
     * @return Database
     */
    Database getDataBase() {
        return dataBase;
    }

    /**
     * Return the DatabaseTable
     *
     * @return DatabaseTable
     */
    DatabaseTable getDatabaseTable() {
        return getDataBase().getTable(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_TABLE_NAME);
    }

    /**
     * Method that find an FermatMessage by id in the data base.
     *
     * @param id Long id.
     * @return FermatMessage found.
     * @throws CantReadRecordDataBaseException
     */
    public FermatMessage findById(String id) throws CantReadRecordDataBaseException {

        if (id == null) {
            throw new IllegalArgumentException("The id is required, can not be null");
        }

        FermatMessage outgoingTemplateNetworkServiceMessage = null;

        try {

            /*
             * 1 - load the data base to memory with filter
             */
            DatabaseTable incomingMessageTable = getDatabaseTable();
            incomingMessageTable.addStringFilter(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_ID_COLUMN_NAME, id, DatabaseFilterType.EQUAL);
            incomingMessageTable.loadToMemory();

            /*
             * 2 - read all records
             */
            List<DatabaseTableRecord> records = incomingMessageTable.getRecords();


            /*
             * 3 - Convert into FermatMessage objects
             */
            for (DatabaseTableRecord record : records) {

                /*
                 * 3.1 - Create and configure a  FermatMessage
                 */
                outgoingTemplateNetworkServiceMessage = constructFrom(record);
            }

        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {
            // Register the failure.
            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Database Name: " + CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME);

            String context = contextBuffer.toString();
            String possibleCause = "The data no exist";
            CantReadRecordDataBaseException cantReadRecordDataBaseException = new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, cantLoadTableToMemory, context, possibleCause);
            throw cantReadRecordDataBaseException;
        }

        return outgoingTemplateNetworkServiceMessage;
    }

    ;

    /**
     * Method that list the all entities on the data base.
     *
     * @return All FermatMessage.
     * @throws CantReadRecordDataBaseException
     */
    public List<FermatMessage> findAll() throws CantReadRecordDataBaseException {


        List<FermatMessage> list = null;

        try {

            /*
             * 1 - load the data base to memory
             */
            DatabaseTable networkIntraUserTable = getDatabaseTable();
            networkIntraUserTable.loadToMemory();

            /*
             * 2 - read all records
             */
            List<DatabaseTableRecord> records = networkIntraUserTable.getRecords();

            /*
             * 3 - Create a list of FermatMessage objects
             */
            list = new ArrayList<>();
            list.clear();

            /*
             * 4 - Convert into FermatMessage objects
             */
            for (DatabaseTableRecord record : records) {

                /*
                 * 4.1 - Create and configure a  FermatMessage
                 */
                FermatMessage outgoingTemplateNetworkServiceMessage = constructFrom(record);

                /*
                 * 4.2 - Add to the list
                 */
                list.add(outgoingTemplateNetworkServiceMessage);

            }

        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {

            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Database Name: " + CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME);

            String context = contextBuffer.toString();
            String possibleCause = "The data no exist";
            CantReadRecordDataBaseException cantReadRecordDataBaseException = new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, cantLoadTableToMemory, context, possibleCause);
            throw cantReadRecordDataBaseException;
        }

        /*
         * return the list
         */
        return list;
    }

    ;


    /**
     * Method that list the all entities on the data base. The valid value of
     * the column name are the att of the <code>CommunicationNetworkServiceDatabaseConstants</code>
     *
     * @return All FermatMessage.
     * @throws CantReadRecordDataBaseException
     * @see com.bitdubai.fermat_dap_plugin.layer.network.service.asset.transmission.developer.bitdubai.version_1.database.communications.CommunicationNetworkServiceDatabaseConstants
     */
    public List<FermatMessage> findAll(String columnName, String columnValue) throws CantReadRecordDataBaseException {

        if (columnName == null ||
                columnName.isEmpty() ||
                columnValue == null ||
                columnValue.isEmpty()) {

            throw new IllegalArgumentException("The filter are required, can not be null or empty");
        }


        List<FermatMessage> list = null;

        try {

            /*
             * 1 - load the data base to memory with filters
             */
            DatabaseTable templateTable = getDatabaseTable();
            templateTable.addStringFilter(columnName, columnValue, DatabaseFilterType.EQUAL);
            templateTable.loadToMemory();

            /*
             * 2 - read all records
             */
            List<DatabaseTableRecord> records = templateTable.getRecords();

            /*
             * 3 - Create a list of FermatMessage objects
             */
            list = new ArrayList<>();
            list.clear();

            /*
             * 4 - Convert into FermatMessage objects
             */
            for (DatabaseTableRecord record : records) {

                /*
                 * 4.1 - Create and configure a  FermatMessage
                 */
                FermatMessage outGoingTemplateNetworkServiceMessage = constructFrom(record);

                /*
                 * 4.2 - Add to the list
                 */
                list.add(outGoingTemplateNetworkServiceMessage);

            }

        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {

            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Database Name: " + CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME);

            String context = contextBuffer.toString();
            String possibleCause = "The data no exist";
            CantReadRecordDataBaseException cantReadRecordDataBaseException = new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, cantLoadTableToMemory, context, possibleCause);
            throw cantReadRecordDataBaseException;
        }

        /*
         * return the list
         */
        return list;
    }

    ;


    /**
     * Method that list the all entities on the data base. The valid value of
     * the key are the att of the <code>CommunicationNetworkServiceDatabaseConstants</code>
     *
     * @param filters
     * @return List<FermatMessage>
     * @throws CantReadRecordDataBaseException
     */
    public List<FermatMessage> findAll(Map<String, Object> filters) throws CantReadRecordDataBaseException {

        if (filters == null ||
                filters.isEmpty()) {

            throw new IllegalArgumentException("The filters are required, can not be null or empty");
        }

        List<FermatMessage> list = null;
        List<DatabaseTableFilter> filtersTable = new ArrayList<>();

        try {


            /*
             * 1- Prepare the filters
             */
            DatabaseTable templateTable = getDatabaseTable();

            for (String key : filters.keySet()) {

                DatabaseTableFilter newFilter = templateTable.getEmptyTableFilter();
                newFilter.setType(DatabaseFilterType.EQUAL);
                newFilter.setColumn(key);
                newFilter.setValue((String) filters.get(key));

                filtersTable.add(newFilter);
            }


            /*
             * 2 - load the data base to memory with filters
             */
            templateTable.setFilterGroup(filtersTable, null, DatabaseFilterOperator.OR);
            templateTable.loadToMemory();

            /*
             * 3 - read all records
             */
            List<DatabaseTableRecord> records = templateTable.getRecords();

            /*
             * 4 - Create a list of FermatMessage objects
             */
            list = new ArrayList<>();
            list.clear();

            /*
             * 5 - Convert into FermatMessage objects
             */
            for (DatabaseTableRecord record : records) {

                /*
                 * 5.1 - Create and configure a  FermatMessage
                 */
                FermatMessage outgoingTemplateNetworkServiceMessage = constructFrom(record);

                /*
                 * 5.2 - Add to the list
                 */
                list.add(outgoingTemplateNetworkServiceMessage);

            }

        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {

            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Database Name: " + CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME);

            String context = contextBuffer.toString();
            String possibleCause = "The data no exist";
            CantReadRecordDataBaseException cantReadRecordDataBaseException = new CantReadRecordDataBaseException(CantReadRecordDataBaseException.DEFAULT_MESSAGE, cantLoadTableToMemory, context, possibleCause);
            throw cantReadRecordDataBaseException;
        }

        /*
         * return the list
         */
        return list;
    }

    ;

    /**
     * Method that create a new entity in the data base.
     *
     * @param entity FermatMessage to create.
     * @throws CantInsertRecordDataBaseException
     */
    public void create(FermatMessage entity, String idDAPMessage) throws CantInsertRecordDataBaseException {
        try {
            if (findById(entity.getId().toString()) != null) {
                return;
            }
            /*
             * 1- Create the record to the entity
             */
            DatabaseTable table = getDatabaseTable();
            DatabaseTableRecord entityRecord = table.getEmptyRecord();
            setValuesToRecord(entityRecord, entity, idDAPMessage);
            table.insertRecord(entityRecord);
        } catch (CantInsertRecordException | CantReadRecordDataBaseException databaseTransactionFailedException) {
            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Database Name: " + CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME);

            String context = contextBuffer.toString();
            String possibleCause = "The Template Database triggered an unexpected problem that wasn't able to solve by itself";
            CantInsertRecordDataBaseException cantInsertRecordDataBaseException = new CantInsertRecordDataBaseException(CantInsertRecordDataBaseException.DEFAULT_MESSAGE, databaseTransactionFailedException, context, possibleCause);
            throw cantInsertRecordDataBaseException;

        }

    }

    /**
     * Method that update an entity in the data base.
     *
     * @param entity FermatMessage to update.
     * @throws CantUpdateRecordDataBaseException
     */
    public void update(FermatMessage entity) throws CantUpdateRecordDataBaseException {
        try {

            DatabaseTable outgoingMessageTable = getDatabaseTable();
            outgoingMessageTable.addStringFilter(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_ID_COLUMN_NAME, entity.getId().toString(), DatabaseFilterType.EQUAL);
            outgoingMessageTable.loadToMemory();

            if (outgoingMessageTable.getRecords().isEmpty()) throw new RecordsNotFoundException();
            DatabaseTableRecord record = outgoingMessageTable.getRecords().get(0);
            setValuesToRecord(record, entity, record.getStringValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_ID_DAP_MESSAGE_COLUMN_NAME));
            outgoingMessageTable.updateRecord(record);
        } catch (RecordsNotFoundException | CantUpdateRecordException | CantLoadTableToMemoryException databaseTransactionFailedException) {
            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Database Name: " + CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME);
            String context = contextBuffer.toString();
            String possibleCause = "The record do not exist";
            CantUpdateRecordDataBaseException cantUpdateRecordDataBaseException = new CantUpdateRecordDataBaseException(CantUpdateRecordDataBaseException.DEFAULT_MESSAGE, databaseTransactionFailedException, context, possibleCause);
            throw cantUpdateRecordDataBaseException;
        }
    }

    /**
     * @param record with values from the table
     * @return FermatMessage setters the values from table
     */
    private FermatMessage constructFrom(DatabaseTableRecord record) {

        FermatMessageCommunication outgoingTemplateNetworkServiceMessage = new FermatMessageCommunication();

        try {

            outgoingTemplateNetworkServiceMessage.setId(UUID.fromString(record.getStringValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_ID_COLUMN_NAME)));
            outgoingTemplateNetworkServiceMessage.setSender(record.getStringValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_SENDER_ID_COLUMN_NAME));
            outgoingTemplateNetworkServiceMessage.setReceiver(record.getStringValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_RECEIVER_ID_COLUMN_NAME));
            outgoingTemplateNetworkServiceMessage.setContent(record.getStringValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_TEXT_CONTENT_COLUMN_NAME));
            outgoingTemplateNetworkServiceMessage.setFermatMessageContentType(FermatMessageContentType.getByCode(record.getStringValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_TYPE_COLUMN_NAME)));
            outgoingTemplateNetworkServiceMessage.setShippingTimestamp(new Timestamp(record.getLongValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_SHIPPING_TIMESTAMP_COLUMN_NAME)));
            outgoingTemplateNetworkServiceMessage.setDeliveryTimestamp(new Timestamp(record.getLongValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_DELIVERY_TIMESTAMP_COLUMN_NAME)));
            outgoingTemplateNetworkServiceMessage.setFermatMessagesStatus(FermatMessagesStatus.getByCode(record.getStringValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_STATUS_COLUMN_NAME)));
            DAPMessage message = dapMessageDAO.findById(record.getStringValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_ID_DAP_MESSAGE_COLUMN_NAME));
            outgoingTemplateNetworkServiceMessage.setContent(message.toXML());

        } catch (InvalidParameterException | CantReadRecordDataBaseException e) {
            //TODO METODO CON RETURN NULL - OJO: solo INFORMATIVO de ayuda VISUAL para DEBUG - Eliminar si molesta
            //this should not happen, but if it happens return null
            e.printStackTrace();
            return null;
        }

        return outgoingTemplateNetworkServiceMessage;
    }

    /**
     * Construct a DatabaseTableRecord whit the values of the a FermatMessage pass
     * by parameter
     *
     * @param outGoingTemplateNetworkServiceMessage the contains the values
     * @return DatabaseTableRecord whit the values
     */
    private DatabaseTableRecord constructFrom(FermatMessage outGoingTemplateNetworkServiceMessage, String idDAPMessage) {

        /*
         * Create the record to the entity
         */
        DatabaseTableRecord entityRecord = getDatabaseTable().getEmptyRecord();
        setValuesToRecord(entityRecord, outGoingTemplateNetworkServiceMessage, idDAPMessage);
        return entityRecord;
    }

    private void setValuesToRecord(DatabaseTableRecord entityRecord, FermatMessage outGoingTemplateNetworkServiceMessage, String idDAPMessage) {
        entityRecord.setStringValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_ID_COLUMN_NAME, outGoingTemplateNetworkServiceMessage.getId().toString());
        entityRecord.setStringValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_SENDER_ID_COLUMN_NAME, outGoingTemplateNetworkServiceMessage.getSender());
        entityRecord.setStringValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_RECEIVER_ID_COLUMN_NAME, outGoingTemplateNetworkServiceMessage.getReceiver());
        entityRecord.setStringValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_STATUS_COLUMN_NAME, MessageStatus.PENDING_TO_SEND.getCode());
        entityRecord.setStringValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_TYPE_COLUMN_NAME, outGoingTemplateNetworkServiceMessage.getFermatMessageContentType().getCode());

        if (outGoingTemplateNetworkServiceMessage.getShippingTimestamp() != null) {
            entityRecord.setLongValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_SHIPPING_TIMESTAMP_COLUMN_NAME, outGoingTemplateNetworkServiceMessage.getShippingTimestamp().getTime());
        } else {
            entityRecord.setLongValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_SHIPPING_TIMESTAMP_COLUMN_NAME, (long) 0);
        }

        if (outGoingTemplateNetworkServiceMessage.getDeliveryTimestamp() != null) {
            entityRecord.setLongValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_DELIVERY_TIMESTAMP_COLUMN_NAME, outGoingTemplateNetworkServiceMessage.getDeliveryTimestamp().getTime());
        } else {
            entityRecord.setLongValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_DELIVERY_TIMESTAMP_COLUMN_NAME, (long) 0);
        }

        entityRecord.setStringValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_STATUS_COLUMN_NAME, outGoingTemplateNetworkServiceMessage.getFermatMessagesStatus().getCode());
        entityRecord.setStringValue(CommunicationNetworkServiceDatabaseConstants.OUTGOING_MESSAGE_ID_DAP_MESSAGE_COLUMN_NAME, idDAPMessage);

    }

}