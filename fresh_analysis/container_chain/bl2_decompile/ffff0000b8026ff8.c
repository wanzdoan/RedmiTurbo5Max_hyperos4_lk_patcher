
int FUN_ffff0000b8026ff8(undefined8 param_1,uint param_2)

{
  int iVar1;
  char *pcVar2;
  long lVar3;
  
  if (param_2 < 0x10) {
    if (*(long *)(&DAT_ffff0000b811ce98 + (ulong)param_2 * 8) == 0) {
      lVar3 = FUN_ffff0000b80263f0(param_1,param_2);
      *(long *)(&DAT_ffff0000b811ce98 + (ulong)param_2 * 8) = lVar3;
      if (lVar3 == 0) {
        return -8;
      }
      iVar1 = FUN_ffff0000b8014d0c
                        (s_mt6316_shutdown_callback_ffff0000b808fcd7,&LAB_ffff0000b80270ac,lVar3);
      if (iVar1 != 0) {
        FUN_ffff0000b804c33c(s_slave_id__d_register_shutdown_ca_ffff0000b8085491,param_2,iVar1);
        return iVar1;
      }
      return 0;
    }
    pcVar2 = s_duplicated_slave_id__d_ffff0000b8091996;
  }
  else {
    pcVar2 = s_invalid_slave_id__d_ffff0000b809e716;
  }
  FUN_ffff0000b804c33c(pcVar2,param_2);
  return -8;
}


