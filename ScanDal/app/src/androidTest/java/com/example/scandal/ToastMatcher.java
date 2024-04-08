package com.example.scandal;

import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.test.espresso.Root;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ToastMatcher extends TypeSafeMatcher<Root> {

    @Override
    public void describeTo(Description description) {
        description.appendText("is toast");
    }

    @Override
    public boolean matchesSafely(Root root) {
        if (root.getWindowLayoutParams().get().type == WindowManager.LayoutParams.TYPE_TOAST) {
            final View decorView = root.getDecorView();
            return decorView.getWindowToken() != null && decorView.getWindowToken() == decorView.getApplicationWindowToken();
        }
        return false;
    }

    public static Matcher<Root> isToast() {
        return new ToastMatcher();
    }

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
