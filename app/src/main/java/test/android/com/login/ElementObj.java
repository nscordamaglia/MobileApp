package test.android.com.login;

import java.util.ArrayList;

/**
 * Clase que da forma a los elementos de un formulario
 */
public class ElementObj {

    private String label;
    private String value;
    private String type;
    private String id;
    private String comment;
    private String path;
    private boolean notsave;
    private boolean required;
    private boolean numeric;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isNotsave() {
        return notsave;
    }

    public void setNotsave(boolean notsave) {
        this.notsave = notsave;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isNumeric() {
        return numeric;
    }

    public void setNumeric(boolean numeric) {
        this.numeric = numeric;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ElementObj() {
        this.numeric = false;
        this.required = false;
        this.comment = "empty";
    }
}
