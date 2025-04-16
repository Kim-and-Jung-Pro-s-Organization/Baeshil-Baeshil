package pro.baeshilbaeshil.application.infra.local_cache.message_subscriber;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessageSubscriberImpl implements MessageSubscriber {

    private final RedisMessageListenerContainer redisMessageListenerContainer;

    private final RedisTemplate<String, String> redisTemplate;

    public void addMessageListener(String channel, MessageListener messageListener) {
        redisMessageListenerContainer.addMessageListener(
                messageListener,
                new ChannelTopic(channel));
    }

    @Override
    public String parseMessage(byte[] message) {
        return (String) redisTemplate.getValueSerializer().deserialize(message);
    }
}
