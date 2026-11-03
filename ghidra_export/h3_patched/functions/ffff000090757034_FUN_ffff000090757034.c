// ffff000090757034  size=192  FUN_ffff000090757034

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090757034(int param_1,long param_2)

{
  long lVar1;
  int iVar2;
  ulong uVar3;
  uint *puVar4;
  uint uVar5;
  
  if (*(int *)(param_2 + 0xc) != 0) {
    if (*(char *)(param_2 + 0x2d8) == '\0') {
      uVar5 = *(uint *)(param_2 + 0x92c);
      lVar1 = (ulong)(param_1 == 0x1c) * 0x10000;
      iVar2 = *(int *)(param_2 + 0x928);
      *(uint *)(&DAT_ffff000032130020 + lVar1) = *(uint *)(&DAT_ffff000032130020 + lVar1) | 1;
      *(uint *)(&DAT_ffff000032130030 + lVar1) = uVar5 | iVar2 << 0x10;
      *(undefined4 *)(&DAT_ffff000032130000 + lVar1) = 1;
    }
    else {
      if (param_1 == 0x1c) {
        uVar5 = *(uint *)(param_2 + 0x92c);
        uVar3 = 0x32140020;
        iVar2 = _DAT_ffff00009087b620;
      }
      else {
        if (param_1 != 0x16) {
          return 0;
        }
        uVar3 = 0x32130020;
        uVar5 = *(uint *)(param_2 + 0x92c);
        iVar2 = _DAT_ffff00009087b614;
      }
      puVar4 = (uint *)(uVar3 | 0xffff000000000000);
      *puVar4 = *puVar4 | 1;
      puVar4[4] = uVar5 | iVar2 << 0x10;
      puVar4[-8] = 1;
    }
  }
  return 0;
}


