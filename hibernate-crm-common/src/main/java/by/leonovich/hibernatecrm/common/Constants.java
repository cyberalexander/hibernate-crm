/*
 * MIT License
 *
 * Copyright (c) 2021 Aliaksandr Leanovich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package by.leonovich.hibernatecrm.common;

/**
 * Created : 27/12/2020 11:47
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public record Constants () {
    public static final String UPDATE_PREFIX = "_UPD_";
    public static final String UPDATE_CASCADE_PREFIX = "_UPD_CASCADE_";

    public record Numbers () {
        public static final int THREE = 3;
        public static final int FOUR = 4;
        public static final int FIVE = 5;
        public static final int SIX = 6;
        public static final int EIGHT = 8;
        public static final int TEN = 10;
        public static final int TWELVE = 12;
        public static final int FIFTEEN = 15;
        public static final int ONE_HUNDRED = 100;
        public static final int ONE_HUNDRED_ONE = 100;
        public static final int ONE_THOUSAND = 100;
        public static final int ONE_HUNDRED_THOUSAND = 100_000;
        public static final long TEN_MILLION = 10_000_000L;
        public static final long ONE_BILLION = 1_000_000_000L;
    }
}
