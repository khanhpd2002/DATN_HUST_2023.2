package vn.com.cardoctor.garage_service.utils;

import java.util.Objects;

public class TextUtil {
    private static final String BOLD_TEXT_FORMAT = "<b>%s</b>";

    public static String toBold(String str){
        if(!Objects.isNull(str)){
            return String.format(BOLD_TEXT_FORMAT, str);
        }
        return "";
    }

    public static boolean isNullOrEmpty(String str) {
        if (str == null || "".equalsIgnoreCase(str)) {
            return true;
        }
        return false;
    }
}
