package stu.mango.wiremock;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * 配置一个假的 server
 */
public class MockServer {
    public static void main(String[] args) throws IOException {
        configureFor(54188);
        removeAllMappings(); // 清空之前的所有配置

        mock("/order/001", "first");

    }

    private static void mock(String testUrl, String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("/mock/response/" +fileName + ".json");
        String content = FileUtils.readFileToString(resource.getFile(), "UTF-8");

        stubFor(get(urlPathEqualTo(testUrl))
                .willReturn(aResponse()
                        .withBody(content)
                        .withStatus(200)));
    }
}
