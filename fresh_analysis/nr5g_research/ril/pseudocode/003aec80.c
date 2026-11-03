
/* RtcNetworkController::onRadioStateChanged(int, unsigned int, RfxVariant, RfxVariant) */

void __thiscall
RtcNetworkController::onRadioStateChanged
          (RtcNetworkController *this,undefined4 param_1,undefined8 param_3,int *param_4,
          int *param_5)

{
  uint uVar1;
  uint uVar2;
  long lVar3;
  ulong uVar4;
  long *local_68;
  undefined8 local_60 [3];
  long local_48;
  
  lVar3 = tpidr_el0;
  local_48 = *(long *)(lVar3 + 0x28);
  if ((*param_4 != 2) || (*param_5 != 2)) {
    uVar4 = __rfx_is_gt_mode();
    if ((uVar4 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x113);
    }
    else {
      local_60[0] = 0;
      android::String8::format("%s%s",local_60,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_60[0],"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x113);
      android::String8::~String8((String8 *)local_60);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  uVar1 = param_4[2];
  uVar2 = param_5[2];
  RfxController::logD((char *)this,"RtcNwCtrl","onRadioStateChanged: old_state:%d, new_state:%d",
                      (ulong)uVar1,(ulong)uVar2);
  if (((uVar1 == 10) && (uVar2 == 0)) && (*(int *)(this + 0x58) == 0)) {
    local_68 = (long *)0x0;
    RfxVoidData::RfxVoidData((RfxVoidData *)local_60);
    RfxMessage::obtainRequest((RfxMessage *)&local_68,param_1,0xc73c,local_60,0);
    RfxVoidData::~RfxVoidData((RfxVoidData *)local_60);
    *(int *)(this + 0x58) = (int)local_68[4];
    RfxController::requestToMcl((RfxController *)this,(sp *)&local_68,false);
    if (local_68 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
    }
  }
  if (*(long *)(lVar3 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


