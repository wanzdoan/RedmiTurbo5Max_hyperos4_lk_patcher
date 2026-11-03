
undefined8 FUN_ffff0000be042584(int param_1,long param_2)

{
  undefined4 uVar1;
  undefined4 *puVar2;
  undefined4 *puVar3;
  undefined4 *puVar4;
  undefined4 *puVar5;
  
  if (*(char *)(param_2 + 0x2d8) != '\0') {
    if (param_1 < 0xf2) {
      puVar3 = (undefined4 *)(param_2 + 0x2c8);
      if ((param_1 != 0x28) && (param_1 != 0x29)) {
        return 0;
      }
      puVar2 = &DAT_ffff0000be1257a4;
      puVar5 = &DAT_ffff0000be1257a8;
      puVar4 = (undefined4 *)(param_2 + 0x2cc);
    }
    else {
      if ((param_1 != 0xf2) && (param_1 != 0xf3)) {
        return 0;
      }
      puVar2 = &DAT_ffff0000be1257b0;
      puVar4 = (undefined4 *)(param_2 + 0x2d4);
      puVar5 = &DAT_ffff0000be1257b4;
      puVar3 = (undefined4 *)(param_2 + 0x2d0);
    }
    puVar2[2] = 0;
    uVar1 = *puVar4;
    *puVar2 = *puVar3;
    *puVar5 = uVar1;
  }
  return 0;
}


