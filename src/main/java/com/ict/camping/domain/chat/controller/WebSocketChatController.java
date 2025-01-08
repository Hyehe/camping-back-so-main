package com.ict.camping.domain.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.ict.camping.domain.chat.service.ChatService;
import com.ict.camping.domain.chat.vo.ChatMessageVO;

import lombok.RequiredArgsConstructor;

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
    chatService.sendMessage(message);

    // sender_ avatar_url 추가
    String avatar = chatService.getAvatarUrlByUserIdx(message.getSender_idx());
    message.setSender_avatar_url(avatar);
    // 모임별로 구독 주소를 분리
    simpMessagingTemplate.convertAndSend("/topic/chat/" + message.getRoom_idx(), message);
  }

}
