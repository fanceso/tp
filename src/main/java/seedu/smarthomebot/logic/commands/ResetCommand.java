package seedu.smarthomebot.logic.commands;

import seedu.smarthomebot.data.appliance.Appliance;

import static seedu.smarthomebot.commons.Messages.LINE;
import static seedu.smarthomebot.commons.Messages.MESSAGE_LIST_NO_APPLIANCES;
import static seedu.smarthomebot.commons.Messages.MESSAGE_USAGE_RESET;

public class ResetCommand extends Command {

    public static final String COMMAND_WORD = "p_reset";
    public static final String MESSAGE_USAGE = "Resetting usage of appliances: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        if (applianceList.getAllAppliance().size() == 0) {
            return new CommandResult(LINE + MESSAGE_LIST_NO_APPLIANCES);
        } else {
            for (Appliance a : applianceList.getAllAppliance()) {
                a.resetPowerUsage();
            }
            return new CommandResult(LINE + MESSAGE_USAGE_RESET);
        }
    }

}
