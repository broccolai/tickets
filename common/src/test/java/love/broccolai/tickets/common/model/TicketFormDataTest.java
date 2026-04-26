package love.broccolai.tickets.common.model;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import love.broccolai.tickets.api.model.format.TicketFormData;
import love.broccolai.tickets.common.serialization.gson.TicketFormDataAdapter;
import love.broccolai.tickets.common.utilities.PremadeTicketTypeRegistry;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TicketFormDataTest {

    @Test
    void getsTypedContent() {
        TicketFormData data = TicketFormData.empty("question")
            .with("message", "How do I claim this ticket?");

        assertThat(data.require("message", String.class)).isEqualTo("How do I claim this ticket?");
    }

    @Test
    void rejectsWrongContentType() {
        TicketFormData data = TicketFormData.empty("question")
            .with("message", "How do I claim this ticket?");

        assertThrows(ClassCastException.class, () -> data.require("message", Integer.class));
    }

    @Test
    void exposesReadOnlyParts() {
        TicketFormData data = TicketFormData.empty("question")
            .with("message", "How do I claim this ticket?");

        assertThrows(UnsupportedOperationException.class, () -> data.parts().put("other", "value"));
    }

    @Test
    void exposesSnapshotParts() {
        TicketFormData data = TicketFormData.empty("question")
            .with("message", "How do I claim this ticket?");

        Map<String, Object> parts = data.parts();
        TicketFormData updated = data.with("other", "value");

        assertThat(parts).doesNotContainKey("other");
        assertThat(updated.parts()).containsKey("other");
    }

    @Test
    void readsPartsBeforeFormatIdentifier() throws IOException {
        TicketFormDataAdapter adapter = new TicketFormDataAdapter(PremadeTicketTypeRegistry.create());
        JsonReader reader = new JsonReader(new StringReader("""
            {
                "parts": {
                    "message": "How do I claim this ticket?"
                },
                "formatIdentifier": "question"
            }
            """));

        TicketFormData data = adapter.read(reader);

        assertThat(data.formatIdentifier()).isEqualTo("question");
        assertThat(data.require("message", String.class)).isEqualTo("How do I claim this ticket?");
    }
}
