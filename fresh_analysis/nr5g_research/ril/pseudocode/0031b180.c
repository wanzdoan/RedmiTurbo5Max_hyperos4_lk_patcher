
/* RfxMessage::obtainRequest(int, int, RILD_RadioTechnology_Group) */

void __thiscall
RfxMessage::obtainRequest
          (undefined8 *param_1_00,undefined4 param_2_00,undefined4 param_1,undefined4 param_2)

{
  int iVar1;
  int iVar2;
  long lVar3;
  long *plVar4;
  undefined *puVar5;
  undefined *puVar6;
  uint uVar7;
  long *plVar8;
  long lVar9;
  RfxMessage *this;
  ulong uVar10;
  undefined8 local_68;
  long *local_60;
  long local_58;
  
  lVar3 = tpidr_el0;
  local_58 = *(long *)(lVar3 + 0x28);
  local_60 = (long *)0x0;
  plVar8 = operator_new(0xb8,(nothrow_t *)PTR_nothrow_005d1aa0);
  plVar4 = local_60;
  if (plVar8 != (long *)0x0) {
    android::RefBase::RefBase((RefBase *)(plVar8 + 0x15));
    plVar4 = local_60;
    puVar5 = PTR_vtable_005d1a60;
    *(undefined8 *)((long)plVar8 + 0x34) = 0;
    *(undefined8 *)((long)plVar8 + 0x2c) = 0;
    plVar8[9] = 0;
    *(undefined4 *)(plVar8 + 5) = 0xffffffff;
    *(undefined4 *)(plVar8 + 0xb) = 0;
    plVar8[2] = 0;
    plVar8[1] = 0;
    plVar8[4] = 0;
    plVar8[3] = 0;
    *plVar8 = (long)(puVar5 + 0x18);
    plVar8[0x15] = (long)(puVar5 + 0x60);
    *(undefined2 *)(plVar8 + 0x11) = 0;
    *(undefined8 *)((long)plVar8 + 0x3c) = 0x200000000;
    plVar8[10] = -0x100000000;
    *(undefined1 *)((long)plVar8 + 0x8a) = 0;
    plVar8[0x12] = 0;
    *(undefined1 *)(plVar8 + 0x13) = 0;
    *(undefined4 *)((long)plVar8 + 0x9c) = 0;
    *(undefined1 *)(plVar8 + 0x14) = 0;
    *(undefined4 *)((long)plVar8 + 0xa4) = 1;
    android::RefBase::incStrong(plVar8 + 0x15);
  }
  if (plVar4 != (long *)0x0) {
    android::RefBase::decStrong((RefBase *)((long)plVar4 + *(long *)(*plVar4 + -0x18)),&local_60);
  }
  if (plVar4 != local_60) {
    android::sp_report_race();
  }
  local_60 = plVar8;
  if (plVar8 == (long *)0x0) {
    this = (RfxMessage *)RfxRilUtils::isEngLoad();
    if ((int)this != 0) {
      uVar10 = __rfx_is_gt_mode();
      if ((uVar10 & 1) == 0) {
        mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
                "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/base/RfxMessage.cpp"
                ,0xf7);
      }
      else {
        local_68 = 0;
        android::String8::format("%s%s",&local_68,&DAT_001fbebf,"RFX_ASSERT");
        mtkLogE(local_68,"RFX_ASSERT:%s, %d",
                "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/base/RfxMessage.cpp"
                ,0xf7);
        android::String8::~String8((String8 *)&local_68);
      }
      mtkAssert(0);
                    /* WARNING: Subroutine does not return */
      exit(0);
    }
    uVar10 = doTrm(this);
  }
  else {
    *(undefined4 *)(plVar8 + 1) = 0;
    *(undefined4 *)((long)plVar8 + 0x24) = param_2_00;
    *(undefined4 *)((long)plVar8 + 0x1c) = param_1;
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
    *(int *)(local_60 + 4) = iVar1;
    lVar9 = systemTime(1);
    local_60[6] = lVar9;
    lVar9 = systemTime(1);
    local_60[7] = lVar9;
    pthread_mutex_lock((pthread_mutex_t *)PTR_pTokenMutex_005d1a80);
    puVar6 = PTR_pTokenCounter_005d1a88;
    iVar1 = *(int *)PTR_pTokenCounter_005d1a88;
    iVar2 = *(int *)PTR_intMin_005d1a90;
    *(int *)PTR_pTokenCounter_005d1a88 = iVar1 + -1;
    puVar5 = PTR_pTokenMutex_005d1a80;
    if (iVar1 == iVar2) {
      pthread_mutex_lock((pthread_mutex_t *)PTR_pTokenMutex_005d1a80);
      *(undefined4 *)puVar6 = 0;
      pthread_mutex_unlock((pthread_mutex_t *)puVar5);
    }
    uVar7 = pthread_mutex_unlock((pthread_mutex_t *)PTR_pTokenMutex_005d1a80);
    uVar10 = (ulong)uVar7;
    *(int *)(local_60 + 3) = iVar1;
    *(undefined4 *)(local_60 + 2) = param_2;
    *param_1_00 = local_60;
    if (*(long *)(lVar3 + 0x28) == local_58) {
      return;
    }
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar10);
}


