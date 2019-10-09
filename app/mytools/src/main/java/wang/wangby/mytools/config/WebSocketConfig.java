package wang.wangby.mytools.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import wang.wangby.mytools.service.websocket.MyWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册自定义消息处理，消息路径为`/ws/foo`
        registry.addHandler(new MyWebSocketHandler(),"/ws").setAllowedOrigins("*");
    }
}