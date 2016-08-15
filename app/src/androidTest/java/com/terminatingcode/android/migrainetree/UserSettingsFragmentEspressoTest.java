package com.terminatingcode.android.migrainetree;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumentation Test for UserSettingsFragment
 * Created by Sarah on 7/3/2016.
 */
public class UserSettingsFragmentEspressoTest {

    private Fragment mFragment;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp(){
        mFragment = new UserSettingsFragment();
        mMainActivityActivityTestRule
                .getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_frame, mFragment, "testUserSettings")
                .commit();
    }

    @Test
    public void testFragmentIsVisible(){
        mFragment.isVisible();
    }

    @Test
    public void testTextViewsAreVisible(){
        onView(withId(R.id.displayCity))
                .check(matches(isDisplayed()));

        onView(withId(R.id.location))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testMenstrualPrefCheckboxIsVisibleAndChecked(){
        onView(withId(R.id.useMenstrualDataCheckBox))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .check(matches(isChecked()));

    }
}
