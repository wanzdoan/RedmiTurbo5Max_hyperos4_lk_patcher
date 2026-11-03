
/* RfxStatusManager::unRegisterStatusChangedEx(unsigned int, RfxCallback4<int, unsigned int,
   RfxVariant, RfxVariant> const&) */

void __thiscall
RfxStatusManager::unRegisterStatusChangedEx
          (RfxStatusManager *this,uint param_1,RfxCallback4 *param_2)

{
  long lVar1;
  ulong uVar2;
  long lVar3;
  _func_void *local_50;
  weakref_type *local_48;
  _func_void *local_30;
  long local_28;
  
  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  if (0xf0 < param_1 - 1) {
    uVar2 = __rfx_is_gt_mode();
    if ((uVar2 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/core/RfxStatusManager.cpp"
              ,0xf2);
    }
    else {
      local_30 = (_func_void *)0x0;
      android::String8::format("%s%s",&local_30,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_30,"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/core/RfxStatusManager.cpp"
              ,0xf2);
      android::String8::~String8((String8 *)&local_30);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  lVar3 = *(long *)(this + (ulong)param_1 * 8 + 0x48);
  if (lVar3 != 0) {
    RfxCallback4<int,unsigned_int,RfxVariant,RfxVariant>::RfxCallback4
              ((RfxCallback4<int,unsigned_int,RfxVariant,RfxVariant> *)&local_50,param_2);
    local_30 = (_func_void *)0x0;
    if ((local_50 != (_func_void *)0x0) &&
       (uVar2 = android::RefBase::weakref_type::attemptIncStrong(local_48,&local_30),
       (uVar2 & 1) != 0)) {
      local_30 = local_50;
    }
    RfxBaseSignal::baseDisconnect((RfxObject *)(lVar3 + 0x38),local_30);
    if (local_30 != (_func_void *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)(local_30 + *(long *)(*(long *)local_30 + -0x18)),&local_30);
    }
    if (local_50 != (_func_void *)0x0) {
      android::RefBase::weakref_type::decWeak(local_48,&local_50);
    }
  }
  if (*(long *)(lVar1 + 0x28) != local_28) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}


