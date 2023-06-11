package design.kfu.tgintegrator.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;

@Service
public class HeaderService {

    public final static String BASIC = "Basic ";
    @Value("${forum.accounts}")
    private String[] accounts;

    @Value("${forum.permit.all.by}")
    private String defaultAccount;

    public String getAuthorizationBase64ForId(Long telegramAccountId) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < accounts.length; i++) {
            String account = accounts[i];
            String[] accountData = account.split(":");
            if (accountData[2].equals(telegramAccountId.toString())) {
                String tmp = accountData[0] + ":" + accountData[1];
                return BASIC + Base64Utils.encodeToString(tmp.getBytes(StandardCharsets.UTF_8));
            }
        }
        if (defaultAccount != null) {
            s.append(defaultAccount);
        } else {
            s.append("no-auth");
        }
        return BASIC + Base64Utils.encodeToString(s.toString().getBytes(StandardCharsets.UTF_8));
    }

}
