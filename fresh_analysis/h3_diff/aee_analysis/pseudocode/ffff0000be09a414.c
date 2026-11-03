
bool FUN_ffff0000be09a414(void)

{
  bool bVar1;
  int iVar2;
  long lVar3;
  
  DAT_ffff0000be1b7e38 = 0x8c00000;
  lVar3 = FUN_ffff0000be01c3b4(0x8c00000,0x10000,0xffffffffffffffff,0,0,s_mb_avb_ffff0000be0c4b4b);
  if (lVar3 == 0) {
    bVar1 = true;
  }
  else {
    iVar2 = FUN_ffff0000be05d9a8
                      (PTR_DAT_ffff0000be0f5570,s_vm_avb_ffff0000be0b960c,DAT_ffff0000be1b7e38,
                       &DAT_ffff0000be1b7e40,0xc,lVar3,0,0);
    bVar1 = iVar2 != 0;
  }
  return bVar1;
}


