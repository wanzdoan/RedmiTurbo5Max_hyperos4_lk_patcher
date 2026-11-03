
/* RmcNetworkRequestHandler::requestSetNrdcState(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcNetworkRequestHandler::requestSetNrdcState(RmcNetworkRequestHandler *this,sp *param_1)

{
  int iVar1;
  undefined4 uVar2;
  long lVar3;
  long *plVar4;
  uint uVar5;
  int iVar6;
  ulong uVar7;
  RfxBaseData *pRVar8;
  undefined8 uVar9;
  int *piVar10;
  long *local_88;
  long *local_80;
  long *local_78;
  long *local_70;
  long *local_68;
  RfxVoidData aRStack_60 [24];
  long local_48;
  
  lVar3 = tpidr_el0;
  local_48 = *(long *)(lVar3 + 0x28);
  local_68 = (long *)0x0;
  piVar10 = *(int **)(*(long *)(*(long *)param_1 + 0x10) + 8);
  uVar5 = *(uint *)(*(long *)(*(long *)param_1 + 0x10) + 0x10);
  uVar7 = RmcNetworkHandler::isNrSupported((RmcNetworkHandler *)this);
  if ((uVar7 & 1) == 0) {
    uVar9 = 6;
  }
  else {
    uVar9 = 0x2c;
    if ((piVar10 != (int *)0x0) && ((uVar5 & 0xfffffffc) == 4)) {
      uVar5 = getNrOption(this);
      if ((-1 < (int)uVar5) && (iVar6 = isEndcDeactivate(this), -1 < iVar6)) {
        iVar1 = *piVar10;
        if (iVar1 == 3) {
          if (((uVar5 >> 2 & 1) == 0) && (iVar6 != 0)) goto LAB_00485c9c;
          if ((uVar5 >> 2 & 1) != 0) {
            setNrOption(this,uVar5 & 0x7ffffffb);
          }
          uVar5 = uVar5 >> 2 & 1;
          if (iVar6 == 0) {
            setEndcDeactivate(this,true);
          }
        }
        else {
          if (iVar1 == 2) {
            if ((uVar5 >> 2 & 1) != 0) {
              uVar5 = uVar5 & 0x7ffffffb;
LAB_00485c84:
              setNrOption(this,uVar5);
              uVar5 = 1;
              goto LAB_00485ca0;
            }
          }
          else {
            if (iVar1 != 1) {
              uVar9 = 0x2c;
              goto LAB_00485b84;
            }
            if (((uVar5 >> 2 & 1) == 0) || (iVar6 != 0)) {
              if (iVar6 != 0) {
                setEndcDeactivate(this,false);
              }
              if ((uVar5 >> 2 & 1) == 0) {
                uVar5 = uVar5 | 4;
                goto LAB_00485c84;
              }
            }
          }
LAB_00485c9c:
          uVar5 = 0;
        }
LAB_00485ca0:
        uVar2 = *(undefined4 *)(*(long *)param_1 + 0xc);
        RfxVoidData::RfxVoidData(aRStack_60);
        local_78 = *(long **)param_1;
        if (local_78 != (long *)0x0) {
          android::RefBase::incStrong((void *)((long)local_78 + *(long *)(*local_78 + -0x18)));
        }
        pRVar8 = (RfxBaseData *)&local_78;
        RfxMclMessage::obtainResponse((RfxMclMessage *)&local_70,uVar2,0,aRStack_60,pRVar8,0);
        plVar4 = local_68;
        if (local_68 != (long *)0x0) {
          android::RefBase::decStrong
                    ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
        }
        if (plVar4 != local_68) {
          android::sp_report_race();
        }
        local_68 = local_70;
        local_70 = (long *)0x0;
        if (local_78 != (long *)0x0) {
          android::RefBase::decStrong
                    ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
        }
        RfxVoidData::~RfxVoidData(aRStack_60);
        RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_68);
        if (uVar5 != 0) {
          iVar6 = *(int *)(this + 0x10);
          local_70 = (long *)0x0;
          RfxVoidData::RfxVoidData(aRStack_60);
          RfxMclMessage::obtainUrc
                    ((RfxMclMessage *)0xc7a9,iVar6,(int)&stack0xffffffffffffffc0 + -0x20,pRVar8);
          plVar4 = local_70;
          if (local_70 != (long *)0x0) {
            android::RefBase::decStrong
                      ((RefBase *)((long)local_70 + *(long *)(*local_70 + -0x18)),&local_70);
          }
          if (plVar4 != local_70) {
            android::sp_report_race();
          }
          local_70 = local_80;
          local_80 = (long *)0x0;
          RfxVoidData::~RfxVoidData(aRStack_60);
          RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_70);
          if (local_70 != (long *)0x0) {
            android::RefBase::decStrong
                      ((RefBase *)((long)local_70 + *(long *)(*local_70 + -0x18)),&local_70);
          }
        }
        goto LAB_00485c34;
      }
      uVar9 = 0x26;
    }
  }
LAB_00485b84:
  uVar2 = *(undefined4 *)(*(long *)param_1 + 0xc);
  RfxVoidData::RfxVoidData(aRStack_60);
  local_88 = *(long **)param_1;
  if (local_88 != (long *)0x0) {
    android::RefBase::incStrong((void *)((long)local_88 + *(long *)(*local_88 + -0x18)));
  }
  RfxMclMessage::obtainResponse((RfxMclMessage *)&local_70,uVar2,uVar9,aRStack_60,&local_88,0);
  plVar4 = local_68;
  if (local_68 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
  }
  if (plVar4 != local_68) {
    android::sp_report_race();
  }
  local_68 = local_70;
  local_70 = (long *)0x0;
  if (local_88 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_88 + *(long *)(*local_88 + -0x18)),&local_88);
  }
  RfxVoidData::~RfxVoidData(aRStack_60);
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_68);
LAB_00485c34:
  if (local_68 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
  }
  if (*(long *)(lVar3 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


