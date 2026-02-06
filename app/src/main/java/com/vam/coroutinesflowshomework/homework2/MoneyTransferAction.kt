package com.vam.coroutinesflowshomework.homework2

sealed interface MoneyTransferAction {
    data object TransferFunds : MoneyTransferAction
    data object CancelTransfer : MoneyTransferAction
}