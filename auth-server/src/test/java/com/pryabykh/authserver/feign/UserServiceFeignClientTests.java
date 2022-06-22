package com.pryabykh.authserver.feign;

import com.pryabykh.authserver.utils.AuthTestUtils;
import io.netty.handler.codec.http.HttpMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest()
@ActiveProfiles("test")
public class UserServiceFeignClientTests {
    private UserServiceFeignClient userServiceFeignClient;
    private ClientAndServer mockServer;
    @Value("${user-service.mock-server.port}")
    private int userServiceMockPort;

    @BeforeEach
    public void setupMockServer() {
        mockServer = ClientAndServer.startClientAndServer(userServiceMockPort);
    }

    @AfterEach
    public void tearDownServer() {
        mockServer.stop();
    }

    @Test
    public void checkCredentialsPositive() {
        mockServer.when(
                request()
                        .withMethod(HttpMethod.POST.name())
                        .withPath("/v1/users/check-credentials")
        ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("true")
        );

        boolean result = userServiceFeignClient.checkCredentials(AuthTestUtils.shapeUserCredentialsDto());
        Assertions.assertTrue(result);
    }

    @TestConfiguration
    public static class UserServiceFeignClientConfiguration {
        @Value("${user-service.mock.host}")
        private String userServiceMockHost;
        @Value("${user-service.mock-server.port}")
        private int userServiceMockPort;
        @Value("${user-service.name}")
        private String userServiceName;

        @Bean
        public ServiceInstanceListSupplier serviceInstanceListSupplier() {
            return new ServiceInstanceListSupplierImpl(userServiceName, userServiceMockHost , userServiceMockPort);
        }

        public static class ServiceInstanceListSupplierImpl implements ServiceInstanceListSupplier {
            private final String host;
            private final String serviceId;
            private final int[] ports;

            public ServiceInstanceListSupplierImpl(String serviceId, String host, int... ports) {
                this.host = host;
                this.serviceId = serviceId;
                this.ports = ports;
            }

            @Override
            public String getServiceId() {
                return serviceId;
            }

            @Override
            public Flux<List<ServiceInstance>> get() {
                List<ServiceInstance> result = new ArrayList<>();
                for (int i = 0; i < ports.length; i++) {
                    result.add(new DefaultServiceInstance(serviceId + i, getServiceId(), host, ports[i], false));
                }
                return Flux.just(result);
            }
        }
    }

    @Autowired
    public void setUserServiceFeignClient(UserServiceFeignClient userServiceFeignClient) {
        this.userServiceFeignClient = userServiceFeignClient;
    }
}
