package seedu.duke;

import seedu.duke.commands.AddCommand;
import seedu.duke.commands.Command;
import seedu.duke.commands.CreateCommand;
import seedu.duke.commands.DeleteCommand;
import seedu.duke.commands.ExitCommand;
import seedu.duke.commands.HelpCommand;
import seedu.duke.commands.InvalidCommand;
import seedu.duke.commands.ListCommand;
import seedu.duke.commands.OffCommand;
import seedu.duke.commands.OnCommand;
import seedu.duke.commands.RemoveCommand;
import seedu.duke.commands.UsageCommand;
import seedu.duke.exceptions.EmptyParameterException;
import seedu.duke.exceptions.InvalidPowerNumber;
import seedu.duke.exceptions.PowerValueExceed;
import seedu.duke.ui.TextUi;

import static seedu.duke.common.Messages.MESSAGE_INVALID_ADD_COMMAND;
import static seedu.duke.common.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.duke.common.Messages.MESSAGE_INVALID_LIST_COMMAND;
import static seedu.duke.common.Messages.MESSAGE_POWER_EXCEEDED;
import static seedu.duke.common.Messages.MESSAGE_POWER_NOT_NUMBER;


public class Parser {

    private static final String APPLIANCE_TYPE = "appliance";
    private static final String LOCATION_TYPE = "location";
    private static TextUi ui = new TextUi();

    public static Command parseCommand(String userInput) {
        String[] words = userInput.trim().split(" ", 2);
        final String commandWord = words[0];
        final String arguments = userInput.replaceFirst(commandWord, "").trim();
        try {
            switch (commandWord) {
            case HelpCommand.COMMAND_WORD:
                return new HelpCommand();
            case CreateCommand.COMMAND_WORD:
                return new CreateCommand(arguments, true);
            case RemoveCommand.COMMAND_WORD:
                return new RemoveCommand(arguments);
            case AddCommand.COMMAND_WORD:
                return prepareAddCommand(arguments);
            case DeleteCommand.COMMAND_WORD:
                return new DeleteCommand(arguments);
            case OnCommand.COMMAND_WORD:
                return new OnCommand(arguments);
            case OffCommand.COMMAND_WORD:
                return new OffCommand(arguments);
            case ListCommand.COMMAND_WORD:
                return prepareListCommand(arguments);
            case UsageCommand.COMMAND_WORD:
                return new UsageCommand();
            case ExitCommand.COMMAND_WORD:
                return new ExitCommand();
            default:
                return new InvalidCommand(MESSAGE_INVALID_COMMAND_FORMAT);
            }
        } catch (EmptyParameterException e) {
            return new InvalidCommand("The parameter of " + commandWord + " cannot be empty.");
        }
    }

    private static Command prepareAddCommand(String arguments) {
        int indexLocation = arguments.indexOf("l/");
        int indexPower = arguments.indexOf("w/");
        int indexType = arguments.indexOf("t/");
        String name;
        String location;
        String power;
        String type;


        if (indexLocation < indexPower && indexPower < indexType) {
            name = arguments.substring(0, indexLocation).trim();
            location = arguments.substring(indexLocation + 2, indexPower).trim();
            power = arguments.substring(indexPower + 2, indexType).trim();
            type = arguments.substring(indexType + 2).toLowerCase().trim();
        } else {
            return new InvalidCommand(MESSAGE_INVALID_ADD_COMMAND);
        }

        try {
            testPowerValidity(power);
            return new AddCommand(name, location, power, type, true);
        } catch (InvalidPowerNumber e) {
            return new InvalidCommand(MESSAGE_POWER_NOT_NUMBER);
        } catch (PowerValueExceed e) {
            return new InvalidCommand(MESSAGE_POWER_EXCEEDED);
        }


    }

    private static Command prepareListCommand(String arguments) {
        if (arguments.equals(APPLIANCE_TYPE) || arguments.equals(LOCATION_TYPE)) {
            return new ListCommand(arguments);
        } else {
            return new InvalidCommand(MESSAGE_INVALID_LIST_COMMAND);
        }
    }

    private static void testPowerValidity(String power) throws PowerValueExceed, InvalidPowerNumber {
        try {
            int powerValue = Integer.parseInt(power);
            // Common appliance should not exceed 9999 watts
            if ((powerValue < 1) || (powerValue > 9999)) {
                throw new PowerValueExceed();
            }
        } catch (NumberFormatException e) {
            throw new InvalidPowerNumber();
        }
    }

}
