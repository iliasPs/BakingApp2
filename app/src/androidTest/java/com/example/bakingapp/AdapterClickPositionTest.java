package com.example.bakingapp;



import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.espresso.contrib.RecyclerViewActions;



@RunWith(AndroidJUnit4.class)
public class AdapterClickPositionTest {

    @Rule
    public ActivityTestRule<ItemListActivity> activityTestRule = new ActivityTestRule<>(ItemListActivity.class);

    @Test
    public void clickItem(){
        onView(withId(R.id.recipeRV)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.stepTvShort)).check(matches((isDisplayed())));
    }
}
