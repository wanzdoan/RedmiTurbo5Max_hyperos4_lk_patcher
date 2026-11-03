
/* RfxRootController::unregisterInternal(android::SortedVector<RfxRegistryEntry>&, int, int const*,
   unsigned long, RfxController::HANDLER_PRIORITY, int) */

void __thiscall
RfxRootController::unregisterInternal
          (undefined8 param_1_00,void *param_1,undefined8 param_3,undefined8 param_4_00,
          ulong param_4,undefined4 param_6,undefined4 param_7)

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
  if ((param_4 != 0) && (*(long *)((long)param_1 + 0x10) != 0)) {
    uVar3 = 0;
    do {
      local_80 = 0;
      local_78 = (weakref_type *)0x0;
      local_70 = param_6;
      uStack_6c = param_7;
      uVar2 = android::SortedVectorImpl::indexOf(param_1);
      if (-1 < (long)uVar2) {
        android::VectorImpl::removeItemsAt((ulong)param_1,uVar2);
      }
      if (local_80 != 0) {
        android::RefBase::weakref_type::decWeak(local_78,&local_80);
      }
      uVar3 = uVar3 + 1;
    } while ((uVar3 < param_4) && (*(long *)((long)param_1 + 0x10) != 0));
  }
  if (*(long *)(lVar1 + 0x28) == local_68) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


