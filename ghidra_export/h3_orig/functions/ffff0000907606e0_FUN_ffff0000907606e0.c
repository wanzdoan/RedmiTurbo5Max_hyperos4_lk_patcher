// ffff0000907606e0  size=348  FUN_ffff0000907606e0

long FUN_ffff0000907606e0(int *param_1)

{
  uint uVar1;
  ulong uVar2;
  int iVar3;
  char *pcVar4;
  undefined8 uVar5;
  long lVar6;
  undefined8 local_40;
  ulong local_38;
  int local_14;
  
  local_14 = 0;
  local_40 = 0;
  local_38 = 0;
  FUN_ffff0000907819d8(&local_38,&local_14);
  uVar2 = local_38;
  if ((local_38 == 0) || (local_14 == 0)) {
    FUN_ffff00009077de54
              (s__s__d__failed_to_get_pstore_mem_ffff0000907f3304,s_Read_Last_Kmsg_ffff0000908011c8,
               0x145);
    lVar6 = 0;
  }
  else {
    uVar1 = local_14 + 0xfffU & 0xfffff000;
    iVar3 = FUN_ffff00009077b79c
                      (DAT_ffff000090839908,s_lkmsg_ffff0000907f3db2,uVar1,&local_40,0xc,
                       local_38 & 0xfffff000,0,0);
    if (iVar3 == 0) {
      FUN_ffff00009077de54
                (s__s__d__lkmsg__sz_misc_pstore___0_ffff0000907f8980,
                 s_Read_Last_Kmsg_ffff0000908011c8,0x152,(uint)uVar2 & 0xfffff000,uVar1);
      lVar6 = FUN_ffff000090760478(local_40,uVar1,param_1,0);
      if ((lVar6 == 0) || (*param_1 == 0)) {
        pcVar4 = s__s__d__No_valid_data_was_found__ffff0000907e3597;
        uVar5 = 0x158;
      }
      else {
        pcVar4 = s__s__d__valid_data_was_found__ffff0000907e50e6;
        uVar5 = 0x156;
      }
      FUN_ffff00009077de54(pcVar4,s_Read_Last_Kmsg_ffff0000908011c8,uVar5);
      FUN_ffff00009077bb68(DAT_ffff000090839908,local_40);
    }
    else {
      FUN_ffff00009077de54
                (s__s__d___d__map_error_for_log_sto_ffff0000907cff59,
                 s_Read_Last_Kmsg_ffff0000908011c8,0x14e,iVar3);
      lVar6 = 0;
    }
  }
  return lVar6;
}


