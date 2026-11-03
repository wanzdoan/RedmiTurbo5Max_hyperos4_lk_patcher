
undefined8 FUN_ffff000090789664(long param_1,long *param_2,byte *param_3,uint param_4)

{
  byte bVar1;
  char *pcVar2;
  long *plVar3;
  byte *pbVar4;
  
  if (((param_1 == 0) || (param_2 == (long *)0x0)) || (param_3 == (byte *)0x0)) {
    pcVar2 = s_wrong_args_in_find_token_value_ffff0000907d034b;
  }
  else {
    plVar3 = (long *)(ulong)(param_4 & 0xff);
    if ((param_4 & 0xff) - 4 < 0xfffffffd) {
      FUN_ffff00009077e11c(s_wrong_token_type___d_ffff0000907f8ec7,plVar3);
      return 0xffffffff;
    }
    pbVar4 = (byte *)(param_1 + 4);
    do {
      if ((uint)*pbVar4 == (param_4 & 0xff)) {
        FUN_ffff00009077e11c(s__unlock_token_type__d_found___le_ffff0000907ffe39,plVar3);
        bVar1 = pbVar4[1];
        *param_2 = (long)(pbVar4 + 2);
        *param_3 = bVar1;
        return 0;
      }
      pbVar4 = pbVar4 + (ulong)pbVar4[1] + 2;
    } while ((ulong)((long)pbVar4 - (param_1 + 4)) < (ulong)*(byte *)(param_1 + 3));
    pcVar2 = s__unlock_token_no_token_found__ffff0000907d1f75;
    param_2 = plVar3;
  }
  FUN_ffff00009077e11c(pcVar2,param_2);
  return 0xffffffff;
}


