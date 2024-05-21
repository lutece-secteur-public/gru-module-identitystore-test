package fr.paris.lutece.plugins.identitystore.modules.test.data;

import java.util.ArrayList;
import java.util.List;

public class TestDuplicateRule {
    private String name;
    private String code;
    private List<String> checkedAttributes = new ArrayList<>( );
    private int nbFilledAttributes;
    private int nbEqualAttributes;
    private int nbMissingAttributes;
    private List<TestDuplicateRuleAttributeTreatment> listAttributeTreatments = new ArrayList<>( );
    private int priority;
    private boolean active;
    private boolean daemon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getCheckedAttributes() {
        return checkedAttributes;
    }

    public void setCheckedAttributes(List<String> checkedAttributes) {
        this.checkedAttributes = checkedAttributes;
    }

    public int getNbFilledAttributes() {
        return nbFilledAttributes;
    }

    public void setNbFilledAttributes(int nbFilledAttributes) {
        this.nbFilledAttributes = nbFilledAttributes;
    }

    public int getNbEqualAttributes() {
        return nbEqualAttributes;
    }

    public void setNbEqualAttributes(int nbEqualAttributes) {
        this.nbEqualAttributes = nbEqualAttributes;
    }

    public int getNbMissingAttributes() {
        return nbMissingAttributes;
    }

    public void setNbMissingAttributes(int nbMissingAttributes) {
        this.nbMissingAttributes = nbMissingAttributes;
    }

    public List<TestDuplicateRuleAttributeTreatment> getListAttributeTreatments() {
        return listAttributeTreatments;
    }

    public void setListAttributeTreatments(List<TestDuplicateRuleAttributeTreatment> listAttributeTreatments) {
        this.listAttributeTreatments = listAttributeTreatments;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }
}
