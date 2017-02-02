package util.option;

import java.util.Arrays;

import static util.option.Options.ErrorMessage.*;

public class Option {
    private final String name;
    private final OptionType type;
    private boolean isSet;

    private int numberParam;
    private String textParam;
    private String[] validValues = new String[0];

    Option(String name) {
        this(name, OptionType.Boolean);
    }

    private Option(String name, OptionType type) {
        this.name = name;
        this.type = type;
    }

    Option(String name, OptionType type, int defaultValue) {
        this.name = name;
        this.type = type;
        setParam(defaultValue);
    }

    Option(String name, OptionType type, String... validValues) {
        this(name, type);

        if (validValues.length == 0) {
            throw new RuntimeException(NO_VALID_VALUES);
        }

        this.validValues = validValues;
        setParam(validValues[0]);
    }

    public String getName() {
        return name;
    }

    boolean isUsed() {
        return isSet;
    }

    public void setUsed(boolean state) {
        isSet = state;
    }

    public final void setParam(String value) {
        if (this.type != OptionType.Text) {
            throw new RuntimeException(TEXT_TO_NON_TEXT);
        }
        if (!Arrays.asList(validValues).contains(value)) {
            throw new RuntimeException(String.format(NOT_VAlID_VALUE_FMT, value, this.name));
        }
        textParam = value;
    }

    public final void setParam(int value) {
        if (this.type != OptionType.Number) {
            throw new RuntimeException(NUM_TO_NOT_NUM);
        }
        numberParam = value;
    }

    String getTextParam() {
        if (this.type != OptionType.Text) {
            throw new RuntimeException(TEXT_FROM_NON_TEXT);
        }
        return textParam;
    }

    int getNumberParam() {
        if (type != OptionType.Number) {
            throw new RuntimeException(NUM_FROM_NON_NUM);
        }
        return this.numberParam;
    }

    public String getValue() {
        switch (this.type) {
            case Text:
                return this.getTextParam();
            case Number:
                return String.valueOf(getNumberParam());
            case Boolean:
            default:
                return String.valueOf(isUsed());
        }
    }

    boolean isNumberOption() {
        return type == OptionType.Number;
    }

    boolean isTextOption() {
        return type == OptionType.Text;
    }

    @Override
    public String toString() {
        return String.format("(%b) %s: %s", this.isUsed(), this.getName(), this.getValue());
    }
}
