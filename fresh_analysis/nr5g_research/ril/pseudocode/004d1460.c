
/* RmcCommSimRequestHandler::handleTfnRsuRelock(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcCommSimRequestHandler::handleTfnRsuRelock(RmcCommSimRequestHandler *this,sp *param_1)

{
  undefined4 uVar1;
  long lVar2;
  RfxAtResponse *pRVar3;
  uint uVar4;
  uint *__ptr;
  ulong uVar5;
  long **pplVar6;
  char *pcVar7;
  char *pcVar8;
  long lVar9;
  long *plVar10;
  RfxAtLine *this_00;
  uint *puVar11;
  long *local_a0;
  long *local_98;
  long *local_90;
  long *local_88;
  long *local_80;
  RfxAtResponse *local_78;
  undefined8 local_70;
  int local_64;
  RfxAtResponse *local_60 [3];
  long local_48;
  
  lVar2 = tpidr_el0;
  local_48 = *(long *)(lVar2 + 0x28);
  local_64 = 0;
  local_70 = 0;
  android::String8::String8((String8 *)&local_70,"");
  local_80 = (long *)0x0;
  local_78 = (RfxAtResponse *)0x0;
  puVar11 = *(uint **)(*(long *)(*(long *)param_1 + 0x10) + 8);
  __ptr = calloc(1,0x38);
  if (__ptr == (uint *)0x0) {
    uVar5 = __rfx_is_gt_mode();
    if ((uVar5 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/mdcomm/sim/RmcCommSimRequestHandler.cpp"
              ,0x27b3);
    }
    else {
      local_60[0] = (RfxAtResponse *)0x0;
      android::String8::format("%s%s",local_60,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_60[0],"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/mdcomm/sim/RmcCommSimRequestHandler.cpp"
              ,0x27b3);
      android::String8::~String8((String8 *)local_60);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  *__ptr = *puVar11;
  __ptr[1] = puVar11[1];
  android::String8::format("AT+ESMLRSU=%d,%d",local_60);
  android::String8::append((String8 *)&local_70);
  android::String8::~String8((String8 *)local_60);
  RfxBaseHandler::logD
            ((char *)this,*(char **)(this + 0x10),"[RSU-SIMLOCK] AT+ESMLRSU=%d,%d",(ulong)*puVar11,
             (ulong)puVar11[1]);
  printReducedLog(this,"[RSU-SIMLOCK] handleTfnRsuRelock ",*(char **)(puVar11 + 4));
  RfxBaseHandler::atSendCommandSingleline((String8 *)this,(char *)&local_70,"+ESMLRSU:");
  pRVar3 = local_78;
  if (local_78 != (RfxAtResponse *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)(local_78 + *(long *)(*(long *)local_78 + -0x18)),&local_78);
  }
  if (pRVar3 != local_78) {
    android::sp_report_race();
  }
  local_78 = local_60[0];
  if (local_60[0] == (RfxAtResponse *)0x0) {
    pcVar7 = *(char **)(this + 0x10);
    pcVar8 = "[RSU-SIMLOCK] handleTfnRsuRelock atResponse is null";
    goto LAB_004d17c8;
  }
  uVar5 = RfxAtResponse::isAtResponseFail(local_60[0]);
  if ((uVar5 & 1) != 0) {
    uVar4 = RfxAtResponse::atGetCmeError(local_78);
    RfxBaseHandler::logE
              ((char *)this,*(char **)(this + 0x10),"[RSU-SIMLOCK] handleTfnRsuRelock errCode = %d",
               (ulong)uVar4);
    lVar9 = *(long *)param_1;
    __ptr[3] = uVar4;
    uVar1 = *(undefined4 *)(lVar9 + 0xc);
    RfxRsuResponseData::RfxRsuResponseData((RfxRsuResponseData *)local_60,__ptr,0x38);
    local_90 = *(long **)param_1;
    if (local_90 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_90 + *(long *)(*local_90 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_88,uVar1,2,local_60,&local_90,0);
    plVar10 = local_80;
    if (local_80 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_80 + *(long *)(*local_80 + -0x18)),&local_80);
    }
    if (plVar10 != local_80) {
      android::sp_report_race();
    }
    local_80 = local_88;
    local_88 = (long *)0x0;
    if (local_90 != (long *)0x0) {
      lVar9 = *local_90;
      pplVar6 = &local_90;
      plVar10 = local_90;
LAB_004d1644:
      android::RefBase::decStrong((RefBase *)((long)plVar10 + *(long *)(lVar9 + -0x18)),pplVar6);
    }
LAB_004d1650:
    RfxRsuResponseData::~RfxRsuResponseData((RfxRsuResponseData *)local_60);
    RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_80);
    free(__ptr);
    goto joined_r0x004d188c;
  }
  this_00 = *(RfxAtLine **)(local_78 + 0x20);
  if (this_00 == (RfxAtLine *)0x0) {
    pcVar7 = *(char **)(this + 0x10);
    pcVar8 = "[RSU-SIMLOCK] handleTfnRsuRelock pLine is null";
LAB_004d17c8:
    RfxBaseHandler::logE((char *)this,pcVar7,pcVar8);
  }
  else {
    RfxAtLine::atTokStart(this_00,&local_64);
    if (local_64 < 0) {
      pcVar7 = *(char **)(this + 0x10);
      pcVar8 = "[RSU-SIMLOCK] handleTfnRsuRelock atTokStart error";
      goto LAB_004d17c8;
    }
    uVar4 = RfxAtLine::atTokNextint(this_00,&local_64);
    if ((local_64 < 0) || (uVar4 != *puVar11)) {
      pcVar7 = *(char **)(this + 0x10);
      pcVar8 = 
      "[RSU-SIMLOCK] handleTfnRsuRelock err < 0 or opId = %d not equal ot request opId = %d";
    }
    else {
      uVar4 = RfxAtLine::atTokNextint(this_00,&local_64);
      if ((-1 < local_64) && (uVar4 == puVar11[1])) {
        uVar4 = RfxAtLine::atTokNextint(this_00,&local_64);
        __ptr[3] = uVar4;
        pcVar7 = *(char **)(this + 0x10);
        if (-1 < local_64) {
          RfxBaseHandler::logD
                    ((char *)this,pcVar7,"[RSU-SIMLOCK] handleTfnRsuRelock result = %d",(ulong)uVar4
                    );
          uVar1 = *(undefined4 *)(*(long *)param_1 + 0xc);
          RfxRsuResponseData::RfxRsuResponseData((RfxRsuResponseData *)local_60,__ptr,0x38);
          local_98 = *(long **)param_1;
          if (local_98 != (long *)0x0) {
            android::RefBase::incStrong((void *)((long)local_98 + *(long *)(*local_98 + -0x18)));
          }
          RfxMclMessage::obtainResponse((RfxMclMessage *)&local_88,uVar1,0,local_60,&local_98,0);
          android::sp<RfxMclMessage>::operator=((sp<RfxMclMessage> *)&local_80,(sp *)&local_88);
          if (local_88 != (long *)0x0) {
            android::RefBase::decStrong
                      ((RefBase *)((long)local_88 + *(long *)(*local_88 + -0x18)),&local_88);
          }
          if (local_98 != (long *)0x0) {
            lVar9 = *local_98;
            pplVar6 = &local_98;
            plVar10 = local_98;
            goto LAB_004d1644;
          }
          goto LAB_004d1650;
        }
        pcVar8 = "[RSU-SIMLOCK] handleTfnRsuRelock err < 0 on get result";
        goto LAB_004d17c8;
      }
      pcVar7 = *(char **)(this + 0x10);
      pcVar8 = 
      "[RSU-SIMLOCK] handleTfnRsuRelock err < 0 or requestId = %d not equal ot request requestId = %d"
      ;
    }
    RfxBaseHandler::logE((char *)this,pcVar7,pcVar8);
  }
  free(__ptr);
  uVar1 = *(undefined4 *)(*(long *)param_1 + 0xc);
  RfxVoidData::RfxVoidData((RfxVoidData *)local_60);
  local_a0 = *(long **)param_1;
  if (local_a0 != (long *)0x0) {
    android::RefBase::incStrong((void *)((long)local_a0 + *(long *)(*local_a0 + -0x18)));
  }
  RfxMclMessage::obtainResponse((RfxMclMessage *)&local_88,uVar1,2,local_60,&local_a0,0);
  plVar10 = local_80;
  if (local_80 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_80 + *(long *)(*local_80 + -0x18)),&local_80);
  }
  if (plVar10 != local_80) {
    android::sp_report_race();
  }
  local_80 = local_88;
  local_88 = (long *)0x0;
  if (local_a0 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_a0 + *(long *)(*local_a0 + -0x18)),&local_a0);
  }
  RfxVoidData::~RfxVoidData((RfxVoidData *)local_60);
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_80);
joined_r0x004d188c:
  if (local_80 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_80 + *(long *)(*local_80 + -0x18)),&local_80);
  }
  if (local_78 != (RfxAtResponse *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)(local_78 + *(long *)(*(long *)local_78 + -0x18)),&local_78);
  }
  android::String8::~String8((String8 *)&local_70);
  if (*(long *)(lVar2 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


