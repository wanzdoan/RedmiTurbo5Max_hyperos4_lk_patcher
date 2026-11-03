
/* RmcCommSimRequestHandler::handleTfnRsuGenerateRequest(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcCommSimRequestHandler::handleTfnRsuGenerateRequest(RmcCommSimRequestHandler *this,sp *param_1)

{
  uint uVar1;
  undefined4 uVar2;
  long lVar3;
  RfxAtResponse *pRVar4;
  uint uVar5;
  uint *__ptr;
  ulong uVar6;
  char *pcVar7;
  long **pplVar8;
  char *pcVar9;
  long lVar10;
  long *plVar11;
  RfxAtLine *this_00;
  uint *puVar12;
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
  
  lVar3 = tpidr_el0;
  local_48 = *(long *)(lVar3 + 0x28);
  local_64 = 0;
  local_70 = 0;
  android::String8::String8((String8 *)&local_70,"");
  local_80 = (long *)0x0;
  local_78 = (RfxAtResponse *)0x0;
  puVar12 = *(uint **)(*(long *)(*(long *)param_1 + 0x10) + 8);
  __ptr = calloc(1,0x38);
  if (__ptr == (uint *)0x0) {
    uVar6 = __rfx_is_gt_mode();
    if ((uVar6 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/mdcomm/sim/RmcCommSimRequestHandler.cpp"
              ,0x2753);
    }
    else {
      local_60[0] = (RfxAtResponse *)0x0;
      android::String8::format("%s%s",local_60,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_60[0],"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/mdcomm/sim/RmcCommSimRequestHandler.cpp"
              ,0x2753);
      android::String8::~String8((String8 *)local_60);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  uVar5 = *puVar12;
  *__ptr = uVar5;
  uVar1 = puVar12[1];
  __ptr[1] = uVar1;
  android::String8::format
            ("AT+ESMLRSU=%d,%d,\"%s\"",local_60,(ulong)uVar5,(ulong)uVar1,
             *(undefined8 *)(puVar12 + 4));
  android::String8::append((String8 *)&local_70);
  android::String8::~String8((String8 *)local_60);
  RfxBaseHandler::logD
            ((char *)this,*(char **)(this + 0x10),"[RSU-SIMLOCK] AT+ESMLRSU=%d,%d",(ulong)*puVar12,
             (ulong)puVar12[1]);
  printReducedLog(this,"[RSU-SIMLOCK] handleTfnRsuGenerateRequest ",*(char **)(puVar12 + 4));
  RfxBaseHandler::atSendCommandSingleline((String8 *)this,(char *)&local_70,"+ESMLRSU:");
  pRVar4 = local_78;
  if (local_78 != (RfxAtResponse *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)(local_78 + *(long *)(*(long *)local_78 + -0x18)),&local_78);
  }
  if (pRVar4 != local_78) {
    android::sp_report_race();
  }
  local_78 = local_60[0];
  if (local_60[0] == (RfxAtResponse *)0x0) {
    pcVar9 = *(char **)(this + 0x10);
    pcVar7 = "[RSU-SIMLOCK] handleTfnRsuGenerateRequest atResponse is null";
    goto LAB_004d126c;
  }
  uVar6 = RfxAtResponse::isAtResponseFail(local_60[0]);
  if ((uVar6 & 1) != 0) {
    uVar5 = RfxAtResponse::atGetCmeError(local_78);
    RfxBaseHandler::logE
              ((char *)this,*(char **)(this + 0x10),
               "[RSU-SIMLOCK] handleTfnRsuGenerateRequest errCode = %d",(ulong)uVar5);
    lVar10 = *(long *)param_1;
    __ptr[3] = uVar5;
    uVar2 = *(undefined4 *)(lVar10 + 0xc);
    RfxRsuResponseData::RfxRsuResponseData((RfxRsuResponseData *)local_60,__ptr,0x38);
    local_90 = *(long **)param_1;
    if (local_90 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_90 + *(long *)(*local_90 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_88,uVar2,2,local_60,&local_90,0);
    plVar11 = local_80;
    if (local_80 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_80 + *(long *)(*local_80 + -0x18)),&local_80);
    }
    if (plVar11 != local_80) {
      android::sp_report_race();
    }
    local_80 = local_88;
    local_88 = (long *)0x0;
    if (local_90 != (long *)0x0) {
      lVar10 = *local_90;
      pplVar8 = &local_90;
      plVar11 = local_90;
LAB_004d10e8:
      android::RefBase::decStrong((RefBase *)((long)plVar11 + *(long *)(lVar10 + -0x18)),pplVar8);
    }
LAB_004d10f4:
    RfxRsuResponseData::~RfxRsuResponseData((RfxRsuResponseData *)local_60);
    RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_80);
    free(__ptr);
    goto joined_r0x004d1330;
  }
  this_00 = *(RfxAtLine **)(local_78 + 0x20);
  if (this_00 == (RfxAtLine *)0x0) {
    pcVar9 = *(char **)(this + 0x10);
    pcVar7 = "[RSU-SIMLOCK] handleTfnRsuGenerateRequest pLine is null";
LAB_004d126c:
    RfxBaseHandler::logE((char *)this,pcVar9,pcVar7);
  }
  else {
    RfxAtLine::atTokStart(this_00,&local_64);
    if (local_64 < 0) {
      pcVar9 = *(char **)(this + 0x10);
      pcVar7 = "[RSU-SIMLOCK] handleTfnRsuGenerateRequest atTokStart error";
      goto LAB_004d126c;
    }
    uVar5 = RfxAtLine::atTokNextint(this_00,&local_64);
    if ((local_64 < 0) || (uVar5 != *puVar12)) {
      pcVar9 = *(char **)(this + 0x10);
      pcVar7 = 
      "[RSU-SIMLOCK] handleTfnRsuGenerateRequest err < 0 or opId = %d not equal ot request opId = %d"
      ;
    }
    else {
      uVar5 = RfxAtLine::atTokNextint(this_00,&local_64);
      if ((-1 < local_64) && (uVar5 == puVar12[1])) {
        pcVar7 = (char *)RfxAtLine::atTokNextstr(this_00,&local_64);
        if ((pcVar7 != (char *)0x0) && (-1 < local_64)) {
          *(char **)(__ptr + 4) = pcVar7;
          printReducedLog(this,"[RSU-SIMLOCK] handleTfnRsuGenerateRequest ",pcVar7);
          uVar2 = *(undefined4 *)(*(long *)param_1 + 0xc);
          RfxRsuResponseData::RfxRsuResponseData((RfxRsuResponseData *)local_60,__ptr,0x38);
          local_98 = *(long **)param_1;
          if (local_98 != (long *)0x0) {
            android::RefBase::incStrong((void *)((long)local_98 + *(long *)(*local_98 + -0x18)));
          }
          RfxMclMessage::obtainResponse((RfxMclMessage *)&local_88,uVar2,0,local_60,&local_98,0);
          android::sp<RfxMclMessage>::operator=((sp<RfxMclMessage> *)&local_80,(sp *)&local_88);
          if (local_88 != (long *)0x0) {
            android::RefBase::decStrong
                      ((RefBase *)((long)local_88 + *(long *)(*local_88 + -0x18)),&local_88);
          }
          if (local_98 != (long *)0x0) {
            lVar10 = *local_98;
            pplVar8 = &local_98;
            plVar11 = local_98;
            goto LAB_004d10e8;
          }
          goto LAB_004d10f4;
        }
        pcVar9 = *(char **)(this + 0x10);
        pcVar7 = "[RSU-SIMLOCK] handleTfnRsuGenerateRequest err < 0 or signedJwt is null";
        goto LAB_004d126c;
      }
      pcVar9 = *(char **)(this + 0x10);
      pcVar7 = 
      "[RSU-SIMLOCK] handleTfnRsuGenerateRequest err < 0 or requestId = %d not equal ot request requestId = %d"
      ;
    }
    RfxBaseHandler::logE((char *)this,pcVar9,pcVar7);
  }
  free(__ptr);
  uVar2 = *(undefined4 *)(*(long *)param_1 + 0xc);
  RfxVoidData::RfxVoidData((RfxVoidData *)local_60);
  local_a0 = *(long **)param_1;
  if (local_a0 != (long *)0x0) {
    android::RefBase::incStrong((void *)((long)local_a0 + *(long *)(*local_a0 + -0x18)));
  }
  RfxMclMessage::obtainResponse((RfxMclMessage *)&local_88,uVar2,2,local_60,&local_a0,0);
  plVar11 = local_80;
  if (local_80 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_80 + *(long *)(*local_80 + -0x18)),&local_80);
  }
  if (plVar11 != local_80) {
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
joined_r0x004d1330:
  if (local_80 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_80 + *(long *)(*local_80 + -0x18)),&local_80);
  }
  if (local_78 != (RfxAtResponse *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)(local_78 + *(long *)(*(long *)local_78 + -0x18)),&local_78);
  }
  android::String8::~String8((String8 *)&local_70);
  if (*(long *)(lVar3 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


