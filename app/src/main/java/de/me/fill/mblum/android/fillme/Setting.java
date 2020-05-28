package de.me.fill.mblum.android.fillme;

class Setting {
    private int id;
    private String name;
    private String value;
    private String[] choices;

    /**
     * Constructor excluding id.
     *
     * @param name    Setting name
     * @param value   Value of setting
     * @param choices Possible choices for value
     */
    Setting(String name, String value, String[] choices) {
        this.name = name;
        this.value = value;
        this.choices = choices;
    }

    /**
     * Constructor including id.
     *
     * @param id      object id
     * @param name    Setting name
     * @param value   Value of setting
     * @param choices Possible choices for value
     */
    Setting(int id, String name, String value, String[] choices) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.choices = choices;
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    String[] getChoices() {
        return choices;
    }
}
