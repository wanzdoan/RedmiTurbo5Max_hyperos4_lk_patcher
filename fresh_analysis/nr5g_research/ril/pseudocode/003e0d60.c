
/* RtcCapabilitySwitchController::unregisterStatusKeys() */

void __thiscall
RtcCapabilitySwitchController::unregisterStatusKeys(RtcCapabilitySwitchController *this)

{
  long lVar1;
  undefined *puVar2;
  int iVar3;
  uint uVar4;
  RfxStatusManager *pRVar5;
  uint uVar6;
  RtcCapabilitySwitchController *local_68;
  weakref_type *local_60;
  undefined *puStack_58;
  undefined8 local_50;
  long local_48;
  
  lVar1 = tpidr_el0;
  local_48 = *(long *)(lVar1 + 0x28);
  iVar3 = RfxRilUtils::rfxGetSimCount();
  puVar2 = PTR_onStatusKeyChanged_005d30b8;
  if (iVar3 != 0) {
    uVar6 = 0;
    do {
      pRVar5 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,uVar6);
      local_68 = this;
      local_60 = (weakref_type *)
                 android::RefBase::createWeak(this + *(long *)(*(long *)this + -0x18));
      puStack_58 = puVar2;
      local_50 = 0;
      RfxStatusManager::unRegisterStatusChanged(pRVar5,0x4e,(RfxCallback3 *)&local_68);
      if (local_68 != (RtcCapabilitySwitchController *)0x0) {
        android::RefBase::weakref_type::decWeak(local_60,&local_68);
      }
      pRVar5 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,uVar6);
      local_68 = this;
      local_60 = (weakref_type *)
                 android::RefBase::createWeak(this + *(long *)(*(long *)this + -0x18));
      puStack_58 = puVar2;
      local_50 = 0;
      RfxStatusManager::unRegisterStatusChanged(pRVar5,0x37,(RfxCallback3 *)&local_68);
      if (local_68 != (RtcCapabilitySwitchController *)0x0) {
        android::RefBase::weakref_type::decWeak(local_60,&local_68);
      }
      pRVar5 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,uVar6);
      local_68 = this;
      local_60 = (weakref_type *)
                 android::RefBase::createWeak(this + *(long *)(*(long *)this + -0x18));
      puStack_58 = puVar2;
      local_50 = 0;
      RfxStatusManager::unRegisterStatusChanged(pRVar5,0x2c,(RfxCallback3 *)&local_68);
      if (local_68 != (RtcCapabilitySwitchController *)0x0) {
        android::RefBase::weakref_type::decWeak(local_60,&local_68);
      }
      pRVar5 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,uVar6);
      local_68 = this;
      local_60 = (weakref_type *)
                 android::RefBase::createWeak(this + *(long *)(*(long *)this + -0x18));
      puStack_58 = puVar2;
      local_50 = 0;
      RfxStatusManager::unRegisterStatusChanged(pRVar5,0x38,(RfxCallback3 *)&local_68);
      if (local_68 != (RtcCapabilitySwitchController *)0x0) {
        android::RefBase::weakref_type::decWeak(local_60,&local_68);
      }
      pRVar5 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,uVar6);
      local_68 = this;
      local_60 = (weakref_type *)
                 android::RefBase::createWeak(this + *(long *)(*(long *)this + -0x18));
      puStack_58 = puVar2;
      local_50 = 0;
      RfxStatusManager::unRegisterStatusChanged(pRVar5,0x28,(RfxCallback3 *)&local_68);
      if (local_68 != (RtcCapabilitySwitchController *)0x0) {
        android::RefBase::weakref_type::decWeak(local_60,&local_68);
      }
      iVar3 = getGeminiMode();
      if ((iVar3 == 0) && (iVar3 = RfxRilUtils::rfxGetSimCount(), iVar3 == 2)) {
        pRVar5 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,uVar6);
        local_68 = this;
        local_60 = (weakref_type *)
                   android::RefBase::createWeak(this + *(long *)(*(long *)this + -0x18));
        puStack_58 = puVar2;
        local_50 = 0;
        RfxStatusManager::unRegisterStatusChanged(pRVar5,0x2a,(RfxCallback3 *)&local_68);
        if (local_68 != (RtcCapabilitySwitchController *)0x0) {
          android::RefBase::weakref_type::decWeak(local_60,&local_68);
        }
        pRVar5 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,uVar6);
        local_68 = this;
        local_60 = (weakref_type *)
                   android::RefBase::createWeak(this + *(long *)(*(long *)this + -0x18));
        puStack_58 = puVar2;
        local_50 = 0;
        RfxStatusManager::unRegisterStatusChanged(pRVar5,9,(RfxCallback3 *)&local_68);
        if (local_68 != (RtcCapabilitySwitchController *)0x0) {
          android::RefBase::weakref_type::decWeak(local_60,&local_68);
        }
      }
      uVar6 = uVar6 + 1;
      uVar4 = RfxRilUtils::rfxGetSimCount();
    } while (uVar6 < uVar4);
  }
  pRVar5 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
  local_68 = this;
  local_60 = (weakref_type *)android::RefBase::createWeak(this + *(long *)(*(long *)this + -0x18));
  puStack_58 = puVar2;
  local_50 = 0;
  RfxStatusManager::unRegisterStatusChanged(pRVar5,0x27,(RfxCallback3 *)&local_68);
  if (local_68 != (RtcCapabilitySwitchController *)0x0) {
    android::RefBase::weakref_type::decWeak(local_60,&local_68);
  }
  pRVar5 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
  local_68 = this;
  local_60 = (weakref_type *)android::RefBase::createWeak(this + *(long *)(*(long *)this + -0x18));
  puStack_58 = puVar2;
  local_50 = 0;
  RfxStatusManager::unRegisterStatusChanged(pRVar5,0x5e,(RfxCallback3 *)&local_68);
  if (local_68 != (RtcCapabilitySwitchController *)0x0) {
    android::RefBase::weakref_type::decWeak(local_60,&local_68);
  }
  pRVar5 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
  local_68 = this;
  local_60 = (weakref_type *)android::RefBase::createWeak(this + *(long *)(*(long *)this + -0x18));
  puStack_58 = puVar2;
  local_50 = 0;
  RfxStatusManager::unRegisterStatusChanged(pRVar5,0x5d,(RfxCallback3 *)&local_68);
  if (local_68 != (RtcCapabilitySwitchController *)0x0) {
    android::RefBase::weakref_type::decWeak(local_60,&local_68);
  }
  if (*(long *)(lVar1 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


