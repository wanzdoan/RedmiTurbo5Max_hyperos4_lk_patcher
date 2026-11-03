// ffff0000907573cc  size=76  FUN_ffff0000907573cc

undefined8 FUN_ffff0000907573cc(int param_1,long param_2)

{
  undefined4 uVar1;
  undefined4 *puVar2;
  undefined4 *puVar3;
  undefined4 *puVar4;
  undefined4 *puVar5;
  
  if (*(char *)(param_2 + 0x2d8) != '\0') {
    if (param_1 == 0x1c) {
      puVar2 = (undefined4 *)&DAT_ffff00009087b6b8;
      puVar4 = (undefined4 *)(param_2 + 0x2d4);
      puVar5 = (undefined4 *)&DAT_ffff00009087b6bc;
      puVar3 = (undefined4 *)(param_2 + 0x2d0);
    }
    else {
      if (param_1 != 0x16) {
        return 0;
      }
      puVar2 = (undefined4 *)&DAT_ffff00009087b6ac;
      puVar3 = (undefined4 *)(param_2 + 0x2c8);
      puVar5 = (undefined4 *)&DAT_ffff00009087b6b0;
      puVar4 = (undefined4 *)(param_2 + 0x2cc);
    }
    puVar2[2] = 0;
    uVar1 = *puVar4;
    *puVar2 = *puVar3;
    *puVar5 = uVar1;
  }
  return 0;
}


