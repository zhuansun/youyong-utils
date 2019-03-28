package com.posoup.string;

import com.posoup.array.ArrayUtils;
import com.posoup.collection.CollectionUtils;
import com.posoup.statis.ConstArrays;
import com.posoup.statis.ConstCharacters;
import com.posoup.statis.ConstStrings;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author zhuansunpengcheng
 */
public class StringUtils {

    private static Random RANDOM = new Random(System.currentTimeMillis());

    /**
     * 字符串截取
     *
     * @param source 待截取字符串
     * @param size   保留截取长度
     * @param suffix 多余部门字符替换
     */
    public static String truncate(String source, Integer size, String suffix) {
        if (source != null) {
            String s = substring(source, size == null ? source.length() : size);
            if (s.length() < source.length()) {
                return s + suffix;
            }
            return s;
        }
        return ConstStrings.EMPTY;
    }

    /**
     * 字符串截取
     *
     * @param source 源字符串
     * @param size   截取长度
     */
    public static String substring(String source, int size) {
        return substring(source, 0, size);
    }

    /**
     * 字符串截取
     *
     * @param source 源字符串
     * @param offset 字符截取位子
     * @param size   截取长度
     */
    public static String substring(String source, int offset, int size) {
        if (source == null || size < 1 || offset < 0) {
            return "";
        }

        if (source.length() <= (offset + size)) {
            return source;
        }

        return source.substring(offset, size);
    }

    /**
     * 字符串是否不为空
     *
     * @param str
     */
    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }

    /**
     * 字符串是否为空
     *
     * @param str
     */
    public static boolean isEmpty(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串为null时赋""值，不为null则返回本身
     *
     * @param value 字符串
     */
    public static String nullToEmpty(String value) {
        return value == null ? ConstStrings.EMPTY : value;
    }

    /**
     * 判断字符串不为空
     *
     * @param values 多字符串
     */
    public static boolean isNotEmpty(String... values) {
        if (values != null && values.length > 0) {
            for (String value : values) {
                if (value == null || value.length() == 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 字符串为空时赋默认值，不为空则返回本身
     *
     * @param value 字符串
     * @param def   默认值
     */
    public static String emptyToDefault(String value, String def) {
        return (value == null || value.length() == 0) ? def : value;
    }

    /**
     * 过滤表情等特殊符号
     *
     * @param source
     */
    public static String escapeEmoji(String source) {
        if (StringUtils.isNotEmpty(source)) {
            StringBuilder buff = new StringBuilder(source.length());
            for (char codePoint : source.toCharArray()) {
                if (((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                        || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                        || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)))) {
                    buff.append(codePoint);
                }
            }
            return buff.toString();
        }
        return source;
    }

    /**
     * 字符串转义
     *
     * @param s 源字符串
     * @param c 转义字符
     */
    public static String decode(String s, char c) {
        if (s != null && s.length() > 0) {
            char[] cs = s.toCharArray();
            StringBuilder buff = new StringBuilder(cs.length);
            for (int i = 0, n = cs.length; i < n; i++) {
                if (!(cs[i] == ConstCharacters.BACKSLASH && i < n - 1 && cs[i + 1] == c)) {
                    buff.append(cs[i]);
                }
            }
            return buff.toString();
        }
        return s;
    }

    public static String decode(String s, char... chars) {
        if (s != null && s.length() > 0 && chars != null) {
            Arrays.sort(chars);
            char[] cs = s.toCharArray();
            StringBuilder buff = new StringBuilder(cs.length);
            for (int i = 0, n = cs.length; i < n; i++) {
                if (!(cs[i] == ConstCharacters.BACKSLASH && i < n - 1 && Arrays.binarySearch(chars, cs[i + 1]) >= 0)) {
                    buff.append(cs[i]);
                }
            }
            return buff.toString();
        }
        return s;
    }

    /**
     * 字符串加密
     *
     * @param s
     * @param chars
     */
    public static String encode(String s, char... chars) {
        if (s != null && s.length() > 0 && chars != null) {
            Arrays.sort(chars);
            char[] cs = s.toCharArray();
            StringBuilder sb = new StringBuilder(cs.length + 10);
            for (int i = 0; i < cs.length; i++) {
                if (Arrays.binarySearch(chars, cs[i]) >= 0) {
                    sb.append(ConstCharacters.BACKSLASH);
                }
                sb.append(cs[i]);
            }
            return sb.toString();
        }
        return s;
    }

    /**
     * 生成随机字符串
     *
     * @param length 字符串长度
     */
    public static String random(int length) {
        return random(length, ConstArrays.RANDOM_SEEDS);
    }

    /**
     * 生成随机字符串
     *
     * @param length  字符串长度
     * @param options 随机字符范围数组
     */
    public static String random(final int length, char[] options) {
        if (length > 1) {
            int maxLength = 0;
            if (ArrayUtils.isEmpty(options)) {
                options = ConstArrays.BASE64;
                maxLength = 62;
            } else {
                maxLength = options.length;
            }
            char[] buff = new char[length];
            for (int i = 0; i < length; i++) {
                buff[i] = options[RANDOM.nextInt(maxLength)];
            }
            return new String(buff);
        }

        return ConstStrings.EMPTY;
    }

    /**
     * 字符串累加
     *
     * @param values
     */
    public static String join(String... values) {
        return concate(values, ConstStrings.EMPTY);
    }

    /**
     * 字符串数组间添加固定字符串进行累加
     *
     * @param values    字符串数组
     * @param delimiter 字符串间隔间添加的字符
     */
    public static String concate(String[] values, String delimiter) {
        if (values != null && values.length > 0) {
            if (delimiter == null) {
                delimiter = ConstStrings.EMPTY;
            }
            int length = 0;
            for (String value : values) {
                if (value != null) {
                    length += (value.length() + delimiter.length());
                }
            }

            StringBuilder buff = new StringBuilder(length);
            for (String value : values) {
                if (isNotEmpty(value)) {
                    if (buff.length() > 0) {
                        buff.append(delimiter);
                    }
                    buff.append(value);
                }
            }

            return buff.toString();
        }

        return ConstStrings.EMPTY;
    }

    /**
     * 判断字符串中第几位开始包含指定字符串
     *
     * @param source
     * @param sub
     * @param offset 第几位开始
     */
    public static boolean contains(String source, String sub, int offset) {
        return isNotEmpty(source) && source.indexOf(sub, offset) >= offset;
    }

    /**
     * 字符串标准化处理
     *
     * @param value
     */
    public static String normalize(String value) {
        if (isNotEmpty(value)) {
            return value.toLowerCase().trim();
        }

        return ConstStrings.EMPTY;
    }

    public static String sqlLike(String value) {
        if (StringUtils.isNotEmpty(value)) {
            StringBuilder builder = new StringBuilder();
            builder.append("%");
            builder.append(value);
            builder.append("%");
            return builder.toString();

        }

        return ConstStrings.EMPTY;
    }


    /**
     * 过滤非utf－8编码字符
     *
     * @param text
     * @throws java.io.UnsupportedEncodingException
     */
    public static String filterOffUtf8Mb4(String text) {
        try {
            byte[] bytes = text.getBytes("UTF-8");
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            int i = 0;
            while (i < bytes.length) {
                short b = bytes[i];
                if (b > 0) {
                    buffer.put(bytes[i++]);
                    continue;
                }
                b += 256;
                if ((b ^ 0xC0) >> 4 == 0) {
                    buffer.put(bytes, i, 2);
                    i += 2;
                } else if ((b ^ 0xE0) >> 4 == 0) {
                    buffer.put(bytes, i, 3);
                    i += 3;
                } else if ((b ^ 0xF0) >> 4 == 0) {
                    i += 4;
                }
            }
            buffer.flip();
            return new String(buffer.array(), "utf-8").trim();
        } catch (Exception e) {
            return ConstStrings.EMPTY;
        }
    }


    public static final char UNDERLINE = '_';

    /**
     * 驼峰转下划线写法
     * @param param
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰写法
     * @param param
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 通过循环对象数组参数列表(中间用分隔符相隔)，获取新的字符串
     * @param separator 分隔符 如："_"
     * @param arrays 如：String "abc", String "ddd"
     * @return 获取新的字符串 如： "abc_ddd"
     * @author liuting
     */
    public static String getStringByArraySplitSeparator(String separator, Object[] arrays) {
        if (isStringEmpty(separator) || arrays == null || arrays.length == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("");
        for (Object val : arrays) {
            if (val != null) {
                stringBuilder.append(val).append(separator);
            }
        }
        String targetVar = stringBuilder.toString();
        if (isStringEmpty(targetVar)) {
            return targetVar;
        }
        return targetVar.substring(0, stringBuilder.length() - separator.length());
    }

    public static boolean isStringEmpty(String str){
        if(null == str || "".equals(str.trim())){
            return true;
        }
        return false;
    }

    /**
     * map value是jsonstring 避免转化加转译字符
     * @param map
     */
    public static String translateMapToString(Map<String, String> map) {

        if (CollectionUtils.isEmpty(map)) {
            return "";
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        Set<Map.Entry<String, String>> entrySet =  map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            buffer.append("\"").append(entry.getKey()).append("\":").append(entry.getValue()).append(",");
        }
        buffer.deleteCharAt(buffer.length() - 1).append("}");

        return buffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(camelToUnderline("updatedAt"));
        System.out.println(underlineToCamel("updated_at"));
    }


}
