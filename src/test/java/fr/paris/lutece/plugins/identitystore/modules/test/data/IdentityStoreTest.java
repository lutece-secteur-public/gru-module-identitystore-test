package fr.paris.lutece.plugins.identitystore.modules.test.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class IdentityStoreTest {

    @JsonProperty("$schema")
    private String schema;

    @JsonProperty("testDefinition")
    private TestDefinition testDefinition;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public TestDefinition getTestDefinition() {
        return testDefinition;
    }

    public void setTestDefinition(TestDefinition testDefinition) {
        this.testDefinition = testDefinition;
    }
}
