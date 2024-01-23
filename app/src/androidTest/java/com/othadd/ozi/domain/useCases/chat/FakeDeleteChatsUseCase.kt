package com.othadd.ozi.domain.useCases.chat

import com.othadd.ozi.domain.useCases.interfaces.chat.DeleteChatsUseCase

class FakeDeleteChatsUseCase: DeleteChatsUseCase {
    override suspend fun deleteEmptyChats() {  }
}