
undefined8 FUN_ffff0000b8038914(int param_1,long param_2)

{
  undefined4 uVar1;
  undefined4 *puVar2;
  undefined4 *puVar3;
  undefined4 *puVar4;
  undefined4 *puVar5;
  
  if (*(char *)(param_2 + 0x2d8) != '\0') {
    if (param_1 == 0x12) {
      puVar2 = &DAT_ffff0000b8128f68;
      puVar4 = (undefined4 *)(param_2 + 0x2d4);
      puVar5 = &DAT_ffff0000b8128f6c;
      puVar3 = (undefined4 *)(param_2 + 0x2d0);
    }
    else {
      if (param_1 != 0xd) {
        return 0;
      }
      puVar2 = &DAT_ffff0000b8128f5c;
      puVar3 = (undefined4 *)(param_2 + 0x2c8);
      puVar5 = &DAT_ffff0000b8128f60;
      puVar4 = (undefined4 *)(param_2 + 0x2cc);
    }
    puVar2[2] = 0;
    uVar1 = *puVar4;
    *puVar2 = *puVar3;
    *puVar5 = uVar1;
  }
  return 0;
}


