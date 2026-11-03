// ffff000090756f14  size=136  FUN_ffff000090756f14

undefined8 FUN_ffff000090756f14(int param_1,long param_2)

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
    puVar2 = (undefined4 *)&DAT_ffff00009087b634;
    break;
  default:
    goto code_r0xffff000090756fb8;
  case 0x13:
    puVar2 = (undefined4 *)&DAT_ffff00009087b64c;
    break;
  case 0x20:
    puVar2 = (undefined4 *)&DAT_ffff00009087b658;
    goto LAB_ffff000090756f8c;
  case 0x21:
    puVar2 = (undefined4 *)&DAT_ffff00009087b640;
LAB_ffff000090756f8c:
    puVar4 = (undefined4 *)(param_2 + 0x2d4);
    puVar3 = (undefined4 *)(param_2 + 0x2d0);
    goto LAB_ffff000090756f98;
  }
  puVar4 = (undefined4 *)(param_2 + 0x2cc);
LAB_ffff000090756f98:
  puVar2[2] = 0;
  uVar1 = *puVar4;
  *puVar2 = *puVar3;
  puVar2[1] = uVar1;
code_r0xffff000090756fb8:
  return 0;
}


