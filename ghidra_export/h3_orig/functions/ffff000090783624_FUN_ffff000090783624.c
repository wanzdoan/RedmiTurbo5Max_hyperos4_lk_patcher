// ffff000090783624  size=112  FUN_ffff000090783624

long FUN_ffff000090783624(void)

{
  int iVar1;
  long lVar2;
  
  lVar2 = FUN_ffff000090721af0(s_mrdump_enable_ffff0000907dd277);
  if (lVar2 == 0) {
    lVar2 = 2;
  }
  else {
    iVar1 = FUN_ffff00009077ede4(lVar2,&DAT_ffff0000907f0b86);
    if (iVar1 == 0) {
      lVar2 = 3;
    }
    else {
      iVar1 = FUN_ffff00009077ede4(lVar2,&DAT_ffff0000907df2ba);
      lVar2 = (ulong)(iVar1 != 0) << 1;
    }
  }
  return lVar2;
}


