// ffff0000907c5644  size=144  FUN_ffff0000907c5644

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

bool FUN_ffff0000907c5644(void)

{
  bool bVar1;
  int iVar2;
  long lVar3;
  
  _DAT_ffff00009090d908 = 0x8c00000;
  lVar3 = FUN_ffff000090719798(0x8c00000,0x10000,0xffffffffffffffff,0,0,s_mb_avb_ffff000090800049);
  if (lVar3 == 0) {
    bVar1 = true;
  }
  else {
    iVar2 = FUN_ffff00009077b79c
                      (DAT_ffff000090839908,s_vm_avb_ffff0000907ef09a,_DAT_ffff00009090d908,
                       &DAT_ffff00009090d910,0xc,lVar3,0,0);
    bVar1 = iVar2 != 0;
  }
  return bVar1;
}


