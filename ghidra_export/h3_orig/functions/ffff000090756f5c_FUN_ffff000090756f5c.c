// ffff000090756f5c  size=216  FUN_ffff000090756f5c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090756f5c(undefined4 param_1,long param_2)

{
  undefined4 *puVar1;
  int *piVar2;
  undefined4 uVar3;
  int iVar4;
  undefined4 uVar5;
  int iVar6;
  
  if (*(char *)(param_2 + 0x2d8) == '\0') {
    return 0;
  }
  switch(param_1) {
  case 0x14:
    puVar1 = (undefined4 *)&DAT_ffff00009087b5fc;
    uVar3 = *(undefined4 *)(param_2 + 0x2c8);
    uVar5 = *(undefined4 *)(param_2 + 0x2cc);
    break;
  case 0x15:
    piVar2 = (int *)&DAT_ffff00009087b5e4;
    iVar4 = *(int *)(param_2 + 0x2cc) + 8;
    iVar6 = *(int *)(param_2 + 0x2c8) + 8;
    _DAT_ffff00009087b5ec = 8;
    *(int *)(param_2 + 0x2cc) = iVar4;
    *(int *)(param_2 + 0x2c8) = iVar6;
    goto LAB_ffff000090757020;
  default:
    goto code_r0xffff000090757028;
  case 0x1a:
    uVar3 = *(undefined4 *)(param_2 + 0x2d0);
    puVar1 = (undefined4 *)&DAT_ffff00009087b608;
    uVar5 = *(undefined4 *)(param_2 + 0x2d4);
    break;
  case 0x1b:
    piVar2 = (int *)&DAT_ffff00009087b5f0;
    iVar4 = *(int *)(param_2 + 0x2d4) + 8;
    iVar6 = *(int *)(param_2 + 0x2d0) + 8;
    _DAT_ffff00009087b5f8 = 8;
    *(int *)(param_2 + 0x2d4) = iVar4;
    *(int *)(param_2 + 0x2d0) = iVar6;
LAB_ffff000090757020:
    *piVar2 = iVar6;
    piVar2[1] = iVar4;
    goto code_r0xffff000090757028;
  }
  puVar1[1] = uVar5;
  puVar1[2] = 0;
  *puVar1 = uVar3;
code_r0xffff000090757028:
  return 0;
}


