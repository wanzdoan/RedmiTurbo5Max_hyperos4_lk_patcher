// ffff000090760ad4  size=408  FUN_ffff000090760ad4

long FUN_ffff000090760ad4(int *param_1)

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
  FUN_ffff000090781ca0(&local_38,&local_14);
  uVar3 = local_38;
  if ((local_38 == 0) || (local_14 == 0)) {
    FUN_ffff00009077e11c
              (s__s__d__failed_to_get_pstore_mem_ffff0000907f3490,s_Read_Last_Pmsg_ffff0000907f6eba,
               0x16c);
    lVar4 = 0;
  }
  else {
    uVar1 = local_14 + 0xfffU & 0xfffff000;
    iVar2 = FUN_ffff00009077ba64
                      (DAT_ffff000090839928,s_lpmsg_ffff0000907f1b22,uVar1,&local_40,0xc,
                       local_38 & 0xfffff000,0,0);
    if (iVar2 == 0) {
      FUN_ffff00009077e11c
                (s__s__d__lpmsg__sz_misc_pstore___0_ffff0000907de894,
                 s_Read_Last_Pmsg_ffff0000907f6eba,0x179,(uint)uVar3 & 0xfffff000,uVar1);
      lVar4 = FUN_ffff000090760710(local_40,uVar1,param_1,1);
      if ((lVar4 == 0) || (*param_1 == 0)) {
        FUN_ffff00009077e11c
                  (s__s__d__No_valid_data_was_found__ffff0000907e3668,
                   s_Read_Last_Pmsg_ffff0000907f6eba,0x183);
      }
      else {
        FUN_ffff00009077e11c
                  (s__s__d__valid_data_was_found__ffff0000907e51ea,s_Read_Last_Pmsg_ffff0000907f6eba
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
      FUN_ffff00009077be30(DAT_ffff000090839928,local_40);
    }
    else {
      FUN_ffff00009077e11c
                (s__s__d___d__map_error_for_log_sto_ffff0000907cff78,
                 s_Read_Last_Pmsg_ffff0000907f6eba,0x175,iVar2);
      lVar4 = 0;
    }
  }
  return lVar4;
}


