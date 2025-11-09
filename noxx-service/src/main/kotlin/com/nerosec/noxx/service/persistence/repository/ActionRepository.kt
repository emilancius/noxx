package com.nerosec.noxx.service.persistence.repository

import com.nerosec.noxx.commons.persistence.repository.BaseRepository
import com.nerosec.noxx.service.persistence.entity.ActionEntity
import org.springframework.stereotype.Repository

@Repository
interface ActionRepository : BaseRepository<ActionEntity>