package id.renner.web.library.json;

import java.util.Stack;

public class JsonWriter {
    private final StringBuilder builder;
    private final Stack<ElementState> elementStateStack;
    private final boolean prettyPrint;

    public JsonWriter(boolean prettyPrint) {
        this.builder = new StringBuilder();
        this.elementStateStack = new Stack<>();
        this.prettyPrint = prettyPrint;

        nextLevel();
    }

    public void startElement() {
        if (!elementStateStack.empty()) {
            handlePropertySeparation();
        }
        handlePrettyPrint();
        nextLevel();
        builder.append('{');
    }

    public void startList() {
        if (elementStateStack.peek().hasProperty) {
            throw new RuntimeException("invalid");
        }
        handlePropertySeparation();
        handlePrettyPrint();
        builder.append('[');
        nextLevel();
    }

    public void putPropertyKey(String key) {
        putProperty(key, null);
    }

    public void endElement() {
        previousLevel();
        handlePrettyPrint();
        builder.append('}');
    }

    public void endList() {
        previousLevel();
        handlePrettyPrint();
        builder.append(']');
    }

    public void putProperty(String key, String value) {
        handlePropertySeparation();
        handlePrettyPrint();

        builder.append('\"')
                .append(key)
                .append("\":");

        if (prettyPrint) {
            builder.append(' ');
        }

        if (value == null) {
            builder.append("{");
            nextLevel();
        } else {
            builder.append('\"')
                    .append(value)
                    .append('\"');
        }
    }

    private void nextLevel() {
        elementStateStack.push(new ElementState());
    }

    private void previousLevel() {
        elementStateStack.pop();
    }

    private void handlePropertySeparation() {
        ElementState elementState = elementStateStack.peek();
        if (elementState.hasProperty()) {
            builder.append(',');
        } else {
            elementState.setHasProperty();
        }
    }

    private void handlePrettyPrint() {
        if (prettyPrint) {
            builder.append('\n');
            builder.append(" ".repeat(elementStateStack.size() - 1));
        }
    }

    public String getString() {
        validate();
        return builder.toString();
    }

    private void validate() {
        if (elementStateStack.size() != 1) {
            throw new RuntimeException("element not finished");
        }
    }

    class ElementState {
        private boolean hasProperty = false;

        public boolean hasProperty() {
            return hasProperty;
        }

        public void setHasProperty() {
            hasProperty = true;
        }
    }
}