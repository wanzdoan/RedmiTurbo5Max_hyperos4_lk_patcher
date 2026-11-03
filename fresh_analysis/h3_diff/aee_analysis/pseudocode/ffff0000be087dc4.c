
int FUN_ffff0000be087dc4(void)

{
  int iVar1;
  char *pcVar2;
  
  FUN_ffff0000be05fd98
            (s___s__sec_func_init_lib_version____ffff0000be0c6cd7,&DAT_ffff0000be0bb811,
             s_202520730_ffff0000be0bdcf4);
  iVar1 = FUN_ffff0000be087b74();
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000be08da1c();
    if (iVar1 != 0) {
      FUN_ffff0000be05fd98(s_Get_seccfg_info_fail___0x_x___ca_ffff0000be0b4c0e,iVar1);
    }
    FUN_ffff0000be08566c();
    iVar1 = FUN_ffff0000be086a4c();
    if (iVar1 == 0) goto LAB_ffff0000be087e44;
    pcVar2 = s___s__fail_to_init_OTP_table_ffff0000be0c4a8a;
  }
  else {
    pcVar2 = s___s__sec_func_init____fail_to_se_ffff0000be0c38c5;
  }
  FUN_ffff0000be05fd98(pcVar2,s_SEC_INIT_ffff0000be0bdcfe);
LAB_ffff0000be087e44:
  FUN_ffff0000be075194();
  return iVar1;
}


