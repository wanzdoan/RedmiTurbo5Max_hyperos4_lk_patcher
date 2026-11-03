
/* RmcCommSimRequestHandler::handleCommonRsuGetCatInfo(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcCommSimRequestHandler::handleCommonRsuGetCatInfo(RmcCommSimRequestHandler *this,sp *param_1)

{
  undefined4 uVar1;
  long lVar2;
  long *plVar3;
  uint uVar4;
  uint uVar5;
  undefined4 *__ptr;
  ulong uVar6;
  char *pcVar7;
  uint uVar8;
  undefined4 *puVar9;
  long *local_180;
  long *local_178;
  long *local_170;
  undefined8 local_168;
  undefined8 local_160;
  undefined8 uStack_158;
  undefined8 uStack_150;
  undefined8 uStack_148;
  undefined8 local_140;
  undefined8 uStack_138;
  undefined8 uStack_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 uStack_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 uStack_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 uStack_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 uStack_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined8 uStack_68;
  long local_58;
  
  lVar2 = tpidr_el0;
  local_58 = *(long *)(lVar2 + 0x28);
  local_168 = 0;
  android::String8::String8((String8 *)&local_168,"");
  local_170 = (long *)0x0;
  puVar9 = *(undefined4 **)(*(long *)(*(long *)param_1 + 0x10) + 8);
  __ptr = calloc(1,0x38);
  if (__ptr == (undefined4 *)0x0) {
    uVar6 = __rfx_is_gt_mode();
    if ((uVar6 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/mdcomm/sim/RmcCommSimRequestHandler.cpp"
              ,0x23be);
    }
    else {
      local_160 = 0;
      android::String8::format("%s%s",&local_160,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_160,"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/mdcomm/sim/RmcCommSimRequestHandler.cpp"
              ,0x23be);
      android::String8::~String8((String8 *)&local_160);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  uVar8 = puVar9[6];
  if (6 < uVar8) {
    RfxBaseHandler::logE
              ((char *)this,*(char **)(this + 0x10),
               "[RSU-SIMLOCK] handleCommonRsuGetCatInfo invalid cat(%d)",(ulong)uVar8);
    uVar8 = 0;
  }
  pcVar7 = *(char **)(this + 0x10);
  *__ptr = *puVar9;
  __ptr[1] = puVar9[1];
  RfxBaseHandler::logD
            ((char *)this,pcVar7,"[RSU-SIMLOCK] handleCommonRsuGetCatInfo(%d)",(ulong)uVar8);
  uVar4 = smlGetCarrierId(this);
  if (((uVar4 & 0xff) == 7) || ((uVar4 & 0xfb) - 4 < 0xfffffffd)) {
    uStack_158 = 0;
    local_160 = 0;
    uStack_148 = 0;
    uStack_150 = 0;
    uStack_138 = 0;
    local_140 = 0;
    uStack_128 = 0;
    uStack_130 = 0;
    uStack_118 = 0;
    local_120 = 0;
    uStack_108 = 0;
    uStack_110 = 0;
    uStack_f8 = 0;
    local_100 = 0;
    uStack_e8 = 0;
    uStack_f0 = 0;
    uStack_d8 = 0;
    local_e0 = 0;
    uStack_c8 = 0;
    uStack_d0 = 0;
    uStack_b8 = 0;
    local_c0 = 0;
    uStack_a8 = 0;
    uStack_b0 = 0;
    uStack_98 = 0;
    local_a0 = 0;
    uStack_88 = 0;
    uStack_90 = 0;
    uStack_78 = 0;
    local_80 = 0;
    uStack_68 = 0;
    uStack_70 = 0;
    uVar4 = smlGetMaxRetry(this,uVar8);
    uVar5 = getPersoRemainRetry(this,uVar8,(RIL_SimMeLockInfo *)&local_160);
    uVar4 = (uVar5 & 0xff) << 0x10 |
            (uint)(*(int *)((long)&local_160 + (ulong)uVar8 * 0x1c + 4) == 1) << 0x1a |
            (uVar4 & 0xff) << 8;
  }
  else {
    RfxBaseHandler::logD
              ((char *)this,*(char **)(this + 0x10),
               "[RSU-SIMLOCK] handleCommonRsuGetCatInfo, RSU has no cat info");
    uVar4 = 0;
  }
  pcVar7 = *(char **)(this + 0x10);
  __ptr[9] = uVar4 | uVar8;
  RfxBaseHandler::logD((char *)this,pcVar7,"[RSU-SIMLOCK] handleCommonRsuGetCatInfo, status=0X%X");
  uVar1 = *(undefined4 *)(*(long *)param_1 + 0xc);
  RfxRsuResponseData::RfxRsuResponseData((RfxRsuResponseData *)&local_160,__ptr,0x38);
  local_180 = *(long **)param_1;
  if (local_180 != (long *)0x0) {
    android::RefBase::incStrong((void *)((long)local_180 + *(long *)(*local_180 + -0x18)));
  }
  RfxMclMessage::obtainResponse((RfxMclMessage *)&local_178,uVar1,0,&local_160,&local_180,0);
  plVar3 = local_170;
  if (local_170 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_170 + *(long *)(*local_170 + -0x18)),&local_170);
  }
  if (plVar3 != local_170) {
    android::sp_report_race();
  }
  local_170 = local_178;
  local_178 = (long *)0x0;
  if (local_180 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_180 + *(long *)(*local_180 + -0x18)),&local_180);
  }
  RfxRsuResponseData::~RfxRsuResponseData((RfxRsuResponseData *)&local_160);
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_170);
  free(__ptr);
  if (local_170 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_170 + *(long *)(*local_170 + -0x18)),&local_170);
  }
  android::String8::~String8((String8 *)&local_168);
  if (*(long *)(lVar2 + 0x28) == local_58) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


