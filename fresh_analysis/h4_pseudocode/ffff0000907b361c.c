
int FUN_ffff0000907b361c(void)

{
  int iVar1;
  long lVar2;
  
  FUN_ffff00009077ee50(&DAT_ffff00009090d780,0,0x3c);
  FUN_ffff00009077ee50(&DAT_ffff00009090d7c0,0,0x3c);
  lVar2 = FUN_ffff00009077d55c(0x40,0x3000);
  if (lVar2 == 0) {
    iVar1 = 0x100006;
  }
  else {
    iVar1 = FUN_ffff0000907b8110(s_seccfg_ffff0000907d1c70,lVar2,0,0x3000);
    if (iVar1 == 0) {
      FUN_ffff00009077eb20(&DAT_ffff00009090d780,lVar2,0x3c);
      FUN_ffff00009077eb20(&DAT_ffff00009090d7c0,lVar2 + 0x3c,0x3c);
    }
    else {
      FUN_ffff00009077e11c
                (s___s__read_seccfg_fail__err_0x_x_ffff0000907f2292,s_SEC_CFG_ffff00009080355a,iVar1
                );
    }
    thunk_FUN_ffff0000907c314c(lVar2);
  }
  return iVar1;
}


