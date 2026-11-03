// ffff000090757a7c  size=136  FUN_ffff000090757a7c

undefined8 FUN_ffff000090757a7c(int param_1,long param_2)

{
  undefined4 *puVar1;
  undefined4 *puVar2;
  
  puVar1 = (undefined4 *)&DAT_ffff000032560000;
  if (param_1 != 0xf6) {
    puVar1 = (undefined4 *)&DAT_ffff000032550000;
  }
  puVar2 = (undefined4 *)&DAT_ffff000032550000;
  if (param_1 != 0xf5) {
    puVar2 = puVar1;
  }
  puVar2[0x13] = puVar2[0x13] | 2;
  puVar2[1] = *(uint *)(param_2 + 0x928) | *(int *)(param_2 + 0x92c) << 0x10;
  puVar2[4] = 0;
  puVar2[5] = *(int *)(param_2 + 0x92c) << 0x10 | *(uint *)(param_2 + 0x928) >> 1;
  puVar2[6] = *(uint *)(param_2 + 0x928) >> 1;
  puVar2[7] = *(int *)(param_2 + 0x92c) << 0x10 | *(uint *)(param_2 + 0x928) >> 1;
  *puVar2 = 0x10001;
  return 0;
}


