// ffff0000907571f4  size=216  FUN_ffff0000907571f4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907571f4(undefined4 param_1,long param_2)

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
    puVar1 = (undefined4 *)&DAT_ffff00009087b694;
    uVar3 = *(undefined4 *)(param_2 + 0x2c8);
    uVar5 = *(undefined4 *)(param_2 + 0x2cc);
    break;
  case 0x15:
    piVar2 = (int *)&DAT_ffff00009087b67c;
    iVar4 = *(int *)(param_2 + 0x2cc) + 8;
    iVar6 = *(int *)(param_2 + 0x2c8) + 8;
    _DAT_ffff00009087b684 = 8;
    *(int *)(param_2 + 0x2cc) = iVar4;
    *(int *)(param_2 + 0x2c8) = iVar6;
    goto LAB_ffff0000907572b8;
  default:
    goto code_r0xffff0000907572c0;
  case 0x1a:
    uVar3 = *(undefined4 *)(param_2 + 0x2d0);
    puVar1 = (undefined4 *)&DAT_ffff00009087b6a0;
    uVar5 = *(undefined4 *)(param_2 + 0x2d4);
    break;
  case 0x1b:
    piVar2 = (int *)&DAT_ffff00009087b688;
    iVar4 = *(int *)(param_2 + 0x2d4) + 8;
    iVar6 = *(int *)(param_2 + 0x2d0) + 8;
    _DAT_ffff00009087b690 = 8;
    *(int *)(param_2 + 0x2d4) = iVar4;
    *(int *)(param_2 + 0x2d0) = iVar6;
LAB_ffff0000907572b8:
    *piVar2 = iVar6;
    piVar2[1] = iVar4;
    goto code_r0xffff0000907572c0;
  }
  puVar1[1] = uVar5;
  puVar1[2] = 0;
  *puVar1 = uVar3;
code_r0xffff0000907572c0:
  return 0;
}


