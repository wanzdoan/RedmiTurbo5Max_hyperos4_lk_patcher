// ffff000090756c7c  size=136  FUN_ffff000090756c7c

undefined8 FUN_ffff000090756c7c(int param_1,long param_2)

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
    puVar2 = (undefined4 *)&DAT_ffff00009087b59c;
    break;
  default:
    goto code_r0xffff000090756d20;
  case 0x13:
    puVar2 = (undefined4 *)&DAT_ffff00009087b5b4;
    break;
  case 0x20:
    puVar2 = (undefined4 *)&DAT_ffff00009087b5c0;
    goto LAB_ffff000090756cf4;
  case 0x21:
    puVar2 = (undefined4 *)&DAT_ffff00009087b5a8;
LAB_ffff000090756cf4:
    puVar4 = (undefined4 *)(param_2 + 0x2d4);
    puVar3 = (undefined4 *)(param_2 + 0x2d0);
    goto LAB_ffff000090756d00;
  }
  puVar4 = (undefined4 *)(param_2 + 0x2cc);
LAB_ffff000090756d00:
  puVar2[2] = 0;
  uVar1 = *puVar4;
  *puVar2 = *puVar3;
  puVar2[1] = uVar1;
code_r0xffff000090756d20:
  return 0;
}


