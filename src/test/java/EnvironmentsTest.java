import com.axreng.backend.utility.CommonUtils;
import org.junit.jupiter.api.Test;

import static com.axreng.backend.utility.CommonUtils.isNotNull;
import static com.axreng.backend.utility.CommonUtils.isNull;
import static com.axreng.backend.utility.CommonUtils.isNumeric;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EnvironmentsTest {

    @Test
    public void testURIIsValid() throws Exception {
        assertThat(CommonUtils.isValidURI(System.getenv("BASE_URL")), equalTo(true));
    }

    @Test
    public void testKeywordIsValid() throws Exception {
        assertThat(CommonUtils.isValidKEYWORD(System.getenv("KEYWORD")), equalTo(true));
    }

    @Test
    public void testMaxResultsIsValid() throws Exception {
        String STRING_MAX_RESULTS = System.getenv("MAX_RESULTS");
        assertThat((isNull(STRING_MAX_RESULTS) || (isNotNull(STRING_MAX_RESULTS) && isNumeric(STRING_MAX_RESULTS))), equalTo(true));
    }
}
