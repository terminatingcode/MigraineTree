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
}
