package fr.paris.lutece.plugins.identitystore.modules.test.data;

import java.util.ArrayList;
import java.util.List;

public class TestDuplicateRuleAttributeTreatment {
    private List<String> attributeKeys = new ArrayList<>( );
    private String type;

    public List<String> getAttributeKeys() {
        return attributeKeys;
    }

    public void setAttributeKeys(List<String> attributeKeys) {
        this.attributeKeys = attributeKeys;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
