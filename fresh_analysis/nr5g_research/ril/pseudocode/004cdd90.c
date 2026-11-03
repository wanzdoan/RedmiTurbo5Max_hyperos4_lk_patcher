
/* RmcCommSimRequestHandler::handleCommonRsuGetLockInfo(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcCommSimRequestHandler::handleCommonRsuGetLockInfo(RmcCommSimRequestHandler *this,sp *param_1)

{
  undefined4 uVar1;
  long lVar2;
  long *plVar3;
  byte bVar4;
  uint uVar5;
  int iVar6;
  uint uVar7;
  undefined4 *__ptr;
  RfxMclStatusManager *this_00;
  ulong uVar8;
  char *pcVar9;
  undefined4 *puVar10;
  long *local_98;
  long *local_90;
  int local_84;
  long *local_80;
  undefined8 local_78;
  undefined8 local_70 [3];
  long local_58;
  
  lVar2 = tpidr_el0;
  local_58 = *(long *)(lVar2 + 0x28);
  local_78 = 0;
  android::String8::String8((String8 *)&local_78,"");
  local_80 = (long *)0x0;
  puVar10 = *(undefined4 **)(*(long *)(*(long *)param_1 + 0x10) + 8);
  __ptr = calloc(1,0x38);
  local_84 = 0;
  if (__ptr == (undefined4 *)0x0) {
    uVar8 = __rfx_is_gt_mode();
    if ((uVar8 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/mdcomm/sim/RmcCommSimRequestHandler.cpp"
              ,0x2225);
    }
    else {
      local_70[0] = 0;
      android::String8::format("%s%s",local_70,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_70[0],"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/mdcomm/sim/RmcCommSimRequestHandler.cpp"
              ,0x2225);
      android::String8::~String8((String8 *)local_70);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  pcVar9 = *(char **)(this + 0x10);
  *__ptr = *puVar10;
  __ptr[1] = puVar10[1];
  RfxBaseHandler::logD((char *)this,pcVar9,"[RSU-SIMLOCK] handleCommonRsuGetLockInfo...");
  bVar4 = smlGetCarrierId(this);
  uVar5 = smlGetLockStatus(this,bVar4,&local_84,(int *)0x0);
  this_00 = (RfxMclStatusManager *)RfxBaseHandler::getNonSlotMclStatusManager();
  iVar6 = RfxMclStatusManager::getIntValue(this_00,0x8d,0);
  uVar7 = smlGetLockRule(this);
  pcVar9 = *(char **)(this + 0x10);
  uVar5 = (uint)bVar4 << 0x10 | (uVar5 & 0xf) << 0x18 | (uint)(iVar6 == 0) << 0x1c |
          local_84 << 0x1d | 0x7f;
  __ptr[8] = 1;
  __ptr[9] = uVar5;
  __ptr[10] = uVar7;
  RfxBaseHandler::logD
            ((char *)this,pcVar9,
             "[RSU-SIMLOCK] handleCommonRsuGetLockInfo, status=0X%X, rule=%d, version=%d",
             (ulong)uVar5,(ulong)uVar7,1);
  uVar1 = *(undefined4 *)(*(long *)param_1 + 0xc);
  RfxRsuResponseData::RfxRsuResponseData((RfxRsuResponseData *)local_70,__ptr,0x38);
  local_98 = *(long **)param_1;
  if (local_98 != (long *)0x0) {
    android::RefBase::incStrong((void *)((long)local_98 + *(long *)(*local_98 + -0x18)));
  }
  RfxMclMessage::obtainResponse((RfxMclMessage *)&local_90,uVar1,0,local_70,&local_98,0);
  plVar3 = local_80;
  if (local_80 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_80 + *(long *)(*local_80 + -0x18)),&local_80);
  }
  if (plVar3 != local_80) {
    android::sp_report_race();
  }
  local_80 = local_90;
  local_90 = (long *)0x0;
  if (local_98 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_98 + *(long *)(*local_98 + -0x18)),&local_98);
  }
  RfxRsuResponseData::~RfxRsuResponseData((RfxRsuResponseData *)local_70);
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_80);
  free(__ptr);
  if (local_80 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_80 + *(long *)(*local_80 + -0x18)),&local_80);
  }
  android::String8::~String8((String8 *)&local_78);
  if (*(long *)(lVar2 + 0x28) == local_58) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


