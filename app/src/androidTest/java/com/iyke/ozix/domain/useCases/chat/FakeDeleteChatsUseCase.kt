package com.iyke.ozix.domain.useCases.chat

import com.iyke.ozix.domain.useCases.interfaces.chat.DeleteChatsUseCase

class FakeDeleteChatsUseCase: DeleteChatsUseCase {
    override suspend fun deleteEmptyChats() {  }
}