package seedu.smarthomebot.data.appliance;

import seedu.smarthomebot.ui.TextUi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import static seedu.smarthomebot.commons.Messages.LINE;
import static seedu.smarthomebot.commons.Messages.MESSAGE_TIME_FORMAT_ERROR;

/**
 * Class representing the power consumption of appliances.
 */

public class Power {

    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy-HH:mm:ss";
    private final int wattage;
    private String offTime;
    private String onTime;
    private Boolean status;
    private double powerUsed;
    private double totalPowerConsumption;

    public Power(String wattage) {
        this.wattage = Integer.parseInt(wattage);
        this.status = false;
        this.totalPowerConsumption = 0.00;
    }

    /**
     * Gets the status of the appliance.
     *
     * @return the status of the appliance in Boolean.
     */
    public Boolean getStatus() {
        return this.status;
    }

    /**
     * Appliance only can be switched on if it was 'off' previously.
     *
     * @return true if appliance has not been turned on before.
     */
    public boolean onAppliance() {
        if (!this.status) {
            // if Appliance has not been turned ON
            this.status = true;
            this.onTime = getCurrentTime();
            return true;
        } else {
            // if Appliance has been turned ON previously
            return false;
        }
    }

    /**
     * Appliance only can be switched off if it was 'on' previously.
     * Compute the total power consumption once appliance is off.
     *
     * @return true if appliance has not been turned off before.
     */
    public boolean offAppliance() {
        if (this.status) {
            // if Appliance has not been turned OFF
            this.status = false;
            this.offTime = getCurrentTime();
            computeTotalPower();
            return true;
        } else {
            // if Appliance has been turned OFF previously
            return false;
        }
    }

    /**
     * Reset all the appliance power usage and the total power consumption.
     */
    public void resetPower() {
        this.powerUsed = 0.00;
        this.totalPowerConsumption = 0.00;
    }

    /**
     * Gets the power consumption of appliance.
     *
     * @return the power consumption in double.
     */
    public double getPower() {
        computeTotalPower();
        return this.totalPowerConsumption;
    }

    /**
     * Gets the current system time from local machine.
     *
     * @return formatted current time with given format "dd/MM/yyyy-HH:mm:ss".
     */
    private String getCurrentTime() {
        LocalDateTime currentTime;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.ENGLISH);
        currentTime = LocalDateTime.now();
        return dateTimeFormatter.format(currentTime);
    }

    /**
     * Computes power consumption from loaded file.
     *
     * @param powerConsumption of each appliance used within a day
     */
    public void loadDataFromFile(double powerConsumption) {
        this.powerUsed = powerConsumption;
        this.totalPowerConsumption += this.powerUsed;
    }

    /**
     * Calculate time used while is appliance on.
     */
    private double calculateTimeUsed() throws ParseException {
        double totalHours;
        SimpleDateFormat timeFormat = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.ENGLISH);
        Date onTimeValue;
        Date offTimeValue;
        double millisecondsUsed = 0;

        if (this.onTime != null) {
            onTimeValue = timeFormat.parse(this.onTime);
            if (!this.status) {
                offTimeValue = timeFormat.parse(this.offTime);
                millisecondsUsed = offTimeValue.getTime() - onTimeValue.getTime();
                // System time cannot be negative time
                assert millisecondsUsed >= 0 : "System Time is not correct! " + millisecondsUsed;
                this.onTime = this.offTime;
            } else {
                Date currentUsedTime = timeFormat.parse(getCurrentTime());
                millisecondsUsed = currentUsedTime.getTime() - onTimeValue.getTime();
                this.onTime = getCurrentTime();
            }
        }

        totalHours = millisecondsUsed / (1000 * 60 * 60);
        return totalHours;
    }

    private void computePower() {
        try {
            double totalTimeUsed = calculateTimeUsed();
            // Convert power unit to kWh
            this.powerUsed = totalTimeUsed * this.wattage / 1000.00;
        } catch (ParseException e) {
            TextUi ui = new TextUi();
            ui.printToUser(LINE + MESSAGE_TIME_FORMAT_ERROR);
        }
        assert this.powerUsed >= 0 : "Power usage cannot be negative! " + this.powerUsed;
    }

    private void computeTotalPower() {
        computePower();
        // Rounding off to 2 decimal places
        this.totalPowerConsumption += Math.round(this.powerUsed * 100.0) / 100.0;
        assert this.totalPowerConsumption >= 0 : "Total power consumption cannot be negative";
    }

    /**
     * Gets the power consumption in String.
     *
     * @return the power consumption with only 2 decimal places in String.
     */
    public String toString() {
        return String.format("%.2f", this.totalPowerConsumption);
    }

}
