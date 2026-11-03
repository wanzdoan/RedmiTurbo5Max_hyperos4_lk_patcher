
/* RfxNetworkScanResultData::RfxNetworkScanResultData(void*, int) */

void __thiscall
RfxNetworkScanResultData::RfxNetworkScanResultData
          (RfxNetworkScanResultData *this,void *param_1,int param_2)

{
  uint uVar1;
  int iVar2;
  undefined *puVar3;
  undefined4 *__ptr;
  void *__dest;
  char **__ptr_00;
  undefined8 *puVar4;
  long lVar5;
  long lVar6;
  
  puVar3 = PTR_vtable_005d1d70;
  *(undefined4 *)(this + 0x10) = 0;
  *(undefined **)this = puVar3 + 0x10;
  *(undefined8 *)(this + 8) = 0;
  if (param_1 == (void *)0x0) {
    return;
  }
  __ptr = calloc(1,0x10);
  if (__ptr != (undefined4 *)0x0) {
    uVar1 = *(uint *)((long)param_1 + 4);
    *__ptr = *(undefined4 *)param_1;
    __ptr[1] = uVar1;
    if (uVar1 == 0) goto LAB_00324df8;
    __dest = calloc((long)(int)uVar1,0x90);
    *(void **)(__ptr + 2) = __dest;
    if (__dest != (void *)0x0) {
      memcpy(__dest,*(void **)((long)param_1 + 8),(ulong)uVar1 * 0x90);
      if (0 < (int)uVar1) {
        lVar5 = 0;
        lVar6 = 0;
        do {
          iVar2 = *(int *)((long)__dest + lVar5);
          if (iVar2 < 4) {
            if ((iVar2 == 1) || (iVar2 == 3)) {
LAB_00324d20:
              asprintf((char **)((long)__dest + lVar5 + 0x38),"%s",
                       *(undefined8 *)(*(long *)((long)param_1 + 8) + lVar5 + 0x38));
              __dest = *(void **)(__ptr + 2);
              __ptr_00 = (char **)((long)__dest + lVar6 * 0x90 + 0x40);
              puVar4 = (undefined8 *)(*(long *)((long)param_1 + 8) + lVar6 * 0x90 + 0x40);
              goto LAB_00324d5c;
            }
          }
          else {
            if (iVar2 == 4) goto LAB_00324d20;
            if (iVar2 != 6) goto LAB_00324d68;
            asprintf((char **)((long)__dest + lVar5 + 0x78),"%s",
                     *(undefined8 *)(*(long *)((long)param_1 + 8) + lVar5 + 0x78));
            __dest = *(void **)(__ptr + 2);
            __ptr_00 = (char **)((long)__dest + lVar5 + 0x80);
            puVar4 = (undefined8 *)(*(long *)((long)param_1 + 8) + lVar5 + 0x80);
LAB_00324d5c:
            asprintf(__ptr_00,"%s",*puVar4);
          }
LAB_00324d68:
          lVar5 = lVar5 + 0x90;
          lVar6 = lVar6 + 1;
        } while ((ulong)uVar1 * 0x90 - lVar5 != 0);
      }
      goto LAB_00324df8;
    }
    free(__ptr);
    __ptr = (undefined4 *)0x0;
  }
  param_2 = 0;
LAB_00324df8:
  *(undefined4 **)(this + 8) = __ptr;
  *(int *)(this + 0x10) = param_2;
  return;
}


