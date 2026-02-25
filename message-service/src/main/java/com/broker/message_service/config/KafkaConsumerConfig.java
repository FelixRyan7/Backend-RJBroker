package com.broker.message_service.config;

import com.broker.message_service.event.TradeEvent;
import com.broker.message_service.event.WalletEvent;
import com.broker.message_service.repository.MessageRepository;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {


    private final String bootstrapServers = "localhost:9092";
    private final String groupId = "message-service-group";

    @Bean
    public ConsumerFactory<String, WalletEvent> walletConsumerFactory() {
        JacksonJsonDeserializer<WalletEvent> deserializer =
                new JacksonJsonDeserializer<>(WalletEvent.class);
        deserializer.addTrustedPackages("*");
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "message-service-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WalletEvent> walletKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, WalletEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(walletConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, TradeEvent> tradeConsumerFactory() {
        JacksonJsonDeserializer<TradeEvent> deserializer =
                new JacksonJsonDeserializer<>(TradeEvent.class);
        deserializer.addTrustedPackages("*");
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "message-service-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TradeEvent> tradeKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TradeEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(tradeConsumerFactory());
        return factory;
    }
}
