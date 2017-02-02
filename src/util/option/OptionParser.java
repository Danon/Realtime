package util.option;

import util.Validate;

import java.util.Optional;

import static java.lang.String.format;
import static util.option.Options.ErrorMessage.*;

class OptionParser {
    private final Options options;

    OptionParser(Options options) {
        this.options = options;
    }

    void parseOptionExpression(String expression) {
        if (expression.startsWith(":")) {
            throw new RuntimeException(format(NO_OPTION_NAME_FMT, expression));
        }
        if (expression.endsWith(":")) {
            throw new RuntimeException(format(NO_PARAM_FMT, expression));
        }

        assignOption(parseOptionLiteral(expression));
    }

    private void assignOption(OptionLiteral literal) {
        if (!options.exists(literal.getName())) {
            throw new OptionException(format(UNKNOWN_OPTION_FMT, literal.getName()));
        }

        Option option = options.getOptionByName(literal.getName());
        option.setUsed(true);

        Optional<String> param = literal.getParam();
        if (param.isPresent()) {
            if (option.isTextOption()) {
                option.setParam(param.get());
            } else if (option.isNumberOption()) {
                if (Validate.isInt(param.get())) {
                    option.setParam(Integer.parseInt(param.get()));
                } else {
                    throw new RuntimeException(
                            format(INTEGER_EXPECTED_STRING_GIVEN_FMT, literal.getName(), literal.getParam()));
                }
            } else {
                throw new RuntimeException(
                        format(NO_ARGUMENTS_EXPECTED_FMT, literal.getName()));
            }
        }
    }

    private OptionLiteral parseOptionLiteral(String expression) {
        int colonIndex = expression.indexOf(':');
        if (colonIndex == -1) {
            return new OptionLiteral(expression);
        }
        return new OptionLiteral(
                expression.substring(0, colonIndex), expression.substring(colonIndex + 1)
        );
    }
}
