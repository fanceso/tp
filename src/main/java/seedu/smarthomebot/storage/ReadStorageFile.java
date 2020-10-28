package seedu.smarthomebot.storage;

import seedu.smarthomebot.commons.Messages;
import seedu.smarthomebot.commons.exceptions.DuplicateDataException;
import seedu.smarthomebot.data.appliance.ApplianceList;
import seedu.smarthomebot.data.appliance.type.AirConditioner;
import seedu.smarthomebot.data.appliance.type.Fan;
import seedu.smarthomebot.data.appliance.type.Lights;
import seedu.smarthomebot.data.appliance.type.SmartPlug;
import seedu.smarthomebot.data.location.LocationList;
import seedu.smarthomebot.logic.commands.exceptions.InvalidApplianceNameException;
import seedu.smarthomebot.logic.commands.exceptions.LocationNotFoundException;
import seedu.smarthomebot.storage.exceptions.FileCorruptedException;
import seedu.smarthomebot.ui.TextUi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static seedu.smarthomebot.commons.Messages.MESSAGE_APPLIANCE_TYPE_NOT_EXIST;

public class ReadStorageFile extends StorageFile {

    private static final String FILE_PATH = "data/SmartHomeBot.txt";
    private final TextUi ui = new TextUi();

    public ReadStorageFile(ApplianceList applianceList, LocationList locationList) {
        super(applianceList, locationList);
    }

    @Override
    public void execute() {
        try {
            assert FILE_PATH.equals("data/SmartHomeBot.txt") : "filePath should be data/SmartHome.txt";
            int i = 0;
            File myFile = new File(FILE_PATH);
            Scanner myReader = new Scanner(myFile);
            String locationList = myReader.nextLine();
            try {
                readToLocationList(locationList);
                readToApplianceList(i, myReader);
                ui.printToUser(Messages.MESSAGE_IMPORT);
            } catch (FileCorruptedException e) {
                ui.printToUser(Messages.MESSAGE_FILE_CORRUPTED);
            }

            myReader.close();
        } catch (FileNotFoundException | DuplicateDataException e) {
            ui.printToUser("Load File does not exist. No contents will be loaded.");
        }
    }

    private void readToApplianceList(int i, Scanner myReader) throws FileCorruptedException, DuplicateDataException {
        while (myReader.hasNextLine()) {
            try {
                String appliance = myReader.nextLine();
                String[] splitString = appliance.split("\\|", 9);
                if (splitString[1].isEmpty() || splitString[0].isEmpty()
                        || splitString[2].isEmpty() || splitString[3].isEmpty()) {
                    throw new FileCorruptedException();
                }
                String name = splitString[1];
                String location = splitString[0];
                String power = splitString[2];
                String type = splitString[3];
                String powerConsumption = splitString[4];
                String parameter = splitString[5];

                switch (type.toLowerCase()) {
                case Fan.TYPE_WORD:
                    Fan fan = new Fan(name, location, power, locationList);
                    applianceList.addAppliance(fan);
                    fan.getSpeedFromLoadFile(parameter);
                    break;
                case AirConditioner.TYPE_WORD:
                    AirConditioner ac = new AirConditioner(name, location, power, locationList);
                    applianceList.addAppliance(ac);
                    ac.getTemperatureFromLoadFile(parameter);
                    break;
                case Lights.TYPE_WORD:
                    Lights light = new Lights(name, location, power, locationList);
                    applianceList.addAppliance(light);
                    break;
                case SmartPlug.TYPE_WORD:
                    SmartPlug smartPlug = new SmartPlug(name, location, power, locationList);
                    applianceList.addAppliance(smartPlug);
                    break;
                default:
                    ui.printToUser(MESSAGE_APPLIANCE_TYPE_NOT_EXIST);
                }
                // when user exit, get the current system and save in datafile
                applianceList.getAppliance(i).loadDataFromFile(powerConsumption);
                i++;
            } catch (IndexOutOfBoundsException | InvalidApplianceNameException | LocationNotFoundException e) {
                throw new FileCorruptedException();
            }
        }
    }

    private void readToLocationList(String location) throws FileCorruptedException {
        try {
            int openBracesIndex = location.indexOf("[") + 1;
            int closeBracesIndex = location.indexOf("]");
            String locations = location.substring(openBracesIndex, closeBracesIndex);
            String[] stringSplit = locations.split(",");
            for (String locationName : stringSplit) {
                if (!locationName.isEmpty()) {
                    locationList.addLocation(locationName.trim());
                }
            }
        } catch (IndexOutOfBoundsException | DuplicateDataException e) {
            throw new FileCorruptedException();
        }

    }
}
