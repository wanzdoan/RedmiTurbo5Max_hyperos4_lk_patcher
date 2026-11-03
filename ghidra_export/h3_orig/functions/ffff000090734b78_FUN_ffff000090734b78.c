// ffff000090734b78  size=428  FUN_ffff000090734b78

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff000090734b78(long param_1,long param_2,undefined8 param_3)

{
  int iVar1;
  uint uVar2;
  undefined4 uVar3;
  uint uVar4;
  long lVar5;
  ulong uVar6;
  
  *(undefined4 *)(param_2 + 0x10) = 0;
  *(undefined8 *)(param_2 + 0x18) = *(undefined8 *)(param_2 + 0x20);
  *(undefined8 *)(param_2 + 0x20) = 1;
  lVar5 = FUN_ffff00009077bc50(s_bootdevice_ffff0000907d0ccc);
  if (lVar5 == 0) {
    lVar5 = -0x12;
  }
  else {
    iVar1 = *(int *)(lVar5 + 0x38);
    FUN_ffff00009077bce4();
    lVar5 = (ulong)(iVar1 - 1) + 1;
  }
  uVar3 = _DAT_ffff00009086da30;
  *(ulong *)(param_2 + 0x48) = lVar5 - (ulong)_DAT_ffff00009086da2c;
  *(undefined4 *)(param_2 + 0x58) = uVar3;
  uVar3 = FUN_ffff00009076c7c4(0,param_2,0x5c);
  *(undefined4 *)(param_2 + 0x10) = uVar3;
  lVar5 = FUN_ffff00009077bc50(s_bootdevice_ffff0000907d0ccc);
  if (lVar5 == 0) {
    uVar6 = 0xffffffffffffffed;
  }
  else {
    uVar6 = (ulong)(*(int *)(lVar5 + 0x38) - 1);
    FUN_ffff00009077bce4();
  }
  uVar4 = FUN_ffff00009077be7c(param_1,param_2,*(long *)(param_1 + 0x28) * uVar6);
  if (((int)uVar4 < 0) || (*(ulong *)(param_1 + 0x28) != (ulong)uVar4)) {
    FUN_ffff00009077de54
              (s__s__d___GPT_Update_write_sheader_ffff000090802513,
               s_write_secondary_gpt_ffff0000907ef9e4,0x135,uVar4);
  }
  else {
    lVar5 = FUN_ffff00009077bc50(s_bootdevice_ffff0000907d0ccc);
    if (lVar5 == 0) {
      lVar5 = -0x12;
    }
    else {
      iVar1 = *(int *)(lVar5 + 0x38);
      FUN_ffff00009077bce4();
      lVar5 = (ulong)(iVar1 - 1) + 1;
    }
    uVar6 = *(ulong *)(param_1 + 0x28);
    uVar4 = 0;
    if ((ulong)*(uint *)(param_2 + 0x54) != 0) {
      uVar4 = (uint)(uVar6 / *(uint *)(param_2 + 0x54));
    }
    uVar2 = 0;
    if (uVar4 != 0) {
      uVar2 = ((uVar4 + *(int *)(param_2 + 0x50)) - 1) / uVar4;
    }
    uVar4 = FUN_ffff00009077be7c
                      (param_1,param_3,(lVar5 - (ulong)_DAT_ffff00009086da2c) * uVar6,uVar6 * uVar2)
    ;
    if (((int)uVar4 < 0) || ((ulong)uVar4 != uVar6 * uVar2)) {
      FUN_ffff00009077de54
                (s__s__d___GPT_Update_write_pentrie_ffff0000907d2c7c,
                 s_write_secondary_gpt_ffff0000907ef9e4,0x141,uVar4);
    }
    else {
      uVar4 = 0;
    }
  }
  return uVar4;
}


