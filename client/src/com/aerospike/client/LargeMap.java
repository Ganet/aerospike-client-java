package com.aerospike.client;

import java.util.Map;

import com.aerospike.client.policy.Policy;

/**
 * Create and manage a map within a single bin.
 */
public final class LargeMap {
	private static final String PackageName = "lmap";
	
	private final AerospikeClient client;
	private final Policy policy;
	private final Key key;
	private final Value binName;
	private final Value userModule;
	
	/**
	 * Initialize large map operator.
	 * 
	 * @param client				client
	 * @param policy				generic configuration parameters, pass in null for defaults
	 * @param key					unique record identifier
	 * @param binName				bin name
	 * @param userModule			Lua function name that initializes list configuration parameters, pass null for default set
	 */
	public LargeMap(AerospikeClient client, Policy policy, Key key, String binName, String userModule) {
		this.client = client;
		this.policy = policy;
		this.key = key;
		this.binName = Value.get(binName);
		this.userModule = Value.get(userModule);
	}
	
	/**
	 * Add entry to map.  If the map does not exist, create it using specified userModule configuration.
	 * 
	 * @param name				entry key
	 * @param value				entry value
	 */
	public final void put(Value name, Value value) throws AerospikeException {
		client.execute(policy, key, PackageName, "put", binName, name, value, userModule);
	}

	/**
	 * Add map values to map.  If the map does not exist, create it using specified userModule configuration.
	 * 
	 * @param map				map values to push
	 */
	public final void put(Map<?,?> map) throws AerospikeException {
		client.execute(policy, key, PackageName, "put_all", binName, Value.getAsMap(map), userModule);
	}
	
	/**
	 * Get value from map given name key.
	 * 
	 * @param name				key.
	 * @return					map of items selected
	 */
	public final Map<?,?> get(Value name) throws AerospikeException {
		return (Map<?,?>)client.execute(policy, key, PackageName, "get", binName, name);
	}

	/**
	 * Return all objects in the map.
	 */
	public final Map<?,?> scan() throws AerospikeException {
		return (Map<?,?>)client.execute(policy, key, PackageName, "scan", binName);
	}

	/**
	 * Select items from map.
	 * 
	 * @param filterName		Lua function name which applies filter to returned list
	 * @param filterArgs		arguments to Lua function name
	 * @return					list of items selected
	 */
	public final Map<?,?> filter(String filterName, Value... filterArgs) throws AerospikeException {
		return (Map<?,?>)client.execute(policy, key, PackageName, "filter", binName, userModule, Value.get(filterName), Value.get(filterArgs));
	}

	/**
	 * Delete bin containing the map.
	 */
	public final void destroy() throws AerospikeException {
		client.execute(policy, key, PackageName, "destroy", binName);
	}

	/**
	 * Return size of map.
	 */
	public final int size() throws AerospikeException {
		return (Integer)client.execute(policy, key, PackageName, "size", binName);
	}

	/**
	 * Return map configuration parameters.
	 */
	public final Map<?,?> getConfig() throws AerospikeException {
		return (Map<?,?>)client.execute(policy, key, PackageName, "config", binName);
	}
	
	/**
	 * Set maximum number of entries for the map.
	 *  
	 * @param capacity			max entries in set
	 */
	public final void setCapacity(int capacity) throws AerospikeException {
		client.execute(policy, key, PackageName, "set_capacity", binName, Value.get(capacity));
	}

	/**
	 * Return maximum number of entries for the map.
	 */
	public final int getCapacity() throws AerospikeException {
		return (Integer)client.execute(policy, key, PackageName, "get_capacity", binName);
	}
}
