
/* RfxMessage::obtainSapUrc(int, int, RfxBaseData const&) */

void __thiscall
RfxMessage::obtainSapUrc(RfxMessage *this,int param_1,int param_2,RfxBaseData *param_3)

{
  long lVar1;
  long *plVar2;
  undefined *puVar3;
  long *plVar4;
  long lVar5;
  RfxMessage *this_00;
  ulong uVar6;
  undefined8 *in_x8;
  undefined8 local_68;
  long *local_60;
  long local_58;
  
  lVar1 = tpidr_el0;
  local_58 = *(long *)(lVar1 + 0x28);
  local_60 = (long *)0x0;
  plVar4 = operator_new(0xb8,(nothrow_t *)PTR_nothrow_005d1aa0);
  plVar2 = local_60;
  if (plVar4 != (long *)0x0) {
    android::RefBase::RefBase((RefBase *)(plVar4 + 0x15));
    plVar2 = local_60;
    puVar3 = PTR_vtable_005d1a60;
    *(undefined8 *)((long)plVar4 + 0x34) = 0;
    *(undefined8 *)((long)plVar4 + 0x2c) = 0;
    plVar4[9] = 0;
    *(undefined4 *)(plVar4 + 5) = 0xffffffff;
    *(undefined4 *)(plVar4 + 0xb) = 0;
    plVar4[2] = 0;
    plVar4[1] = 0;
    plVar4[4] = 0;
    plVar4[3] = 0;
    *plVar4 = (long)(puVar3 + 0x18);
    plVar4[0x15] = (long)(puVar3 + 0x60);
    *(undefined2 *)(plVar4 + 0x11) = 0;
    *(undefined8 *)((long)plVar4 + 0x3c) = 0x200000000;
    plVar4[10] = -0x100000000;
    *(undefined1 *)((long)plVar4 + 0x8a) = 0;
    plVar4[0x12] = 0;
    *(undefined1 *)(plVar4 + 0x13) = 0;
    *(undefined4 *)((long)plVar4 + 0x9c) = 0;
    *(undefined1 *)(plVar4 + 0x14) = 0;
    *(undefined4 *)((long)plVar4 + 0xa4) = 1;
    android::RefBase::incStrong(plVar4 + 0x15);
  }
  if (plVar2 != (long *)0x0) {
    android::RefBase::decStrong((RefBase *)((long)plVar2 + *(long *)(*plVar2 + -0x18)),&local_60);
  }
  if (plVar2 != local_60) {
    android::sp_report_race();
  }
  local_60 = plVar4;
  if (plVar4 == (long *)0x0) {
    this_00 = (RfxMessage *)RfxRilUtils::isEngLoad();
    if ((int)this_00 != 0) {
      uVar6 = __rfx_is_gt_mode();
      if ((uVar6 & 1) == 0) {
        mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
                "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/base/RfxMessage.cpp"
                ,0x224);
      }
      else {
        local_68 = 0;
        android::String8::format("%s%s",&local_68,&DAT_001fbebf,"RFX_ASSERT");
        mtkLogE(local_68,"RFX_ASSERT:%s, %d",
                "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/base/RfxMessage.cpp"
                ,0x224);
        android::String8::~String8((String8 *)&local_68);
      }
      mtkAssert(0);
                    /* WARNING: Subroutine does not return */
      exit(0);
    }
    doTrm(this_00);
  }
  else {
    *(int *)((long)plVar4 + 0x24) = (int)this;
    *(int *)((long)plVar4 + 0x14) = param_1;
    *(undefined4 *)(plVar4 + 1) = 9;
    *(int *)((long)plVar4 + 0x1c) = param_1;
    lVar5 = systemTime(1);
    local_60[6] = lVar5;
    lVar5 = RfxDataCloneManager::copyData(param_1,(RfxBaseData *)(ulong)(uint)param_2,2);
    local_60[9] = lVar5;
    *in_x8 = local_60;
    if (*(long *)(lVar1 + 0x28) == local_58) {
      return;
    }
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


