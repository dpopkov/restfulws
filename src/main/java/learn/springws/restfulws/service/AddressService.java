package learn.springws.restfulws.service;

import learn.springws.restfulws.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> getAddresses(String userPublicId);
}
