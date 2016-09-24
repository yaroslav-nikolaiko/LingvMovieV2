package lingvo.movie;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by yaroslav on 20.09.16.
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AbstractTest {
    protected static final Long adminID=1L;
    protected static final Long userID=2L;
}
