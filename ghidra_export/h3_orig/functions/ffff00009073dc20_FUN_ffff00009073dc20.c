// ffff00009073dc20  size=72  FUN_ffff00009073dc20

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009073dc20(int param_1)

{
  undefined8 uVar1;
  ulong uVar2;
  undefined4 *puVar3;
  
  uVar2 = (ulong)_DAT_ffff00009086f0f8;
  if (_DAT_ffff00009086f0f8 != 0) {
    puVar3 = (undefined4 *)0xffff00009086f100;
    do {
      if (puVar3[-1] == param_1) {
        uVar1 = FUN_ffff00009077b004(*puVar3);
        return uVar1;
      }
      puVar3 = puVar3 + 3;
      uVar2 = uVar2 - 1;
    } while (uVar2 != 0);
  }
  return 0;
}


