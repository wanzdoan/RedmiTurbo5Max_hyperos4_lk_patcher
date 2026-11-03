
/* RmcRadioRequestHandler::updateSupportDSBP() */

void __thiscall RmcRadioRequestHandler::updateSupportDSBP(RmcRadioRequestHandler *this)

{
  long lVar1;
  String8 aSStack_a0 [8];
  long *local_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 uStack_50;
  undefined4 uStack_48;
  undefined4 local_44;
  undefined4 uStack_40;
  undefined8 uStack_3c;
  long local_28;
  
  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  uStack_3c = 0;
  uStack_40 = 0;
  uStack_78 = 0;
  local_80 = 0;
  uStack_68 = 0;
  uStack_70 = 0;
  uStack_58 = 0;
  local_60 = 0;
  uStack_48 = 0;
  local_44 = 0;
  uStack_50 = 0;
  uStack_88 = 0;
  local_90 = 0;
  rfx_property_get("persist.vendor.radio.mtk_dsbp_support",&local_90,&DAT_00235024);
  android::String8::format("AT+EDSBP=%s",aSStack_a0,&local_90);
  RfxBaseHandler::atSendCommand((String8 *)this,aSStack_a0);
  if (local_98 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_98 + *(long *)(*local_98 + -0x18)),&local_98);
  }
  android::String8::~String8(aSStack_a0);
  if (*(long *)(lVar1 + 0x28) == local_28) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


