// ffff000090786628  size=176  FUN_ffff000090786628

void FUN_ffff000090786628(undefined8 *param_1)

{
  undefined4 uVar1;
  int iVar2;
  
  uVar1 = FUN_ffff000090785f04();
  *(undefined4 *)(param_1 + 4) = uVar1;
  *(undefined4 *)((long)param_1 + 0x24) = 0;
  *param_1 = 1;
  param_1[1] = 0;
  *(undefined8 *)((long)param_1 + 0x14) = 1;
  *(undefined4 *)((long)param_1 + 0x1c) = 1;
  iVar2 = FUN_ffff00009077f080(&DAT_ffff0000907df27e,&DAT_ffff0000907f2e0e,3);
  if (iVar2 == 0) {
    uVar1 = 0x10e;
  }
  else {
    iVar2 = FUN_ffff00009077f080(&DAT_ffff0000907df27e,&DAT_ffff0000907d947d,2);
    if (iVar2 == 0) {
      uVar1 = 0x5a;
    }
    else {
      iVar2 = FUN_ffff00009077f080(&DAT_ffff0000907df27e,&DAT_ffff0000907f65b6,3);
      if (iVar2 != 0) {
        *(undefined4 *)(param_1 + 2) = 0;
        return;
      }
      uVar1 = 0xb4;
    }
  }
  *(undefined4 *)(param_1 + 2) = uVar1;
  return;
}


