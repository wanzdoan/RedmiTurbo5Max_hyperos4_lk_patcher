
int FUN_ffff0000be030ff0(undefined8 param_1,uint param_2)

{
  int iVar1;
  char *pcVar2;
  long lVar3;
  
  if (param_2 < 0x10) {
    if (*(long *)(&DAT_ffff0000be119688 + (ulong)param_2 * 8) == 0) {
      lVar3 = FUN_ffff0000be0303e8(param_1,param_2);
      *(long *)(&DAT_ffff0000be119688 + (ulong)param_2 * 8) = lVar3;
      if (lVar3 == 0) {
        return -8;
      }
      iVar1 = FUN_ffff0000be0203a4
                        (s_mt6316_shutdown_callback_ffff0000be0b0d77,&LAB_ffff0000be0310a4,lVar3);
      if (iVar1 != 0) {
        FUN_ffff0000be05fd98(s_slave_id__d_register_shutdown_ca_ffff0000be0a35c2,param_2,iVar1);
        return iVar1;
      }
      return 0;
    }
    pcVar2 = s_duplicated_slave_id__d_ffff0000be0b305a;
  }
  else {
    pcVar2 = s_invalid_slave_id__d_ffff0000be0c2e7b;
  }
  FUN_ffff0000be05fd98(pcVar2,param_2);
  return -8;
}


