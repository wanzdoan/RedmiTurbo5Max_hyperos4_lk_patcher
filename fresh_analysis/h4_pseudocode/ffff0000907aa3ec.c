
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000907aa3ec(uint *param_1)

{
  undefined4 uVar1;
  uint uVar2;
  
  uVar2 = _DAT_ffff000013260060 >> 0x1c & 7;
  uVar1 = 0x101023;
  if (uVar2 < 3) {
    uVar1 = 0;
  }
  *param_1 = uVar2;
  return uVar1;
}


