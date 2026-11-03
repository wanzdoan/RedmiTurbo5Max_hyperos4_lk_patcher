
/* RmcRatSwitchHandler::requestGetPreferredNetworkType(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcRatSwitchHandler::requestGetPreferredNetworkType(RmcRatSwitchHandler *this,sp *param_1)

{
  undefined4 uVar1;
  long lVar2;
  long *plVar3;
  uint uVar4;
  int iVar5;
  ulong uVar6;
  RfxAtLine *this_00;
  long *local_90;
  long *local_88;
  long *local_80;
  long *local_78;
  int local_70;
  int iStack_6c;
  long *local_68;
  long *local_60 [3];
  long local_48;
  
  lVar2 = tpidr_el0;
  local_48 = *(long *)(lVar2 + 0x28);
  iVar5 = *(int *)(this + 0x18);
  local_70 = 0;
  iStack_6c = 0;
  local_68 = (long *)0x0;
  local_78 = (long *)0x0;
  if (iVar5 < 0) {
    RfxBaseHandler::atSendCommandSingleline((char *)this,"AT+ERAT?","+ERAT:");
    plVar3 = local_68;
    if (local_68 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
    }
    if (plVar3 != local_68) {
      android::sp_report_race();
    }
    iStack_6c = (int)local_60[0][5];
    local_68 = local_60[0];
    if ((-1 < iStack_6c) && (*(int *)((long)local_60[0] + 0xc) != 0)) {
      this_00 = (RfxAtLine *)local_60[0][4];
      RfxAtLine::atTokStart(this_00,&iStack_6c);
      if ((-1 < iStack_6c) &&
         (((RfxAtLine::atTokNextint(this_00,&iStack_6c), -1 < iStack_6c &&
           (RfxAtLine::atTokNextint(this_00,&iStack_6c), -1 < iStack_6c)) &&
          (uVar4 = RfxAtLine::atTokNextint(this_00,&iStack_6c), -1 < iStack_6c)))) {
        uVar6 = (ulong)uVar4;
        iVar5 = RfxAtLine::atTokNextint(this_00,&iStack_6c);
        if (-1 < iStack_6c) {
          if ((uVar4 == 7) && (iVar5 == 0x20)) {
            uVar6 = 0x1f;
          }
          else if ((uVar4 == 7) && (iVar5 == 0x40)) {
            uVar6 = 0x20;
          }
          else if ((uVar4 == 8) && (iVar5 == 0x20)) {
            uVar6 = 0x21;
          }
          else if (0x1e < uVar4) goto LAB_004958d0;
          iVar5 = (&DAT_002930e8)[uVar6];
          goto LAB_004956ec;
        }
      }
    }
LAB_004958d0:
    uVar1 = *(undefined4 *)(*(long *)param_1 + 0xc);
    RfxVoidData::RfxVoidData((RfxVoidData *)local_60);
    local_90 = *(long **)param_1;
    if (local_90 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_90 + *(long *)(*local_90 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_80,uVar1,2,local_60,&local_90,0);
    plVar3 = local_78;
    if (local_78 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
    }
    if (plVar3 != local_78) {
      android::sp_report_race();
    }
    local_78 = local_80;
    local_80 = (long *)0x0;
    if (local_90 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_90 + *(long *)(*local_90 + -0x18)),&local_90);
    }
    RfxVoidData::~RfxVoidData((RfxVoidData *)local_60);
  }
  else {
LAB_004956ec:
    uVar1 = *(undefined4 *)(*(long *)param_1 + 0xc);
    local_70 = iVar5;
    RfxIntsData::RfxIntsData((RfxIntsData *)local_60,&local_70,1);
    local_88 = *(long **)param_1;
    if (local_88 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_88 + *(long *)(*local_88 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_80,uVar1,0,local_60,&local_88,0);
    plVar3 = local_78;
    if (local_78 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
    }
    if (plVar3 != local_78) {
      android::sp_report_race();
    }
    local_78 = local_80;
    local_80 = (long *)0x0;
    if (local_88 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_88 + *(long *)(*local_88 + -0x18)),&local_88);
    }
    RfxIntsData::~RfxIntsData((RfxIntsData *)local_60);
  }
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_78);
  if (local_78 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
  }
  if (local_68 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
  }
  if (*(long *)(lVar2 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


