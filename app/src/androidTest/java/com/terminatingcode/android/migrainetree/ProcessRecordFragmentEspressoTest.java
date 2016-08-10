package com.terminatingcode.android.migrainetree;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Sarah on 8/10/2016.
 */
public class ProcessRecordFragmentEspressoTest {

    ProcessRecordFragment mFragment;
    MigraineRecordObject mMockRecordObject;
    long startHour = 631152000000L;
    String city = "city";
    int painAtOnset = 1;
    boolean aura = false;
    boolean eaten = true;
    boolean water = true;
    int sleep = 6;
    int stress = 7;
    int eyeStrain = 8;
    String painType = "no pain";
    String medication = "no medication";
    String painSource = "eye";
    boolean nausea = true;
    boolean light = true;
    boolean smell = true;
    boolean noise = false;
    boolean congestion = true;
    boolean ears = true;
    boolean confusion = true;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp(){
        mMockRecordObject = initialiseMockObject();
        mFragment = ProcessRecordFragment.newInstance(mMockRecordObject);
        mMainActivityActivityTestRule
                .getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_frame, mFragment, "testInputTriggers")
                .commit();
    }

    private MigraineRecordObject initialiseMockObject() {
        MigraineRecordObject mockObject = new MigraineRecordObject();
        mockObject.setStartHour(startHour);
        mockObject.setCity(city);
        mockObject.setPainAtOnset(painAtOnset);
        mockObject.setAura(aura);
        mockObject.setEaten(eaten);
        mockObject.setWater(water);
        mockObject.setSleep(sleep);
        mockObject.setStress(stress);
        mockObject.setEyeStrain(eyeStrain);
        mockObject.setPainType(painType);
        mockObject.setMedication(medication);
        mockObject.setPainSource(painSource);
        mockObject.setNausea(nausea);
        mockObject.setSensitivityToLight(light);
        mockObject.setSensitivityToNoise(noise);
        mockObject.setSensitivityToSmell(smell);
        mockObject.setCongestion(congestion);
        mockObject.setEars(ears);
        mockObject.setConfusion(confusion);
        return mockObject;
    }

    @Test
    public void testMigraineRecordDataDisplayed() throws Exception {
        String expectedDateString = DateUtils.convertLongToString(startHour);
        StringBuilder sb = new StringBuilder();
        sb.append("Nausea\n");
        sb.append("Sensitivity to light\n");
        sb.append("Sensitivity to smell\n");
        sb.append("Nasal congestion\n");
        sb.append("Ringin/popped ears\n");
        sb.append("Confusion/mental fog");
        String expectedSymptomsString = sb.toString();

        onView(withId(R.id.recordLocationTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(withText(city)));
        onView(withId(R.id.dateTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(withText(expectedDateString)));
        onView(withId(R.id.painEnteredTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(withText(String.valueOf(painAtOnset))));
        onView(withId(R.id.auraEntered))
                .perform(ViewActions.scrollTo())
                .check(matches(EspressoActions.withDrawable(android.R.drawable.checkbox_off_background)));
        onView(withId(R.id.eatenEntered))
                .perform(ViewActions.scrollTo())
                .check(matches(EspressoActions.withDrawable(android.R.drawable.checkbox_on_background)));
        onView(withId(R.id.waterEntered))
                .perform(ViewActions.scrollTo())
                .check(matches(EspressoActions.withDrawable(android.R.drawable.checkbox_on_background)));
        onView(withId(R.id.sleepEntered))
                .perform(ViewActions.scrollTo())
                .check(matches(withText(String.valueOf(sleep))));
        onView(withId(R.id.stressEntered))
                .perform(ViewActions.scrollTo())
                .check(matches(withText(String.valueOf(stress))));
        onView(withId(R.id.eyeStrainEntered))
                .perform(ViewActions.scrollTo())
                .check(matches(withText(String.valueOf(eyeStrain))));
        onView(withId(R.id.typeOfPainEntered))
                .perform(ViewActions.scrollTo())
                .check(matches(withText(String.valueOf(painType))));
        onView(withId(R.id.sourceOfPainEntered))
                .perform(ViewActions.scrollTo())
                .check(matches(withText(String.valueOf(painSource))));
        onView(withId(R.id.medicationEntered))
                .perform(ViewActions.scrollTo())
                .check(matches(withText(String.valueOf(medication))));
        onView(withId(R.id.symptomsEntered))
                .perform(ViewActions.scrollTo())
                .check(matches(withText(expectedSymptomsString)));
    }
}