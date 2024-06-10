package com.ensa.jibi.cmi;

import com.ensa.jibi.model.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class CmiService {

    private final RestTemplate restTemplate;
    private final String cmiUrl;

    @Autowired
    public CmiService(RestTemplate restTemplate, @Value("${cmi.service.url}") String cmiUrl) {
        this.restTemplate = restTemplate;
        this.cmiUrl = cmiUrl;
    }

    public boolean isResponseFavorable(Client client) {
        String url = cmiUrl + "/verify";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ClientToSend c = new ClientToSend();
        c.setFirstName(client.getFirstname());
        c.setLastName(client.getLastname());
        c.setCin(client.getCin());

        HttpEntity<ClientToSend> requestEntity = new HttpEntity<>(c, headers);

        try {
            ResponseEntity<CmiResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    CmiResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().isFavorable();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean isSoldeSuffisant(Long userId, Double amount) {
        String url = cmiUrl + "/checkBalance";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        BalanceRequest balanceRequest = new BalanceRequest(userId, amount);

        HttpEntity<BalanceRequest> requestEntity = new HttpEntity<>(balanceRequest, headers);

        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Boolean.class
            );
            return response.getStatusCode().is2xxSuccessful() && Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean istransfer(double montant, Long senderId, Long recieverId) {
        String url = cmiUrl + "/maketransfert";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        TransferRequest transferRequest = new TransferRequest(senderId,recieverId, montant);

        HttpEntity<TransferRequest> requestEntity = new HttpEntity<>(transferRequest, headers);

        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Boolean.class
            );
            return response.getStatusCode().is2xxSuccessful() && Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}





@Getter
class CmiResponse {
    private boolean favorable;

    public void setFavorable(boolean favorable) {
        this.favorable = favorable;
    }
}
@Data
@NoArgsConstructor
class ClientToSend {
    private String firstName;
    private String lastName;
    private String cin;
}
@Data
@NoArgsConstructor
@AllArgsConstructor
class BalanceRequest {
    private Long userId;
    private Double amount;
}
@Data
@NoArgsConstructor
@AllArgsConstructor
class TransferRequest {
    private Long sender;
    private Long reciever;
    private Double amount;
}

