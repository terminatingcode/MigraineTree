package com.terminatingcode.android.migrainetree;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;


/**
 * Instrumentation Test for InputTriggerFragment
 * Created by Sarah on 6/30/2016.
 */
public class InputTriggersFragmentEspressoTest {

    private Fragment mFragment;
    private SharedPreferences mSharedPreferences;
    private String location = "testLocation";

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp(){
        mSharedPreferences = mMainActivityActivityTestRule.getActivity()
                .getSharedPreferences(Constants.PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
        mSharedPreferences.edit().putString(Constants.LOCATION_NAME, location).commit();
        mFragment = new InputTriggersFragment();
        mMainActivityActivityTestRule
                .getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_frame, mFragment, "testInputTriggers")
                .commit();
    }

    @Test
    public void testFragmentIsVisible(){
        mFragment.isVisible();
    }

    @Test
    public void testDatePickerIsMadeVisibleByClickingSetButton(){
        onView(withId(R.id.datePicker)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.set_button)).perform(click());
        onView(withId(R.id.datePicker)).check(matches(isDisplayed())).check(matches(isClickable()));
        onView(withId(R.id.set_button)).perform(click());
        onView(withId(R.id.datePicker)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void testTimePickerMadeVisibleByClickingSetButton(){
        onView(withId(R.id.timePicker)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.set_button)).perform(click());
        onView(withId(R.id.timePicker)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.set_button)).perform(click());
        onView(withId(R.id.timePicker)).check(matches(isDisplayed())).check(matches(isClickable()));
    }

    @Test
    public void testTimePickerDisappearsByClickingSetButton(){
        onView(withId(R.id.set_button)).perform(click());
        onView(withId(R.id.set_button)).perform(click());
        onView(withId(R.id.timePicker)).check(matches(isDisplayed())).check(matches(isClickable()));
        onView(withId(R.id.set_button)).perform(click());
        onView(withId(R.id.timePicker)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void testDateTextViewChangesTextToDateChosenOnDatePicker(){
        int year = 2016;
        int month = 7;
        int day = 13;
        onView(withId(R.id.set_button)).perform(click());
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(year, month + 1, day));
        onView(withId(R.id.set_button)).perform(click());
        onView(withId(R.id.migraineStartDateTextView)).check(matches(withText(day + "/" + month + "/" + year)));
    }

    @Test
    public void testTimeTextViewChangesTextToDateChosenOnDatePicker(){
        int hour = 10;
        int minutes = 7;
        String expected = "10:07";
        onView(withId(R.id.set_button)).perform(click());
        onView(withId(R.id.set_button)).perform(click());
        onView(withId(R.id.timePicker)).perform(PickerActions.setTime(hour, minutes));
        onView(withId(R.id.set_button)).perform(click());
        onView(withId(R.id.migraineStartTimeTextView)).check(matches(withText(expected)));
    }

    @Test
    public void testPainLevelIsVisible(){
        onView(withId(R.id.painTextView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.painLevelTextView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.painSeekBar))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testPainSeekBarUpdatesTextView(){
        onView(withId(R.id.painSeekBar)).perform(EspressoActions.setProgress(10));
        onView(allOf(withId(R.id.painLevelTextView), withText("10"))).check(matches(isDisplayed()));
    }

    @Test
    public void testAuraIsVisible(){
        onView(withId(R.id.auraCheckBox))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .check(matches(ViewMatchers.isNotChecked()));
    }

    @Test
    public void testEatenIsVisible(){
        onView(withId(R.id.eatenCheckBox))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .check(matches(ViewMatchers.isNotChecked()));
    }

    @Test
    public void testSleepLevelIsVisible(){
        onView(withId(R.id.sleepTextView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.sleepLevelTextView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.sleepSeekBar))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSleepSeekBarUpdatesTextView(){
        onView(withId(R.id.sleepSeekBar)).perform(EspressoActions.setProgress(10));
        onView(allOf(withId(R.id.sleepLevelTextView), withText("10"))).check(matches(isDisplayed()));
    }

    @Test
    public void testStressLevelIsVisible(){
        onView(withId(R.id.stressTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
        onView(withId(R.id.stressLevelTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
        onView(withId(R.id.stressSeekBar))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testStressSeekBarUpdatesTextView(){
        onView(withId(R.id.stressSeekBar))
                .perform(ViewActions.scrollTo())
                .perform(EspressoActions.setProgress(10));
        onView(allOf(withId(R.id.stressLevelTextView), withText("10")))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEyeStrainLevelIsVisible(){
        onView(withId(R.id.eyesTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
        onView(withId(R.id.eyesLevelTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
        onView(withId(R.id.eyesSeekBar))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEyeStrainSeekBarUpdatesTextView(){
        onView(withId(R.id.eyesSeekBar))
                .perform(ViewActions.scrollTo())
                .perform(EspressoActions.setProgress(10));
        onView(allOf(withId(R.id.eyesLevelTextView), withText("10")))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLocationTextViewNoLocationSet(){
        mSharedPreferences.edit().clear().commit();
        onView(withId(R.id.locationTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(withText(Constants.CITY_NOT_SET)));
    }

    @Test
    public void testLocationTextViewDisplaysLocation(){
        String location = "testLocation";
        onView(withId(R.id.locationTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(withText(location)));
    }

    @Test
    public void testLocationIsVisible(){
        onView(withId(R.id.newLocationButton))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()));
        onView(withId(R.id.locationTextView))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testTypeOfPainIsVisible(){
        String type = "dull";
        onView(withId(R.id.typeOfPainSpinner))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is(type))).perform(click());
        onView(withId(R.id.typeOfPainSpinner))
                .check(matches(withSpinnerText(containsString(type))));
        onView(withId(R.id.typeOfPainTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testAreasOfPainIsVisible(){
        String area = "eye";
        onView(withId(R.id.areasOfPainSpinner))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is(area))).perform(click());
        onView(withId(R.id.areasOfPainSpinner))
                .check(matches(withSpinnerText(containsString(area))));
        onView(withId(R.id.areaOfPainTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testTypeOfMedsIsVisible(){
        String medication = "Excedrin";
        onView(withId(R.id.typeOfMedsSpinner))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is(medication))).perform(click());
        onView(withId(R.id.typeOfMedsSpinner))
                .check(matches(withSpinnerText(containsString(medication))));
        onView(withId(R.id.typeOfMedsTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSymptomsIsVisible(){
        onView(withId(R.id.symptomsTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
        onView(withId(R.id.nauseaCheckBox))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .check(matches(ViewMatchers.isNotChecked()));
        onView(withId(R.id.sensitivityLightCheckBox))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .check(matches(ViewMatchers.isNotChecked()));
        onView(withId(R.id.sensitivityNoiseCheckBox))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .check(matches(ViewMatchers.isNotChecked()));
        onView(withId(R.id.sensitivitySmellCheckBox))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .check(matches(ViewMatchers.isNotChecked()));
        onView(withId(R.id.nasalCongestionCheckBox))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .check(matches(ViewMatchers.isNotChecked()));
        onView(withId(R.id.earsCheckBox))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .check(matches(ViewMatchers.isNotChecked()));
        onView(withId(R.id.confusionCheckBox))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .check(matches(ViewMatchers.isNotChecked()));
    }

    @Test
    public void testsToastDisplayedWhenDateNotSet(){
        onView(withId(R.id.saveRecordButton))
                .perform(ViewActions.scrollTo())
                .perform(click());
        onView(withText(R.string.dateTimeError))
                .inRoot(withDecorView(not(mMainActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testsToastDisplayedWhenCityNotSet(){
        onView(withId(R.id.saveRecordButton))
                .perform(ViewActions.scrollTo())
                .perform(click());
        onView(withText(R.string.error_city_not_set))
                .inRoot(withDecorView(not(mMainActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @After
    public void cleanUp(){
        mSharedPreferences.edit().clear().commit();
    }
}
