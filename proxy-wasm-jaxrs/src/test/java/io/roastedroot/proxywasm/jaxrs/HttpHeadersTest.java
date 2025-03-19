package io.roastedroot.proxywasm.jaxrs;

import static io.restassured.RestAssured.given;
import static io.roastedroot.proxywasm.jaxrs.TestHelpers.EXAMPLES_DIR;
import static org.hamcrest.Matchers.equalTo;

import com.dylibso.chicory.wasm.Parser;
import io.quarkus.test.junit.QuarkusTest;
import io.roastedroot.proxywasm.StartException;
import jakarta.enterprise.inject.Produces;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class HttpHeadersTest {

    @Produces
    public WasmPluginFactory create() throws StartException {
        return () ->
                WasmPlugin.builder()
                        .withName("httpHeaders")
                        .withShared(true)
                        .withPluginConfig("{\"header\": \"x-wasm-header\", \"value\": \"foo\"}")
                        .build(
                                Parser.parse(
                                        Path.of(
                                                EXAMPLES_DIR
                                                        + "/go-examples/http_headers/main.wasm")));
    }

    @Test
    public void testRequest() {
        given().when()
                .get("/test/httpHeaders")
                .then()
                .statusCode(200)
                .header("x-proxy-wasm-go-sdk-example", "http_headers")
                .header("x-wasm-header", "foo")
                .header("x-proxy-wasm-counter", "1")
                .body(equalTo("hello world"));

        given().when()
                .get("/test/httpHeaders")
                .then()
                .statusCode(200)
                .header("x-proxy-wasm-go-sdk-example", "http_headers")
                .header("x-wasm-header", "foo")
                .header("x-proxy-wasm-counter", "2")
                .body(equalTo("hello world"));
    }
}
