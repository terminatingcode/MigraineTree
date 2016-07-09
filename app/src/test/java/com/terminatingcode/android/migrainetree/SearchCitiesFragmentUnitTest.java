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
 * Unit Test for SearchCitiesFragment
 * Created by Sarah on 7/5/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21)
public class SearchCitiesFragmentUnitTest {

    private SearchCitiesFragment mSearchCitiesFragment;
    private MainActivity mActivity;


    @Before
    public void setUp() throws Exception {
        mActivity = Robolectric.setupActivity(MainActivity.class);
        mSearchCitiesFragment = new SearchCitiesFragment();
        SupportFragmentTestUtil.startFragment(mSearchCitiesFragment);
    }

    @Test
    public void testValuesNotNull() {
        Assert.assertNotNull(mActivity);
        Assert.assertNotNull(mSearchCitiesFragment);
    }

    @Test
    public void testOnMessageEventSetCities() throws Exception {

    }

    @Test
    public void testStartGeoLookupService() throws Exception {

    }
}