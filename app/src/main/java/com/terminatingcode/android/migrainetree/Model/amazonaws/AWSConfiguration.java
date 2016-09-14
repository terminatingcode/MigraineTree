//
// Copyright 2016 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.8
//
package com.terminatingcode.android.migrainetree.model.amazonaws;

import com.amazonaws.regions.Regions;

/**
 * This class defines constants for the developer's resource
 * identifiers and API keys. This configuration should not
 * be shared or posted to any public source code repository.
 */
public class AWSConfiguration {
    // AWS MobileHub user agent string
    public static final String AWS_MOBILEHUB_USER_AGENT =
            "MobileHub a5c4332f-a7d1-4946-83a0-8cde49d0383c aws-my-sample-app-android-v0.8";
    // AMAZON COGNITO
    public static final Regions AMAZON_COGNITO_REGION =
            Regions.fromName("us-east-1");
    public static final String  AMAZON_COGNITO_IDENTITY_POOL_ID =
            "us-east-1:1e6250e2-8df1-40a7-9766-da9b93628ad0";
    // AMAZON MOBILE ANALYTICS
    public static final String  AMAZON_MOBILE_ANALYTICS_APP_ID =
            "b2c7280f3834455da8a6715d12ab2a14";
    // Amazon Mobile Analytics region
    public static final Regions AMAZON_MOBILE_ANALYTICS_REGION = Regions.US_EAST_1;
    // Google Client ID for Web application
    public static final String GOOGLE_CLIENT_ID =
            "286657348881-dogtrm7k4u7gpebpbf71rg66jt8aguch.apps.googleusercontent.com";
    // GOOGLE CLOUD MESSAGING API KEY
    public static final String GOOGLE_CLOUD_MESSAGING_API_KEY =
            "AIzaSyBihB3piKPQhLSzTH11OLBaZyPkI8l39Xc";
    // GOOGLE CLOUD MESSAGING SENDER ID
    public static final String GOOGLE_CLOUD_MESSAGING_SENDER_ID =
            "286657348881";

    // SNS PLATFORM APPLICATION ARN
    public static final String AMAZON_SNS_PLATFORM_APPLICATION_ARN =
            "arn:aws:sns:us-east-1:438801383197:app/GCM/migrainetree_MOBILEHUB_1937228315";
    public static final Regions AMAZON_SNS_REGION =
            Regions.fromName("us-east-1");
    // SNS DEFAULT TOPIC ARN
    public static final String AMAZON_SNS_DEFAULT_TOPIC_ARN =
            "arn:aws:sns:us-east-1:438801383197:migrainetree_alldevices_MOBILEHUB_1937228315";
    // SNS PLATFORM TOPIC ARNS
    public static final String[] AMAZON_SNS_TOPIC_ARNS =
            {};
    // S3 BUCKET
    public static final String AMAZON_S3_USER_FILES_BUCKET =
            "migrainetree-userfiles-mobilehub-1937228315";
    // S3 BUCKET REGION
    public static final Regions AMAZON_S3_USER_FILES_BUCKET_REGION =
            Regions.fromName("us-east-1");
    public static final Regions AMAZON_CLOUD_LOGIC_REGION =
            Regions.fromName("us-east-1");
    public static final Regions AMAZON_DYNAMODB_REGION =
            Regions.fromName("us-east-1");
}