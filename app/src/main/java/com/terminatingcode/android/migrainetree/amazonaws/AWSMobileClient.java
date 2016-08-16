//
// Copyright 2016 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.8
//
package com.terminatingcode.android.migrainetree.amazonaws;

import android.content.Context;
import android.util.Log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.AnalyticsConfig;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.EventClient;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.InitializationException;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.MobileAnalyticsManager;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.SessionClient;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.machinelearning.AmazonMachineLearningClient;
import com.terminatingcode.android.migrainetree.amazonaws.push.GCMTokenHelper;
import com.terminatingcode.android.migrainetree.amazonaws.push.PushManager;
import com.terminatingcode.android.migrainetree.amazonaws.user.IdentityManager;

/**
 * The AWS Mobile Client bootstraps the application to make calls to AWS 
 * services. It creates clients which can be used to call services backing the
 * features you selected in your project.
 */
public class AWSMobileClient {

    private final static String LOG_TAG = AWSMobileClient.class.getSimpleName();

    private static AWSMobileClient instance;

    private final Context context;

    private ClientConfiguration clientConfiguration;
    private IdentityManager identityManager;
    private GCMTokenHelper gcmTokenHelper;
    private PushManager pushManager;
    private MobileAnalyticsManager mobileAnalyticsManager;
    private CognitoSyncManager syncManager;
    private AmazonDynamoDBClient dynamoDBClient;
    private DynamoDBMapper dynamoDBMapper;
    private AmazonMachineLearningClient mMachineLearningClient;

    /**
     * Build class used to create the AWS mobile client.
     */
    public static class Builder {

        private Context applicationContext;
        private String  cognitoIdentityPoolID;
        private Regions cognitoRegion;
        private String  mobileAnalyticsAppID;
        private ClientConfiguration clientConfiguration;
        private IdentityManager identityManager;

        /**
         * Constructor.
         * @param context Android context.
         */
        public Builder(final Context context) {
            this.applicationContext = context.getApplicationContext();
        };

        /**
         * Provides the Amazon Cognito Identity Pool ID.
         * @param cognitoIdentityPoolID identity pool ID
         * @return builder
         */
        public Builder withCognitoIdentityPoolID(final String cognitoIdentityPoolID) {
            this.cognitoIdentityPoolID = cognitoIdentityPoolID;
            return this;
        };

        /**
         * Provides the Amazon Cognito service region.
         * @param cognitoRegion service region
         * @return builder
         */
        public Builder withCognitoRegion(final Regions cognitoRegion) {
            this.cognitoRegion = cognitoRegion;
            return this;
        }

        /**
         * Provides the Amazon Mobile Analytics App ID.
         * @param mobileAnalyticsAppID application ID
         * @return builder
         */
        public Builder withMobileAnalyticsAppID(final String mobileAnalyticsAppID) {
            this.mobileAnalyticsAppID = mobileAnalyticsAppID;
            return this;
        };

        /**
         * Provides the identity manager.
         * @param identityManager identity manager
         * @return builder
         */
        public Builder withIdentityManager(final IdentityManager identityManager) {
            this.identityManager = identityManager;
            return this;
        }

        /**
         * Provides the client configuration
         * @param clientConfiguration client configuration
         * @return builder
         */
        public Builder withClientConfiguration(final ClientConfiguration clientConfiguration) {
            this.clientConfiguration = clientConfiguration;
            return this;
        }

        /**
         * Creates the AWS mobile client instance and initializes it.
         * @return AWS mobile client
         */
        public AWSMobileClient build() {
            return
                    new AWSMobileClient(applicationContext,
                            cognitoIdentityPoolID,
                            cognitoRegion,
                            mobileAnalyticsAppID,
                            identityManager,
                            clientConfiguration);
        }
    }

    private AWSMobileClient(final Context context,
                            final String  cognitoIdentityPoolID,
                            final Regions cognitoRegion,
                            final String mobileAnalyticsAppID,
                            final IdentityManager identityManager,
                            final ClientConfiguration clientConfiguration) {

        this.context = context;
        this.identityManager = identityManager;
        this.clientConfiguration = clientConfiguration;

        try {
            this.mobileAnalyticsManager =
                    MobileAnalyticsManager.
                            getOrCreateInstance(context,
                                    AWSConfiguration.AMAZON_MOBILE_ANALYTICS_APP_ID,
                                    AWSConfiguration.AMAZON_MOBILE_ANALYTICS_REGION,
                                    identityManager.getCredentialsProvider(),
                                    new AnalyticsConfig(clientConfiguration));
        }
        catch (final InitializationException ie) {
            Log.e(LOG_TAG, "Unable to initalize Amazon Mobile Analytics. " + ie.getMessage(), ie);
        }

        this.gcmTokenHelper = new GCMTokenHelper(context, AWSConfiguration.GOOGLE_CLOUD_MESSAGING_SENDER_ID);
        this.pushManager =
                new PushManager(context,
                        gcmTokenHelper,
                        identityManager.getCredentialsProvider(),
                        AWSConfiguration.AMAZON_SNS_PLATFORM_APPLICATION_ARN,
                        clientConfiguration,
                        AWSConfiguration.AMAZON_SNS_DEFAULT_TOPIC_ARN,
                        AWSConfiguration.AMAZON_SNS_TOPIC_ARNS,
                        AWSConfiguration.AMAZON_SNS_REGION);
        gcmTokenHelper.init();

        this.syncManager = new CognitoSyncManager(context, AWSConfiguration.AMAZON_COGNITO_REGION,
                identityManager.getCredentialsProvider(), clientConfiguration);
        this.dynamoDBClient = new AmazonDynamoDBClient(identityManager.getCredentialsProvider(), clientConfiguration);
        this.dynamoDBClient.setRegion(Region.getRegion(AWSConfiguration.AMAZON_DYNAMODB_REGION));
        this.dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
        this.mMachineLearningClient = new AmazonMachineLearningClient(identityManager.getCredentialsProvider());
    }

    /**
     * Sets the singleton instance of the AWS mobile client.
     * @param client client instance
     */
    public static void setDefaultMobileClient(AWSMobileClient client) {
        instance = client;
    }

    /**
     * Gets the default singleton instance of the AWS mobile client.
     * @return client
     */
    public static AWSMobileClient defaultMobileClient() {
        return instance;
    }

    /**
     * Gets the identity manager.
     * @return identity manager
     */
    public IdentityManager getIdentityManager() {
        return this.identityManager;
    }

    /**
     * Gets the push notifications manager.
     * @return push manager
     */
    public PushManager getPushManager() {
        return this.pushManager;
    }

    /**
     * Gets the Amazon Cognito Sync Manager, which is responsible for saving and
     * loading user profile data, such as game state or user settings.
     * @return sync manager
     */
    public CognitoSyncManager getSyncManager() {
        return syncManager;
    }

    /**
     * Creates and initialize the default AWSMobileClient if it doesn't already
     * exist using configuration constants from {@link AWSConfiguration}.
     *
     * @param context an application context.
     */
    public static void initializeMobileClientIfNecessary(final Context context) {
        if (AWSMobileClient.defaultMobileClient() == null) {
            Log.d(LOG_TAG, "Initializing AWS Mobile Client...");
            final ClientConfiguration clientConfiguration = new ClientConfiguration();
            clientConfiguration.setUserAgent(AWSConfiguration.AWS_MOBILEHUB_USER_AGENT);
            final IdentityManager identityManager = new IdentityManager(context, clientConfiguration);
            final AWSMobileClient awsClient =
                    new AWSMobileClient.Builder(context)
                            .withCognitoRegion(AWSConfiguration.AMAZON_COGNITO_REGION)
                            .withCognitoIdentityPoolID(AWSConfiguration.AMAZON_COGNITO_IDENTITY_POOL_ID)
                            .withMobileAnalyticsAppID(AWSConfiguration.AMAZON_MOBILE_ANALYTICS_APP_ID)
                            .withIdentityManager(identityManager)
                            .withClientConfiguration(clientConfiguration)
                            .build();

            AWSMobileClient.setDefaultMobileClient(awsClient);
        }
        Log.d(LOG_TAG, "AWS Mobile Client is OK");
    }


    /**
     * Gets the DynamoDB Client, which allows accessing Amazon DynamoDB tables.
     * @return the DynamoDB client instance.
     */
    public AmazonDynamoDBClient getDynamoDBClient() {
        return dynamoDBClient;
    }

    /**
     * Gets the Dynamo DB Object Mapper, which allows accessing DynamoDB tables using annotated
     * data object classes to represent your data using POJOs (Plain Old Java Objects).
     * @return the DynamoDB Object Mapper instance.
     */
    public DynamoDBMapper getDynamoDBMapper() {
        return dynamoDBMapper;
    }

    /**
     * Gets the Amazon Mobile Analytics Manager, which allows you to submit
     * custom and monetization events to the Amazon Mobile Analytics system. It
     * also handles recording user session data events.
     * @return mobile analytics manager
     */
    public MobileAnalyticsManager getMobileAnalyticsManager() {
        return this.mobileAnalyticsManager;
    }

    /**
     * This method should be invoked when each activity is paused. It is used
     * to assist in tracking user session data in Amazon Mobile Analytics system.
     */
    public void handleOnPause() {

        SessionClient sessionClient = null;
        EventClient eventClient = null;

        try {
            if (mobileAnalyticsManager != null &&
                    (sessionClient = mobileAnalyticsManager.getSessionClient()) != null &&
                    (eventClient = mobileAnalyticsManager.getEventClient()) != null) {
                sessionClient.pauseSession();
                eventClient.submitEvents();
            }
        }
        catch (final Exception e) {
            Log.w(LOG_TAG, "Unable to report analytics. " + e.getMessage(), e);
        }
    }

    /**
     * This method should be called whenever any activity resumes. It assists in
     * tracking user session data in the Amazon Mobile Analytics system.
     */
    public void handleOnResume() {
        SessionClient sessionClient = null;

        try {
            if (mobileAnalyticsManager != null &&
                    (sessionClient = mobileAnalyticsManager.getSessionClient()) != null) {
                sessionClient.resumeSession();
            }
        }
        catch (final Exception e) {
            Log.w(LOG_TAG, "Unable to resume analytics. " + e.getMessage(), e);
        }
    }

    /**
     * Gets an AWS Lambda cloud function factory instance. This factory can be used
     * to create proxies to Lambda cloud functions, which encode and decode JSON parameters
     * to and from your own annotated POJO (plain old java object) classes. For more information
     * on using this method to invoke AWS Lambda, please reference the Android AWS Lambda SDK
     * developer documentation.
     * @param context application context
     * @return lambda invoker factory
     */
    public LambdaInvokerFactory getCloudFunctionFactory(final Context context) {
        return
                new LambdaInvokerFactory(context,
                        AWSConfiguration.AMAZON_CLOUD_LOGIC_REGION,
                        getIdentityManager().getCredentialsProvider(),
                        clientConfiguration);
    }

    /**
     * Gets an AWS Lambda cloud function client instance. This client can be used
     * to directly invoke Lambda cloud functions with JSON parameters and results.
     * @return lambda client
     */
    public AWSLambdaClient getCloudFunctionClient() {
        AWSLambdaClient awsLambdaClient = new AWSLambdaClient(identityManager.getCredentialsProvider(), clientConfiguration);
        awsLambdaClient.setRegion(Region.getRegion(AWSConfiguration.AMAZON_CLOUD_LOGIC_REGION));
        return awsLambdaClient;
    }

    public AmazonMachineLearningClient getMachineLearningClient(){
        return mMachineLearningClient;
    }
}
