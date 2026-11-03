
/* RtcCapabilitySwitchController::powerOnRadio() */

void __thiscall RtcCapabilitySwitchController::powerOnRadio(RtcCapabilitySwitchController *this)

{
  long lVar1;
  int iVar2;
  RfxStatusManager *pRVar3;
  RfxClassInfo *pRVar4;
  RtcRadioController *pRVar5;
  ulong uVar6;
  ulong uVar7;
  long *local_88;
  undefined4 local_80;
  long local_58;
  
  lVar1 = tpidr_el0;
  local_58 = *(long *)(lVar1 + 0x28);
  iVar2 = RfxRilUtils::rfxGetSimCount();
  if (iVar2 != 0) {
    uVar6 = 0;
    do {
      iVar2 = (int)uVar6;
      if (uVar6 < 4) {
        pRVar3 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,iVar2);
        uVar7 = RfxStatusManager::getIntValue(pRVar3,0x4e,0);
        uVar7 = uVar7 & 0xffffffff;
      }
      else {
        uVar7 = 0xffffffff;
      }
      RfxController::logD((char *)this,"RtcCapa","powerOnRadio[%d]:%d,radio_lock:%d,start:%d",
                          uVar6 & 0xffffffff,(ulong)(byte)this[uVar6 + 0x5c],uVar7,
                          (ulong)(byte)this[0x60]);
      if ((uVar6 < 4) && ((int)uVar7 == 3)) {
        pRVar3 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,iVar2);
        local_88 = (long *)CONCAT44(local_88._4_4_,2);
        local_80 = 0;
        RfxStatusManager::setValue(pRVar3,0x4e,(RfxVariant *)&local_88,false,false);
        RfxVariant::deinit((RfxVariant *)&local_88);
      }
      if ((this[0x60] == (RtcCapabilitySwitchController)0x0) &&
         (this[uVar6 + 0x5c] != (RtcCapabilitySwitchController)0x0)) {
        pRVar4 = (RfxClassInfo *)RtcRadioController::getRtcRadioControllerClassInfo();
        pRVar5 = (RtcRadioController *)
                 RfxController::findController((RfxController *)this,iVar2,pRVar4);
        local_88 = (long *)0x0;
        RtcRadioController::moduleRequestRadioPower(pRVar5,this[uVar6 + 0x5c],&local_88,3);
        if (local_88 != (long *)0x0) {
          android::RefBase::decStrong
                    ((RefBase *)((long)local_88 + *(long *)(*local_88 + -0x18)),&local_88);
        }
      }
      uVar6 = uVar6 + 1;
      uVar7 = RfxRilUtils::rfxGetSimCount();
    } while (uVar6 < (uVar7 & 0xffffffff));
  }
  if (*(long *)(lVar1 + 0x28) == local_58) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


