// ffff0000907b647c  size=124  FUN_ffff0000907b647c

void FUN_ffff0000907b647c(long param_1,undefined8 *param_2,uint param_3)

{
  undefined1 *puVar1;
  ulong uVar2;
  uint uVar3;
  
  if (param_3 != 0) {
    uVar3 = param_3 * 8;
    uVar2 = (ulong)param_3;
    do {
      uVar3 = uVar3 - 8;
      puVar1 = (undefined1 *)(param_1 + (ulong)uVar3);
      uVar2 = uVar2 - 1;
      *puVar1 = *(undefined1 *)((long)param_2 + 7);
      puVar1[1] = (char)*(undefined2 *)((long)param_2 + 6);
      puVar1[2] = (char)((ulong)*param_2 >> 0x28);
      puVar1[3] = (char)*(undefined4 *)((long)param_2 + 4);
      puVar1[4] = (char)((ulong)*param_2 >> 0x18);
      puVar1[5] = (char)((ulong)*param_2 >> 0x10);
      puVar1[6] = (char)((ulong)*param_2 >> 8);
      puVar1[7] = (char)*param_2;
      param_2 = param_2 + 1;
    } while (uVar2 != 0);
  }
  return;
}


