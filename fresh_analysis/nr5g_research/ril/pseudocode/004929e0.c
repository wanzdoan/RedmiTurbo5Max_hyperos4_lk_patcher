
/* RmcNetworkNrtRequestHandler::handleSimulatedNetworkScanResultByRat() */

void __thiscall
RmcNetworkNrtRequestHandler::handleSimulatedNetworkScanResultByRat
          (RmcNetworkNrtRequestHandler *this)

{
  int iVar1;
  long lVar2;
  undefined *puVar3;
  undefined *puVar4;
  long *plVar5;
  uint uVar6;
  uint uVar7;
  RfxMclStatusManager *pRVar8;
  ulong uVar9;
  undefined8 uVar10;
  undefined4 *__ptr;
  RfxBaseData *pRVar11;
  long *local_88;
  long *local_80;
  undefined4 local_78 [2];
  undefined1 local_70;
  long local_48;
  
  puVar3 = PTR_sPlmnListOngoingType_005d3f08;
  lVar2 = tpidr_el0;
  local_48 = *(long *)(lVar2 + 0x28);
  local_80 = (long *)0x0;
  *(undefined4 *)PTR_sPlmnListOngoingType_005d3f08 = 1;
  pRVar8 = (RfxMclStatusManager *)RfxBaseHandler::getMclStatusManager((RfxBaseHandler *)this);
  local_78[0] = 1;
  local_70 = 1;
  RfxMclStatusManager::setValue(pRVar8,0xa0,(RfxVariant *)local_78,false,false);
  RfxVariant::deinit((RfxVariant *)local_78);
  uVar9 = RmcNetworkHandler::isAPInCall((RmcNetworkHandler *)this);
  puVar4 = PTR_mPlmnListAbort_005d3f10;
  uVar6 = 0;
  if (((uVar9 & 1) == 0) && (*(int *)PTR_mPlmnListAbort_005d3f10 != 1)) {
    uVar6 = handleSimulatedNetworkScanResultByRat(this,3);
    uVar9 = RmcNetworkHandler::isAPInCall((RmcNetworkHandler *)this);
    if (((uVar9 & 1) == 0) && (*(int *)puVar4 != 1)) {
      uVar7 = handleSimulatedNetworkScanResultByRat(this,1);
      uVar6 = uVar6 | uVar7;
      uVar9 = RmcNetworkHandler::isAPInCall((RmcNetworkHandler *)this);
      if (((uVar9 & 1) == 0) && (*(int *)puVar4 != 1)) {
        uVar7 = handleSimulatedNetworkScanResultByRat(this,0);
        uVar6 = uVar6 | uVar7;
      }
    }
  }
  pRVar8 = (RfxMclStatusManager *)RfxBaseHandler::getMclStatusManager((RfxBaseHandler *)this);
  pRVar11 = (RfxBaseData *)0x0;
  local_78[0] = 1;
  local_70 = 0;
  RfxMclStatusManager::setValue(pRVar8,0xa0,(RfxVariant *)local_78,false,false);
  RfxVariant::deinit((RfxVariant *)local_78);
  iVar1 = *(int *)puVar4;
  *(undefined4 *)puVar3 = 0;
  if (iVar1 == 0) {
    if ((uVar6 & 1) == 0) {
      uVar10 = 0;
      goto LAB_00492b24;
    }
    __ptr = calloc(1,0x10);
    if (__ptr == (undefined4 *)0x0) {
      RfxBaseHandler::logE
                ((char *)this,"RmcNwReqHdlr","handleSimulatedNetworkScanResultByRat result = null");
    }
    else {
      iVar1 = *(int *)(this + 0x10);
      *__ptr = 2;
      RfxNetworkScanResultData::RfxNetworkScanResultData
                ((RfxNetworkScanResultData *)local_78,__ptr,0x10);
      RfxMclMessage::obtainUrc((RfxMclMessage *)0xc7bb,iVar1,(int)local_78,pRVar11);
      plVar5 = local_80;
      if (local_80 != (long *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)((long)local_80 + *(long *)(*local_80 + -0x18)),&local_80);
      }
      if (plVar5 != local_80) {
        android::sp_report_race();
      }
      local_80 = local_88;
      RfxNetworkScanResultData::~RfxNetworkScanResultData((RfxNetworkScanResultData *)local_78);
      RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_80);
      free(__ptr);
    }
  }
  *(undefined4 *)puVar4 = 0;
  if (local_80 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_80 + *(long *)(*local_80 + -0x18)),&local_80);
  }
  uVar10 = 1;
LAB_00492b24:
  if (*(long *)(lVar2 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar10);
}


