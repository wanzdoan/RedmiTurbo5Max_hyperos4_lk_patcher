
/* NetAgentService::removeAllTransactionInterfaceId() */

void __thiscall NetAgentService::removeAllTransactionInterfaceId(NetAgentService *this)

{
  mtkLogD("NetAgentService","[%s] X","removeAllTransactionInterfaceId");
  std::list<int,std::allocator<int>>::clear_abi_ne180000_
            ((list<int,std::allocator<int>> *)(this + 0xa8));
  return;
}


