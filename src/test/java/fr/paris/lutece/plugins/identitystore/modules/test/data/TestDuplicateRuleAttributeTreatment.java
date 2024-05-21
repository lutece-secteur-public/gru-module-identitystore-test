package fr.paris.lutece.plugins.identitystore.modules.test.data;

import java.util.ArrayList;
import java.util.List;

public class TestDuplicateRuleAttributeTreatment {
    private List<String> attributes = new ArrayList<>( );
    private String type;

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
