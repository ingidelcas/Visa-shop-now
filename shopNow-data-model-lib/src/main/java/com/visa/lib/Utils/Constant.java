package com.visa.lib.Utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
        public static final String LOCAL_DATE_FORMAT = "dd-MM-yyyy";
        public static final String LOCAL_DATE_TIME_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
        public static final String ZONED_DATE_TIME_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
        public static final String INSTANT_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";

        public static final long LIFE_TIME = 86_400_000;
        public static final String HEADER = "Authorization";
        public static final String PREFIX = "Bearer ";
        public static final String SECRET_KEY = "rlMgCsN3sKhoIWb7e9lLwTaaN6oxgZfTQtNsBSBXi54";
}
