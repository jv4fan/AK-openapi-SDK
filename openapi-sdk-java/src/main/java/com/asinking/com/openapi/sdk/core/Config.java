package com.asinking.com.openapi.sdk.core;

public class Config {

    private int connectTimeout;

    private int readTimeout;

    private int maxConnections;

    public static final Config DEFAULT = new Config();

    /**
     * Sets the connection timeout in milliseconds
     *
     * @param connectTimeout timeout in milliseconds
     * @return Config
     */
    public Config withConnectionTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * Sets the read timeout in milliseconds
     *
     * @param readTimeout timeout in milliseconds
     * @return Config
     */
    public Config withReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    /**
     * This sets the max allowed connections for connectors who are using a connection pool.  This option if set will be
     * a no-op to connectors that don't offer this setting.
     *
     * @param maxConnections the max connections allowed
     * @return Config
     */
    public Config withMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getMaxConnections() {
        return maxConnections;
    }
}
