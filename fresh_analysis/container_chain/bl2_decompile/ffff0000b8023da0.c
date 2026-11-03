
undefined8 FUN_ffff0000b8023da0(int param_1)

{
  undefined8 uVar1;
  ulong uVar2;
  undefined4 *puVar3;
  
  uVar2 = (ulong)DAT_ffff0000b811cc20;
  if (DAT_ffff0000b811cc20 != 0) {
    puVar3 = (undefined4 *)0xffff0000b811cc28;
    do {
      if (puVar3[-1] == param_1) {
        uVar1 = FUN_ffff0000b8049e60(*puVar3);
        return uVar1;
      }
      puVar3 = puVar3 + 3;
      uVar2 = uVar2 - 1;
    } while (uVar2 != 0);
  }
  return 0;
}


