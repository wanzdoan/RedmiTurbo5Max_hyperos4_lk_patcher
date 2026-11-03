// ffff00009077e858  size=280  FUN_ffff00009077e858

void FUN_ffff00009077e858(undefined8 *param_1,undefined8 *param_2,ulong param_3)

{
  undefined8 *puVar1;
  uint uVar2;
  undefined8 *puVar3;
  uint uVar4;
  int iVar5;
  
  uVar4 = (uint)param_1;
  if ((param_1 != param_2) && (param_3 != 0)) {
    if ((((uint)param_2 | uVar4) & 7) != 0) {
      iVar5 = (int)param_3;
      if ((((uint)param_2 ^ uVar4) & 7) == 0 && 7 < param_3) {
        iVar5 = 8 - (uVar4 & 7);
      }
      param_3 = param_3 - (long)iVar5;
      if (0 < iVar5) {
        uVar4 = iVar5 + 1;
        puVar1 = param_2;
        puVar3 = param_1;
        do {
          param_2 = (undefined8 *)((long)puVar1 + 1);
          uVar4 = uVar4 - 1;
          param_1 = (undefined8 *)((long)puVar3 + 1);
          *(undefined1 *)puVar3 = *(undefined1 *)puVar1;
          puVar1 = param_2;
          puVar3 = param_1;
        } while (1 < uVar4);
      }
    }
    iVar5 = (int)(param_3 >> 3);
    if (0 < iVar5) {
      uVar4 = iVar5 + 1;
      puVar1 = param_2;
      puVar3 = param_1;
      do {
        param_2 = puVar1 + 1;
        uVar4 = uVar4 - 1;
        param_1 = puVar3 + 1;
        *puVar3 = *puVar1;
        puVar1 = param_2;
        puVar3 = param_1;
      } while (1 < uVar4);
    }
    uVar2 = (uint)param_3;
    uVar4 = uVar2 & 7;
    if (((((param_3 & 7) != 0) && (*(undefined1 *)param_1 = *(undefined1 *)param_2, 1 < uVar4)) &&
        (*(undefined1 *)((long)param_1 + 1) = *(undefined1 *)((long)param_2 + 1),
        uVar4 - 3 < 0xfffffffe)) &&
       (((*(undefined1 *)((long)param_1 + 2) = *(undefined1 *)((long)param_2 + 2), (uVar2 & 6) != 2
         && (*(undefined1 *)((long)param_1 + 3) = *(undefined1 *)((long)param_2 + 3),
            uVar4 - 5 < 0xfffffffe)) &&
        ((*(undefined1 *)((long)param_1 + 4) = *(undefined1 *)((long)param_2 + 4), (uVar2 & 6) != 4
         && (*(undefined1 *)((long)param_1 + 5) = *(undefined1 *)((long)param_2 + 5),
            uVar4 - 7 < 0xfffffffe)))))) {
      *(undefined1 *)((long)param_1 + 6) = *(undefined1 *)((long)param_2 + 6);
    }
  }
  return;
}


