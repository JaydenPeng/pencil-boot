package org.pencil.redis;

/**
 * The interface Message sender.
 *
 * @author pencil
 * @date 24 /10/11 20:31
 */
public interface MessageSender {

    /**
     * Send broadcast.
     *
     * @param topic   the topic
     * @param message the message
     */
    void sendBroadcast(String topic, String message);


    /**
     * Send message.
     *
     * @param topic   the topic
     * @param group   the group
     * @param message the message
     */
    void sendMessage(String topic, String group, String message);
}
