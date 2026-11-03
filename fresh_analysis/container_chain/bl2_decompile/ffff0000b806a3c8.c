
int FUN_ffff0000b806a3c8(void)

{
  int iVar1;
  char *pcVar2;
  
  FUN_ffff0000b804c33c
            (s___s__sec_func_init_lib_version____ffff0000b80a1849,&DAT_ffff0000b80987e6,
             s_202520730_ffff0000b809a46b);
  iVar1 = FUN_ffff0000b806a178();
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000b806fe64();
    if (iVar1 != 0) {
      FUN_ffff0000b804c33c(s_Get_seccfg_info_fail___0x_x___ca_ffff0000b8092edb,iVar1);
    }
    FUN_ffff0000b8067cac();
    iVar1 = FUN_ffff0000b8069050();
    if (iVar1 == 0) goto LAB_ffff0000b806a448;
    pcVar2 = s___s__fail_to_init_OTP_table_ffff0000b809fefd;
  }
  else {
    pcVar2 = s___s__sec_func_init____fail_to_se_ffff0000b809f045;
  }
  FUN_ffff0000b804c33c(pcVar2,s_SEC_INIT_ffff0000b809a475);
LAB_ffff0000b806a448:
  FUN_ffff0000b805afa4();
  return iVar1;
}


