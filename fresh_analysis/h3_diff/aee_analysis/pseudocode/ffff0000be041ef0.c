
undefined8 FUN_ffff0000be041ef0(int param_1,long param_2)

{
  undefined4 uVar1;
  undefined4 *puVar2;
  undefined4 *puVar3;
  undefined4 *puVar4;
  
  if (*(char *)(param_2 + 0x2d8) == '\0') {
    return 0;
  }
  if (0x13 < param_1 - 0xeU) {
    return 0;
  }
  puVar3 = (undefined4 *)(param_2 + 0x2c8);
  switch(param_1) {
  case 0xe:
    puVar2 = &DAT_ffff0000be125774;
    break;
  default:
    goto code_r0xffff0000be041f94;
  case 0x13:
    puVar2 = &DAT_ffff0000be12578c;
    break;
  case 0x20:
    puVar2 = &DAT_ffff0000be125798;
    goto LAB_ffff0000be041f68;
  case 0x21:
    puVar2 = &DAT_ffff0000be125780;
LAB_ffff0000be041f68:
    puVar4 = (undefined4 *)(param_2 + 0x2d4);
    puVar3 = (undefined4 *)(param_2 + 0x2d0);
    goto LAB_ffff0000be041f74;
  }
  puVar4 = (undefined4 *)(param_2 + 0x2cc);
LAB_ffff0000be041f74:
  puVar2[2] = 0;
  uVar1 = *puVar4;
  *puVar2 = *puVar3;
  puVar2[1] = uVar1;
code_r0xffff0000be041f94:
  return 0;
}


