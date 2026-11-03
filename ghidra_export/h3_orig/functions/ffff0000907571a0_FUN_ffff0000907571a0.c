// ffff0000907571a0  size=280  FUN_ffff0000907571a0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907571a0(int param_1,long param_2)

{
  long lVar1;
  long lVar2;
  int iVar3;
  uint uVar4;
  ulong uVar5;
  undefined4 *puVar6;
  
  if (*(int *)(param_2 + 0xc) != 0) {
    if (*(char *)(param_2 + 0x2d8) == '\0') {
      lVar1 = 0x20000;
      if (param_1 != 0xf2) {
        lVar1 = (ulong)(param_1 == 0x29) << 0x10;
      }
      lVar2 = 0x30000;
      if (param_1 != 0xf3) {
        lVar2 = lVar1;
      }
      *(uint *)(&DAT_ffff000032050024 + lVar2) =
           *(uint *)(param_2 + 0x92c) | *(int *)(param_2 + 0x928) << 0x10;
      *(undefined4 *)(&DAT_ffff000032050004 + lVar2) = 0x11;
      *(undefined4 *)(&DAT_ffff000032050000 + lVar2) = 1;
    }
    else {
      if (param_1 < 0xf2) {
        iVar3 = _DAT_ffff00009087b5cc;
        if (param_1 == 0x28) {
          uVar5 = 0x32050004;
          uVar4 = *(uint *)(param_2 + 0x92c);
        }
        else {
          if (param_1 != 0x29) {
            return 0;
          }
          uVar4 = *(uint *)(param_2 + 0x92c);
          uVar5 = 0x32060004;
        }
      }
      else {
        iVar3 = _DAT_ffff00009087b5d8;
        if (param_1 == 0xf2) {
          uVar4 = *(uint *)(param_2 + 0x92c);
          uVar5 = 0x32070004;
        }
        else {
          if (param_1 != 0xf3) {
            return 0;
          }
          uVar4 = *(uint *)(param_2 + 0x92c);
          uVar5 = 0x32080004;
        }
      }
      puVar6 = (undefined4 *)(uVar5 | 0xffff000000000000);
      puVar6[8] = uVar4 | iVar3 << 0x10;
      *puVar6 = 0x11;
      puVar6[-1] = 1;
    }
  }
  return 0;
}


