package com.tech.common.string;

import io.micrometer.common.util.StringUtils;
import org.apache.logging.log4j.util.Strings;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Non accent vietnamese util.
 */
public class NonAccentVietnameseUtil {

    /**
     * Dùng để loại bỏ dấu tiếng việt.
     *
     * @param value the value
     * @return Trả về một giá trị đã bỏ dấu tiếng việt
     */
    public static String nonAccentVietnamese(String value) {
        if (StringUtils.isBlank(value)) {
            return Strings.EMPTY;
        }

        for (Map.Entry<String, String> entry : initNonAccent().entrySet()) {
            Pattern pattern = Pattern.compile(entry.getKey());
            Matcher matcher = pattern.matcher(value);
            if (matcher.find()) {
                value = matcher.replaceAll(entry.getValue());
            }
        }

        return value;
    }

    private static Map<String, String> initNonAccent() {
        Map<String, String> map = new HashMap<>();
        map.put("A", "Á|À|Ả|Ã|Ạ|Â|Ẩ|Ấ|Ầ|Ẫ|Ậ|Ă|Ắ|Ằ|Ẵ|Ặ|Ẳ");
        map.put("a", "à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ");
        map.put("E", "É|È|Ẻ|Ẽ|Ẹ|Ê|Ế|Ề|Ễ|Ể|Ệ");
        map.put("e", "è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ");
        map.put("I", "Í|Ì|Ỉ|Ĩ|Ị");
        map.put("i", "ì|í|ị|ỉ|ĩ");
        map.put("O", "Ó|Ò|Ỏ|Õ|Ọ|Ô|Ố|Ồ|Ỗ|Ổ|Ộ|Ơ|Ớ|Ờ|Ỡ|Ở|Ợ");
        map.put("o", "ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ");
        map.put("U", "Ú|Ù|Ủ|Ũ|Ụ|Ư|Ứ|Ừ|Ữ|Ử|Ự");
        map.put("u", "ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ");
        map.put("Y", "Ỷ|Ý|Ỳ|Ỹ|Ỵ");
        map.put("y", "ỳ|ý|ỵ|ỷ|ỹ");
        map.put("D", "Đ");
        map.put("d", "đ");
        map.put("", "\u031B|\u0301|\u0303|\u0309|\u0323");
        return map;
    }
}
