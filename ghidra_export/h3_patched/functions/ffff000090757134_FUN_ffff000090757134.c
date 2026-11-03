// ffff000090757134  size=76  FUN_ffff000090757134

undefined8 FUN_ffff000090757134(int param_1,long param_2)

{
  undefined4 uVar1;
  undefined4 *puVar2;
  undefined4 *puVar3;
  undefined4 *puVar4;
  undefined4 *puVar5;
  
  if (*(char *)(param_2 + 0x2d8) != '\0') {
    if (param_1 == 0x1c) {
      puVar2 = (undefined4 *)&DAT_ffff00009087b620;
      puVar4 = (undefined4 *)(param_2 + 0x2d4);
      puVar5 = (undefined4 *)&DAT_ffff00009087b624;
      puVar3 = (undefined4 *)(param_2 + 0x2d0);
    }
    else {
      if (param_1 != 0x16) {
        return 0;
      }
      puVar2 = (undefined4 *)&DAT_ffff00009087b614;
      puVar3 = (undefined4 *)(param_2 + 0x2c8);
      puVar5 = (undefined4 *)&DAT_ffff00009087b618;
      puVar4 = (undefined4 *)(param_2 + 0x2cc);
    }
    puVar2[2] = 0;
    uVar1 = *puVar4;
    *puVar2 = *puVar3;
    *puVar5 = uVar1;
  }
  return 0;
}


