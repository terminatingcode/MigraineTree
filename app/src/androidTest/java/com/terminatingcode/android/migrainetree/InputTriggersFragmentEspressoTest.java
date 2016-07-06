package com.terminatingcode.android.migrainetree;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

// Tests for MainActivity

/**
 * Instrumentation Test for InputTriggerFragment
 * Created by Sarah on 6/30/2016.
 */
public class InputTriggersFragmentEspressoTest {

    private Fragment mFragment;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp(){
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
    public void testStartButtonIsVisibleAndClickable(){
        onView(withId(R.id.start_date_button))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()));
    }

    @Test
    public void testDatePickerSetButtonAreMadeVisibleByClickingStartButton(){
        onView(withId(R.id.datePicker)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.set_button)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.start_date_button)).perform(click());
        onView(withId(R.id.datePicker)).check(matches(isDisplayed())).check(matches(isClickable()));
        onView(withId(R.id.set_button)).check(matches(isDisplayed())).check(matches(isClickable()));
    }

    @Test
    public void testTimePickerMadeVisibleByClickingSetButton(){
        onView(withId(R.id.timePicker)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.start_date_button)).perform(click());
        onView(withId(R.id.timePicker)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.set_button)).perform(click());
        onView(withId(R.id.timePicker)).check(matches(isDisplayed())).check(matches(isClickable()));
    }

    @Test
    public void testTimePickerDisappearsByClickingSetButton(){
        onView(withId(R.id.start_date_button)).perform(click());
        onView(withId(R.id.set_button)).perform(click());
        onView(withId(R.id.timePicker)).check(matches(isDisplayed())).check(matches(isClickable()));
        onView(withId(R.id.set_button)).perform(click());
        onView(withId(R.id.timePicker)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
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
    public void testStressLevelIsVisible(){
        onView(withId(R.id.stressTextView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.stressLevelTextView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.stressSeekBar))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEyeStrainLevelIsVisible(){
        onView(withId(R.id.eyesTextView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.eyesLevelTextView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.eyesSeekBar))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLocationIsVisible(){
        onView(withId(R.id.correctLocationTextView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.locationTextView))
                .check(matches(isDisplayed()));
    }
}
