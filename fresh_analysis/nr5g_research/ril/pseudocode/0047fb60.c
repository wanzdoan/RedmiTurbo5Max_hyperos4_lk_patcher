
/* RmcNetworkRequestHandler::requestSetNrOption(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcNetworkRequestHandler::requestSetNrOption(RmcNetworkRequestHandler *this,sp *param_1)

{
  uint uVar1;
  undefined4 uVar2;
  int iVar3;
  long lVar4;
  long *plVar5;
  bool bVar6;
  bool bVar7;
  uint uVar8;
  RfxBaseData *pRVar9;
  undefined8 uVar10;
  RfxAtLine *this_00;
  long *local_90;
  long *local_88;
  long *local_80;
  long *local_78;
  long *local_70;
  int local_64;
  long *local_60 [3];
  long local_48;
  
  lVar4 = tpidr_el0;
  local_48 = *(long *)(lVar4 + 0x28);
  local_78 = (long *)0x0;
  local_70 = (long *)0x0;
  uVar1 = **(uint **)(*(long *)(*(long *)param_1 + 0x10) + 8);
  RfxBaseHandler::atSendCommandSingleline((char *)this,"AT+E5GOPT?","+E5GOPT:");
  plVar5 = local_70;
  if (local_70 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_70 + *(long *)(*local_70 + -0x18)),&local_70);
  }
  if (plVar5 != local_70) {
    android::sp_report_race();
  }
  local_64 = (int)local_60[0][5];
  local_70 = local_60[0];
  if (((local_64 == 0) && (*(int *)((long)local_60[0] + 0xc) != 0)) &&
     (this_00 = (RfxAtLine *)local_60[0][4], this_00 != (RfxAtLine *)0x0)) {
    RfxAtLine::atTokStart(this_00,&local_64);
    if (-1 < local_64) {
      uVar8 = RfxAtLine::atTokNextint(this_00,&local_64);
      uVar10 = 0;
      if ((local_64 < 0) || (uVar8 == uVar1)) goto LAB_0047fc00;
      android::String8::format("AT+E5GOPT=%d",&local_80,(ulong)uVar1);
      RfxBaseHandler::atSendCommand((String8 *)this,&local_80);
      plVar5 = local_70;
      if (local_70 != (long *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)((long)local_70 + *(long *)(*local_70 + -0x18)),&local_70);
      }
      if (plVar5 != local_70) {
        android::sp_report_race();
      }
      local_70 = local_60[0];
      local_60[0] = (long *)0x0;
      android::String8::~String8((String8 *)&local_80);
      local_64 = (int)local_70[5];
      if ((local_64 < 0) || (*(int *)((long)local_70 + 0xc) == 0)) goto LAB_0047fbfc;
      bVar6 = (uVar8 & 0xfffffffd) == 5;
      pRVar9 = (RfxBaseData *)(ulong)bVar6;
      bVar7 = (uVar1 & 0xfffffffd) == 5;
      RfxBaseHandler::logV
                ((char *)this,"RmcNwReqHdlr","requestDisableNr oldNsa=%d, newNsa=%d",pRVar9,
                 (ulong)bVar7);
      if (bVar7 != bVar6) {
        iVar3 = *(int *)(this + 0x10);
        local_80 = (long *)0x0;
        RfxVoidData::RfxVoidData((RfxVoidData *)local_60);
        RfxMclMessage::obtainUrc
                  ((RfxMclMessage *)0xc7a9,iVar3,(int)&stack0xffffffffffffffc0 + -0x20,pRVar9);
        android::sp<RfxMclMessage>::operator=((sp<RfxMclMessage> *)&local_80,(sp *)&local_88);
        if (local_88 != (long *)0x0) {
          android::RefBase::decStrong
                    ((RefBase *)((long)local_88 + *(long *)(*local_88 + -0x18)),&local_88);
        }
        RfxVoidData::~RfxVoidData((RfxVoidData *)local_60);
        RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_80);
        if (local_80 != (long *)0x0) {
          android::RefBase::decStrong
                    ((RefBase *)((long)local_80 + *(long *)(*local_80 + -0x18)),&local_80);
        }
      }
    }
    uVar10 = 0;
  }
  else {
LAB_0047fbfc:
    uVar10 = 2;
  }
LAB_0047fc00:
  uVar2 = *(undefined4 *)(*(long *)param_1 + 0xc);
  RfxVoidData::RfxVoidData((RfxVoidData *)local_60);
  local_90 = *(long **)param_1;
  if (local_90 != (long *)0x0) {
    android::RefBase::incStrong((void *)((long)local_90 + *(long *)(*local_90 + -0x18)));
  }
  RfxMclMessage::obtainResponse((RfxMclMessage *)&local_80,uVar2,uVar10,local_60,&local_90,0);
  plVar5 = local_78;
  if (local_78 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
  }
  if (plVar5 != local_78) {
    android::sp_report_race();
  }
  local_78 = local_80;
  local_80 = (long *)0x0;
  if (local_90 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_90 + *(long *)(*local_90 + -0x18)),&local_90);
  }
  RfxVoidData::~RfxVoidData((RfxVoidData *)local_60);
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_78);
  if (local_78 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
  }
  if (local_70 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_70 + *(long *)(*local_70 + -0x18)),&local_70);
  }
  if (*(long *)(lVar4 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


