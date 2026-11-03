
long FUN_ffff00009071af8c(uint param_1)

{
  long lVar1;
  
  FUN_ffff000090717738(&LAB_ffff000090818208,0xffffffff);
  if (((param_1 < 0x80) && (DAT_ffff00009085b8e0 != 0)) &&
     (lVar1 = DAT_ffff000090850c90 + (ulong)param_1 * 0x98, *(long *)(lVar1 + 0xc18) != 0)) {
    lVar1 = lVar1 + 0xc18;
  }
  else {
    lVar1 = 0;
  }
  FUN_ffff0000907177d0(&LAB_ffff000090818208);
  return lVar1;
}


