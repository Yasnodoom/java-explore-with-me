package ru.practicum.explore.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.statdata.StatData;

import java.util.List;
import java.util.Map;

@Service
public class FuckinService {
    private final RestTemplate restTemplate;

    @Autowired
    public FuckinService(@Value("${statistic-service.url}") String serverUrl) {
        restTemplate = new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();
    }

    public List<StatData> getStat(Map<String, Object> parameters) {
        String path = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
//        if (parameters.get("start") == null) {
//            path = "/stats?uris={uris}&unique={unique}";
//        }
        ResponseEntity<List<StatData>> responseEntity =
                restTemplate.exchange(
                        path,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<StatData>>() {},
                        parameters
                );
        return responseEntity.getBody();
    }
}
