package seedu.smarthomebot.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.smarthomebot.commons.exceptions.DuplicateDataException;
import seedu.smarthomebot.commons.exceptions.InvalidRemovalLocationException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocationListTest {

    private LocationList myHome;
    private ArrayList<String> expectedOutput;
    private ArrayList<String> emptyList;

    @BeforeEach
    public void setUp() throws Exception {
        myHome = new LocationList();
        myHome.addLocation("Living Room");
        expectedOutput = new ArrayList<>();
        expectedOutput.add("Living Room");
        emptyList = new ArrayList<>();
    }

    @Test
    void addLocation_locationNotInList_locationAddedNormally() throws DuplicateDataException {
        myHome.addLocation("MasterRoom");
        expectedOutput.add("MasterRoom");
        assertEquals(expectedOutput, myHome.getLocations());
    }

    @Test
    void addLocation_locationAlreadyInList_throwsDuplicateDataException() {
        assertThrows(DuplicateDataException.class, () -> myHome.addLocation("Living Room"));
    }

    @Test
    void removeLocation_locationInList_locationRemovedNormally() throws InvalidRemovalLocationException {
        myHome.removeLocation("Living Room");
        assertEquals(emptyList, myHome.getLocations());
    }

    @Test
    void removeLocation_locationNotExist_throws_InvalidRemovalLocationException() {
        assertThrows(InvalidRemovalLocationException.class, () -> myHome.removeLocation("Other Places"));
    }

    @Test
    void getLocations() {
        assertEquals(expectedOutput, myHome.getLocations());
    }

    @Test
    void testToString() {
        assertEquals("Living Room\n", myHome.toString());
    }

    @Test
    void isLocationCreated_locationInList_returnTrue() {
        assertTrue(myHome.isLocationCreated("Living Room"));
    }

}