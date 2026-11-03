
/* RtcRadioController::onRadioLockChanged(unsigned int, RfxVariant, RfxVariant) */

void RtcRadioController::onRadioLockChanged(RfxController *param_1)

{
  long lVar1;
  undefined *puVar2;
  int iVar3;
  uint uVar4;
  RfxStatusManager *pRVar5;
  uint uVar6;
  RfxController *local_68;
  weakref_type *local_60;
  undefined *puStack_58;
  undefined8 local_50;
  long local_48;
  
  lVar1 = tpidr_el0;
  local_48 = *(long *)(lVar1 + 0x28);
  RfxController::logD((char *)param_1,"RtcRadioCont","onRadioLockChanged");
  iVar3 = RfxRilUtils::rfxGetSimCount();
  if (iVar3 != 0) {
    uVar6 = 0;
    do {
      pRVar5 = (RfxStatusManager *)RfxController::getStatusManager(param_1,uVar6);
      uVar4 = RfxStatusManager::getIntValue(pRVar5,0x4e,0);
      if (uVar4 != 0) {
        if (*(long *)(lVar1 + 0x28) == local_48) {
          RfxController::logD((char *)param_1,"RtcRadioCont","isRadioLocked = true(%d)",(ulong)uVar4
                             );
          return;
        }
        goto LAB_0036394c;
      }
      uVar6 = uVar6 + 1;
      uVar4 = RfxRilUtils::rfxGetSimCount();
    } while (uVar6 < uVar4);
  }
  iVar3 = RfxRilUtils::rfxGetSimCount();
  puVar2 = PTR_onRadioLockChanged_005d26c0;
  if (iVar3 != 0) {
    uVar6 = 0;
    do {
      pRVar5 = (RfxStatusManager *)RfxController::getStatusManager(param_1,uVar6);
      local_68 = param_1;
      local_60 = (weakref_type *)
                 android::RefBase::createWeak(param_1 + *(long *)(*(long *)param_1 + -0x18));
      puStack_58 = puVar2;
      local_50 = 0;
      RfxStatusManager::unRegisterStatusChanged(pRVar5,0x4e,(RfxCallback3 *)&local_68);
      if (local_68 != (RfxController *)0x0) {
        android::RefBase::weakref_type::decWeak(local_60,&local_68);
      }
      uVar6 = uVar6 + 1;
      uVar4 = RfxRilUtils::rfxGetSimCount();
    } while (uVar6 < uVar4);
  }
  updateRadioState((RtcRadioController *)param_1);
  if (*(long *)(lVar1 + 0x28) == local_48) {
    return;
  }
LAB_0036394c:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


