package ru.practicum.explore.stat;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.logevent.LogEvent;
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
    public StatDataService(@Value("${statistic-server.url}") String serverUrl) {
        restTemplate = new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();
    }

    public void logRequest(HttpServletRequest request) {
        LogEvent log = LogEvent.builder()
                .app(request.getRemoteHost())
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        HttpEntity<LogEvent> requestEntity = new HttpEntity<>(log);

        ResponseEntity<LogEvent> res = restTemplate
                .exchange("/hit", HttpMethod.POST, requestEntity, LogEvent.class);

        if (!res.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("error saving the log");
        }
    }

    public Integer getRequestHits(String requestURI) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", LocalDateTime.parse("1980-02-20T00:00:00"));
        parameters.put("end", LocalDateTime.parse("2180-02-20T00:00:00"));
        parameters.put("unique", true);
        parameters.put("uris", requestURI);
        List<StatData> stat = getStat(parameters);

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

    private List<StatData> getStat(Map<String, Object> parameters) {
        String path = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        HttpHeaders headers = new HttpHeaders();
        headers.clearContentHeaders();

        ResponseEntity<List<StatData>> responseEntity =
                restTemplate.exchange(
                        path,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        parameters
                );
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("error saving the log");
        }
        return responseEntity.getBody();
    }
}
