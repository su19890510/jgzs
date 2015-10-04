package gateway;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author 刘飞 E-mail:liufei_it@126.com
 *
 * @version 1.0.0
 * @since 2015年10月4日 下午3:30:42
 */
public class SimpleHttpJsonGatewayTemplate {

    public static void main(String[] args) {
        String url = "http://123.57.55.59/news";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String json = restTemplate.getForObject(url, String.class);
        System.out.println(json);
        
    }
}
