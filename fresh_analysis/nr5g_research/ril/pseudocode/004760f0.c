
/* RmcNetworkHandler::cleanNetworkScanResultOperator(RIL_NetworkScanResult*) */

void __thiscall
RmcNetworkHandler::cleanNetworkScanResultOperator
          (RmcNetworkHandler *this,RIL_NetworkScanResult *param_1)

{
  uint uVar1;
  int iVar2;
  void *pvVar3;
  long lVar4;
  long lVar5;
  
  if (param_1 != (RIL_NetworkScanResult *)0x0) {
    uVar1 = *(uint *)(param_1 + 4);
    if (((uVar1 != 0) && (*(long *)(param_1 + 8) != 0)) && (0 < (int)uVar1)) {
      lVar5 = 0;
      do {
        lVar4 = *(long *)(param_1 + 8);
        iVar2 = *(int *)(lVar4 + lVar5);
        if (iVar2 < 4) {
          if ((iVar2 != 1) && (iVar2 != 3)) goto LAB_00476134;
LAB_004761a0:
          pvVar3 = *(void **)(lVar4 + lVar5 + 0x38);
          if (pvVar3 != (void *)0x0) {
            free(pvVar3);
            lVar4 = *(long *)(param_1 + 8);
          }
          pvVar3 = *(void **)(lVar4 + lVar5 + 0x40);
          if (pvVar3 != (void *)0x0) {
LAB_00476130:
            free(pvVar3);
          }
        }
        else {
          if (iVar2 == 4) goto LAB_004761a0;
          if (iVar2 == 6) {
            pvVar3 = *(void **)(lVar4 + lVar5 + 0x78);
            if (pvVar3 != (void *)0x0) {
              free(pvVar3);
              lVar4 = *(long *)(param_1 + 8);
            }
            pvVar3 = *(void **)(lVar4 + lVar5 + 0x80);
            if (pvVar3 == (void *)0x0) goto LAB_00476134;
            goto LAB_00476130;
          }
        }
LAB_00476134:
        lVar5 = lVar5 + 0x90;
      } while ((ulong)uVar1 * 0x90 - lVar5 != 0);
    }
  }
  return;
}


