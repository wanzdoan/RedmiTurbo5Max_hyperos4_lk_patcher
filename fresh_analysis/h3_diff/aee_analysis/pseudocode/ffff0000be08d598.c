
ulong FUN_ffff0000be08d598(void)

{
  ulong uVar1;
  long lVar2;
  
  FUN_ffff0000be05fd98
            (s___s___s_ffff0000be0ad120,s_SEC_CFG_ffff0000be0c6d63,s_sec_backup_ffff0000be0a52fe);
  FUN_ffff0000be0607b0(&DAT_ffff0000be1b7c80,&DAT_ffff0000be1b7c40,0x3c);
  uVar1 = FUN_ffff0000be091ff8(s_seccfg_ffff0000be0a4fd5,&DAT_ffff0000be1b7c80,0x3c,0x3c);
  if ((int)uVar1 == 0) {
    lVar2 = FUN_ffff0000be05f250(0x3000,1);
    if (lVar2 == 0) {
      uVar1 = 0x100006;
    }
    else {
      uVar1 = FUN_ffff0000be091f34(s_seccfg_ffff0000be0a4fd5,lVar2,0x3c,0x3c);
      uVar1 = uVar1 & 0xffffffff;
      thunk_FUN_ffff0000be09937c(lVar2);
    }
  }
  return uVar1;
}


