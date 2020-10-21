package seedu.smarthomebot.commands;

import seedu.smarthomebot.data.framework.Appliance;

import java.util.ArrayList;

import static java.util.stream.Collectors.toList;
import static seedu.smarthomebot.common.Messages.LINE;
import static seedu.smarthomebot.common.Messages.MESSAGE_LIST_APPLIANCES;
import static seedu.smarthomebot.common.Messages.MESSAGE_LIST_NO_APPLIANCES;
import static seedu.smarthomebot.common.Messages.MESSAGE_LIST_NO_LOCATIONS;
import static seedu.smarthomebot.common.Messages.MESSAGE_LIST_LOCATIONS;

public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Display all the appliances that have been added to SmartHomeBot \n"
            + "Example: " + COMMAND_WORD;
    private static final String APPLIANCE_TYPE = "appliance";
    private static final String LOCATION_TYPE = "location";

    private static final String DISPLAY_LOCATION = " | Location: ";
    private static final String DISPLAY_STATUS = " | Status: ";
    private static final String DISPLAY_WATT = " | Watt: ";
    private static final String DISPLAY_TYPE = " | Type: ";
    private static final String DISPLAY_PARAMETER = " | Parameter:";

    private final String parameter;
    private final String filteredLocation;

    public ListCommand(String arguments,String filteredLocation) {
        this.parameter = arguments;
        this.filteredLocation = filteredLocation;
    }

    @Override
    public CommandResult execute() {
        int index = 1;
        switch (parameter) {
        case LOCATION_TYPE:
            if (locationList.getLocations().size() == 0) {
                return new CommandResult(LINE + MESSAGE_LIST_NO_LOCATIONS);
            }
            String outputLocationsList = LINE + MESSAGE_LIST_LOCATIONS;
            for (String location : locationList.getLocations()) {
                outputLocationsList = outputLocationsList.concat(System.lineSeparator() + index + ": " + location);
                index++;
            }
            return new CommandResult(outputLocationsList);
        case APPLIANCE_TYPE:
            if (filteredLocation.equals("")) {
                if (applianceList.getAllAppliance().size() == 0) {
                    return new CommandResult(LINE + MESSAGE_LIST_NO_APPLIANCES);
                }
                String header = (LINE + MESSAGE_LIST_APPLIANCES);
                String outputApplianceList = displayOutput(header, applianceList.getAllAppliance());
                return new CommandResult(outputApplianceList);
            } else {
                ArrayList<Appliance> filterApplianceList =
                        (ArrayList<Appliance>) applianceList.getAllAppliance().stream()
                        .filter((s) -> s.getLocation().equals(filteredLocation))
                        .collect(toList());

                if (filterApplianceList.isEmpty()) {
                    return new CommandResult("Location: \"" + filteredLocation + "\" does not exist.");
                }
                String header = (LINE + "Here are the appliances in \"" + filteredLocation + "\"");
                String outputFilteredList = displayOutput(header, filterApplianceList);
                return new CommandResult(outputFilteredList);
            }
        default:
            return new CommandResult("Error");
        }
    }

    private String displayOutput(String header, ArrayList<Appliance> displayList) {
        int index = 1;
        String outputList = header;
        String format = "%-2d. %-" + Appliance.getMaxNameLength() + "s"
                + DISPLAY_LOCATION + "%-" + Appliance.getMaxLocationLength() + "s"
                + DISPLAY_STATUS + "%-5s"
                + DISPLAY_WATT + "%-4sW"
                + DISPLAY_TYPE + "%s"
                + DISPLAY_PARAMETER + "%s";

        for (Appliance a : displayList) {
            outputList = outputList.concat(System.lineSeparator() + String.format(format, index,
                    a.getName(), a.getLocation(), a.getStatus(), a.getWattage(), a.getType(), a.getParameter(true)));
            index++;
        }

        return outputList;
    }

}
