package org.example;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class JsonTransformConsumer {
    private static final String TOPIC_INPUT = "input_topic";
    private static final String TOPIC_OUTPUT = "output_topic";

    public void startConsuming() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:29092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "json-transform-group");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(TOPIC_INPUT));

        KafkaProducer<String, String> producer = createKafkaProducer();
        JsonFileWriter fileWriter = new JsonFileWriter("users.txt");

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                String inputJson = record.value();
                fileWriter.writeToJsonFile(inputJson);

                String transformedJson = TransformUtil.transformJson(inputJson);
                produceTransformedMessage(producer, transformedJson);
            }
        }
    }

    private KafkaProducer<String, String> createKafkaProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:29092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaProducer<>(properties);
    }

    private void produceTransformedMessage(KafkaProducer<String, String> producer, String transformedJson) {
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_OUTPUT, transformedJson);

        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                    System.out.println("Message sent successfully");
                }
            }
        });
    }
}


