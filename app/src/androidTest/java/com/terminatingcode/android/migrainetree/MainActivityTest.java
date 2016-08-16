package com.terminatingcode.android.migrainetree;

import android.support.test.rule.ActivityTestRule;

import com.amazonaws.services.machinelearning.model.PredictResult;
import com.terminatingcode.android.migrainetree.amazonaws.SpanishDataConstants;

import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by Sarah on 8/16/2016.
 */
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void testMakeMLPrediction() throws Exception {
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
        PredictResult result = mActivityRule
                .getActivity()
                .makeMLPrediction(SpanishDataConstants.MODEL_ID, dummyUserInput);
        assertNotNull(result);
    }
}