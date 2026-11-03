
/* RfxMessage::obtainResponse(int, int, RIL_Errno, RfxBaseData const&, android::sp<RfxMessage>
   const&) */

void __thiscall
RfxMessage::obtainResponse
          (undefined8 *param_1_00,undefined4 param_2_00,int param_1,undefined4 param_2,
          RfxBaseData *param_5,long *param_6)

{
  undefined4 uVar1;
  long lVar2;
  long *plVar3;
  undefined *puVar4;
  long *plVar5;
  RfxMessage *this;
  ulong uVar6;
  long lVar7;
  undefined8 local_78;
  long *local_70;
  long local_68;
  
  lVar2 = tpidr_el0;
  local_68 = *(long *)(lVar2 + 0x28);
  local_70 = (long *)0x0;
  plVar5 = operator_new(0xb8,(nothrow_t *)PTR_nothrow_005d1aa0);
  plVar3 = local_70;
  if (plVar5 != (long *)0x0) {
    android::RefBase::RefBase((RefBase *)(plVar5 + 0x15));
    plVar3 = local_70;
    puVar4 = PTR_vtable_005d1a60;
    *(undefined8 *)((long)plVar5 + 0x34) = 0;
    *(undefined8 *)((long)plVar5 + 0x2c) = 0;
    plVar5[9] = 0;
    *(undefined4 *)(plVar5 + 5) = 0xffffffff;
    *(undefined4 *)(plVar5 + 0xb) = 0;
    plVar5[2] = 0;
    plVar5[1] = 0;
    plVar5[4] = 0;
    plVar5[3] = 0;
    *plVar5 = (long)(puVar4 + 0x18);
    plVar5[0x15] = (long)(puVar4 + 0x60);
    *(undefined2 *)(plVar5 + 0x11) = 0;
    *(undefined8 *)((long)plVar5 + 0x3c) = 0x200000000;
    plVar5[10] = -0x100000000;
    *(undefined1 *)((long)plVar5 + 0x8a) = 0;
    plVar5[0x12] = 0;
    *(undefined1 *)(plVar5 + 0x13) = 0;
    *(undefined4 *)((long)plVar5 + 0x9c) = 0;
    *(undefined1 *)(plVar5 + 0x14) = 0;
    *(undefined4 *)((long)plVar5 + 0xa4) = 1;
    android::RefBase::incStrong(plVar5 + 0x15);
  }
  if (plVar3 != (long *)0x0) {
    android::RefBase::decStrong((RefBase *)((long)plVar3 + *(long *)(*plVar3 + -0x18)),&local_70);
  }
  if (plVar3 != local_70) {
    android::sp_report_race();
  }
  local_70 = plVar5;
  if (plVar5 == (long *)0x0) {
    this = (RfxMessage *)RfxRilUtils::isEngLoad();
    if ((int)this != 0) {
      uVar6 = __rfx_is_gt_mode();
      if ((uVar6 & 1) == 0) {
        mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
                "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/base/RfxMessage.cpp"
                ,0x18e);
      }
      else {
        local_78 = 0;
        android::String8::format("%s%s",&local_78,&DAT_001fbebf,"RFX_ASSERT");
        mtkLogE(local_78,"RFX_ASSERT:%s, %d",
                "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/base/RfxMessage.cpp"
                ,0x18e);
        android::String8::~String8((String8 *)&local_78);
      }
      mtkAssert(0);
                    /* WARNING: Subroutine does not return */
      exit(0);
    }
    doTrm(this);
  }
  else {
    lVar7 = *param_6;
    *(undefined4 *)((long)plVar5 + 0x2c) = param_2;
    *(undefined4 *)(plVar5 + 1) = 1;
    uVar1 = *(undefined4 *)(lVar7 + 0xc);
    *(undefined4 *)((long)plVar5 + 0x24) = param_2_00;
    *(int *)((long)plVar5 + 0x14) = param_1;
    *(undefined4 *)((long)plVar5 + 0xc) = uVar1;
    *(undefined4 *)(plVar5 + 3) = *(undefined4 *)(lVar7 + 0x18);
    *(int *)((long)plVar5 + 0x1c) = param_1;
    *(undefined4 *)(plVar5 + 4) = *(undefined4 *)(lVar7 + 0x20);
    lVar7 = systemTime(1);
    local_70[6] = lVar7;
    *(undefined4 *)(local_70 + 5) = *(undefined4 *)(*param_6 + 0x28);
    local_70[0x12] = *(long *)(*param_6 + 0x90);
    lVar7 = RfxDataCloneManager::copyData(param_1,param_5,1);
    local_70[9] = lVar7;
    *param_1_00 = local_70;
    if (*(long *)(lVar2 + 0x28) == local_68) {
      return;
    }
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


