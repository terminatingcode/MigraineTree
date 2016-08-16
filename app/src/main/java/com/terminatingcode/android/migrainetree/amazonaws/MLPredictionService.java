package com.terminatingcode.android.migrainetree.amazonaws;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.amazonaws.services.machinelearning.AmazonMachineLearningClient;
import com.amazonaws.services.machinelearning.model.EntityStatus;
import com.amazonaws.services.machinelearning.model.GetMLModelRequest;
import com.amazonaws.services.machinelearning.model.GetMLModelResult;
import com.amazonaws.services.machinelearning.model.PredictRequest;
import com.amazonaws.services.machinelearning.model.PredictResult;
import com.amazonaws.services.machinelearning.model.RealtimeEndpointStatus;
import com.terminatingcode.android.migrainetree.Constants;

import java.util.HashMap;
import java.util.Map;

public class MLPredictionService extends IntentService {
    private static final String NAME = "MLPredictionService";

    public MLPredictionService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null){
            HashMap<String, String> record = (HashMap<String, String>)intent.getSerializableExtra(Constants.RECORD);
            makeMLPrediction(SpanishDataConstants.MODEL_ID, record);
        }
        setIntentRedelivery(false);
    }

    /**
     * make a prediction based on user input
     * @param modelId the AWS ML model used
     * @param record the user input
     * @return the prediction result or null if failed
     */
    public double makeMLPrediction(String modelId, Map<String, String> record) {
        Map<String, String> dummyUserInput = new HashMap<>();
        dummyUserInput.put(SpanishDataConstants.CURRENT_TEMP, "0.0");
        dummyUserInput.put(SpanishDataConstants.CURRENT_HUM, "0.0");
        dummyUserInput.put(SpanishDataConstants.CURRENT_AP, "0.0");
        dummyUserInput.put(SpanishDataConstants.TEMP_3_HOURS, "0.0");
        dummyUserInput.put(SpanishDataConstants.TEMP_12_HOURS, "0.0");
        dummyUserInput.put(SpanishDataConstants.TEMP_24_HOURS, "0.0");
        dummyUserInput.put(SpanishDataConstants.HUM_3_HOURS, "0.0");
        dummyUserInput.put(SpanishDataConstants.HUM_12_HOURS, "0.0");
        dummyUserInput.put(SpanishDataConstants.HUM_24_HOURS, "0.0");
        dummyUserInput.put(SpanishDataConstants.AP_3_HOURS, "0.0");
        dummyUserInput.put(SpanishDataConstants.AP_12_HOURS, "0.0");
        dummyUserInput.put(SpanishDataConstants.AP_24_HOURS, "0.0");
        dummyUserInput.put(SpanishDataConstants.AURA, "false");
        final AmazonMachineLearningClient machineLearningClient = AWSMobileClient
                .defaultMobileClient()
                .getMachineLearningClient();
        GetMLModelRequest getMLModelRequest = new GetMLModelRequest();
        getMLModelRequest.setMLModelId(modelId);
        GetMLModelResult modelResult = machineLearningClient.getMLModel(getMLModelRequest);
        //validate ML model is complete
        if (!modelResult.
                getStatus()
                .equals(EntityStatus.COMPLETED.toString())) {
            Log.d(NAME, "mlModel not complete: " + modelResult.getStatus());
            return -1;
        }
        //validate that the endpoint is ready
        if (!modelResult
                .getEndpointInfo()
                .getEndpointStatus()
                .equals(RealtimeEndpointStatus.READY.toString())) {
            Log.d(NAME, "AWS ML Endpoint not ready: " + modelResult.getEndpointInfo().getEndpointStatus());
            return -1;
        }

        PredictRequest predictRequest = new PredictRequest();
        predictRequest.setMLModelId(modelId);
        predictRequest.setRecord(dummyUserInput);
        predictRequest.setPredictEndpoint(modelResult.getEndpointInfo().getEndpointUrl());
        Log.d(NAME, "predict request: " + predictRequest.toString());
        PredictResult result =  machineLearningClient.predict(predictRequest);
        Log.d(NAME, "predict result: " + result.toString());
        return result.getPrediction().getPredictedValue();
    }
}
