package com.wemeet.flowfixassistant.chat.infrastructure

import com.wemeet.flowfixassistant.chat.domain.model.Conversation
import com.wemeet.flowfixassistant.chat.domain.repository.ConversationRepository
import org.springframework.data.jpa.repository.JpaRepository

interface JpaConversationRepository : ConversationRepository, JpaRepository<Conversation, Long>
