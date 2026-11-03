// ffff0000907adbd0  size=152  FUN_ffff0000907adbd0

int FUN_ffff0000907adbd0(void)

{
  int iVar1;
  char *pcVar2;
  
  FUN_ffff00009077e11c
            (s___s__sec_func_init_lib_version____ffff0000908034ce,&DAT_ffff0000907f228a,
             s_202520730_ffff0000907f5993);
  iVar1 = FUN_ffff0000907ad980();
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000907b3828();
    if (iVar1 != 0) {
      FUN_ffff00009077e11c(s_Get_seccfg_info_fail___0x_x___ca_ffff0000907e89af,iVar1);
    }
    FUN_ffff0000907aa678();
    iVar1 = FUN_ffff0000907aba58();
    if (iVar1 == 0) goto LAB_ffff0000907adc50;
    pcVar2 = s___s__fail_to_init_OTP_table_ffff0000908000c1;
  }
  else {
    pcVar2 = s___s__sec_func_init____fail_to_se_ffff0000907fe608;
  }
  FUN_ffff00009077e11c(pcVar2,s_SEC_INIT_ffff0000907f599d);
LAB_ffff0000907adc50:
  FUN_ffff000090798a84();
  return iVar1;
}


