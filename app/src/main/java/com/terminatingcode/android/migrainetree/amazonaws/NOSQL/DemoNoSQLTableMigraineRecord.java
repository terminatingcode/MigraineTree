package com.terminatingcode.android.migrainetree.amazonaws.nosql;

import android.content.Context;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.terminatingcode.android.migrainetree.R;
import com.terminatingcode.android.migrainetree.amazonaws.AWSMobileClient;
import com.terminatingcode.android.migrainetree.amazonaws.util.ThreadUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class DemoNoSQLTableMigraineRecord extends DemoNoSQLTableBase {
    private static final String LOG_TAG = DemoNoSQLTableMigraineRecord.class.getSimpleName();

    /** Inner classes use this value to determine how many results to retrieve per service call. */
    private static final int RESULTS_PER_RESULT_GROUP = 40;

    /** Removing sample data removes the items in batches of the following size. */
    private static final int MAX_BATCH_SIZE_FOR_DELETE = 50;


    /********* Primary Get Query Inner Classes *********/

    public class DemoGetWithPartitionKeyAndSortKey extends DemoNoSQLOperationBase {
        private MigraineRecordDO result;
        private boolean resultRetrieved = true;

        DemoGetWithPartitionKeyAndSortKey(final Context context) {
            super(context.getString(R.string.nosql_operation_get_by_partition_and_sort_text),
                String.format(context.getString(R.string.nosql_operation_example_get_by_partition_and_sort_text),
                    "userId", AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID(),
                    "RecordId", "1111500000"));
        }

        @Override
        public boolean executeOperation() {
            // Retrieve an item by passing the partition key using the object mapper.
            result = mapper.load(MigraineRecordDO.class, AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID(), 1111500000.0);

            if (result != null) {
                resultRetrieved = false;
                return true;
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            if (resultRetrieved) {
                return null;
            }
            final List<DemoNoSQLResult> results = new ArrayList<>();
            results.add(new DemoNoSQLMigraineRecordResult(result));
            resultRetrieved = true;
            return results;
        }

        @Override
        public void resetResults() {
            resultRetrieved = false;
        }
    }

    /* ******** Primary Index Query Inner Classes ******** */

    public class DemoQueryWithPartitionKeyAndSortKeyCondition extends DemoNoSQLOperationBase {

        private PaginatedQueryList<MigraineRecordDO> results;
        private Iterator<MigraineRecordDO> resultsIterator;

        DemoQueryWithPartitionKeyAndSortKeyCondition(final Context context) {
            super(context.getString(R.string.nosql_operation_title_query_by_partition_and_sort_condition_text),
                  String.format(context.getString(R.string.nosql_operation_example_query_by_partition_and_sort_condition_text),
                      "userId", AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID(),
                      "RecordId", "1111500000"));
        }

        @Override
        public boolean executeOperation() {
            final MigraineRecordDO itemToFind = new MigraineRecordDO();
            itemToFind.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());

            final Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.LT.toString())
                .withAttributeValueList(new AttributeValue().withN(Double.toString(1111500000.0)));
            final DynamoDBQueryExpression<MigraineRecordDO> queryExpression = new DynamoDBQueryExpression<MigraineRecordDO>()
                .withHashKeyValues(itemToFind)
                .withRangeKeyCondition("RecordId", rangeKeyCondition)
                .withConsistentRead(false)
                .withLimit(RESULTS_PER_RESULT_GROUP);

            results = mapper.query(MigraineRecordDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Gets the next page of results from the query.
         * @return list of results, or null if there are no more results.
         */
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoQueryWithPartitionKeyOnly extends DemoNoSQLOperationBase {

        private PaginatedQueryList<MigraineRecordDO> results;
        private Iterator<MigraineRecordDO> resultsIterator;

        DemoQueryWithPartitionKeyOnly(final Context context) {
            super(context.getString(R.string.nosql_operation_title_query_by_partition_text),
                String.format(context.getString(R.string.nosql_operation_example_query_by_partition_text),
                    "userId", AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID()));
        }

        @Override
        public boolean executeOperation() {
            final MigraineRecordDO itemToFind = new MigraineRecordDO();
            itemToFind.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());

            final DynamoDBQueryExpression<MigraineRecordDO> queryExpression = new DynamoDBQueryExpression<MigraineRecordDO>()
                .withHashKeyValues(itemToFind)
                .withConsistentRead(false)
                .withLimit(RESULTS_PER_RESULT_GROUP);

            results = mapper.query(MigraineRecordDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoQueryWithPartitionKeyAndFilter extends DemoNoSQLOperationBase {

        private PaginatedQueryList<MigraineRecordDO> results;
        private Iterator<MigraineRecordDO> resultsIterator;

        DemoQueryWithPartitionKeyAndFilter(final Context context) {
            super(context.getString(R.string.nosql_operation_title_query_by_partition_and_filter_text),
                  String.format(context.getString(R.string.nosql_operation_example_query_by_partition_and_filter_text),
                      "userId", AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID(),
                      "AP12Hours", "1111500000"));
        }

        @Override
        public boolean executeOperation() {
            final MigraineRecordDO itemToFind = new MigraineRecordDO();
            itemToFind.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());

            // Use an expression names Map to avoid the potential for attribute names
            // colliding with DynamoDB reserved words.
            final Map <String, String> filterExpressionAttributeNames = new HashMap<>();
            filterExpressionAttributeNames.put("#AP12Hours", "AP12Hours");

            final Map<String, AttributeValue> filterExpressionAttributeValues = new HashMap<>();
            filterExpressionAttributeValues.put(":MinAP12Hours",
                new AttributeValue().withN("1111500000"));

            final DynamoDBQueryExpression<MigraineRecordDO> queryExpression = new DynamoDBQueryExpression<MigraineRecordDO>()
                .withHashKeyValues(itemToFind)
                .withFilterExpression("#AP12Hours > :MinAP12Hours")
                .withExpressionAttributeNames(filterExpressionAttributeNames)
                .withExpressionAttributeValues(filterExpressionAttributeValues)
                .withConsistentRead(false)
                .withLimit(RESULTS_PER_RESULT_GROUP);

            results = mapper.query(MigraineRecordDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
             resultsIterator = results.iterator();
         }
    }

    public class DemoQueryWithPartitionKeySortKeyConditionAndFilter extends DemoNoSQLOperationBase {

        private PaginatedQueryList<MigraineRecordDO> results;
        private Iterator<MigraineRecordDO> resultsIterator;

        DemoQueryWithPartitionKeySortKeyConditionAndFilter(final Context context) {
            super(context.getString(R.string.nosql_operation_title_query_by_partition_sort_condition_and_filter_text),
                  String.format(context.getString(R.string.nosql_operation_example_query_by_partition_sort_condition_and_filter_text),
                      "userId", AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID(),
                      "RecordId", "1111500000",
                      "AP12Hours", "1111500000"));
        }

        public boolean executeOperation() {
            final MigraineRecordDO itemToFind = new MigraineRecordDO();
            itemToFind.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());

            final Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.LT.toString())
                .withAttributeValueList(new AttributeValue().withN(Double.toString(1111500000.0)));

            // Use an expression names Map to avoid the potential for attribute names
            // colliding with DynamoDB reserved words.
            final Map <String, String> filterExpressionAttributeNames = new HashMap<>();
            filterExpressionAttributeNames.put("#AP12Hours", "AP12Hours");

            final Map<String, AttributeValue> filterExpressionAttributeValues = new HashMap<>();
            filterExpressionAttributeValues.put(":MinAP12Hours",
                new AttributeValue().withN("1111500000"));

            final DynamoDBQueryExpression<MigraineRecordDO> queryExpression = new DynamoDBQueryExpression<MigraineRecordDO>()
                .withHashKeyValues(itemToFind)
                .withRangeKeyCondition("RecordId", rangeKeyCondition)
                .withFilterExpression("#AP12Hours > :MinAP12Hours")
                .withExpressionAttributeNames(filterExpressionAttributeNames)
                .withExpressionAttributeValues(filterExpressionAttributeValues)
                .withConsistentRead(false)
                .withLimit(RESULTS_PER_RESULT_GROUP);

            results = mapper.query(MigraineRecordDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    /* ******** Secondary Named Index Query Inner Classes ******** */

    public class DemoUserIdStartHourQueryWithPartitionKeyAndSortKeyCondition extends DemoNoSQLOperationBase {

        private PaginatedQueryList<MigraineRecordDO> results;
        private Iterator<MigraineRecordDO> resultsIterator;
        DemoUserIdStartHourQueryWithPartitionKeyAndSortKeyCondition (final Context context) {
            super(
                context.getString(R.string.nosql_operation_title_index_query_by_partition_and_sort_condition_text),
                context.getString(R.string.nosql_operation_example_index_query_by_partition_and_sort_condition_text,
                    "userId", AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID(),
                    "StartHour", "1111500000"));
        }

        public boolean executeOperation() {
            // Perform a query using a partition key and sort key condition.
            final MigraineRecordDO itemToFind = new MigraineRecordDO();
            itemToFind.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
            final Condition sortKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.LT.toString())

                .withAttributeValueList(new AttributeValue().withN(Double.toString(1111500000.0)));
            // Perform get using Partition key and sort key condition
            DynamoDBQueryExpression<MigraineRecordDO> queryExpression = new DynamoDBQueryExpression<MigraineRecordDO>()
                .withHashKeyValues(itemToFind)
                .withRangeKeyCondition("StartHour", sortKeyCondition)
                .withConsistentRead(false);
            results = mapper.query(MigraineRecordDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoUserIdStartHourQueryWithPartitionKeyOnly extends DemoNoSQLOperationBase {

        private PaginatedQueryList<MigraineRecordDO> results;
        private Iterator<MigraineRecordDO> resultsIterator;

        DemoUserIdStartHourQueryWithPartitionKeyOnly(final Context context) {
            super(
                context.getString(R.string.nosql_operation_title_index_query_by_partition_text),
                context.getString(R.string.nosql_operation_example_index_query_by_partition_text,
                    "userId", AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID()));
        }

        public boolean executeOperation() {
            // Perform a query using a partition key and filter condition.
            final MigraineRecordDO itemToFind = new MigraineRecordDO();
            itemToFind.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());

            // Perform get using Partition key
            DynamoDBQueryExpression<MigraineRecordDO> queryExpression = new DynamoDBQueryExpression<MigraineRecordDO>()
                .withHashKeyValues(itemToFind)
                .withConsistentRead(false);
            results = mapper.query(MigraineRecordDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoUserIdStartHourQueryWithPartitionKeyAndFilterCondition extends DemoNoSQLOperationBase {

        private PaginatedQueryList<MigraineRecordDO> results;
        private Iterator<MigraineRecordDO> resultsIterator;

        DemoUserIdStartHourQueryWithPartitionKeyAndFilterCondition (final Context context) {
            super(
                context.getString(R.string.nosql_operation_title_index_query_by_partition_and_filter_text),
                context.getString(R.string.nosql_operation_example_index_query_by_partition_and_filter_text,
                    "userId",AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID(),
                    "RecordId", "1111500000"));
        }

        public boolean executeOperation() {
            // Perform a query using a partition key and filter condition.
            final MigraineRecordDO itemToFind = new MigraineRecordDO();
            itemToFind.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());

            // Use an expression names Map to avoid the potential for attribute names
            // colliding with DynamoDB reserved words.
            final Map <String, String> filterExpressionAttributeNames = new HashMap<>();
            filterExpressionAttributeNames.put("#RecordId", "RecordId");

            final Map<String, AttributeValue> filterExpressionAttributeValues = new HashMap<>();
            filterExpressionAttributeValues.put(":MinRecordId",
                new AttributeValue().withN("1111500000"));

            // Perform get using Partition key and sort key condition
            DynamoDBQueryExpression<MigraineRecordDO> queryExpression = new DynamoDBQueryExpression<MigraineRecordDO>()
                .withHashKeyValues(itemToFind)
                .withFilterExpression("#RecordId > :MinRecordId")
                .withExpressionAttributeNames(filterExpressionAttributeNames)
                .withExpressionAttributeValues(filterExpressionAttributeValues)
                .withConsistentRead(false);
            results = mapper.query(MigraineRecordDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoUserIdStartHourQueryWithPartitionKeySortKeyAndFilterCondition extends DemoNoSQLOperationBase {

        private PaginatedQueryList<MigraineRecordDO> results;
        private Iterator<MigraineRecordDO> resultsIterator;

        DemoUserIdStartHourQueryWithPartitionKeySortKeyAndFilterCondition (final Context context) {
            super(
                context.getString(R.string.nosql_operation_title_index_query_by_partition_sort_condition_and_filter_text),
                context.getString(R.string.nosql_operation_example_index_query_by_partition_sort_condition_and_filter_text,
                    "userId", AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID(),
                    "StartHour", "1111500000",
                    "RecordId", "1111500000"));
        }

        public boolean executeOperation() {
            // Perform a query using a partition key, sort condition, and filter.
            final MigraineRecordDO itemToFind = new MigraineRecordDO();
            itemToFind.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
            final Condition sortKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.LT.toString())
                .withAttributeValueList(new AttributeValue().withN(Double.toString(1111500000.0)));

            // Use a map of expression names to avoid the potential for attribute names
            // colliding with DynamoDB reserved words.
            final Map<String, String> filterExpressionAttributeNames = new HashMap<>();
            filterExpressionAttributeNames.put("#RecordId", "RecordId");

            final Map<String, AttributeValue> filterExpressionAttributeValues = new HashMap<>();
            filterExpressionAttributeValues.put(":MinRecordId",
                new AttributeValue().withN("1111500000"));

            // Perform get using Partition key and sort key condition
            DynamoDBQueryExpression<MigraineRecordDO> queryExpression = new DynamoDBQueryExpression<MigraineRecordDO>()
                .withHashKeyValues(itemToFind)
                .withRangeKeyCondition("StartHour", sortKeyCondition)
                .withFilterExpression("#RecordId > :MinRecordId")
                .withExpressionAttributeNames(filterExpressionAttributeNames)
                .withExpressionAttributeValues(filterExpressionAttributeValues)
                .withConsistentRead(false);
            results = mapper.query(MigraineRecordDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoUserIdPainAtPeakQueryWithPartitionKeyAndSortKeyCondition extends DemoNoSQLOperationBase {

        private PaginatedQueryList<MigraineRecordDO> results;
        private Iterator<MigraineRecordDO> resultsIterator;
        DemoUserIdPainAtPeakQueryWithPartitionKeyAndSortKeyCondition (final Context context) {
            super(
                context.getString(R.string.nosql_operation_title_index_query_by_partition_and_sort_condition_text),
                context.getString(R.string.nosql_operation_example_index_query_by_partition_and_sort_condition_text,
                    "userId", AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID(),
                    "PainAtPeak", "1111500000"));
        }

        public boolean executeOperation() {
            // Perform a query using a partition key and sort key condition.
            final MigraineRecordDO itemToFind = new MigraineRecordDO();
            itemToFind.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
            final Condition sortKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.LT.toString())

                .withAttributeValueList(new AttributeValue().withN(Double.toString(1111500000.0)));
            // Perform get using Partition key and sort key condition
            DynamoDBQueryExpression<MigraineRecordDO> queryExpression = new DynamoDBQueryExpression<MigraineRecordDO>()
                .withHashKeyValues(itemToFind)
                .withRangeKeyCondition("PainAtPeak", sortKeyCondition)
                .withConsistentRead(false);
            results = mapper.query(MigraineRecordDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoUserIdPainAtPeakQueryWithPartitionKeyOnly extends DemoNoSQLOperationBase {

        private PaginatedQueryList<MigraineRecordDO> results;
        private Iterator<MigraineRecordDO> resultsIterator;

        DemoUserIdPainAtPeakQueryWithPartitionKeyOnly(final Context context) {
            super(
                context.getString(R.string.nosql_operation_title_index_query_by_partition_text),
                context.getString(R.string.nosql_operation_example_index_query_by_partition_text,
                    "userId", AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID()));
        }

        public boolean executeOperation() {
            // Perform a query using a partition key and filter condition.
            final MigraineRecordDO itemToFind = new MigraineRecordDO();
            itemToFind.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());

            // Perform get using Partition key
            DynamoDBQueryExpression<MigraineRecordDO> queryExpression = new DynamoDBQueryExpression<MigraineRecordDO>()
                .withHashKeyValues(itemToFind)
                .withConsistentRead(false);
            results = mapper.query(MigraineRecordDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoUserIdPainAtPeakQueryWithPartitionKeyAndFilterCondition extends DemoNoSQLOperationBase {

        private PaginatedQueryList<MigraineRecordDO> results;
        private Iterator<MigraineRecordDO> resultsIterator;

        DemoUserIdPainAtPeakQueryWithPartitionKeyAndFilterCondition (final Context context) {
            super(
                context.getString(R.string.nosql_operation_title_index_query_by_partition_and_filter_text),
                context.getString(R.string.nosql_operation_example_index_query_by_partition_and_filter_text,
                    "userId",AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID(),
                    "RecordId", "1111500000"));
        }

        public boolean executeOperation() {
            // Perform a query using a partition key and filter condition.
            final MigraineRecordDO itemToFind = new MigraineRecordDO();
            itemToFind.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());

            // Use an expression names Map to avoid the potential for attribute names
            // colliding with DynamoDB reserved words.
            final Map <String, String> filterExpressionAttributeNames = new HashMap<>();
            filterExpressionAttributeNames.put("#RecordId", "RecordId");

            final Map<String, AttributeValue> filterExpressionAttributeValues = new HashMap<>();
            filterExpressionAttributeValues.put(":MinRecordId",
                new AttributeValue().withN("1111500000"));

            // Perform get using Partition key and sort key condition
            DynamoDBQueryExpression<MigraineRecordDO> queryExpression = new DynamoDBQueryExpression<MigraineRecordDO>()
                .withHashKeyValues(itemToFind)
                .withFilterExpression("#RecordId > :MinRecordId")
                .withExpressionAttributeNames(filterExpressionAttributeNames)
                .withExpressionAttributeValues(filterExpressionAttributeValues)
                .withConsistentRead(false);
            results = mapper.query(MigraineRecordDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoUserIdPainAtPeakQueryWithPartitionKeySortKeyAndFilterCondition extends DemoNoSQLOperationBase {

        private PaginatedQueryList<MigraineRecordDO> results;
        private Iterator<MigraineRecordDO> resultsIterator;

        DemoUserIdPainAtPeakQueryWithPartitionKeySortKeyAndFilterCondition (final Context context) {
            super(
                context.getString(R.string.nosql_operation_title_index_query_by_partition_sort_condition_and_filter_text),
                context.getString(R.string.nosql_operation_example_index_query_by_partition_sort_condition_and_filter_text,
                    "userId", AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID(),
                    "PainAtPeak", "1111500000",
                    "RecordId", "1111500000"));
        }

        public boolean executeOperation() {
            // Perform a query using a partition key, sort condition, and filter.
            final MigraineRecordDO itemToFind = new MigraineRecordDO();
            itemToFind.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
            final Condition sortKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.LT.toString())
                .withAttributeValueList(new AttributeValue().withN(Double.toString(1111500000.0)));

            // Use a map of expression names to avoid the potential for attribute names
            // colliding with DynamoDB reserved words.
            final Map<String, String> filterExpressionAttributeNames = new HashMap<>();
            filterExpressionAttributeNames.put("#RecordId", "RecordId");

            final Map<String, AttributeValue> filterExpressionAttributeValues = new HashMap<>();
            filterExpressionAttributeValues.put(":MinRecordId",
                new AttributeValue().withN("1111500000"));

            // Perform get using Partition key and sort key condition
            DynamoDBQueryExpression<MigraineRecordDO> queryExpression = new DynamoDBQueryExpression<MigraineRecordDO>()
                .withHashKeyValues(itemToFind)
                .withRangeKeyCondition("PainAtPeak", sortKeyCondition)
                .withFilterExpression("#RecordId > :MinRecordId")
                .withExpressionAttributeNames(filterExpressionAttributeNames)
                .withExpressionAttributeValues(filterExpressionAttributeValues)
                .withConsistentRead(false);
            results = mapper.query(MigraineRecordDO.class, queryExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    /********* Scan Inner Classes *********/

    public class DemoScanWithFilter extends DemoNoSQLOperationBase {

        private PaginatedScanList<MigraineRecordDO> results;
        private Iterator<MigraineRecordDO> resultsIterator;

        DemoScanWithFilter(final Context context) {
            super(context.getString(R.string.nosql_operation_title_scan_with_filter),
                String.format(context.getString(R.string.nosql_operation_example_scan_with_filter),
                    "AP12Hours", "1111500000"));
        }

        @Override
        public boolean executeOperation() {
            // Use an expression names Map to avoid the potential for attribute names
            // colliding with DynamoDB reserved words.
            final Map <String, String> filterExpressionAttributeNames = new HashMap<>();
            filterExpressionAttributeNames.put("#AP12Hours", "AP12Hours");

            final Map<String, AttributeValue> filterExpressionAttributeValues = new HashMap<>();
            filterExpressionAttributeValues.put(":MinAP12Hours",
                new AttributeValue().withN("1111500000"));
            final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("#AP12Hours > :MinAP12Hours")
                .withExpressionAttributeNames(filterExpressionAttributeNames)
                .withExpressionAttributeValues(filterExpressionAttributeValues);

            results = mapper.scan(MigraineRecordDO.class, scanExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public boolean isScan() {
            return true;
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoScanWithoutFilter extends DemoNoSQLOperationBase {

        private PaginatedScanList<MigraineRecordDO> results;
        private Iterator<MigraineRecordDO> resultsIterator;

        DemoScanWithoutFilter(final Context context) {
            super(context.getString(R.string.nosql_operation_title_scan_without_filter),
                context.getString(R.string.nosql_operation_example_scan_without_filter));
        }

        @Override
        public boolean executeOperation() {
            final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            results = mapper.scan(MigraineRecordDO.class, scanExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public boolean isScan() {
            return true;
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    /**
     * Helper Method to handle retrieving the next group of query results.
     * @param resultsIterator the iterator for all the results (makes a new service call for each result group).
     * @return the next list of results.
     */
    private static List<DemoNoSQLResult> getNextResultsGroupFromIterator(final Iterator<MigraineRecordDO> resultsIterator) {
        if (!resultsIterator.hasNext()) {
            return null;
        }
        List<DemoNoSQLResult> resultGroup = new LinkedList<>();
        int itemsRetrieved = 0;
        do {
            // Retrieve the item from the paginated results.
            final MigraineRecordDO item = resultsIterator.next();
            // Add the item to a group of results that will be displayed later.
            resultGroup.add(new DemoNoSQLMigraineRecordResult(item));
            itemsRetrieved++;
        } while ((itemsRetrieved < RESULTS_PER_RESULT_GROUP) && resultsIterator.hasNext());
        return resultGroup;
    }

    /** The DynamoDB object mapper for accessing DynamoDB. */
    private final DynamoDBMapper mapper;

    public DemoNoSQLTableMigraineRecord() {
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
    public void insertSampleData() throws AmazonClientException {
        Log.d(LOG_TAG, "Inserting Sample data.");
        final MigraineRecordDO firstItem = new MigraineRecordDO();

        firstItem.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
        firstItem.setRecordId(1111500000.0);
        firstItem.setAP12Hours(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setAP24Hours(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setAP3Hours(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setAura(DemoSampleDataGenerator.getRandomSampleBool());
        firstItem.setCity(
            DemoSampleDataGenerator.getRandomSampleString("City"));
        firstItem.setConfusion(DemoSampleDataGenerator.getRandomSampleBool());
        firstItem.setCongestion(DemoSampleDataGenerator.getRandomSampleBool());
        firstItem.setCurrentAP(
            DemoSampleDataGenerator.getRandomSampleString("CurrentAP"));
        firstItem.setCurrentHum(
            DemoSampleDataGenerator.getRandomSampleString("CurrentHum"));
        firstItem.setCurrentTemp(
            DemoSampleDataGenerator.getRandomSampleString("CurrentTemp"));
        firstItem.setEars(DemoSampleDataGenerator.getRandomSampleBool());
        firstItem.setEaten(DemoSampleDataGenerator.getRandomSampleBool());
        firstItem.setEndHour(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setEyeStrain(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setHum12Hours(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setHum24Hours(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setHum3Hours(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setMedication(
            DemoSampleDataGenerator.getRandomSampleString("Medication"));
        firstItem.setMenstrualDay(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setNausea(DemoSampleDataGenerator.getRandomSampleBool());
        firstItem.setPainAtOnset(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setPainAtPeak(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setPainSource(
            DemoSampleDataGenerator.getRandomSampleString("PainSource"));
        firstItem.setPainType(
            DemoSampleDataGenerator.getRandomSampleString("PainType"));
        firstItem.setSensitivityToLight(DemoSampleDataGenerator.getRandomSampleBool());
        firstItem.setSensitivityToNoise(DemoSampleDataGenerator.getRandomSampleBool());
        firstItem.setSensitivityToSmell(DemoSampleDataGenerator.getRandomSampleBool());
        firstItem.setSleep(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setStartHour(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setStress(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setTemp12Hours(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setTemp24Hours(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setTemp3Hours(DemoSampleDataGenerator.getRandomSampleNumber());
        firstItem.setWater(DemoSampleDataGenerator.getRandomSampleBool());
        AmazonClientException lastException = null;

        try {
            mapper.save(firstItem);
        } catch (final AmazonClientException ex) {
            Log.e(LOG_TAG, "Failed saving item : " + ex.getMessage(), ex);
            lastException = ex;
        }

        final MigraineRecordDO[] items = new MigraineRecordDO[SAMPLE_DATA_ENTRIES_PER_INSERT-1];
        for (int count = 0; count < SAMPLE_DATA_ENTRIES_PER_INSERT-1; count++) {
            final MigraineRecordDO item = new MigraineRecordDO();
            item.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
            item.setRecordId(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setAP12Hours(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setAP24Hours(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setAP3Hours(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setAura(DemoSampleDataGenerator.getRandomSampleBool());
            item.setCity(DemoSampleDataGenerator.getRandomSampleString("City"));
            item.setConfusion(DemoSampleDataGenerator.getRandomSampleBool());
            item.setCongestion(DemoSampleDataGenerator.getRandomSampleBool());
            item.setCurrentAP(DemoSampleDataGenerator.getRandomSampleString("CurrentAP"));
            item.setCurrentHum(DemoSampleDataGenerator.getRandomSampleString("CurrentHum"));
            item.setCurrentTemp(DemoSampleDataGenerator.getRandomSampleString("CurrentTemp"));
            item.setEars(DemoSampleDataGenerator.getRandomSampleBool());
            item.setEaten(DemoSampleDataGenerator.getRandomSampleBool());
            item.setEndHour(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setEyeStrain(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setHum12Hours(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setHum24Hours(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setHum3Hours(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setMedication(DemoSampleDataGenerator.getRandomSampleString("Medication"));
            item.setMenstrualDay(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setNausea(DemoSampleDataGenerator.getRandomSampleBool());
            item.setPainAtOnset(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setPainAtPeak(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setPainSource(DemoSampleDataGenerator.getRandomSampleString("PainSource"));
            item.setPainType(DemoSampleDataGenerator.getRandomSampleString("PainType"));
            item.setSensitivityToLight(DemoSampleDataGenerator.getRandomSampleBool());
            item.setSensitivityToNoise(DemoSampleDataGenerator.getRandomSampleBool());
            item.setSensitivityToSmell(DemoSampleDataGenerator.getRandomSampleBool());
            item.setSleep(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setStartHour(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setStress(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setTemp12Hours(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setTemp24Hours(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setTemp3Hours(DemoSampleDataGenerator.getRandomSampleNumber());
            item.setWater(DemoSampleDataGenerator.getRandomSampleBool());

            items[count] = item;
        }
        try {
            mapper.batchSave(Arrays.asList(items));
        } catch (final AmazonClientException ex) {
            Log.e(LOG_TAG, "Failed saving item batch : " + ex.getMessage(), ex);
            lastException = ex;
        }

        if (lastException != null) {
            // Re-throw the last exception encountered to alert the user.
            throw lastException;
        }
    }

    @Override
    public void removeSampleData() throws AmazonClientException {

        final MigraineRecordDO itemToFind = new MigraineRecordDO();
        itemToFind.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());

        final DynamoDBQueryExpression<MigraineRecordDO> queryExpression = new DynamoDBQueryExpression<MigraineRecordDO>()
            .withHashKeyValues(itemToFind)
            .withConsistentRead(false)
            .withLimit(MAX_BATCH_SIZE_FOR_DELETE);

        final PaginatedQueryList<MigraineRecordDO> results = mapper.query(MigraineRecordDO.class, queryExpression);

        Iterator<MigraineRecordDO> resultsIterator = results.iterator();

        AmazonClientException lastException = null;

        if (resultsIterator.hasNext()) {
            final MigraineRecordDO item = resultsIterator.next();

            // Demonstrate deleting a single item.
            try {
                mapper.delete(item);
            } catch (final AmazonClientException ex) {
                Log.e(LOG_TAG, "Failed deleting item : " + ex.getMessage(), ex);
                lastException = ex;
            }
        }

        final List<MigraineRecordDO> batchOfItems = new LinkedList<MigraineRecordDO>();
        while (resultsIterator.hasNext()) {
            // Build a batch of books to delete.
            for (int index = 0; index < MAX_BATCH_SIZE_FOR_DELETE && resultsIterator.hasNext(); index++) {
                batchOfItems.add(resultsIterator.next());
            }
            try {
                // Delete a batch of items.
                mapper.batchDelete(batchOfItems);
            } catch (final AmazonClientException ex) {
                Log.e(LOG_TAG, "Failed deleting item batch : " + ex.getMessage(), ex);
                lastException = ex;
            }

            // clear the list for re-use.
            batchOfItems.clear();
        }


        if (lastException != null) {
            // Re-throw the last exception encountered to alert the user.
            // The logs contain all the exceptions that occurred during attempted delete.
            throw lastException;
        }
    }

    private List<DemoNoSQLOperationListItem> getSupportedDemoOperations(final Context context) {
        List<DemoNoSQLOperationListItem> noSQLOperationsList = new ArrayList<DemoNoSQLOperationListItem>();
        noSQLOperationsList.add(new DemoNoSQLOperationListHeader(
            context.getString(R.string.nosql_operation_header_get)));
        noSQLOperationsList.add(new DemoGetWithPartitionKeyAndSortKey(context));

        noSQLOperationsList.add(new DemoNoSQLOperationListHeader(
            context.getString(R.string.nosql_operation_header_primary_queries)));
        noSQLOperationsList.add(new DemoQueryWithPartitionKeyOnly(context));
        noSQLOperationsList.add(new DemoQueryWithPartitionKeyAndFilter(context));
        noSQLOperationsList.add(new DemoQueryWithPartitionKeyAndSortKeyCondition(context));
        noSQLOperationsList.add(new DemoQueryWithPartitionKeySortKeyConditionAndFilter(context));

        noSQLOperationsList.add(new DemoNoSQLOperationListHeader(
            context.getString(R.string.nosql_operation_header_secondary_queries, "userId-StartHour")));

        noSQLOperationsList.add(new DemoUserIdStartHourQueryWithPartitionKeyOnly(context));
        noSQLOperationsList.add(new DemoUserIdStartHourQueryWithPartitionKeyAndFilterCondition(context));
        noSQLOperationsList.add(new DemoUserIdStartHourQueryWithPartitionKeyAndSortKeyCondition(context));
        noSQLOperationsList.add(new DemoUserIdStartHourQueryWithPartitionKeySortKeyAndFilterCondition(context));
        noSQLOperationsList.add(new DemoNoSQLOperationListHeader(
            context.getString(R.string.nosql_operation_header_secondary_queries, "userId-PainAtPeak")));

        noSQLOperationsList.add(new DemoUserIdPainAtPeakQueryWithPartitionKeyOnly(context));
        noSQLOperationsList.add(new DemoUserIdPainAtPeakQueryWithPartitionKeyAndFilterCondition(context));
        noSQLOperationsList.add(new DemoUserIdPainAtPeakQueryWithPartitionKeyAndSortKeyCondition(context));
        noSQLOperationsList.add(new DemoUserIdPainAtPeakQueryWithPartitionKeySortKeyAndFilterCondition(context));
        noSQLOperationsList.add(new DemoNoSQLOperationListHeader(
            context.getString(R.string.nosql_operation_header_scan)));
        noSQLOperationsList.add(new DemoScanWithoutFilter(context));
        noSQLOperationsList.add(new DemoScanWithFilter(context));
        return noSQLOperationsList;
    }

    @Override
    public void getSupportedDemoOperations(final Context context,
                                           final SupportedDemoOperationsHandler opsHandler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<DemoNoSQLOperationListItem> supportedOperations = getSupportedDemoOperations(context);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        opsHandler.onSupportedOperationsReceived(supportedOperations);
                    }
                });
            }
        }).start();
    }
}
