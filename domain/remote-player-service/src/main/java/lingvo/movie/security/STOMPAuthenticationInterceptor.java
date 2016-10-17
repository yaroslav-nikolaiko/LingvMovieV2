package lingvo.movie.security;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.security.Principal;

/**
 * Created by yaroslav on 17.10.16.
 */
public class STOMPAuthenticationInterceptor extends ChannelInterceptorAdapter implements ExecutorChannelInterceptor {

    @Override
    public Message<?> beforeHandle(Message<?> message, MessageChannel channel, MessageHandler handler) {
        Principal principal = StompHeaderAccessor.getUser(message.getHeaders());
        if(principal!=null){
            OAuth2Authentication oauthPrincipal = (OAuth2Authentication) principal;
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, oauthPrincipal.getAuthorities());
            authentication.setDetails(oauthPrincipal.getDetails());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        HystrixRequestContext.initializeContext();
        return message;
    }

    @Override
    public void afterMessageHandled(Message<?> message, MessageChannel channel, MessageHandler handler, Exception ex) {
        SecurityContextHolder.clearContext();
        HystrixRequestContext.getContextForCurrentThread().shutdown();
    }
}
