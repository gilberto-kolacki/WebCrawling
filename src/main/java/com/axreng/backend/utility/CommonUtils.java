package com.axreng.backend.utility;

import com.axreng.backend.Config;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    public static final Set<String> SUPPORTED_URI_SCHEMES = Set.of("http", "https");

    private static final String URL_REGEX = "^((https?://)(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)([).!';/?:,][[:blank:]])?$";

    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    private CommonUtils() {
    }

    public static boolean isNotNull(final Object value) {
        return value != null && !value.equals("");
    }

    public static boolean isNull(final Object value) {
        return value == null;
    }

    /**
     * Se todos os valores forem diferente de nulo retorna true
     *
     * @param values
     * @return
     */
    public static boolean isValuesNotNull(final Object... values) {
        if (isEmpty(values)) {
            return false;
        }
        for (Object value : values) {
            if (isNull(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Se todos os valores forem diferente de nulo retorna true
     *
     * @param values
     * @return
     */
    public static boolean isValuesContainsNull(final Object... values) {
        if (isEmpty(values)) {
            return true;
        }
        for (Object value : values) {
            if (isNull(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(final Object[] value) {
        return value == null || value.length == 0 || (value.length == 1 && value[0] == null);
    }

    public static String convertDate(Date data) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(data);
    }

    public static boolean isNotEmpty(final String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isNumeric(final String value) {
        try {
            if (isNull(value)) {
                return false;
            }
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isValidURI(String uri) {
        final URL url;
        try {
            url = new URL(uri);
        } catch (Exception ex) {
            return false;
        }
        return "http".equals(url.getProtocol());
    }

    public static boolean isValidKEYWORD(String keyword) {
        return isNotNull(keyword) && keyword.length() >=4 && keyword.length() <=32 && isLetterOrDigit(keyword);
    }

    public static boolean isLetterOrDigit(String value) {
        boolean result = true;
        char[] valueArray = value.toCharArray();
        for (char c : valueArray) {
            if (!Character.isLetterOrDigit(c)) {
                result = false;
                break;
            }
        }
        return result;
    }

    public static URI absoluteUriWithValidScheme(String value) {
        try {
            URI result = new URI(value);
            if (!result.isAbsolute() || !SUPPORTED_URI_SCHEMES.contains(result.getScheme())) {
                throw new IllegalArgumentException(String.format("Invalid environment variable URL: '%1s'", value));
            }
            return result;
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid environment variable value: BASE_URL");
        }
    }

    public static boolean urlValidator(String url) {
        if (isNull(url)) return false;
        if (Set.of(".css", ".js", ".gif").contains(url.substring(url.length() - 4))) return false;
        if (url.contains("../")) return false;
        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }

}
