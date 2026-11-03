
undefined8 FUN_ffff0000be02e184(int param_1)

{
  undefined8 uVar1;
  ulong uVar2;
  undefined4 *puVar3;
  
  uVar2 = (ulong)DAT_ffff0000be119458;
  if (DAT_ffff0000be119458 != 0) {
    puVar3 = (undefined4 *)0xffff0000be119460;
    do {
      if (puVar3[-1] == param_1) {
        uVar1 = FUN_ffff0000be05d1c0(*puVar3);
        return uVar1;
      }
      puVar3 = puVar3 + 3;
      uVar2 = uVar2 - 1;
    } while (uVar2 != 0);
  }
  return 0;
}


