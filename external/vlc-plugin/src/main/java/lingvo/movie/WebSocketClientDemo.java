package lingvo.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by yaroslav on 10.10.16.
 */
@SpringBootApplication
public class WebSocketClientDemo {
    public static void main(String[] args) throws Exception{
        SpringApplication.run(WebSocketClientDemo.class, args);

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line = "";

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String url = "ws://localhost:8081/gs-guide-websocket";
        //MyStompSessionHandler sessionHandler = new MyStompSessionHandler();


        System.out.println("Using TOKEN = "+args[0]);
        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        handshakeHeaders.add("Authorization","Bearer "+args[0]);

        ListenableFuture<StompSession> future = stompClient.connect(url, handshakeHeaders, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                super.afterConnected(session, connectedHeaders);
            }
        });
        StompSession session = future.get();

        StompHeaders headers = new StompHeaders();
        headers.add("destination", "/app/hello");
        headers.add("content-type", "application/json");

        StompHeaders userHeaders = new StompHeaders();
        userHeaders.add("destination", "/app/user/hello");
        userHeaders.add("content-type", "application/json");

        session.subscribe("/topic/greetings", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Greeting.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Greeting greeting = (Greeting) payload;
                System.out.println(greeting.getContent());
            }
        });

        session.subscribe("/user/queue/greetings", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Greeting.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Greeting greeting = (Greeting) payload;
                System.out.println("Handle User Frame");
                System.out.println(greeting.getContent());
            }
        });

        while (! line.equalsIgnoreCase("quit")) {
            line = in.readLine();
            session.send(headers, new HelloMessage(line));
            session.send(userHeaders, new HelloMessage("User"+line));
        }
        in.close();

        //session.send("/app/hello", "{\"name\": \"Melisandra\"}");


    }

/*    private static class MyStompSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            super.afterConnected(session, connectedHeaders);
        }
    }*/

    private static class Greeting {

        private String content;

        public Greeting() {
        }

        public Greeting(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

    }

    private static class HelloMessage {

        private String name;

        public HelloMessage() {
        }

        public HelloMessage(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
