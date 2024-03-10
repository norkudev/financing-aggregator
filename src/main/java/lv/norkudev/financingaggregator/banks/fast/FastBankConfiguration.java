package lv.norkudev.financingaggregator.banks.fast;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FastBankConfiguration {

    @Bean
    @Qualifier("fastBankClient")
    public WebClient fastBankClient(@Value("${banks.fastbank.url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

}
