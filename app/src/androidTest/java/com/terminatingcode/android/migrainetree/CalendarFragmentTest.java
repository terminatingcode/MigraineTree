package com.terminatingcode.android.migrainetree;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumentation Test for CalendarFragment
 * Created by Sarah on 7/21/2016.
 */
public class CalendarFragmentTest {
    private android.support.v4.app.Fragment mFragment;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp(){
        mFragment = new CalendarFragment();
        mMainActivityActivityTestRule
                .getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_frame, mFragment, "testCalendar")
                .commit();
    }

    @Test
    public void testFragmentIsVisible(){
        mFragment.isVisible();
    }

    @Test
    public void testCalendarIsVisible(){
        onView(withId(R.id.calendar_view))
                .check(matches(isDisplayed()));
    }

}