
/* RmcCommSimRequestHandler::handleCommonRsuGetLockStatus(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcCommSimRequestHandler::handleCommonRsuGetLockStatus(RmcCommSimRequestHandler *this,sp *param_1)

{
  undefined4 uVar1;
  long lVar2;
  long *plVar3;
  int iVar4;
  undefined4 *__ptr;
  RfxMclStatusManager *this_00;
  long lVar5;
  ulong uVar6;
  uchar uVar7;
  char *pcVar8;
  undefined4 *puVar9;
  long *local_d8;
  long *local_d0;
  int local_c8;
  int local_c4;
  long *local_c0;
  undefined8 local_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 uStack_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined4 uStack_68;
  undefined4 local_64;
  undefined4 uStack_60;
  undefined8 uStack_5c;
  long local_48;
  
  lVar2 = tpidr_el0;
  local_48 = *(long *)(lVar2 + 0x28);
  local_b8 = 0;
  android::String8::String8((String8 *)&local_b8,"");
  local_c0 = (long *)0x0;
  puVar9 = *(undefined4 **)(*(long *)(*(long *)param_1 + 0x10) + 8);
  __ptr = calloc(1,0x38);
  local_c8 = 0;
  local_c4 = -1;
  if (__ptr == (undefined4 *)0x0) {
    uVar6 = __rfx_is_gt_mode();
    if ((uVar6 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/mdcomm/sim/RmcCommSimRequestHandler.cpp"
              ,0x2470);
    }
    else {
      local_b0 = 0;
      android::String8::format("%s%s",&local_b0,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_b0,"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/mdcomm/sim/RmcCommSimRequestHandler.cpp"
              ,0x2470);
      android::String8::~String8((String8 *)&local_b0);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  pcVar8 = *(char **)(this + 0x10);
  *__ptr = *puVar9;
  __ptr[1] = puVar9[1];
  RfxBaseHandler::logD((char *)this,pcVar8,"[RSU-SIMLOCK] handleCommonRsuGetLockStatus...");
  this_00 = (RfxMclStatusManager *)RfxBaseHandler::getNonSlotMclStatusManager();
  iVar4 = RfxMclStatusManager::getIntValue(this_00,0x8c,0);
  if (iVar4 == 0xb) {
LAB_004ce4ac:
    uVar7 = '\x05';
  }
  else {
    if (iVar4 == 0xf) {
      uVar7 = '\x03';
      goto LAB_004ce4b0;
    }
    uStack_5c = 0;
    uStack_60 = 0;
    uStack_98 = 0;
    local_a0 = 0;
    uStack_88 = 0;
    uStack_90 = 0;
    uStack_78 = 0;
    local_80 = 0;
    uStack_68 = 0;
    local_64 = 0;
    uStack_70 = 0;
    uStack_a8 = 0;
    local_b0 = 0;
    rfx_property_get("ro.vendor.sim_me_lock_mode",&local_b0,&DAT_00207797);
    lVar5 = __strlen_chk(&local_b0,0x5c);
    if (lVar5 != 0) {
      if ((int)local_b0 == 0x33303231 && local_b0._4_1_ == '\0') goto LAB_004ce4ac;
    }
    lVar5 = __strlen_chk(&local_b0,0x5c);
    if (lVar5 == 0) {
      uVar7 = 0xff;
    }
    else {
      uVar7 = '\x03';
      if ((int)local_b0 != 0x303038) {
        uVar7 = 0xff;
      }
    }
  }
LAB_004ce4b0:
  smlGetLockStatus(this,uVar7,&local_c8,&local_c4);
  pcVar8 = *(char **)(this + 0x10);
  __ptr[9] = local_c4;
  RfxBaseHandler::logD
            ((char *)this,pcVar8,"[RSU-SIMLOCK] handleCommonRsuGetLockStatus, status=0X%X");
  uVar1 = *(undefined4 *)(*(long *)param_1 + 0xc);
  RfxRsuResponseData::RfxRsuResponseData((RfxRsuResponseData *)&local_b0,__ptr,0x38);
  local_d8 = *(long **)param_1;
  if (local_d8 != (long *)0x0) {
    android::RefBase::incStrong((void *)((long)local_d8 + *(long *)(*local_d8 + -0x18)));
  }
  RfxMclMessage::obtainResponse((RfxMclMessage *)&local_d0,uVar1,0,&local_b0,&local_d8,0);
  plVar3 = local_c0;
  if (local_c0 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_c0 + *(long *)(*local_c0 + -0x18)),&local_c0);
  }
  if (plVar3 != local_c0) {
    android::sp_report_race();
  }
  local_c0 = local_d0;
  local_d0 = (long *)0x0;
  if (local_d8 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_d8 + *(long *)(*local_d8 + -0x18)),&local_d8);
  }
  RfxRsuResponseData::~RfxRsuResponseData((RfxRsuResponseData *)&local_b0);
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_c0);
  free(__ptr);
  if (local_c0 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_c0 + *(long *)(*local_c0 + -0x18)),&local_c0);
  }
  android::String8::~String8((String8 *)&local_b8);
  if (*(long *)(lVar2 + 0x28) != local_48) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}


