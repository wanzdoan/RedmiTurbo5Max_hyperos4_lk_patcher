
int FUN_ffff000090740e6c(undefined8 param_1,uint param_2)

{
  int iVar1;
  char *pcVar2;
  long lVar3;
  
  if (param_2 < 0x10) {
    if (*(long *)(&DAT_ffff00009086f3d0 + (ulong)param_2 * 8) == 0) {
      lVar3 = FUN_ffff000090740610(param_1,param_2);
      *(long *)(&DAT_ffff00009086f3d0 + (ulong)param_2 * 8) = lVar3;
      if (lVar3 == 0) {
        return -8;
      }
      iVar1 = FUN_ffff000090722730
                        (s_mt6316_shutdown_callback_ffff0000907e2feb,&LAB_ffff000090740f20,lVar3);
      if (iVar1 != 0) {
        FUN_ffff00009077e11c(s_slave_id__d_register_shutdown_ca_ffff0000907cf9b4,param_2,iVar1);
        return iVar1;
      }
      return 0;
    }
    pcVar2 = s_duplicated_slave_id__d_ffff0000907e648b;
  }
  else {
    pcVar2 = s_invalid_slave_id__d_ffff0000907fd74c;
  }
  FUN_ffff00009077e11c(pcVar2,param_2);
  return -8;
}


