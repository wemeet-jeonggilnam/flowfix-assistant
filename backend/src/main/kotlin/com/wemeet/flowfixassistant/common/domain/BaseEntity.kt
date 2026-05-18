package com.wemeet.flowfixassistant.common.domain

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
        protected set

    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 100)
    var createdBy: String? = null
        protected set

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
        protected set

    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    var updatedBy: String? = null
        protected set

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null
        protected set

    @Column(name = "deleted_by", length = 100)
    var deletedBy: String? = null
        protected set

    fun softDelete(deletedBy: String? = null) {
        this.deletedAt = LocalDateTime.now()
        this.deletedBy = deletedBy
    }

    val isDeleted: Boolean
        get() = deletedAt != null
}
