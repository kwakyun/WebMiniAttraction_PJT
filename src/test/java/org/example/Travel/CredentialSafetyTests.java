package org.example.Travel;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Travel.location_image.PublicDataService;
import org.example.Travel.location_relation_title.PublicRelatedDataService;
import org.example.Travel.map.RestMapController;
import org.example.Travel.member.MemberVO;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CredentialSafetyTests {
    private static final String FAKE_SECRET = "test-only-not-a-real-credential";

    @Test
    void missingServerKeyFailsWithoutCallingExternalServices() {
        PublicDataService images = new PublicDataService();
        PublicRelatedDataService related = new PublicRelatedDataService();
        assertThrows(IllegalStateException.class, () -> images.getImageUrl("test"));
        assertThrows(IllegalStateException.class, () -> related.getTitle(List.of()));
    }

    @Test
    void imageFailureDoesNotRetainCredentialInExceptionChain() {
        PublicDataService service = new PublicDataService();
        ReflectionTestUtils.setField(service, "serviceKey", FAKE_SECRET);
        ReflectionTestUtils.setField(service, "apiUrl", "invalid-protocol://example.invalid/?q=1");
        RuntimeException error = assertThrows(RuntimeException.class, () -> service.getImageUrl("test"));
        StringWriter trace = new StringWriter();
        error.printStackTrace(new PrintWriter(trace));
        assertFalse(trace.toString().contains(FAKE_SECRET));
        assertNull(error.getCause());
    }

    @Test
    void relatedFailureDoesNotRetainCredentialInExceptionChain() {
        RuntimeException error = assertThrows(RuntimeException.class, () ->
                ReflectionTestUtils.invokeMethod(new PublicRelatedDataService(), "callApi",
                        "invalid-protocol://example.invalid/?serviceKey=" + FAKE_SECRET));
        StringWriter trace = new StringWriter();
        error.printStackTrace(new PrintWriter(trace));
        assertFalse(trace.toString().contains(FAKE_SECRET));
        assertNull(error.getCause());
    }

    @Test
    void passwordIsNotLoggedOrSerializedButCanBeSubmitted() throws Exception {
        MemberVO member = new MemberVO();
        member.setPw(FAKE_SECRET);
        ObjectMapper mapper = new ObjectMapper();
        assertFalse(member.toString().contains(FAKE_SECRET));
        assertFalse(mapper.writeValueAsString(member).contains(FAKE_SECRET));
        assertEquals(FAKE_SECRET, mapper.readValue("{\"pw\":\"" + FAKE_SECRET + "\"}", MemberVO.class).getPw());
    }

    @Test
    void mapControllerOnlyExposesBrowserKey() {
        ExtendedModelMap model = new ExtendedModelMap();
        assertEquals("map/kakao_map", new RestMapController(FAKE_SECRET).kakaoMap_do(model));
        assertEquals(1, model.size());
        assertEquals(FAKE_SECRET, model.get("kakaoMapJavascriptKey"));
    }

    @Test
    void missingBrowserKeyDoesNotLoadSdk() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        Context context = new Context();
        context.setVariable("kakaoMapJavascriptKey", "");
        String rendered = engine.process("map/kakao_map", context);
        assertFalse(rendered.contains("sdk.js"));
        assertTrue(rendered.contains("지도 키가 설정되지 않았습니다"));
        context.setVariable("kakaoMapJavascriptKey", FAKE_SECRET);
        rendered = engine.process("map/kakao_map", context);
        assertTrue(rendered.contains("https://dapi.kakao.com/v2/maps/sdk.js?appkey=" + FAKE_SECRET));
    }

    @Test
    void templatesDoNotContainRestKeyOrPasswordValueBindings() throws Exception {
        Path templates = Path.of("src/main/resources/templates");
        try (var paths = Files.walk(templates)) {
            for (Path file : paths.filter(Files::isRegularFile).toList()) {
                String text = Files.readString(file);
                assertFalse(text.contains("KakaoAK "), file.toString());
                assertFalse(text.contains("${vo2.pw}"), file.toString());
                assertFalse(text.matches("(?s).*appkey=[a-fA-F0-9]{32}.*"), file.toString());
            }
        }
    }
}
