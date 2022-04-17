package learn.springws.restfulws.shared;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles({"h2"})
@SpringBootTest
class UtilsIT {

    @Autowired
    Utils utils;

    @Test
    void generatePublicId() {
        final String publicId1 = utils.generatePublicId();
        final String publicId2 = utils.generatePublicId();
        assertNotNull(publicId1);
        assertNotNull(publicId2);
        final int idLength = 32;
        assertEquals(idLength, publicId1.length());
        assertEquals(idLength, publicId2.length());
        assertNotEquals(publicId1, publicId2);
    }

    @Test
    void generateUserId() {
        final int idLength = 16;
        String userId1 = utils.generateUserId(idLength);
        String userId2 = utils.generateUserId(idLength);
        assertNotNull(userId1);
        assertNotNull(userId2);
        assertEquals(idLength, userId1.length());
        assertEquals(idLength, userId2.length());
        assertNotEquals(userId1, userId2);
    }
}
