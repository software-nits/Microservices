package com.software.consumerservice.utils;

import com.software.consumerservice.dto.OrderEvent;
import org.apache.commons.lang.SerializationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class CustomDeserializerTest {

    @Test
    @DisplayName("Returns null when deserializing null data")
    void returnsNullWhenDeserializingNullData() {
        OrderEvent result;
        try (CustomDeserializer deserializer = new CustomDeserializer()) {
            result = deserializer.deserialize("test-topic", null);
        }
        assertNull(result);
    }

    @Test
    @DisplayName("Deserializes valid OrderEvent JSON successfully")
    void deserializesValidOrderEventJsonSuccessfully() {
        OrderEvent result;
        try (CustomDeserializer deserializer = new CustomDeserializer()) {
            String json = "{\"quantity\":5}";
            byte[] data = json.getBytes(StandardCharsets.UTF_8);
            result = deserializer.deserialize("test-topic", data);
        }
        assertNotNull(result);
        assertEquals(5, result.getQuantity());
    }

    @Test
    @DisplayName("Throws SerializationException for invalid JSON")
    void throwsSerializationExceptionForInvalidJson() {
        try (CustomDeserializer deserializer = new CustomDeserializer()) {
            byte[] data = "invalid-json".getBytes(StandardCharsets.UTF_8);
            assertThrows(SerializationException.class, () -> deserializer.deserialize("test-topic", data));
        }
    }
}
