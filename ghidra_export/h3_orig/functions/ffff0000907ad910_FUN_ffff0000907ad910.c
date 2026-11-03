// ffff0000907ad910  size=144  FUN_ffff0000907ad910

int FUN_ffff0000907ad910(void)

{
  int iVar1;
  char *pcVar2;
  
  FUN_ffff00009077de54(s___s__sec_func_init_ffff0000907d88b2,&DAT_ffff0000907f20fe);
  iVar1 = FUN_ffff0000907ad6c0();
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000907b31b8();
    if (iVar1 != 0) {
      FUN_ffff00009077de54(s_Get_seccfg_info_fail___0x_x___ca_ffff0000907e886b,iVar1);
    }
    FUN_ffff0000907aa3b8();
    iVar1 = FUN_ffff0000907ab798();
    if (iVar1 == 0) goto LAB_ffff0000907ad988;
    pcVar2 = s___s__fail_to_init_OTP_table_ffff0000907fff19;
  }
  else {
    pcVar2 = s___s__sec_func_init____fail_to_se_ffff0000907fe460;
  }
  FUN_ffff00009077de54(pcVar2,s_SEC_INIT_ffff0000907f5826);
LAB_ffff0000907ad988:
  FUN_ffff0000907987c4();
  return iVar1;
}


