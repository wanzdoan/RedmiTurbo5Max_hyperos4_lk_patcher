
/* RmcRatSwitchHandler::requestSetPreferredNetworkType(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcRatSwitchHandler::requestSetPreferredNetworkType(RmcRatSwitchHandler *this,sp *param_1)

{
  uint uVar1;
  undefined4 uVar2;
  uint uVar3;
  long lVar4;
  int iVar5;
  uint uVar6;
  ulong uVar7;
  RfxMclStatusManager *pRVar8;
  RfxAtResponse **ppRVar9;
  void *pvVar10;
  undefined4 uVar11;
  uint *puVar12;
  long *plVar13;
  RfxAtResponse *pRVar14;
  long lVar15;
  uint uVar16;
  uint uVar17;
  uint uVar18;
  ulong uVar19;
  undefined7 in_stack_fffffffffffffee1;
  undefined1 uVar20;
  undefined8 in_stack_fffffffffffffee8;
  RfxAtResponse *local_110;
  RfxAtResponse *local_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 uStack_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 uStack_c0;
  undefined4 uStack_b8;
  undefined4 local_b4;
  undefined4 uStack_b0;
  undefined8 uStack_ac;
  RfxAtResponse *local_98;
  undefined1 local_90;
  long local_68;
  
  lVar4 = tpidr_el0;
  local_68 = *(long *)(lVar4 + 0x28);
  local_108 = (RfxAtResponse *)0x0;
  uStack_ac = 0;
  uStack_b0 = 0;
  uStack_e8 = 0;
  local_f0 = 0;
  uStack_d8 = 0;
  uStack_e0 = 0;
  uStack_c8 = 0;
  local_d0 = 0;
  uStack_b8 = 0;
  local_b4 = 0;
  uStack_c0 = 0;
  uStack_f8 = 0;
  local_100 = 0;
  uVar1 = **(uint **)(*(long *)(*(long *)param_1 + 0x10) + 8);
  rfx_property_get("persist.vendor.radio.simswitch",&local_100,&DAT_002240c5);
  iVar5 = atoi((char *)&local_100);
  uVar3 = iVar5 - 1;
  RfxBaseHandler::logD
            ((char *)this,"RmcRatSwHdlr","%s currMajorSim = %d, reqRaf=%d",
             "requestSetPreferredNetworkType",(ulong)uVar3,(ulong)uVar1);
  iVar5 = RmcNetworkHandler::isGcfTestMode();
  if ((iVar5 < 1) &&
     (iVar5 = RmcNetworkHandler::isTestSim((RmcNetworkHandler *)this,*(int *)(this + 0x10)),
     iVar5 < 1)) {
    uVar17 = 1;
  }
  else {
    uVar17 = 0;
  }
  uVar19 = 0x10;
  uVar16 = 4;
  if ((uVar17 & (uVar1 & 0x84000) != 0) == 0) {
    uVar16 = 0;
  }
  uVar18 = 0x80;
  if ((uVar17 & (uVar1 & 0x100000) >> 0x14) == 0) {
    uVar18 = uVar16;
  }
  puVar12 = &DAT_00293108;
  do {
    if (uVar1 == puVar12[-8]) {
      uVar19 = uVar19 - 0x10;
LAB_004952c0:
      uVar16 = (int)uVar19 - 0x1f;
      if (uVar16 < 3) {
        lVar15 = (ulong)uVar16 * 4;
        uVar19 = (ulong)*(uint *)(&UNK_00293170 + lVar15);
        uVar18 = *(uint *)(&UNK_0029317c + lVar15);
      }
      pRVar8 = (RfxMclStatusManager *)RfxBaseHandler::getNonSlotMclStatusManager();
      uVar7 = RfxMclStatusManager::getBoolValue(pRVar8,0x26,false);
      if ((uVar7 & 1) != 0) {
        RfxBaseHandler::logE
                  ((char *)this,"RmcRatSwHdlr","SetPreferredNetworkType: Skip retry in radio off");
        uVar11 = 2;
        goto LAB_004954c0;
      }
      iVar5 = RfxRilUtils::rfxGetSimCount();
      uVar20 = (undefined1)in_stack_fffffffffffffee8;
      if (iVar5 == 0) goto LAB_00495360;
      uVar16 = 0;
      goto LAB_00495330;
    }
    if (uVar1 == puVar12[-7]) {
      uVar19 = uVar19 - 0xf;
      goto LAB_004952c0;
    }
    if (uVar1 == puVar12[-6]) {
      uVar19 = uVar19 - 0xe;
      goto LAB_004952c0;
    }
    if (uVar1 == puVar12[-5]) {
      uVar19 = uVar19 - 0xd;
      goto LAB_004952c0;
    }
    if (uVar1 == puVar12[-4]) {
      uVar19 = uVar19 - 0xc;
      goto LAB_004952c0;
    }
    if (uVar1 == puVar12[-3]) {
      uVar19 = uVar19 - 0xb;
      goto LAB_004952c0;
    }
    if (uVar1 == puVar12[-2]) {
      uVar19 = uVar19 - 10;
      goto LAB_004952c0;
    }
    if (uVar1 == puVar12[-1]) {
      uVar19 = uVar19 - 9;
      goto LAB_004952c0;
    }
    if (uVar1 == *puVar12) {
      uVar19 = uVar19 - 8;
      goto LAB_004952c0;
    }
    if (uVar1 == puVar12[1]) {
      uVar19 = uVar19 - 7;
      goto LAB_004952c0;
    }
    if (uVar1 == puVar12[2]) {
      uVar19 = uVar19 - 6;
      goto LAB_004952c0;
    }
    if (uVar1 == puVar12[3]) {
      uVar19 = uVar19 - 5;
      goto LAB_004952c0;
    }
    if (uVar1 == puVar12[4]) {
      uVar19 = uVar19 - 4;
      goto LAB_004952c0;
    }
    if (uVar1 == puVar12[5]) {
      uVar19 = uVar19 - 3;
      goto LAB_004952c0;
    }
    if (uVar1 == puVar12[6]) {
      uVar19 = uVar19 - 2;
      goto LAB_004952c0;
    }
    if (uVar1 == puVar12[7]) {
      uVar19 = uVar19 - 1;
      goto LAB_004952c0;
    }
    if (uVar1 == puVar12[8]) goto LAB_004952c0;
    uVar19 = uVar19 + 0x11;
    puVar12 = puVar12 + 0x11;
  } while (uVar19 != 0x32);
  uVar11 = 0xd;
LAB_004954c0:
  *(undefined4 *)(this + 0x1c) = 1;
  pRVar8 = (RfxMclStatusManager *)RfxBaseHandler::getMclStatusManager((RfxBaseHandler *)this);
  local_98 = (RfxAtResponse *)CONCAT44(local_98._4_4_,1);
  local_90 = 0;
  RfxMclStatusManager::setValue(pRVar8,0x57,(RfxVariant *)&local_98,false,false);
  RfxVariant::deinit((RfxVariant *)&local_98);
  local_110 = (RfxAtResponse *)0x0;
  uVar2 = *(undefined4 *)(*(long *)param_1 + 0xc);
  RfxVoidData::RfxVoidData((RfxVoidData *)&local_98);
  plVar13 = *(long **)param_1;
  if (plVar13 != (long *)0x0) {
    android::RefBase::incStrong((void *)((long)plVar13 + *(long *)(*plVar13 + -0x18)));
  }
  RfxMclMessage::obtainResponse
            ((RfxMclMessage *)&local_110,uVar2,uVar11,&local_98,&stack0xfffffffffffffee8,0);
  if (plVar13 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)plVar13 + *(long *)(*plVar13 + -0x18)),&stack0xfffffffffffffee8);
  }
  RfxVoidData::~RfxVoidData((RfxVoidData *)&local_98);
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_110);
  if (local_110 == (RfxAtResponse *)0x0) goto LAB_00495590;
  lVar15 = *(long *)local_110;
  ppRVar9 = &local_110;
  pRVar14 = local_110;
  goto LAB_00495584;
  while( true ) {
    uVar16 = uVar16 + 1;
    uVar6 = RfxRilUtils::rfxGetSimCount();
    uVar20 = (undefined1)in_stack_fffffffffffffee8;
    if (uVar6 <= uVar16) break;
LAB_00495330:
    pRVar8 = (RfxMclStatusManager *)
             RfxBaseHandler::getMclStatusManager((RfxBaseHandler *)this,uVar16);
    iVar5 = RfxMclStatusManager::getIntValue(pRVar8,0x2b,0);
    if (0 < iVar5) {
      RfxBaseHandler::logE
                ((char *)this,"RmcRatSwHdlr","requestSetPreferredNetworkType: Skip retry in call");
      uVar11 = 0x36;
      goto LAB_004954c0;
    }
  }
LAB_00495360:
  android::String8::format("AT+ERAT=%d,%d",&local_110,uVar19 & 0xffffffff,(ulong)uVar18);
  RfxBaseHandler::atSendCommand((String8 *)this,&local_110);
  pRVar14 = local_108;
  if (local_108 != (RfxAtResponse *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)(local_108 + *(long *)(*(long *)local_108 + -0x18)),&local_108);
  }
  if (pRVar14 != local_108) {
    android::sp_report_race();
  }
  local_108 = local_98;
  local_98 = (RfxAtResponse *)0x0;
  android::String8::~String8((String8 *)&local_110);
  if ((-1 < *(int *)(local_108 + 0x28)) && (*(int *)(local_108 + 0xc) != 0)) {
    RfxBaseHandler::logV
              ((char *)this,"RmcRatSwHdlr",
               "SetPreferredNetworkType: ok, count=%d, MajorSim=%d, Preferred=%d",
               (ulong)*(uint *)(this + 0x1c),(ulong)uVar3,(ulong)uVar17);
    uVar11 = 0;
    *(uint *)(this + 0x18) = uVar1;
    pRVar14 = local_108;
joined_r0x00495410:
    local_108 = pRVar14;
    if (pRVar14 != (RfxAtResponse *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)(pRVar14 + *(long *)(*(long *)pRVar14 + -0x18)),&local_108);
    }
    if (pRVar14 != local_108) {
      android::sp_report_race();
    }
    local_108 = (RfxAtResponse *)0x0;
    goto LAB_004954c0;
  }
  uVar1 = *(uint *)(this + 0x1c);
  uVar19 = RfxAtResponse::atGetCmeError(local_108);
  RfxBaseHandler::logE
            ((char *)this,"RmcRatSwHdlr",
             "SetPreferredNetworkType: fail, count=%d, error=%d, MajorSim=%d",(ulong)uVar1,
             uVar19 & 0xffffffff,(ulong)uVar3);
  iVar5 = RfxAtResponse::atGetCmeError(local_108);
  if (iVar5 == 4) {
    RfxBaseHandler::logE
              ((char *)this,"RmcRatSwHdlr",
               "SetPreferredNetworkType: fail, modem not support dual C2K RAT");
    uVar11 = 0xd;
    pRVar14 = local_108;
    goto joined_r0x00495410;
  }
  uVar1 = *(uint *)(this + 0x1c);
  if (0x6c < (int)uVar1) {
    iVar5 = RfxAtResponse::atGetCmeError(local_108);
    uVar11 = 0x36;
    pRVar14 = local_108;
    if (iVar5 != 3) {
      uVar11 = 0xd;
    }
    goto joined_r0x00495410;
  }
  *(uint *)(this + 0x1c) = uVar1 + 1;
  if ((int)uVar1 < 10) {
    pvVar10 = (void *)0x5f5e100;
  }
  else if (uVar1 < 0x14) {
    pvVar10 = (void *)0xbebc200;
  }
  else {
    pvVar10 = (void *)0x3b9aca00;
    if (0x38 < uVar1) {
      pvVar10 = (void *)0x12a05f200;
    }
  }
  lVar15 = *(long *)param_1;
  local_98 = (RfxAtResponse *)0x0;
  RfxMclMessage::obtainRequest
            ((RfxMclMessage *)(ulong)*(uint *)(lVar15 + 0xc),(int)*(undefined8 *)(lVar15 + 0x10),
             (RfxBaseData *)(ulong)*(uint *)(lVar15 + 0x1c),*(int *)(lVar15 + 0x24),
             (uint)*(byte *)(lVar15 + 0xa3),SUB81(*(undefined8 *)(lVar15 + 0x60),0),pvVar10,
             *(long *)(lVar15 + 0xa8),
             CONCAT71(in_stack_fffffffffffffee1,*(undefined1 *)(lVar15 + 0xb0)),(bool)uVar20);
  RfxMclDispatcherThread::enqueueMclMessageDelay((sp *)&local_98);
  if (local_98 == (RfxAtResponse *)0x0) goto LAB_00495590;
  lVar15 = *(long *)local_98;
  ppRVar9 = &local_98;
  pRVar14 = local_98;
LAB_00495584:
  android::RefBase::decStrong((RefBase *)(pRVar14 + *(long *)(lVar15 + -0x18)),ppRVar9);
LAB_00495590:
  if (local_108 != (RfxAtResponse *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)(local_108 + *(long *)(*(long *)local_108 + -0x18)),&local_108);
  }
  if (*(long *)(lVar4 + 0x28) == local_68) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


