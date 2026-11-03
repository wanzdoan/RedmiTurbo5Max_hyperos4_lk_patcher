
undefined8 FUN_ffff00009073deb8(int param_1)

{
  undefined8 uVar1;
  ulong uVar2;
  undefined4 *puVar3;
  
  uVar2 = (ulong)DAT_ffff00009086f190;
  if (DAT_ffff00009086f190 != 0) {
    puVar3 = (undefined4 *)0xffff00009086f198;
    do {
      if (puVar3[-1] == param_1) {
        uVar1 = FUN_ffff00009077b2cc(*puVar3);
        return uVar1;
      }
      puVar3 = puVar3 + 3;
      uVar2 = uVar2 - 1;
    } while (uVar2 != 0);
  }
  return 0;
}


