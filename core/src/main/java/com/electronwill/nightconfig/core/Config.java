package com.electronwill.nightconfig.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.electronwill.nightconfig.core.utils.StringUtils.split;

/**
 * A (modifiable) configuration that contains key/value mappings. Configurations are generally
 * <b>not</b> thread-safe.
 *
 * @author TheElectronWill
 */
public interface Config extends UnmodifiableConfig {

	/**
	 * Sets a config value.
	 *
	 * @param path  the value's path, each part separated by a dot. Example "a.b.c"
	 * @param value the value to set
	 * @return the old value if any, or {@code null}
	 */
	default Object setValue(String path, Object value) {
		return setValue(split(path, '.'), value);
	}

	/**
	 * Sets a config value.
	 *
	 * @param path  the value's path, each element of the list is a different part of the path.
	 * @param value the value to set
	 * @return the old value if any, or {@code null}
	 */
	Object setValue(List<String> path, Object value);

	/**
	 * Removes a value from the config.
	 *
	 * @param path the value's path, each part separated by a dot. Example "a.b.c"
	 */
	default void removeValue(String path) {
		removeValue(split(path, '.'));
	}

	/**
	 * Removes a value from the config.
	 *
	 * @param path the value's path, each element of the list is a different part of the path.
	 */
	void removeValue(List<String> path);

	/**
	 * Removes all values from the config.
	 */
	void clear();

	/**
	 * Returns an Unmodifiable view of the config. Any change to the original (modifiable) config
	 * is still reflected to the returned UnmodifiableConfig, so it's unmodifiable but not
	 * immutable.
	 *
	 * @return an Unmodifiable view of the config.
	 */
	default UnmodifiableConfig unmodifiable() {
		return new UnmodifiableConfig() {
			@Override
			public <T> T getValue(List<String> path) {
				return Config.this.getValue(path);
			}

			@Override
			public boolean containsValue(List<String> path) {
				return Config.this.containsValue(path);
			}

			@Override
			public int size() {
				return Config.this.size();
			}

			@Override
			public Map<String, Object> valueMap() {
				return Collections.unmodifiableMap(Config.this.valueMap());
			}

			@Override
			public Set<? extends Entry> entrySet() {
				return Config.this.entrySet();
			}
		};
	}

	/**
	 * Checks if the given type is supported by this config. If the type is null, it checks if the
	 * config supports null values.
	 * <p>
	 * Please note that an implementation of the Config interface is <b>not</b> required to check
	 * the type of the values that you add to it.
	 *
	 * @param type the type's class, or {@code null} to check if the config supports null values
	 * @return {@code true} if it is supported, {@code false} if it isn't.
	 */
	boolean supportsType(Class<?> type);

	/**
	 * Returns a Map view of the config's values. Any change to the map is reflected in the config
	 * and vice-versa.
	 */
	Map<String, Object> valueMap();

	/**
	 * Returns a Set view of the config's entries. Any change to the set or to the entries is
	 * reflected in the config, and vice-versa.
	 */
	@Override
	Set<? extends Entry> entrySet();

	/**
	 * A modifiable config entry.
	 */
	interface Entry extends UnmodifiableConfig.Entry {
		/**
		 * Sets the entry's value.
		 *
		 * @param value the value to set
		 * @return the previous value
		 */
		Object setValue(Object value);
	}
}