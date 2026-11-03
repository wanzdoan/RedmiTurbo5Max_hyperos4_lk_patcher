
/* RmcDcCommonReqHandler::onReleaseRfOccupy(android::sp<RfxMclMessage> const&) */

void __thiscall RmcDcCommonReqHandler::onReleaseRfOccupy(RmcDcCommonReqHandler *this,sp *param_1)

{
  long lVar1;
  ulong uVar2;
  long *local_60;
  long *local_58;
  undefined8 local_50 [3];
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  uVar2 = __rfx_is_gt_mode();
  if ((uVar2 & 1) == 0) {
    mtkLogI("RmcDcCommon","[%d][%s] release occupy",*(undefined4 *)(this + 8),"onReleaseRfOccupy");
  }
  else {
    local_50[0] = 0;
    android::String8::format("%s%s",local_50,&DAT_001fbebf,"RmcDcCommon");
    mtkLogI(local_50[0],"[%d][%s] release occupy",*(undefined4 *)(this + 8),"onReleaseRfOccupy");
    android::String8::~String8((String8 *)local_50);
  }
  sendRfOccupyCommandIfNeed(this,false);
  local_58 = (long *)0x0;
  RfxVoidData::RfxVoidData((RfxVoidData *)local_50);
  local_60 = *(long **)param_1;
  if (local_60 != (long *)0x0) {
    android::RefBase::incStrong((void *)((long)local_60 + *(long *)(*local_60 + -0x18)));
  }
  RfxMclMessage::obtainResponse((RfxMclMessage *)&local_58,0,local_50,&local_60);
  if (local_60 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_60 + *(long *)(*local_60 + -0x18)),&local_60);
  }
  RfxVoidData::~RfxVoidData((RfxVoidData *)local_50);
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_58);
  if (local_58 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_58 + *(long *)(*local_58 + -0x18)),&local_58);
  }
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


