// ffff0000907b2fac  size=216  FUN_ffff0000907b2fac

int FUN_ffff0000907b2fac(void)

{
  int iVar1;
  long lVar2;
  
  FUN_ffff00009077eb88(&DAT_ffff00009090d6c0,0,0x3c);
  FUN_ffff00009077eb88(&DAT_ffff00009090d700,0,0x3c);
  lVar2 = FUN_ffff00009077d294(0x40,0x3000);
  if (lVar2 == 0) {
    iVar1 = 0x100006;
  }
  else {
    iVar1 = FUN_ffff0000907b7a9c(s_seccfg_ffff0000907d1c33,lVar2,0,0x3000);
    if (iVar1 == 0) {
      FUN_ffff00009077e858(&DAT_ffff00009090d6c0,lVar2,0x3c);
      FUN_ffff00009077e858(&DAT_ffff00009090d700,lVar2 + 0x3c,0x3c);
    }
    else {
      FUN_ffff00009077de54
                (s___s__read_seccfg_fail__err_0x_x_ffff0000907f2106,s_SEC_CFG_ffff000090803332,iVar1
                );
    }
    thunk_FUN_ffff0000907c2ad8(lVar2);
  }
  return iVar1;
}


