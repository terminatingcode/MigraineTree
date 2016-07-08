package com.terminatingcode.android.migrainetree;

import android.support.test.espresso.action.ViewActions;
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
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

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
                .check(matches(isDisplayed()));
        onView(withId(R.id.stressLevelTextView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.stressSeekBar))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testStressSeekBarUpdatesTextView(){
        onView(withId(R.id.stressSeekBar)).perform(EspressoActions.setProgress(10));
        onView(allOf(withId(R.id.stressLevelTextView), withText("10"))).check(matches(isDisplayed()));
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
    public void testEyeStrainSeekBarUpdatesTextView(){
        onView(withId(R.id.eyesSeekBar)).perform(EspressoActions.setProgress(10));
        onView(allOf(withId(R.id.eyesLevelTextView), withText("10"))).check(matches(isDisplayed()));
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
        onView(withId(R.id.typeOfPainSpinner))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isClickable()));
        onView(withId(R.id.typeOfPainTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testAreasOfPainIsVisible(){
        onView(withId(R.id.areasOfPainSpinner))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isClickable()));
        onView(withId(R.id.areaOfPainTextView))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testTypeOfMedsIsVisible(){
        onView(withId(R.id.typeOfMedsSpinner))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()))
                .check(matches(isClickable()));
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
}
