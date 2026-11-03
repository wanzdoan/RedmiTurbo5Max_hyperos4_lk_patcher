
bool FUN_ffff0000907c5cb8(void)

{
  bool bVar1;
  int iVar2;
  long lVar3;
  
  DAT_ffff00009090d9c8 = 0x8c00000;
  lVar3 = FUN_ffff0000907198dc(0x8c00000,0x10000,0xffffffffffffffff,0,0,s_mb_avb_ffff0000908001f1);
  if (lVar3 == 0) {
    bVar1 = true;
  }
  else {
    iVar2 = FUN_ffff00009077ba64
                      (PTR_DAT_ffff000090839928,s_vm_avb_ffff0000907ef222,DAT_ffff00009090d9c8,
                       &DAT_ffff00009090d9d0,0xc,lVar3,0,0);
    bVar1 = iVar2 != 0;
  }
  return bVar1;
}


