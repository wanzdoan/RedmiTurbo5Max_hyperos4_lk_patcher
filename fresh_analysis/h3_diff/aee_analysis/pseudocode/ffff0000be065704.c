
long FUN_ffff0000be065704(void)

{
  int iVar1;
  long lVar2;
  
  lVar2 = FUN_ffff0000be01fa8c(s_mrdump_enable_ffff0000be0acf6b);
  if (lVar2 == 0) {
    lVar2 = 2;
  }
  else {
    iVar1 = FUN_ffff0000be060cb8(lVar2,&DAT_ffff0000be0bac01);
    if (iVar1 == 0) {
      lVar2 = 3;
    }
    else {
      iVar1 = FUN_ffff0000be060cb8(lVar2,&DAT_ffff0000be0ae542);
      lVar2 = (ulong)(iVar1 != 0) << 1;
    }
  }
  return lVar2;
}


