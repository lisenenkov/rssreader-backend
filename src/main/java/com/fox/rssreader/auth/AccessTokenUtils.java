package com.fox.rssreader.auth;

import com.fox.rssreader.model.entities.User;
import com.fox.rssreader.model.entities.UserAccessToken;
import com.fox.rssreader.model.repositories.UserAccessTokenRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Objects;

@Component
@AllArgsConstructor
public class AccessTokenUtils {
    private static final int MAX_ACTIVE_TOKENS = 10;
    private UserAccessTokenRepository userAccessTokenRepository;

    @Transactional
    public String generateToken(User user, RequestParameters requestParameters) {
        var accessTokens = user.getAccessTokens();

        UserAccessToken allEqualsUserAccessToken = null;
        UserAccessToken uaEqualsUserAccessToken = null;
        UserAccessToken ipEqualsUserAccessToken = null;
        UserAccessToken leastUsedUserAccessToken = null;

        for (UserAccessToken uaToken : accessTokens) {
            if (Objects.equals(requestParameters.getUserAgent(), uaToken.getUserAgent())) {
                if (Objects.equals(requestParameters.getLastAccessIp(), uaToken.getLastAccessIp())) {
                    allEqualsUserAccessToken = uaToken;
                    break;
                } else {
                    uaEqualsUserAccessToken = uaToken;
                }
            } else if (Objects.equals(requestParameters.getLastAccessIp(), uaToken.getLastAccessIp())) {
                ipEqualsUserAccessToken = uaToken;
            } else if (leastUsedUserAccessToken == null ||
                       leastUsedUserAccessToken.getLastUsed().after(uaToken.getLastUsed())) {
                leastUsedUserAccessToken = uaToken;
            }
        }

        //first by useragent && ip
        if (allEqualsUserAccessToken != null) {
            allEqualsUserAccessToken.setLastUsed(new Date());
            userAccessTokenRepository.save(allEqualsUserAccessToken);
            return allEqualsUserAccessToken.getToken();
        }

        //second by useragent
        if (uaEqualsUserAccessToken != null) {
            uaEqualsUserAccessToken.setLastAccessIp(requestParameters.getLastAccessIp());
            uaEqualsUserAccessToken.setLastUsed(new Date());
            userAccessTokenRepository.save(uaEqualsUserAccessToken);
            return uaEqualsUserAccessToken.getToken();
        }

        //third by ip
        if (ipEqualsUserAccessToken != null) {
            ipEqualsUserAccessToken.setUserAgent(requestParameters.getUserAgent());
            ipEqualsUserAccessToken.setLastUsed(new Date());
            userAccessTokenRepository.save(ipEqualsUserAccessToken);
            return ipEqualsUserAccessToken.getToken();
        }

        //delete least accessed
        if (accessTokens.size() >= MAX_ACTIVE_TOKENS) {
            userAccessTokenRepository.delete(leastUsedUserAccessToken);
        }

        SecureRandom random = new SecureRandom();
        UserAccessToken userAccessToken = new UserAccessToken();
        userAccessToken.setUser(user);
        userAccessToken.setLastUsed(new Date());
        userAccessToken.setLastAccessIp(requestParameters.getLastAccessIp());
        userAccessToken.setUserAgent(requestParameters.getUserAgent());
        random.nextBytes(userAccessToken.getSecret());
        userAccessTokenRepository.save(userAccessToken);

        return userAccessToken.getToken();
    }

    public interface RequestParameters {
        @NonNull
        String getLastAccessIp();

        @NonNull
        String getUserAgent();

    }

    @Getter
    public static class RequestParametersImpl implements RequestParameters {
        String lastAccessIp;
        String userAgent;

        public RequestParametersImpl() {
            if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes requestAttributes) {
                var request = requestAttributes.getRequest();
                lastAccessIp = request.getHeader("X-REAL-IP");
                userAgent = request.getHeader("USER-AGENT");
            }
            if (lastAccessIp == null) lastAccessIp = "";
            if (userAgent == null) userAgent = "";


        }
    }
}
