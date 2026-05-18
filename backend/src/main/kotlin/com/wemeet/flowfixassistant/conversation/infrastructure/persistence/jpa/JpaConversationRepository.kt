package com.wemeet.flowfixassistant.conversation.infrastructure.persistence.jpa

import com.wemeet.flowfixassistant.conversation.domain.model.Conversation
import com.wemeet.flowfixassistant.conversation.domain.repository.ConversationRepository
import org.springframework.data.jpa.repository.JpaRepository

interface JpaConversationRepository : ConversationRepository, JpaRepository<Conversation, Long>
