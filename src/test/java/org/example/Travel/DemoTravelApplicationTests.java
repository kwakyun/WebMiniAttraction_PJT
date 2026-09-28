package org.example.Travel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:travel;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "public.api.serviceKey=",
        "kakao.maps.javascript-key="
})
class DemoTravelApplicationTests {

    @Test
    void contextLoads() {
    }

}
