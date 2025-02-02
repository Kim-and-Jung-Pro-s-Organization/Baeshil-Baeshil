package pro.baeshilbaeshil.application.infra.local_cache.message_publisher;

public interface MessagePublisher {

    void publish(String channel, String message);
}
