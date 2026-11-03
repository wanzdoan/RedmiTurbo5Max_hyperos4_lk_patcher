
/* RfxRootController::unregisterToHandleUrc(int, int const*, unsigned long) */

void __thiscall
RfxRootController::unregisterToHandleUrc
          (RfxRootController *this,int param_1,int *param_2,ulong param_3)

{
  long lVar1;
  ulong uVar2;
  ulong uVar3;
  long local_70;
  weakref_type *local_68;
  undefined4 local_60;
  undefined4 local_5c;
  long local_58;
  
  lVar1 = tpidr_el0;
  local_58 = *(long *)(lVar1 + 0x28);
  if ((param_3 != 0) && (*(long *)(this + 0xa8) != 0)) {
    uVar3 = 0;
    do {
      local_70 = 0;
      local_68 = (weakref_type *)0x0;
      local_60 = 0;
      local_5c = 0xffffffff;
      uVar2 = android::SortedVectorImpl::indexOf(this + 0x98);
      if (-1 < (long)uVar2) {
        android::VectorImpl::removeItemsAt((ulong)(this + 0x98),uVar2);
      }
      if (local_70 != 0) {
        android::RefBase::weakref_type::decWeak(local_68,&local_70);
      }
      uVar3 = uVar3 + 1;
    } while ((uVar3 < param_3) && (*(long *)(this + 0xa8) != 0));
  }
  if (*(long *)(lVar1 + 0x28) == local_58) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


