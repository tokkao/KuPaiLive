package com.benben.commoncore.utils;

/**
 * 功能:char处理工具类
 * 作者：zjn on 2017/4/20 14:14
 */

public class CharSequenceUtils {

        public CharSequenceUtils() {
        }

        public static CharSequence subSequence(CharSequence cs, int start) {
            return cs == null?null:cs.subSequence(start, cs.length());
        }

        static int indexOf(CharSequence cs, int searchChar, int start) {
            if(cs instanceof String) {
                return ((String)cs).indexOf(searchChar, start);
            } else {
                int sz = cs.length();
                if(start < 0) {
                    start = 0;
                }

                for(int i = start; i < sz; ++i) {
                    if(cs.charAt(i) == searchChar) {
                        return i;
                    }
                }

                return -1;
            }
        }

        static int indexOf(CharSequence cs, CharSequence searchChar, int start) {
            return cs.toString().indexOf(searchChar.toString(), start);
        }

        static int lastIndexOf(CharSequence cs, int searchChar, int start) {
            if(cs instanceof String) {
                return ((String)cs).lastIndexOf(searchChar, start);
            } else {
                int sz = cs.length();
                if(start < 0) {
                    return -1;
                } else {
                    if(start >= sz) {
                        start = sz - 1;
                    }

                    for(int i = start; i >= 0; --i) {
                        if(cs.charAt(i) == searchChar) {
                            return i;
                        }
                    }

                    return -1;
                }
            }
        }

        static int lastIndexOf(CharSequence cs, CharSequence searchChar, int start) {
            return cs.toString().lastIndexOf(searchChar.toString(), start);
        }

        static char[] toCharArray(CharSequence cs) {
            if(cs instanceof String) {
                return ((String)cs).toCharArray();
            } else {
                int sz = cs.length();
                char[] array = new char[cs.length()];

                for(int i = 0; i < sz; ++i) {
                    array[i] = cs.charAt(i);
                }

                return array;
            }
        }

        static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start, int length) {
            return cs instanceof String && substring instanceof String?((String)cs).regionMatches(ignoreCase, thisStart, (String)substring, start, length):cs.toString().regionMatches(ignoreCase, thisStart, substring.toString(), start, length);
        }
}