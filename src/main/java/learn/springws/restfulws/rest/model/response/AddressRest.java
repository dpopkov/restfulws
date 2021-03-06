package learn.springws.restfulws.rest.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class AddressRest extends RepresentationModel<AddressRest> {
    private String publicId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
}
