
undefined8 FUN_ffff0000b806c7e8(uint *param_1,undefined4 param_2,char *param_3)

{
  int iVar1;
  uint uVar2;
  uint uVar3;
  undefined8 uVar4;
  uint *puVar5;
  ulong uVar6;
  uint *puVar7;
  char *pcVar8;
  ulong uVar9;
  uint local_14;
  
  local_14 = 0;
  iVar1 = *(int *)PTR_DAT_ffff0000b8108820;
  if (iVar1 == 2) {
    puVar5 = param_1 + 0x83;
  }
  else if (iVar1 == 1) {
    puVar5 = param_1 + 99;
  }
  else {
    if (iVar1 != 0) {
      return 0x101023;
    }
    puVar5 = param_1 + 0x43;
  }
  puVar7 = param_1 + 3;
  uVar2 = *param_1;
  uVar9 = (ulong)uVar2;
  if (*(uint *)(param_3 + 0x404) == uVar2) {
    uVar3 = param_1[1];
    uVar6 = (ulong)uVar3;
    if (uVar2 != 0) {
      pcVar8 = param_3 + 0x204;
      do {
        if (*pcVar8 != (char)*puVar7) {
          return 0x7016;
        }
        puVar7 = (uint *)((long)puVar7 + 1);
        pcVar8 = pcVar8 + 1;
        uVar9 = uVar9 - 1;
      } while (uVar9 != 0);
    }
    if (*(uint *)(param_3 + 0x200) == uVar3) {
      pcVar8 = param_3;
      if (uVar3 != 0) {
        do {
          if (*pcVar8 != (char)*puVar5) {
            return 0x7018;
          }
          puVar5 = (uint *)((long)puVar5 + 1);
          uVar6 = uVar6 - 1;
          pcVar8 = pcVar8 + 1;
        } while (uVar6 != 0);
      }
      uVar4 = FUN_ffff0000b806c394(param_1,param_2,param_3);
      if (((int)uVar4 == 0) && (DAT_ffff0000b81b880c != 0)) {
        FUN_ffff0000b80212b4(&local_14);
        FUN_ffff0000b804b704(local_14 % 0x14);
        uVar4 = FUN_ffff0000b806c394(param_1,param_2,param_3);
      }
    }
    else {
      uVar4 = 0x7017;
    }
  }
  else {
    uVar4 = 0x7015;
  }
  return uVar4;
}


