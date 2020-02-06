package util.option;

import java.util.Optional;

class OptionLiteral {
    private String name;
    private String param;

    OptionLiteral(String name) {
        this.name = name;
        this.param = null;
    }

    OptionLiteral(String name, String param) {
        this.name = name;
        this.param = param;
    }

    String getName() {
        return name;
    }

    Optional<String> getParam() {
        return Optional.ofNullable(param);
    }
}
