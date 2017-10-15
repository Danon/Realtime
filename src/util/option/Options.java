package util.option;

public class Options {
    private final Option[] options = {
            new Option("-Debug"),
            new Option("-Display", OptionType.Text, "Window", "Fullscreen"),
            new Option("-View", OptionType.Text, "Fit", "Stretch", "Normal"),
            new Option("-IgnorePassword"),
            new Option("-TCPPort", OptionType.Number, 33455),
            new Option("-UDPPort", OptionType.Number, 33456)
    };

    boolean exists(String optionName) {
        return indexOfOption(optionName) > -1;
    }

    private int indexOfOption(String optionName) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].getName().equals(optionName)) {
                return i;
            }
        }
        return -1;
    }

    public Option getOptionByName(String optionName) {
        int index = indexOfOption(optionName);
        if (index == -1) {
            throw new NoSuchOptionException(optionName);
        }
        return options[index];
    }

    public void setOptions(String[] runArguments) {
        OptionParser parser = new OptionParser(this);
        for (String option : runArguments) {
            parser.parseOptionExpression(option);
        }
    }

    public boolean isUsed(String optionName) {
        return getOptionByName(optionName).isUsed();
    }

    public boolean isUsedWithValue(String optionName, String param) {
        Option option = getOptionByName(optionName);
        return option.isUsed() && option.getTextParam().equals(param);
    }

    public int getNumber(String optionName) {
        return getOptionByName(optionName).getNumberParam();
    }

    public String getText(String optionName) {
        return getOptionByName(optionName).getTextParam();
    }

    static class ErrorMessage {
        final static String TEXT_TO_NON_TEXT = "Assigned text value to a non-text option.";
        final static String NUM_TO_NOT_NUM = "Assigned number value to non-number option.";
        final static String NOT_VAlID_VALUE_FMT = "Param \"%s\" is not a valid value of option \"%s\".";
        final static String TEXT_FROM_NON_TEXT = "Tried to read text value from a non-text option.";
        final static String NUM_FROM_NON_NUM = "Tried to read number value from a non-number option.";
        final static String NO_OPTION_NAME_FMT = "No option name in expression: \"%s\".";
        final static String NO_PARAM_FMT = "No param in expression: \"%s\".";
        final static String UNKNOWN_OPTION_FMT = "No such run option: \"%s\".";
        final static String NO_ARGUMENTS_EXPECTED_FMT = "Option \"%s\" doesn't take any arguments.";
        final static String INTEGER_EXPECTED_STRING_GIVEN_FMT = "Option \"%s\" takes an integer as argument; (string given: \"%s\").";
        final static String NO_VALID_VALUES = "There must be at least one additional valid value.";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Option option : this.options) {
            sb.append(option.toString()).append("\n");
        }
        return sb.toString();
    }
}
