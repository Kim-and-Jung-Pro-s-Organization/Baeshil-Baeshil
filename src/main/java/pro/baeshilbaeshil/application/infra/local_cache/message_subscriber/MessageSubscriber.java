package pro.baeshilbaeshil.application.infra.local_cache.message_subscriber;

import org.springframework.data.redis.connection.MessageListener;

public interface MessageSubscriber {

    void addMessageListener(String channel, MessageListener listener);
}
