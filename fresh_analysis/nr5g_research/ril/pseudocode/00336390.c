
/* RfxRootController::unregisterToHandleRequest(int, int const*, unsigned long,
   RfxController::HANDLER_PRIORITY) */

void __thiscall
RfxRootController::unregisterToHandleRequest
          (RfxRootController *this,undefined8 param_2,undefined8 param_3_00,ulong param_3,
          undefined4 param_5)

{
  long lVar1;
  ulong uVar2;
  ulong uVar3;
  long local_80;
  weakref_type *local_78;
  undefined4 local_70;
  undefined4 uStack_6c;
  long local_68;
  
  lVar1 = tpidr_el0;
  local_68 = *(long *)(lVar1 + 0x28);
  if ((param_3 != 0) && (*(long *)(this + 0x58) != 0)) {
    uVar3 = 0;
    do {
      local_80 = 0;
      local_78 = (weakref_type *)0x0;
      uStack_6c = 0xffffffff;
      local_70 = param_5;
      uVar2 = android::SortedVectorImpl::indexOf(this + 0x48);
      if (-1 < (long)uVar2) {
        android::VectorImpl::removeItemsAt((ulong)(this + 0x48),uVar2);
      }
      if (local_80 != 0) {
        android::RefBase::weakref_type::decWeak(local_78,&local_80);
      }
      uVar3 = uVar3 + 1;
    } while ((uVar3 < param_3) && (*(long *)(this + 0x58) != 0));
  }
  if (*(long *)(lVar1 + 0x28) == local_68) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


