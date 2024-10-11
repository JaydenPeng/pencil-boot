package org.pencil.redis;

import org.springframework.data.redis.connection.MessageListener;

/**
 * The interface Broadcast listener.
 *
 * @author pencil
 * @date 24 /10/11 20:30
 */
public interface BroadcastListener extends MessageListener {

    /**
     * Topic string.
     *
     * @return the string
     */
    String topic();
}
