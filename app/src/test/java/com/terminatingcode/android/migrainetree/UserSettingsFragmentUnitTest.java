package com.terminatingcode.android.migrainetree;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;


/**
 * Unit Test for UserSettingsFragment
 * Created by Sarah on 7/5/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21)
public class UserSettingsFragmentUnitTest {

    private UserSettingsFragment mUserSettingsFragment;
    private MainActivity mActivity;


    @Before
    public void setUp() throws Exception {
        mActivity = Robolectric.setupActivity(MainActivity.class);
        mUserSettingsFragment = new UserSettingsFragment();
        SupportFragmentTestUtil.startFragment(mUserSettingsFragment);
    }

    @Test
    public void testValuesNotNull() {
        Assert.assertNotNull(mActivity);
        Assert.assertNotNull(mUserSettingsFragment);
    }

    @Test
    public void testOnMessageEventSetCities() throws Exception {

    }

    @Test
    public void testStartGeoLookupService() throws Exception {

    }
}