
undefined8 FUN_ffff0000b802d630(int param_1,long param_2)

{
  undefined4 uVar1;
  undefined4 *puVar2;
  undefined4 *puVar3;
  undefined4 *puVar4;
  
  if (*(char *)(param_2 + 0x2d8) != '\0') {
    if (param_1 < 0xb8) {
      puVar3 = (undefined4 *)(param_2 + 0x2c8);
      if (param_1 == 0x18) {
        puVar2 = &DAT_ffff0000b8125560;
      }
      else {
        if (param_1 != 0x1e) {
          return 0;
        }
        puVar2 = &DAT_ffff0000b8125578;
      }
      puVar4 = (undefined4 *)(param_2 + 0x2cc);
    }
    else {
      if (param_1 == 0xb8) {
        puVar2 = &DAT_ffff0000b812556c;
      }
      else {
        if (param_1 != 0xb9) {
          return 0;
        }
        puVar2 = &DAT_ffff0000b8125584;
      }
      puVar4 = (undefined4 *)(param_2 + 0x2d4);
      puVar3 = (undefined4 *)(param_2 + 0x2d0);
    }
    puVar2[2] = 0;
    uVar1 = *puVar4;
    *puVar2 = *puVar3;
    puVar2[1] = uVar1;
  }
  return 0;
}


