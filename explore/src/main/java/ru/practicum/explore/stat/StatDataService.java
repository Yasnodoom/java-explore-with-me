package ru.practicum.explore.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.statdata.StatData;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StatDataService {
    private final RestTemplate restTemplate;

    @Autowired
    public StatDataService(@Value("${statistic-service.url}") String serverUrl) {
        restTemplate = new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();
    }

    public List<StatData> getStat(Map<String, Object> parameters) {
        String path = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";

        ResponseEntity<List<StatData>> responseEntity =
                restTemplate.exchange(
                        path,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        },
                        parameters
                );
        return responseEntity.getBody();
    }

    public Integer getRequestHits(String requestURI) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", LocalDateTime.now().minusMonths(1));
        parameters.put("end", LocalDateTime.now().plusMonths(1));
        parameters.put("unique", true);
        parameters.put("uris", requestURI);

        List<StatData> stat = getStat(parameters);

        if (stat.isEmpty()) {
            return 0;
        } else {
            Optional<StatData> log = stat
                    .stream()
                    .filter(el -> el.getUri().equals(requestURI))
                    .findFirst();
            if (log.isPresent()) {
                return log.get().getHits();
            } else {
                return 0;
            }
        }
    }

}
