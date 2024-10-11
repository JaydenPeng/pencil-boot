package org.pencil.redis;

import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;

/**
 * The interface Consumer listener.
 *
 * @author pencil
 * @date 24 /10/11 20:30
 */
public interface ConsumerListener extends StreamListener<String, ObjectRecord<String, String>> {

    /**
     * Topic string.
     *
     * @return the string
     */
    String topic();

    /**
     * Group string.
     *
     * @return the string
     */
    String group();
}
