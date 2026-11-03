// ffff000090740bd4  size=180  FUN_ffff000090740bd4

int FUN_ffff000090740bd4(undefined8 param_1,uint param_2)

{
  int iVar1;
  char *pcVar2;
  long lVar3;
  
  if (param_2 < 0x10) {
    if (*(long *)((ulong)param_2 * 8 + -0xffff6f790cc8) == 0) {
      lVar3 = FUN_ffff000090740378(param_1,param_2);
      *(long *)((ulong)param_2 * 8 + -0xffff6f790cc8) = lVar3;
      if (lVar3 == 0) {
        return -8;
      }
      iVar1 = FUN_ffff0000907224ec
                        (s_mt6316_shutdown_callback_ffff0000907e2f03,FUN_ffff000090740c88,lVar3);
      if (iVar1 != 0) {
        FUN_ffff00009077de54(s_slave_id__d_register_shutdown_ca_ffff0000907cf995,param_2,iVar1);
        return iVar1;
      }
      return 0;
    }
    pcVar2 = s_duplicated_slave_id__d_ffff0000907e6360;
  }
  else {
    pcVar2 = s_invalid_slave_id__d_ffff0000907fd5a4;
  }
  FUN_ffff00009077de54(pcVar2,param_2);
  return -8;
}


