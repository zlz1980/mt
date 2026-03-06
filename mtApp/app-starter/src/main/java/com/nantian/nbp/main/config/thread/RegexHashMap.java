package com.nantian.nbp.main.config.thread;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexHashMap<T> implements Map<String, T> {

	private final Map<String, T> container = new ConcurrentHashMap<>();

	@Override
	public void clear() {
		container.clear();
	}

	/**
	 * 是否包含此key
	 * @param key 键
	 * @return boolean
	 * @see Map#containsKey(Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		if (container.containsKey(key)) {
            return true;
        }
        for (String s : container.keySet()) {
            if (Pattern.matches(s, (String) key)) {
                return true;
            }
        }

		return false;
	}

	@Override
	public boolean containsValue(Object value) {
        return container.containsValue(value);
    }

	@Override
	public Set<Entry<String, T>> entrySet() {
        return new HashSet<>(container.entrySet());
	}

	@Override
	public T get(Object key) {
		if (key == null) {
            return null;
        }
		T result;
		if ((result = container.get(key)) != null) {
			return result;
		} else {
            for (Entry<String, T> entry : container.entrySet()) {
                Pattern p = Pattern.compile(entry.getKey());
                Matcher m = p.matcher((String) key);
                if (m.find()) {
                    return entry.getValue();
                }
            }
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return container.isEmpty();
	}

	@Override
	public Set<String> keySet() {
        return new HashSet<>(container.keySet());
	}

	@Override
	public T put(String key, T value) {
		return container.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends T> m) {
		container.putAll(m);
	}

	@Override
	public T remove(Object key) {
		return container.remove(key);
	}

	@Override
	public int size() {
		return container.size();
	}

	@Override
	public Collection<T> values() {
        return new HashSet<>(container.values());
	}
}
