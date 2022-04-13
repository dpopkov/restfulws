package learn.springws.restfulws.rest.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRest {
    private String publicId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
}
