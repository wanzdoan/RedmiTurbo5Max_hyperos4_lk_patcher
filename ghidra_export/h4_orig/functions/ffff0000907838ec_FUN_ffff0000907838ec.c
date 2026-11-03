// ffff0000907838ec  size=112  FUN_ffff0000907838ec

long FUN_ffff0000907838ec(void)

{
  int iVar1;
  long lVar2;
  
  lVar2 = FUN_ffff000090721d30(s_mrdump_enable_ffff0000907dd34d);
  if (lVar2 == 0) {
    lVar2 = 2;
  }
  else {
    iVar1 = FUN_ffff00009077f0ac(lVar2,&DAT_ffff0000907f0cf7);
    if (iVar1 == 0) {
      lVar2 = 3;
    }
    else {
      iVar1 = FUN_ffff00009077f0ac(lVar2,&DAT_ffff0000907df35d);
      lVar2 = (ulong)(iVar1 != 0) << 1;
    }
  }
  return lVar2;
}


