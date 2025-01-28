package pro.baeshilbaeshil.config.local_cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisSubscriber {

    private final RedisMessageListenerContainer redisMessageListenerContainer;

    public void addMessageListener(String channel, MessageListener messageListener) {
        redisMessageListenerContainer.addMessageListener(
                messageListener,
                new ChannelTopic(channel));
    }
}
