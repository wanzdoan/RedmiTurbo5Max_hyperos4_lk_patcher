// ffff00009076083c  size=408  FUN_ffff00009076083c

long FUN_ffff00009076083c(int *param_1)

{
  uint uVar1;
  int iVar2;
  ulong uVar3;
  long lVar4;
  undefined8 local_40;
  ulong local_38;
  int local_14;
  
  local_14 = 0;
  local_40 = 0;
  local_38 = 0;
  FUN_ffff0000907819d8(&local_38,&local_14);
  uVar3 = local_38;
  if ((local_38 == 0) || (local_14 == 0)) {
    FUN_ffff00009077de54
              (s__s__d__failed_to_get_pstore_mem_ffff0000907f32ed,s_Read_Last_Pmsg_ffff0000907f6d19,
               0x16c);
    lVar4 = 0;
  }
  else {
    uVar1 = local_14 + 0xfffU & 0xfffff000;
    iVar2 = FUN_ffff00009077b79c
                      (DAT_ffff000090839908,s_lpmsg_ffff0000907f199a,uVar1,&local_40,0xc,
                       local_38 & 0xfffff000,0,0);
    if (iVar2 == 0) {
      FUN_ffff00009077de54
                (s__s__d__lpmsg__sz_misc_pstore___0_ffff0000907de7da,
                 s_Read_Last_Pmsg_ffff0000907f6d19,0x179,(uint)uVar3 & 0xfffff000,uVar1);
      lVar4 = FUN_ffff000090760478(local_40,uVar1,param_1,1);
      if ((lVar4 == 0) || (*param_1 == 0)) {
        FUN_ffff00009077de54
                  (s__s__d__No_valid_data_was_found__ffff0000907e3580,
                   s_Read_Last_Pmsg_ffff0000907f6d19,0x183);
      }
      else {
        FUN_ffff00009077de54
                  (s__s__d__valid_data_was_found__ffff0000907e50cf,s_Read_Last_Pmsg_ffff0000907f6d19
                   ,0x17d);
        iVar2 = *param_1;
        if (iVar2 != 1) {
          uVar3 = 0;
          do {
            if (*(char *)(lVar4 + uVar3) == '\0') {
              *(undefined1 *)(lVar4 + uVar3) = 10;
              iVar2 = *param_1;
            }
            uVar3 = uVar3 + 1;
          } while (uVar3 < iVar2 - 1);
        }
      }
      FUN_ffff00009077bb68(DAT_ffff000090839908,local_40);
    }
    else {
      FUN_ffff00009077de54
                (s__s__d___d__map_error_for_log_sto_ffff0000907cff59,
                 s_Read_Last_Pmsg_ffff0000907f6d19,0x175,iVar2);
      lVar4 = 0;
    }
  }
  return lVar4;
}


