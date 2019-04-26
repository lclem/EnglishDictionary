package com.example.EnglishDictionary;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.*;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.*;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DictionaryActivityTest {

    @Before
    public void setUp() {
    }

    @Before
    public void launchActivity() {
        ActivityScenario.launch(DictionaryActivity.class);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test1() {

    }

    @Test
    public void test2() throws Exception {
        //Espresso.onView(withId(R.id.edit_search)).perform(replaceText("ciao"), closeSoftKeyboard());
        //Thread.sleep(1000);
        //onView(withId(R.id.text_word)).check(matches(withText(containsString("ciao"))));
        //DataInteraction item = Espresso.onData(anything()).inAdapterView(withId(R.id.dictionary_entry_list)).atPosition(0);
        //item.perform(click());
        //Thread.sleep(2000);
    }

    /*
    @Test
    public void test2() {

        ViewInteraction editText = onView(
                allOf(withId(R.id.edit_search),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        editText.perform(replaceText("hello"), closeSoftKeyboard());

        ViewInteraction textView = onView(
                allOf(withId(R.id.text_word), withText("hello"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.dictionary_entry_list),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("hello")));

        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.dictionary_entry_list),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)))
                .atPosition(0);
        linearLayout.perform(click());

        ViewInteraction webView = onView(
                allOf(withId(R.id.webView),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        webView.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
    */
}