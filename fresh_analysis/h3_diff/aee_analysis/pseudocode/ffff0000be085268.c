
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000be085268(void)

{
  undefined4 uVar1;
  
  uVar1 = (undefined4)(0x4ffffff030201ff >> (((ulong)_DAT_ffff00001800e000 & 7) << 3));
  if (7 < (_DAT_ffff00001800e000 & 0xf)) {
    uVar1 = 0xffffffff;
  }
  return uVar1;
}


