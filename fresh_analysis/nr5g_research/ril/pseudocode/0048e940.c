
/* WARNING: Removing unreachable block (ram,0x0048eb00) */
/* RmcNetworkRequestHandler::updateNrDisplayRule(bool, int, int, int) */

void __thiscall
RmcNetworkRequestHandler::updateNrDisplayRule
          (RmcNetworkRequestHandler *this,bool param_1,int param_2,int param_3,int param_4)

{
  long lVar1;
  undefined *puVar2;
  undefined *puVar3;
  undefined *puVar4;
  undefined8 uVar5;
  long *plVar6;
  RfxBaseData *pRVar7;
  ulong uVar8;
  int iVar9;
  long lVar10;
  ulong uVar11;
  uint uVar12;
  ulong uVar13;
  undefined *puVar14;
  long *local_98;
  undefined8 local_90 [3];
  undefined8 local_78;
  undefined4 local_70;
  long local_68;
  
  puVar4 = PTR_nr_display_rule_005d3df8;
  puVar14 = PTR_jp_mode_timer2_delay_timer_005d3de0;
  puVar3 = PTR_jp_mode_timer1_delay_timer_005d3db8;
  lVar1 = tpidr_el0;
  local_68 = *(long *)(lVar1 + 0x28);
  lVar10 = (ulong)*(uint *)(this + 0x10) * 4;
  RfxBaseHandler::logV
            ((char *)this,"RmcNwReqHdlr",
             "updateNrDisplayRule want to set value: %d, %d, %d,current value:%d, %d, %d, bySlot %d"
             ,(ulong)(uint)param_2,(ulong)(uint)param_3,(ulong)(uint)param_4,
             (ulong)*(uint *)(PTR_nr_display_rule_005d3df8 + lVar10),
             (ulong)*(uint *)(PTR_jp_mode_timer1_delay_timer_005d3db8 + lVar10),
             *(undefined4 *)(PTR_jp_mode_timer2_delay_timer_005d3de0 + lVar10),(uint)param_1);
  uVar11 = (ulong)*(uint *)(this + 0x10);
  iVar9 = *(int *)(puVar4 + uVar11 * 4);
  uVar13 = (ulong)(iVar9 != param_2);
  if (iVar9 == param_2) {
    if (param_2 != 4) goto LAB_0048eae8;
    if (*(int *)(puVar3 + uVar11 * 4) == param_3) {
      uVar13 = (ulong)(*(int *)(puVar14 + uVar11 * 4) != param_4);
    }
    else {
      uVar13 = 1;
    }
joined_r0x0048ea78:
    if (0x3c < (uint)param_3) goto LAB_0048ebc4;
  }
  else {
    if (iVar9 == 4) {
      RmcNetworkHandler::stopJpModeTimer1((RmcNetworkHandler *)this);
      RmcNetworkHandler::stopJpModeTimer2((RmcNetworkHandler *)this);
    }
    local_90[0] = 0;
    android::String8::String8((String8 *)local_90,"persist.vendor.radio.nr_display_rule");
    if (*(int *)(this + 0x10) == 0) {
      android::String8::append((char *)local_90);
    }
    else {
      android::String8::format(".%d",&local_78,(ulong)(*(int *)(this + 0x10) + 1));
      android::String8::append((char *)local_90);
      android::String8::~String8((String8 *)&local_78);
    }
    uVar5 = local_90[0];
    android::String8::format("%d",&local_78,(ulong)(uint)param_2);
    rfx_property_set(uVar5,local_78);
    android::String8::~String8((String8 *)&local_78);
    *(int *)(puVar4 + (ulong)*(uint *)(this + 0x10) * 4) = param_2;
    android::String8::~String8((String8 *)local_90);
    uVar11 = (ulong)*(uint *)(this + 0x10);
    param_2 = *(int *)(puVar4 + uVar11 * 4);
    puVar14 = PTR_jp_mode_timer2_delay_timer_005d3de0;
LAB_0048eae8:
    if (1 < (uint)param_2) {
      if ((param_2 != 4) && ((param_2 & 2U) != 0)) {
        param_3 = 0;
        param_4 = 0;
      }
      goto joined_r0x0048ea78;
    }
    param_4 = 0;
    param_3 = 0;
  }
  if (*(uint *)(puVar3 + uVar11 * 4) != param_3) {
    local_90[0] = 0;
    android::String8::String8((String8 *)local_90,"persist.vendor.radio.jp_mode_timer1_delay_timer")
    ;
    if (*(int *)(this + 0x10) == 0) {
      android::String8::append((char *)local_90);
    }
    else {
      android::String8::format(".%d",&local_78,(ulong)(*(int *)(this + 0x10) + 1));
      android::String8::append((char *)local_90);
      android::String8::~String8((String8 *)&local_78);
    }
    uVar5 = local_90[0];
    android::String8::format("%d",&local_78,(ulong)(uint)param_3);
    rfx_property_set(uVar5,local_78);
    android::String8::~String8((String8 *)&local_78);
    *(int *)(puVar3 + (ulong)*(uint *)(this + 0x10) * 4) = param_3;
    android::String8::~String8((String8 *)local_90);
    uVar11 = (ulong)*(uint *)(this + 0x10);
  }
LAB_0048ebc4:
  uVar8 = (ulong)*(uint *)(puVar14 + uVar11 * 4);
  if (((uint)param_4 < 0x3d) && (*(uint *)(puVar14 + uVar11 * 4) != param_4)) {
    local_90[0] = 0;
    android::String8::String8((String8 *)local_90,"persist.vendor.radio.jp_mode_timer2_delay_timer")
    ;
    if (*(int *)(this + 0x10) == 0) {
      android::String8::append((char *)local_90);
    }
    else {
      android::String8::format(".%d",&local_78,(ulong)(*(int *)(this + 0x10) + 1));
      android::String8::append((char *)local_90);
      android::String8::~String8((String8 *)&local_78);
    }
    uVar5 = local_90[0];
    android::String8::format("%d",&local_78,(ulong)(uint)param_4);
    rfx_property_set(uVar5,local_78);
    android::String8::~String8((String8 *)&local_78);
    *(int *)(puVar14 + (ulong)*(uint *)(this + 0x10) * 4) = param_4;
    android::String8::~String8((String8 *)local_90);
    uVar11 = (ulong)*(uint *)(this + 0x10);
    uVar8 = (ulong)*(uint *)(puVar14 + uVar11 * 4);
  }
  pRVar7 = (RfxBaseData *)(ulong)*(uint *)(puVar4 + uVar11 * 4);
  RfxBaseHandler::logV
            ((char *)this,"RmcNwReqHdlr",
             "updateNrDisplayRule set value done,%d, %d, %d, isRuleChange:%d",pRVar7,
             (ulong)*(uint *)(puVar3 + uVar11 * 4),uVar8,uVar13);
  if ((int)uVar13 != 0) {
    updateConnectionStateReport(this,2);
    iVar9 = *(int *)(this + 0x10);
    local_78 = (long *)0x0;
    RfxVoidData::RfxVoidData((RfxVoidData *)local_90);
    RfxMclMessage::obtainUrc((RfxMclMessage *)0xc7a9,iVar9,(int)local_90,pRVar7);
    plVar6 = local_78;
    if (local_78 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
    }
    if (plVar6 != local_78) {
      android::sp_report_race();
    }
    local_78 = local_98;
    RfxVoidData::~RfxVoidData((RfxVoidData *)local_90);
    RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_78);
    if (local_78 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
    }
  }
  puVar2 = PTR_RFX_SLOT_COUNT_005d2270;
  if (param_1) {
    RfxBaseHandler::logV
              ((char *)this,"RmcNwReqHdlr",
               "updateNrDisplayRule BY SLOT set rule, all done now return");
  }
  else {
    iVar9 = *(int *)PTR_RFX_SLOT_COUNT_005d2270;
    if (0 < iVar9) {
      uVar12 = 0;
      do {
        if (uVar12 != *(uint *)(this + 0x10)) {
          RfxBaseHandler::logV
                    ((char *)this,"RmcNwReqHdlr",
                     "updateNrDisplayRule, sendEvent to slotId %d for sync 5g icon rule",
                     (ulong)uVar12);
          lVar10 = (ulong)*(uint *)(this + 0x10) * 4;
          local_70 = *(undefined4 *)(puVar14 + lVar10);
          local_78 = (long *)CONCAT44(*(undefined4 *)(puVar3 + lVar10),
                                      *(undefined4 *)(puVar4 + lVar10));
          RfxIntsData::RfxIntsData((RfxIntsData *)local_90,(int *)&local_78,3);
          RfxBaseHandler::sendEvent
                    ((RfxBaseHandler *)this,0xc7da,local_90,1,uVar12,0xffffffff,0xffffffff,0,1);
          RfxIntsData::~RfxIntsData((RfxIntsData *)local_90);
          iVar9 = *(int *)puVar2;
        }
        uVar12 = uVar12 + 1;
      } while ((int)uVar12 < iVar9);
    }
  }
  if (*(long *)(lVar1 + 0x28) != local_68) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}


