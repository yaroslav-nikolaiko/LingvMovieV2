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
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

        String url = "ws://localhost:8081/remote-player";
        //MyStompSessionHandler sessionHandler = new MyStompSessionHandler();

        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        if(args.length>0){
            System.out.println("Using TOKEN = "+args[0]);
            handshakeHeaders.add("Authorization","Bearer "+args[0]);
        }


        StringBuilder username = new StringBuilder();

        StompHeaders headers = new StompHeaders();
        headers.add("destination", "/app/hello");
        headers.add("content-type", "application/json");

        StompHeaders userHeaders = new StompHeaders();
        userHeaders.add("destination", "/app/user/hello");
        userHeaders.add("content-type", "application/json");

        ListenableFuture<StompSession> future = stompClient.connect(url, handshakeHeaders, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                System.out.println("**************** Connected Headers ***************");
                for (Map.Entry<String, List<String>> entry : connectedHeaders.entrySet()) {
                    System.out.println(entry.getKey()+" : "+entry.getValue());
                }
                if(connectedHeaders.containsKey("user-name")){
                    //userHeaders.put("user-name", connectedHeaders.get("user-name"));
                    //userHeaders.put("user-name", connectedHeaders.get("user-name"));
                   /* List<String> destination = Collections.singletonList("/app/user/" + connectedHeaders.get("user-name").get(0) + "/user/hello");
                    System.out.println("*** DESTINATION : "+destination.get(0));
                    destination = Collections.singletonList("/user/admin/app/user/hello");
                    userHeaders.replace("destination", destination);*/
                }

                    //username.append(connectedHeaders.get("user-name").get(0));
                super.afterConnected(session, connectedHeaders);
            }
        });
        StompSession session = future.get();



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

        String destination = "/user/queue/greetings";
/*        if(username.length()>0)
            destination = "/user/" + username + "/queue/greetings";

        System.out.println("User Destination = " + destination);*/

        session.subscribe(destination, new StompFrameHandler() {
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
