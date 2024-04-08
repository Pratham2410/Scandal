package com.example.scandal;

import android.view.View;
import android.view.WindowManager;

import androidx.test.espresso.Root;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Custom matcher for Espresso to match Toast messages.
 */
public class ToastMatcher extends TypeSafeMatcher<Root> {

    /**
     * Describes the matcher.
     *
     * @param description The description of the matcher
     */
    @Override
    public void describeTo(Description description) {
        description.appendText("is toast");
    }

    /**
     * Matches the given root if it represents a Toast message.
     *
     * @param root The root view to be matched
     * @return True if the root is a Toast, false otherwise
     */
    @Override
    public boolean matchesSafely(Root root) {
        if (root.getWindowLayoutParams().get().type == WindowManager.LayoutParams.TYPE_TOAST) {
            final View decorView = root.getDecorView();
            return decorView.getWindowToken() != null && decorView.getWindowToken() == decorView.getApplicationWindowToken();
        }
        return false;
    }

    /**
     * Creates a matcher for Toast messages.
     *
     * @return A Matcher for Toast messages
     */
    public static Matcher<Root> isToast() {
        return new ToastMatcher();
    }

    /**
     * Creates a matcher for Toast messages with the specified text.
     *
     * @param expectedText The expected text of the Toast message
     * @return A Matcher for Toast messages with the specified text
     */
    public static Matcher<CharSequence> withToastText(final String expectedText) {
        return new TypeSafeMatcher<CharSequence>() {
            @Override
            protected boolean matchesSafely(CharSequence item) {
                return item != null && item.toString().contains(expectedText);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with toast text: " + expectedText);
            }
        };
    }
}
