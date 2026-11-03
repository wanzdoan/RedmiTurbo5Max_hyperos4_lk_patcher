
/* RfxNetworkScanResultData::~RfxNetworkScanResultData() */

void __thiscall RfxNetworkScanResultData::~RfxNetworkScanResultData(RfxNetworkScanResultData *this)

{
  uint uVar1;
  int iVar2;
  void *pvVar3;
  long lVar4;
  void *__ptr;
  long lVar5;
  
  __ptr = *(void **)(this + 8);
  *(undefined **)this = PTR_vtable_005d1d70 + 0x10;
  if (__ptr != (void *)0x0) {
    uVar1 = *(uint *)((long)__ptr + 4);
    if ((uVar1 != 0) && (pvVar3 = *(void **)((long)__ptr + 8), pvVar3 != (void *)0x0)) {
      if (0 < (int)uVar1) {
        lVar5 = 0;
        do {
          lVar4 = *(long *)((long)__ptr + 8);
          iVar2 = *(int *)(lVar4 + lVar5);
          if (iVar2 < 4) {
            if ((iVar2 != 1) && (iVar2 != 3)) goto LAB_00324e78;
LAB_00324ee4:
            pvVar3 = *(void **)(lVar4 + lVar5 + 0x38);
            if (pvVar3 != (void *)0x0) {
              free(pvVar3);
              lVar4 = *(long *)((long)__ptr + 8);
            }
            pvVar3 = *(void **)(lVar4 + lVar5 + 0x40);
            if (pvVar3 != (void *)0x0) {
LAB_00324e74:
              free(pvVar3);
            }
          }
          else {
            if (iVar2 == 4) goto LAB_00324ee4;
            if (iVar2 == 6) {
              pvVar3 = *(void **)(lVar4 + lVar5 + 0x78);
              if (pvVar3 != (void *)0x0) {
                free(pvVar3);
                lVar4 = *(long *)((long)__ptr + 8);
              }
              pvVar3 = *(void **)(lVar4 + lVar5 + 0x80);
              if (pvVar3 == (void *)0x0) goto LAB_00324e78;
              goto LAB_00324e74;
            }
          }
LAB_00324e78:
          lVar5 = lVar5 + 0x90;
        } while ((ulong)uVar1 * 0x90 - lVar5 != 0);
        pvVar3 = *(void **)((long)__ptr + 8);
      }
      free(pvVar3);
    }
    free(__ptr);
  }
  *(undefined8 *)(this + 8) = 0;
  *(undefined4 *)(this + 0x10) = 0;
  return;
}


