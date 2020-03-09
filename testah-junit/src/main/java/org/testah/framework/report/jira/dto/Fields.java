
package org.testah.framework.report.jira.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status"
})
public class Fields {

    @JsonProperty("status")
    private FieldsStatus status;

    @JsonProperty("status")
    public FieldsStatus getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(FieldsStatus status) {
        this.status = status;
    }

}
