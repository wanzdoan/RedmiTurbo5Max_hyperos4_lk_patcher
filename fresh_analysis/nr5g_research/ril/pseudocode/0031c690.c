
/* RfxMessage::obtainSapRequest(int, int, int, void*, int, void*) */

void __thiscall
RfxMessage::obtainSapRequest
          (RfxMessage *this,int param_1,int param_2,int param_3,void *param_4,int param_5,
          void *param_6)

{
  int iVar1;
  int iVar2;
  long lVar3;
  long *plVar4;
  undefined *puVar5;
  undefined *puVar6;
  long *plVar7;
  long lVar8;
  RfxMessage *this_00;
  ulong uVar9;
  undefined8 *in_x8;
  undefined8 local_78;
  long *local_70;
  long local_68;
  
  lVar3 = tpidr_el0;
  local_68 = *(long *)(lVar3 + 0x28);
  local_70 = (long *)0x0;
  plVar7 = operator_new(0xb8,(nothrow_t *)PTR_nothrow_005d1aa0);
  plVar4 = local_70;
  if (plVar7 != (long *)0x0) {
    android::RefBase::RefBase((RefBase *)(plVar7 + 0x15));
    plVar4 = local_70;
    puVar5 = PTR_vtable_005d1a60;
    *(undefined8 *)((long)plVar7 + 0x34) = 0;
    *(undefined8 *)((long)plVar7 + 0x2c) = 0;
    plVar7[9] = 0;
    *(undefined4 *)(plVar7 + 5) = 0xffffffff;
    *(undefined4 *)(plVar7 + 0xb) = 0;
    plVar7[2] = 0;
    plVar7[1] = 0;
    plVar7[4] = 0;
    plVar7[3] = 0;
    *plVar7 = (long)(puVar5 + 0x18);
    plVar7[0x15] = (long)(puVar5 + 0x60);
    *(undefined2 *)(plVar7 + 0x11) = 0;
    *(undefined8 *)((long)plVar7 + 0x3c) = 0x200000000;
    plVar7[10] = -0x100000000;
    *(undefined1 *)((long)plVar7 + 0x8a) = 0;
    plVar7[0x12] = 0;
    *(undefined1 *)(plVar7 + 0x13) = 0;
    *(undefined4 *)((long)plVar7 + 0x9c) = 0;
    *(undefined1 *)(plVar7 + 0x14) = 0;
    *(undefined4 *)((long)plVar7 + 0xa4) = 1;
    android::RefBase::incStrong(plVar7 + 0x15);
  }
  if (plVar4 != (long *)0x0) {
    android::RefBase::decStrong((RefBase *)((long)plVar4 + *(long *)(*plVar4 + -0x18)),&local_70);
  }
  if (plVar4 != local_70) {
    android::sp_report_race();
  }
  local_70 = plVar7;
  if (plVar7 == (long *)0x0) {
    this_00 = (RfxMessage *)RfxRilUtils::isEngLoad();
    if ((int)this_00 != 0) {
      uVar9 = __rfx_is_gt_mode();
      if ((uVar9 & 1) == 0) {
        mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
                "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/base/RfxMessage.cpp"
                ,0x1f2);
      }
      else {
        local_78 = 0;
        android::String8::format("%s%s",&local_78,&DAT_001fbebf,"RFX_ASSERT");
        mtkLogE(local_78,"RFX_ASSERT:%s, %d",
                "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/base/RfxMessage.cpp"
                ,0x1f2);
        android::String8::~String8((String8 *)&local_78);
      }
      mtkAssert(0);
                    /* WARNING: Subroutine does not return */
      exit(0);
    }
    doTrm(this_00);
  }
  else {
    *(int *)((long)plVar7 + 0x24) = (int)this;
    *(int *)((long)plVar7 + 0x14) = param_1;
    *(int *)(plVar7 + 3) = param_2;
    *(int *)((long)plVar7 + 0x1c) = param_1;
    *(undefined4 *)(plVar7 + 1) = 7;
    pthread_mutex_lock((pthread_mutex_t *)PTR_token_mutex_005d1a68);
    puVar6 = PTR_tokenCounter_005d1a70;
    iVar1 = *(int *)PTR_tokenCounter_005d1a70;
    iVar2 = *(int *)PTR_intMax_005d1a78;
    *(int *)PTR_tokenCounter_005d1a70 = iVar1 + 1;
    puVar5 = PTR_token_mutex_005d1a68;
    if (iVar1 == iVar2) {
      pthread_mutex_lock((pthread_mutex_t *)PTR_token_mutex_005d1a68);
      *(undefined4 *)puVar6 = 0;
      pthread_mutex_unlock((pthread_mutex_t *)puVar5);
    }
    pthread_mutex_unlock((pthread_mutex_t *)PTR_token_mutex_005d1a68);
    *(int *)(local_70 + 4) = iVar1;
    lVar8 = systemTime(1);
    local_70[6] = lVar8;
    lVar8 = systemTime(1);
    local_70[7] = lVar8;
    lVar8 = RfxDataCloneManager::copyData(param_1,(void *)(ulong)(uint)param_3,(int)param_4,0);
    local_70[9] = lVar8;
    local_70[0x12] = (ulong)(uint)param_5;
    *in_x8 = local_70;
    if (*(long *)(lVar3 + 0x28) == local_68) {
      return;
    }
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


