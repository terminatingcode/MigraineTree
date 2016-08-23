package com.terminatingcode.android.migrainetree.amazonaws.nosql;

import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.terminatingcode.android.migrainetree.MigraineRecordObject;
import com.terminatingcode.android.migrainetree.amazonaws.AWSMobileClient;

import java.util.Iterator;


public class NoSQLTableMigraineRecord extends NoSQLTableBase {
    private static final String LOG_TAG = NoSQLTableMigraineRecord.class.getSimpleName();
    private static final String RECORD_ID = "RecordId";

    /** Inner classes use this value to determine how many results to retrieve per service call. */
    private static final int RESULTS_PER_RESULT_GROUP = 40;

    /** Removing sample data removes the items in batches of the following size. */
    private static final int MAX_BATCH_SIZE_FOR_DELETE = 50;

    /** The DynamoDB object mapper for accessing DynamoDB. */
    private final DynamoDBMapper mapper;

    public NoSQLTableMigraineRecord() {
        mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
    }

    @Override
    public String getTableName() {
        return "MigraineRecord";
    }

    @Override
    public String getPartitionKeyName() {
        return "Artist";
    }

    public String getPartitionKeyType() {
        return "String";
    }

    @Override
    public String getSortKeyName() {
        return "RecordId";
    }

    public String getSortKeyType() {
        return "Number";
    }

    @Override
    public int getNumIndexes() {
        return 2;
    }

    @Override
    public void insertRecord(MigraineRecordObject migraineRecordObject)throws AmazonClientException{
        Log.d(LOG_TAG, "inserting record to DynamoDb MigraineRecord table");
        Log.d(LOG_TAG, "weather AP = " + migraineRecordObject.getCurrentAP());
        final MigraineRecordDO firstItem = new MigraineRecordDO();

        firstItem.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
        firstItem.setRecordId((double)migraineRecordObject.getStartHour());
        firstItem.setAP12Hours(migraineRecordObject.getAP12Hours());
        firstItem.setAP24Hours(migraineRecordObject.getAP24Hours());
        firstItem.setAP3Hours(migraineRecordObject.getAP3Hours());
        firstItem.setAura(migraineRecordObject.isAura());
        firstItem.setCity(migraineRecordObject.getCity());
        firstItem.setConfusion(migraineRecordObject.isConfusion());
        firstItem.setCongestion(migraineRecordObject.isCongestion());
        firstItem.setCurrentAP(migraineRecordObject.getCurrentAP());
        firstItem.setCurrentHum(migraineRecordObject.getCurrentHum());
        firstItem.setCurrentTemp(migraineRecordObject.getCurrentTemp());
        firstItem.setEars(migraineRecordObject.isEars());
        firstItem.setEaten(migraineRecordObject.isEaten());
        firstItem.setEndHour((double)migraineRecordObject.getEndHour());
        firstItem.setEyeStrain((double)migraineRecordObject.getEyeStrain());
        firstItem.setHum12Hours(migraineRecordObject.getHum12Hours());
        firstItem.setHum24Hours(migraineRecordObject.getHum24Hours());
        firstItem.setHum3Hours(migraineRecordObject.getHum3Hours());
        firstItem.setMedication(migraineRecordObject.getMedication());
        firstItem.setMenstrualDay((double)migraineRecordObject.getMenstrualDay());
        firstItem.setNausea(migraineRecordObject.isNausea());
        firstItem.setPainAtOnset((double)migraineRecordObject.getPainAtOnset());
        firstItem.setPainAtPeak((double)migraineRecordObject.getPainAtPeak());
        firstItem.setPainSource(migraineRecordObject.getPainSource());
        firstItem.setPainType(migraineRecordObject.getPainType());
        firstItem.setSensitivityToLight(migraineRecordObject.isSensitivityToLight());
        firstItem.setSensitivityToNoise(migraineRecordObject.isSensitivityToNoise());
        firstItem.setSensitivityToSmell(migraineRecordObject.isSensitivityToSmell());
        firstItem.setSleep((double)migraineRecordObject.getSleep());
        firstItem.setStartHour((double)migraineRecordObject.getStartHour());
        firstItem.setStress((double)migraineRecordObject.getStress());
        firstItem.setTemp12Hours(migraineRecordObject.getTemp12Hours());
        firstItem.setTemp24Hours(migraineRecordObject.getTemp24Hours());
        firstItem.setTemp3Hours(migraineRecordObject.getTemp3Hours());
        firstItem.setWater(migraineRecordObject.isWater());
        AmazonClientException lastException = null;

        try {
            mapper.save(firstItem);
        } catch (final AmazonClientException ex) {
            Log.e(LOG_TAG, "Failed saving item : " + ex.getMessage(), ex);
            lastException = ex;
        }
        if (lastException != null) {
            // Re-throw the last exception encountered to alert the user.
            throw lastException;
        }
    }

    /**
     * Deletes a record selected by the user
     * created by Sarah c
     * @param startHour the id for migraine record
     */
    @Override
    public void deleteRecord(long startHour){
        final MigraineRecordDO itemToFind = new MigraineRecordDO();
        itemToFind.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
        itemToFind.setRecordId((double) startHour);

        String hour = String.valueOf(startHour);
        Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withN(hour));

        final DynamoDBQueryExpression<MigraineRecordDO> queryExpression = new DynamoDBQueryExpression<MigraineRecordDO>()
                .withHashKeyValues(itemToFind)
                .withRangeKeyCondition(RECORD_ID, rangeKeyCondition)
                .withConsistentRead(false)
                .withLimit(MAX_BATCH_SIZE_FOR_DELETE);

        final PaginatedQueryList<MigraineRecordDO> results = mapper.query(MigraineRecordDO.class, queryExpression);
        Log.d(LOG_TAG, "results: " + results.size());

        Iterator<MigraineRecordDO> resultsIterator = results.iterator();

        AmazonClientException lastException = null;

        if (resultsIterator.hasNext()) {
            final MigraineRecordDO item = resultsIterator.next();

            try {
                mapper.delete(item);
            } catch (final AmazonClientException ex) {
                Log.e(LOG_TAG, "Failed deleting item : " + ex.getMessage(), ex);
                lastException = ex;
            }
        }
    }
}
