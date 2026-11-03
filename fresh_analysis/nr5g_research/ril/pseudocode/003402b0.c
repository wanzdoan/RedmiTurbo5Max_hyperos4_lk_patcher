
/* RfxHandlerManager::unregisterToHandleMipcUrc(RfxBaseHandler*, unsigned int, int, int const*, int)
    */

void RfxHandlerManager::unregisterToHandleMipcUrc
               (RfxBaseHandler *param_1,uint param_2,int param_3,int *param_4,int param_5)

{
  undefined *puVar1;
  int iVar2;
  long lVar3;
  char *pcVar4;
  ulong uVar5;
  ulong uVar6;
  undefined8 uVar7;
  long lVar8;
  ulong uVar9;
  char *local_c8;
  undefined *local_c0;
  undefined8 uStack_b8;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  RfxBaseHandler *local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  ulong local_70;
  long local_68;
  
  puVar1 = PTR_s_self_005d2358;
  lVar3 = tpidr_el0;
  local_68 = *(long *)(lVar3 + 0x28);
  uVar9 = (ulong)(uint)param_5;
  lVar8 = ((ulong)param_2 + (ulong)param_2 * 4) * 8;
  pthread_mutex_lock((pthread_mutex_t *)(*(long *)PTR_s_self_005d2358 + lVar8 + 0x2760));
  local_a0 = 0;
  uStack_b8 = 0;
  local_c0 = (undefined *)0x0;
  uStack_a8 = 0;
  uStack_b0 = 0;
  android::VectorImpl::VectorImpl
            ((VectorImpl *)&local_c0,(VectorImpl *)(*(long *)puVar1 + lVar8 + 0x1860));
  puVar1 = PTR_vtable_005d2368 + 0x10;
  local_c0 = puVar1;
  if (0 < param_5) {
    do {
      iVar2 = *param_4;
      local_70 = 0;
      uStack_88 = 0;
      local_90 = (RfxBaseHandler *)0x0;
      uStack_78 = 0;
      local_80 = 0;
      android::String8::String8((String8 *)&local_c8);
      uStack_88 = CONCAT44(param_3,param_2);
      local_80 = CONCAT44(0xffffffff,iVar2);
      local_90 = param_1;
      android::String8::String8((String8 *)&uStack_78,local_c8);
      local_70 = local_70 & 0xffffffffffffff00;
      android::String8::~String8((String8 *)&local_c8);
      uVar5 = android::SortedVectorImpl::indexOf(&local_c0);
      if (-1 < (long)uVar5) {
        android::VectorImpl::removeItemsAt((ulong)&local_c0,uVar5);
      }
      uVar6 = __rfx_is_gt_mode();
      if ((uVar6 & 1) == 0) {
        uVar7 = RfxChannelManager::proxyIdToString(param_2);
        mtkLogD("RfxHandlerMgr",
                "unregisterToHandleMipcUrc, handler = %p, channel = %s, id = %d, index = %zd",
                param_1,uVar7,*param_4,uVar5);
      }
      else {
        local_c8 = (char *)0x0;
        android::String8::format("%s%s",&local_c8,&DAT_001fbebf,"RfxHandlerMgr");
        pcVar4 = local_c8;
        uVar7 = RfxChannelManager::proxyIdToString(param_2);
        mtkLogD(pcVar4,"unregisterToHandleMipcUrc, handler = %p, channel = %s, id = %d, index = %zd"
                ,param_1,uVar7,*param_4,uVar5);
        android::String8::~String8((String8 *)&local_c8);
      }
      android::String8::~String8((String8 *)&uStack_78);
      param_4 = param_4 + 1;
      uVar9 = uVar9 - 1;
    } while (uVar9 != 0);
  }
  pthread_mutex_unlock
            ((pthread_mutex_t *)(*(long *)PTR_s_self_005d2358 + (ulong)param_2 * 0x28 + 0x2760));
  local_c0 = puVar1;
  android::VectorImpl::finish_vector();
  android::SortedVectorImpl::~SortedVectorImpl((SortedVectorImpl *)&local_c0);
  if (*(long *)(lVar3 + 0x28) != local_68) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}


