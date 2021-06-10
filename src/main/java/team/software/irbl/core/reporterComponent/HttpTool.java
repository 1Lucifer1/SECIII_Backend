package team.software.irbl.core.reporterComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import team.software.irbl.util.Err;

import java.util.Map;

@Component
public class HttpTool {

    private final RestTemplate restTemplate;

    @Autowired
    public HttpTool(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getHttpRequest(String url) throws Err {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String data = responseEntity.getBody();
        System.out.println("请求结果：" + data);
        if (data == null) throw new Err("status is " + responseEntity.getStatusCode());
        return data;
    }

    public String postHttpRequest(String url, Map<String, Object> body) throws Err {
        HttpHeaders headers = new HttpHeaders();
        headers.add("version", "v1");
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        String data = responseEntity.getBody();
        System.out.println("请求结果：" + data);
        if (data == null) throw new Err("status is " + responseEntity.getStatusCode());
        return data;
    }
}
