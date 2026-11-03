
/* RtcSbpController::switchSbp(int, int) */

void __thiscall RtcSbpController::switchSbp(RtcSbpController *this,int param_1,int param_2)

{
  long lVar1;
  RfxStatusManager *this_00;
  ulong uVar2;
  undefined8 local_50;
  long local_48;
  
  lVar1 = tpidr_el0;
  local_48 = *(long *)(lVar1 + 0x28);
  this_00 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
  uVar2 = RfxStatusManager::getIntValue(this_00,0x34,0);
  RfxController::logD((char *)this,"RtcSbp","switchSbp, slot:%d, sbp:%d, subid:%d",
                      uVar2 & 0xffffffff,(ulong)(uint)param_1,(ulong)(uint)param_2);
  rfx_property_set("persist.vendor.radio.sbp_flag",&DAT_002240c5);
  android::String8::format("%d",&local_50,(ulong)(uint)param_2);
  rfx_property_set("persist.vendor.operator.subid",local_50);
  android::String8::~String8((String8 *)&local_50);
  android::String8::format("%d",&local_50,(ulong)(uint)param_1);
  rfx_property_set("persist.vendor.mtk_usp_md_sbp_code",local_50);
  android::String8::~String8((String8 *)&local_50);
  saveSbpIdToFile((int)this);
  sendRequestResetRadio(this);
  if (*(long *)(lVar1 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


