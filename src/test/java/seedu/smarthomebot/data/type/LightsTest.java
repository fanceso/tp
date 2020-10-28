package seedu.smarthomebot.data.type;

import org.junit.jupiter.api.Test;
import seedu.smarthomebot.data.framework.type.Lights;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LightsTest {

    @Test
    void getType_nullInput_returnsLight() {
        assertEquals("light", new Lights("light 1", "bedroom 1", "200").getType());
    }

    //    @Test
    //    void toString_nullInput_returnsName_and_status() {
    //        assertEquals("light 1(200W) in bedroom 1", new Lights("light 1", "bedroom 1", "200").toString());
    //    }

}
