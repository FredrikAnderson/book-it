package com.anderson.bookit.ui;

import java.util.Collection;

public interface Lookup<T> {

	Collection<T> lookup(String prefix);
}


