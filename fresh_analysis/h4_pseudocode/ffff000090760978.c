
long FUN_ffff000090760978(int *param_1)

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
  FUN_ffff000090781ca0(&local_38,&local_14);
  uVar2 = local_38;
  if ((local_38 == 0) || (local_14 == 0)) {
    FUN_ffff00009077e11c
              (s__s__d__failed_to_get_pstore_mem_ffff0000907f3490,s_Read_Last_Kmsg_ffff0000908013a4,
               0x145);
    lVar6 = 0;
  }
  else {
    uVar1 = local_14 + 0xfffU & 0xfffff000;
    iVar3 = FUN_ffff00009077ba64
                      (PTR_DAT_ffff000090839928,s_lkmsg_ffff0000907f3f3e,uVar1,&local_40,0xc,
                       local_38 & 0xfffff000,0,0);
    if (iVar3 == 0) {
      FUN_ffff00009077e11c
                (s__s__d__lkmsg__sz_misc_pstore___0_ffff0000907f8b1c,
                 s_Read_Last_Kmsg_ffff0000908013a4,0x152,(uint)uVar2 & 0xfffff000,uVar1);
      lVar6 = FUN_ffff000090760710(local_40,uVar1,param_1,0);
      if ((lVar6 == 0) || (*param_1 == 0)) {
        pcVar4 = s__s__d__No_valid_data_was_found__ffff0000907e3668;
        uVar5 = 0x158;
      }
      else {
        pcVar4 = s__s__d__valid_data_was_found__ffff0000907e51ea;
        uVar5 = 0x156;
      }
      FUN_ffff00009077e11c(pcVar4,s_Read_Last_Kmsg_ffff0000908013a4,uVar5);
      FUN_ffff00009077be30(PTR_DAT_ffff000090839928,local_40);
    }
    else {
      FUN_ffff00009077e11c
                (s__s__d___d__map_error_for_log_sto_ffff0000907cff78,
                 s_Read_Last_Kmsg_ffff0000908013a4,0x14e,iVar3);
      lVar6 = 0;
    }
  }
  return lVar6;
}


