
int FUN_ffff0000be08d810(void)

{
  int iVar1;
  long lVar2;
  
  FUN_ffff0000be060ae0(&DAT_ffff0000be1b7c40,0,0x3c);
  FUN_ffff0000be060ae0(&DAT_ffff0000be1b7c80,0,0x3c);
  lVar2 = FUN_ffff0000be05f204(0x40,0x3000);
  if (lVar2 == 0) {
    iVar1 = 0x100006;
  }
  else {
    iVar1 = FUN_ffff0000be091f34(s_seccfg_ffff0000be0a4fd5,lVar2,0,0x3000);
    if (iVar1 == 0) {
      FUN_ffff0000be0607b0(&DAT_ffff0000be1b7c40,lVar2,0x3c);
      FUN_ffff0000be0607b0(&DAT_ffff0000be1b7c80,lVar2 + 0x3c,0x3c);
    }
    else {
      FUN_ffff0000be05fd98
                (s___s__read_seccfg_fail__err_0x_x_ffff0000be0bb819,s_SEC_CFG_ffff0000be0c6d63,iVar1
                );
    }
    thunk_FUN_ffff0000be09937c(lVar2);
  }
  return iVar1;
}


