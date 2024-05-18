package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String errorTitle;
    private String errorCode;
    private String errorMessage;


    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Create an ObjectNode representing the error result
            ObjectNode resultNode = mapper.createObjectNode();
            resultNode.put("errorTitle", errorTitle);
            resultNode.put("errorCode", errorCode);
            resultNode.put("errorMessage", errorMessage);

            // Convert ObjectNode to JSON string
            return mapper.writeValueAsString(resultNode);
        } catch (JsonProcessingException e) {
            // Handle potential JSON processing exceptions gracefully
            return "{\"success\":false, \"exception\"" + e.getMessage() + "\"}";
        }
    }
}
