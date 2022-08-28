package com.gtss.mnp_manager.utils;

import java.util.Comparator;

/**
 * Compare Objects with possible equality
 */
public interface ComparatorWithEquals<T> extends Comparator<T> {
    public boolean equals(T o1, T o2);
}
