package guru.springframework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class InlineMockTest {

    @Test
    @DisplayName("Inline Mock Test")
    void testInlineMock() {
        Map mapMock = mock(Map.class);

        assertEquals(mapMock.size(), 0);
    }
}
