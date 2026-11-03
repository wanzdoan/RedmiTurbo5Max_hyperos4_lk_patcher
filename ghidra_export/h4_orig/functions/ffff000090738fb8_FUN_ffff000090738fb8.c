// ffff000090738fb8  size=292  FUN_ffff000090738fb8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090738fb8(ulong param_1,ulong param_2)

{
  uint uVar1;
  int *piVar2;
  undefined4 *puVar3;
  long lVar4;
  
  uVar1 = _DAT_ffff00001cf2a000;
  _DAT_ffff00001cf2a000 = _DAT_ffff00001cf2a000 & 0x7fffffff | 0x30000000;
  FUN_ffff00009077e11c(s__SCP__R_SEC_CTRL__0x_x_ffff0000907fb92f);
  puVar3 = DAT_ffff000090819338;
  piVar2 = DAT_ffff000090819330;
  if (param_1 <= param_2) {
    lVar4 = 0;
    do {
      *piVar2 = (int)lVar4;
      *(undefined4 *)(param_1 + lVar4) = *puVar3;
      lVar4 = lVar4 + 4;
    } while (lVar4 != 0x800);
    lVar4 = 0;
    do {
      *piVar2 = (int)lVar4 + 0x800;
      *(undefined4 *)(param_1 + 0x800 + lVar4) = *puVar3;
      lVar4 = lVar4 + 4;
    } while ((int)lVar4 != 0x800);
    lVar4 = 0;
    do {
      *piVar2 = (int)lVar4 + 0x1000;
      *(undefined4 *)(param_1 + 0x1000 + lVar4) = *puVar3;
      lVar4 = lVar4 + 4;
    } while ((int)lVar4 != 0x800);
    lVar4 = 0;
    do {
      *piVar2 = (int)lVar4 + 0x4000;
      *(undefined4 *)(param_1 + 0x1800 + lVar4) = *puVar3;
      lVar4 = lVar4 + 4;
    } while ((int)lVar4 != 0x4000);
    lVar4 = 0;
    do {
      *piVar2 = (int)lVar4 + 0x100000;
      *(undefined4 *)(param_1 + 0x5800 + lVar4) = *puVar3;
      lVar4 = lVar4 + 4;
      _DAT_ffff00001cf2a000 = uVar1;
    } while ((int)lVar4 != 0x80000);
  }
  return;
}


