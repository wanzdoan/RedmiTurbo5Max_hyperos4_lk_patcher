
/* NetAgentService::setTransactionInterfaceId(int) */

void __thiscall NetAgentService::setTransactionInterfaceId(NetAgentService *this,int param_1)

{
  long lVar1;
  int local_1c;
  long local_18;
  
  lVar1 = tpidr_el0;
  local_18 = *(long *)(lVar1 + 0x28);
  local_1c = param_1;
  mtkLogD("NetAgentService","[%s] transIntfId = %d","setTransactionInterfaceId",param_1);
  if (local_1c != -1) {
    std::list<int,std::allocator<int>>::push_back
              ((list<int,std::allocator<int>> *)(this + 0xa8),&local_1c);
  }
  lVar1 = tpidr_el0;
  lVar1 = *(long *)(lVar1 + 0x28) - local_18;
  if (lVar1 != 0) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail(lVar1);
  }
  return;
}


