package learn.springws.restfulws.rest.model.response;

import learn.springws.restfulws.rest.model.OperationName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperationStatus {
    private OperationResult result;
    private OperationName operationName;
}
