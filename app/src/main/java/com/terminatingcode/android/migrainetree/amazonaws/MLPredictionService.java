package com.terminatingcode.android.migrainetree.amazonaws;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.machinelearning.AmazonMachineLearningClient;
import com.amazonaws.services.machinelearning.model.EntityStatus;
import com.amazonaws.services.machinelearning.model.GetMLModelRequest;
import com.amazonaws.services.machinelearning.model.GetMLModelResult;
import com.amazonaws.services.machinelearning.model.PredictRequest;
import com.amazonaws.services.machinelearning.model.PredictResult;
import com.amazonaws.services.machinelearning.model.RealtimeEndpointStatus;
import com.terminatingcode.android.migrainetree.Constants;
import com.terminatingcode.android.migrainetree.EventMessages.MLPredictionMessage;

import org.greenrobot.eventbus.EventBus;

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
            String result = makeMLPrediction(SpanishDataConstants.MODEL_ID, record);
            EventBus.getDefault().post(new MLPredictionMessage(result));
        }
        setIntentRedelivery(false);
    }

    /**
     * make a prediction based on user input
     * @param modelId the AWS ML model used
     * @param record the user input
     * @return the prediction result or null if failed
     */
    public String makeMLPrediction(String modelId, Map<String, String> record) {
        try {
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
                return null;
            }
            //validate that the endpoint is ready
            if (!modelResult
                    .getEndpointInfo()
                    .getEndpointStatus()
                    .equals(RealtimeEndpointStatus.READY.toString())) {
                Log.d(NAME, "AWS ML Endpoint not ready: " + modelResult.getEndpointInfo().getEndpointStatus());
                return null;
            }

            PredictRequest predictRequest = new PredictRequest();
            predictRequest.setMLModelId(modelId);
            predictRequest.setRecord(record);
            predictRequest.setPredictEndpoint(modelResult.getEndpointInfo().getEndpointUrl());
            Log.d(NAME, "predict request: " + predictRequest.toString());
            PredictResult predictResult = machineLearningClient.predict(predictRequest);
            Log.d(NAME, "predict result: " + predictResult.getPrediction().getPredictedValue());
            String result =  "" + predictResult.getPrediction().getPredictedValue();
            return result;
        }catch(final AmazonClientException ex){
            Log.d(NAME, "exception when trying to predict: " + ex.getMessage());
            return null;
        }
    }
}
