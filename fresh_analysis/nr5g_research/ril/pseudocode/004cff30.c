
/* RmcCommSimRequestHandler::handleTfnRsuGetImei(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcCommSimRequestHandler::handleTfnRsuGetImei(RmcCommSimRequestHandler *this,sp *param_1)

{
  undefined4 uVar1;
  long lVar2;
  RfxAtResponse *pRVar3;
  uint uVar4;
  uint *__ptr;
  ulong uVar5;
  char *pcVar6;
  long **pplVar7;
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
              ,0x2633);
    }
    else {
      local_60[0] = (RfxAtResponse *)0x0;
      android::String8::format("%s%s",local_60,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_60[0],"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/mdcomm/sim/RmcCommSimRequestHandler.cpp"
              ,0x2633);
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
    pcVar8 = *(char **)(this + 0x10);
    pcVar6 = "[RSU-SIMLOCK] handleTfnRsuGetImei atResponse is null";
    goto LAB_004d0284;
  }
  uVar5 = RfxAtResponse::isAtResponseFail(local_60[0]);
  if ((uVar5 & 1) != 0) {
    uVar4 = RfxAtResponse::atGetCmeError(local_78);
    RfxBaseHandler::logE
              ((char *)this,*(char **)(this + 0x10),"[RSU-SIMLOCK] handleTfnRsuGetImei errCode = %d"
               ,(ulong)uVar4);
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
      pplVar7 = &local_90;
      plVar10 = local_90;
LAB_004d0100:
      android::RefBase::decStrong((RefBase *)((long)plVar10 + *(long *)(lVar9 + -0x18)),pplVar7);
    }
LAB_004d010c:
    RfxRsuResponseData::~RfxRsuResponseData((RfxRsuResponseData *)local_60);
    RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_80);
    free(__ptr);
    goto joined_r0x004d0348;
  }
  this_00 = *(RfxAtLine **)(local_78 + 0x20);
  if (this_00 == (RfxAtLine *)0x0) {
    pcVar8 = *(char **)(this + 0x10);
    pcVar6 = "[RSU-SIMLOCK] handleTfnRsuGetImei pLine is null";
LAB_004d0284:
    RfxBaseHandler::logE((char *)this,pcVar8,pcVar6);
  }
  else {
    RfxAtLine::atTokStart(this_00,&local_64);
    if (local_64 < 0) {
      pcVar8 = *(char **)(this + 0x10);
      pcVar6 = "[RSU-SIMLOCK] handleTfnRsuGetImei atTokStart error";
      goto LAB_004d0284;
    }
    uVar4 = RfxAtLine::atTokNextint(this_00,&local_64);
    if ((local_64 < 0) || (uVar4 != *puVar11)) {
      pcVar8 = *(char **)(this + 0x10);
      pcVar6 = 
      "[RSU-SIMLOCK] handleTfnRsuGetImei err < 0 or opId = %d not equal ot request opId = %d";
    }
    else {
      uVar4 = RfxAtLine::atTokNextint(this_00,&local_64);
      if ((-1 < local_64) && (uVar4 == puVar11[1])) {
        pcVar6 = (char *)RfxAtLine::atTokNextstr(this_00,&local_64);
        if ((pcVar6 != (char *)0x0) && (-1 < local_64)) {
          *(char **)(__ptr + 4) = pcVar6;
          printReducedLog(this,"[RSU-SIMLOCK] handleTfnRsuGetImei ",pcVar6);
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
            pplVar7 = &local_98;
            plVar10 = local_98;
            goto LAB_004d0100;
          }
          goto LAB_004d010c;
        }
        pcVar8 = *(char **)(this + 0x10);
        pcVar6 = "[RSU-SIMLOCK] handleTfnRsuGetImei err < 0 or imei is null";
        goto LAB_004d0284;
      }
      pcVar8 = *(char **)(this + 0x10);
      pcVar6 = 
      "[RSU-SIMLOCK] handleTfnRsuGetImei err < 0 or requestId = %d not equal ot request requestId = %d"
      ;
    }
    RfxBaseHandler::logE((char *)this,pcVar8,pcVar6);
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
joined_r0x004d0348:
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


