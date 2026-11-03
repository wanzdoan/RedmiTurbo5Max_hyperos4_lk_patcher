
/* RmcCdmaBcActivationReq::RmcCdmaBcActivationReq(void*, int) */

void __thiscall
RmcCdmaBcActivationReq::RmcCdmaBcActivationReq
          (RmcCdmaBcActivationReq *this,void *param_1,int param_2)

{
  undefined *puVar1;
  long lVar2;
  ulong uVar3;
  undefined8 local_20;
  long local_18;
  
  lVar2 = tpidr_el0;
  local_18 = *(long *)(lVar2 + 0x28);
  puVar1 = PTR_vtable_005d39d0 + 0x10;
  *(undefined8 *)(this + 8) = 0;
  *(undefined8 *)(this + 0x10) = 0;
  *(undefined **)this = puVar1;
  if (param_2 != 4) {
    uVar3 = __rfx_is_gt_mode();
    if ((uVar3 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/mdcomm/sms/RmcCdmaBcActivate.cpp"
              ,0x34);
    }
    else {
      local_20 = 0;
      android::String8::format("%s%s",&local_20,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_20,"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/mdcomm/sms/RmcCdmaBcActivate.cpp"
              ,0x34);
      android::String8::~String8((String8 *)&local_20);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  *(undefined4 *)(this + 0x18) = *(undefined4 *)param_1;
  *(RmcCdmaBcActivationReq **)(this + 8) = this + 0x18;
  *(undefined4 *)(this + 0x10) = 4;
  if (*(long *)(lVar2 + 0x28) == local_18) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


