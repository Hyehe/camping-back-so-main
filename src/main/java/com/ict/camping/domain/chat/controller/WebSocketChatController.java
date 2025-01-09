package com.ict.camping.domain.chat.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.ict.camping.domain.chat.service.ChatService;
import com.ict.camping.domain.chat.vo.ChatMessageVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketChatController {
  private final ChatService chatService;
  private final SimpMessagingTemplate simpMessagingTemplate;

  /*
   * 클라이언트 -> /app/message 로 전송하면 이 메서드가 처리.
   * 그 후 /topic/chat/{roomIdx} 로 메시지 브로드캐스트
   */
  @MessageMapping("/message")
  public void sendMessage(ChatMessageVO message) {
    System.out.println("111111롸로라ㅗ라ㅗㄺㅁㄷ미ㅓㄴ이ㅏ춘춘");
    chatService.sendMessage(message);
    
    // sender_ avatar_url 추가
    List<ChatMessageVO> messages = chatService.getMessages(message.getRoom_idx());
    ChatMessageVO latesMsg = messages.isEmpty() ? message : messages.get(messages.size() - 1);
    // 모임별로 구독 주소를 분리
    System.out.println("2222222롸로라ㅗ라ㅗㄺㅁㄷ미ㅓㄴ이ㅏ춘춘");
    simpMessagingTemplate.convertAndSend("/topic/chat/" + message.getRoom_idx(), message);
    System.out.println("Message sent successfully to /topic/chat/" + message.getRoom_idx());
    log.info("Message sent successfully to /topic/chat/{}", message.getRoom_idx());
  }

}
