package lingvo.movie.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.function.Predicate;

/**
 * Created by yaroslav on 20.09.16.
 */
public class SimpleMatcher extends BaseMatcher<String> {
    Predicate<Object> matcher;

    public SimpleMatcher(Predicate<Object> matcher) {
        this.matcher = matcher;
    }

    public static SimpleMatcher matcher(Predicate<Object> matcher){
        return new SimpleMatcher(matcher);
    }


    @Override
    public boolean matches(Object item) {
        return matcher.test(item);
    }

    @Override
    public void describeTo(Description description) {

    }
}
