
/* RmcDcCommonReqHandler::requestSendQualifiedNetworkTypesChanged(android::sp<RfxMclMessage> const&)
    */

void RmcDcCommonReqHandler::requestSendQualifiedNetworkTypesChanged(sp *param_1)

{
  int iVar1;
  long lVar2;
  long *plVar3;
  ulong uVar4;
  long *in_x1;
  RfxBaseData *in_x3;
  undefined8 in_x7;
  long *in_stack_ffffffffffffff90;
  undefined8 uVar5;
  long *local_68;
  undefined8 local_60 [3];
  int local_44;
  undefined8 local_40;
  long local_38;
  
  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  iVar1 = *(int *)(param_1 + 8);
  local_40 = **(ulong **)(*(long *)(*in_x1 + 0x10) + 8);
  local_68 = (long *)0x0;
  local_44 = 1;
  RfxIntsData::RfxIntsData((RfxIntsData *)local_60,&local_44,3);
  RfxMclMessage::obtainUrc((RfxMclMessage *)0xcd49,iVar1,(int)local_60,in_x3);
  plVar3 = local_68;
  if (local_68 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
  }
  if (plVar3 != local_68) {
    android::sp_report_race();
  }
  uVar5 = 0;
  local_68 = in_stack_ffffffffffffff90;
  RfxIntsData::~RfxIntsData((RfxIntsData *)local_60);
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)param_1,(sp *)&local_68);
  uVar4 = __rfx_is_gt_mode();
  if ((uVar4 & 1) == 0) {
    mtkLogI("RmcDcCommon","[%d][%s] mode=%d, apnType=%d, rat=%d",*(undefined4 *)(param_1 + 8),
            "requestSendQualifiedNetworkTypesChanged",local_44,local_40 & 0xffffffff,local_40._4_4_,
            in_x7,uVar5);
  }
  else {
    local_60[0] = 0;
    android::String8::format("%s%s",local_60,&DAT_001fbebf,"RmcDcCommon");
    mtkLogI(local_60[0],"[%d][%s] mode=%d, apnType=%d, rat=%d",*(undefined4 *)(param_1 + 8),
            "requestSendQualifiedNetworkTypesChanged",local_44,local_40 & 0xffffffff,local_40._4_4_,
            in_x7,uVar5);
    android::String8::~String8((String8 *)local_60);
  }
  if (local_68 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
  }
  if (*(long *)(lVar2 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


