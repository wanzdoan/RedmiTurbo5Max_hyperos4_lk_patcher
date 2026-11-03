// ffff00009071ae48  size=124  FUN_ffff00009071ae48

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long FUN_ffff00009071ae48(uint param_1)

{
  long lVar1;
  
  FUN_ffff0000907175f4(&LAB_ffff0000908181f8,0xffffffff);
  if (((param_1 < 0x80) && (_DAT_ffff00009085b8a0 != 0)) &&
     (lVar1 = _DAT_ffff000090850c50 + (ulong)param_1 * 0x98, *(long *)(lVar1 + 0xc18) != 0)) {
    lVar1 = lVar1 + 0xc18;
  }
  else {
    lVar1 = 0;
  }
  FUN_ffff00009071768c(&LAB_ffff0000908181f8);
  return lVar1;
}


