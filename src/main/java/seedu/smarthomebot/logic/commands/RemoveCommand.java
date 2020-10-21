package seedu.smarthomebot.logic.commands;

import seedu.smarthomebot.commons.exceptions.ApplianceNotFoundException;
import seedu.smarthomebot.commons.exceptions.InvalidRemovalLocationException;

import static seedu.smarthomebot.commons.Messages.LINE;
import static seedu.smarthomebot.commons.Messages.MESSAGE_APPLIANCE_NOT_EXIST;
import static seedu.smarthomebot.commons.Messages.MESSAGE_LOCATION_NOT_EXIST;

public class RemoveCommand extends Command {

    public static final String COMMAND_WORD = "remove";
    public static final String MESSAGE_USAGE = "Remove location: " + COMMAND_WORD
            + " [LOCATION_NAME]";
    private final String usersEnteredLocation;

    public RemoveCommand(String location) {
        this.usersEnteredLocation = location;
    }

    @Override
    public CommandResult execute() {
        try {
            locationList.removeLocation(this.usersEnteredLocation);
            applianceList.deleteByLocation(this.usersEnteredLocation);
            return new CommandResult(LINE + "Removing LOCATION \"" + this.usersEnteredLocation + "\"......REMOVED!");
        } catch (InvalidRemovalLocationException e) {
            return new CommandResult(MESSAGE_LOCATION_NOT_EXIST + " Nothing will be deleted.");
        } catch (ApplianceNotFoundException e) {
            return new CommandResult(MESSAGE_APPLIANCE_NOT_EXIST + " Nothing will be deleted.");
        }
    }
}