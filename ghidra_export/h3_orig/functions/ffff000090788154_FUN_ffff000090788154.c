// ffff000090788154  size=680  FUN_ffff000090788154

int FUN_ffff000090788154(char param_1,int param_2)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  ulong uVar4;
  char *pcVar5;
  undefined4 uVar6;
  
  if (((param_2 == 2) || (param_1 == '\x04')) || (param_1 == '\x01')) {
    FUN_ffff00009077de54(s__SEC__Bypass_Write_Protect__ffff0000907ffcb9);
    return 0;
  }
  lVar2 = FUN_ffff00009071f608();
  iVar1 = FUN_ffff000090734fe4();
  uVar6 = 1;
  if (iVar1 == 2) {
    uVar6 = 2;
  }
  FUN_ffff000090745ebc(1);
  iVar1 = FUN_ffff000090788734();
  if (iVar1 != 0) {
    pcVar3 = s___s__SIMLOCK_Data_Not_sync_0x_x__ffff0000907f05a5;
LAB_ffff000090788254:
    FUN_ffff00009077de54(pcVar3,s_set_write_protect_ffff0000907d85f0,iVar1);
    return iVar1;
  }
  iVar1 = FUN_ffff0000907949ac(&DAT_ffff0000907ffcd6);
  if (iVar1 == 1) {
    iVar1 = FUN_ffff0000907353a0(&DAT_ffff0000907ffcd6,&DAT_ffff0000907ffcd6,0);
    FUN_ffff00009077de54
              (s___s__Lock_otp_partition_____ffff0000907e54a7,s_set_write_protect_ffff0000907d85f0);
    if (iVar1 != 0) {
      pcVar3 = s___s___Lock_otp_failed__d_ffff0000907f8d41;
      goto LAB_ffff000090788254;
    }
  }
  iVar1 = FUN_ffff0000907353a0(s_preloader_ffff0000907d73dd,s_preloader_ffff0000907d73dd,uVar6);
  if (iVar1 != 0) {
    pcVar3 = s___s__Lock_preloader_partition_fa_ffff0000907f722a;
    goto LAB_ffff000090788254;
  }
  uVar4 = FUN_ffff00009071f9e8();
  if ((uVar4 & 1) == 0) {
    lVar2 = FUN_ffff00009077bd60(s_system_ffff0000907ed961);
    pcVar3 = s_super_ffff0000907e5a78;
    if (lVar2 != 0) {
      pcVar3 = s_system_ffff0000907ed961;
    }
    FUN_ffff00009077de54
              (s___s___Lock__s_>_s_ffff0000907dd3cd,s_set_write_protect_ffff0000907d85f0,
               &DAT_ffff0000907e6c84,pcVar3);
    iVar1 = FUN_ffff0000907353a0(&DAT_ffff0000907e6c84,pcVar3,uVar6);
    if (iVar1 != 0) {
      pcVar5 = &DAT_ffff0000907e6c84;
      goto LAB_ffff0000907883f0;
    }
  }
  else {
    if (*(char *)(lVar2 + 1) == 'a') {
      pcVar5 = s_modem_a_ffff000090802e65;
      pcVar3 = s_dram_para_ffff0000907ddbcf;
    }
    else {
      lVar2 = FUN_ffff00009077bd60(s_system_b_ffff0000907e85f1);
      pcVar5 = &DAT_ffff0000907e6c84;
      pcVar3 = s_tee_b_ffff0000907ffcda;
      if (lVar2 != 0) {
        pcVar3 = s_system_b_ffff0000907e85f1;
      }
    }
    FUN_ffff00009077de54
              (s___s___Lock__s_>_s_ffff0000907dd3cd,s_set_write_protect_ffff0000907d85f0,pcVar5,
               pcVar3);
    iVar1 = FUN_ffff0000907353a0(pcVar5,pcVar3,uVar6);
    if (iVar1 != 0) goto LAB_ffff0000907883f0;
  }
  iVar1 = FUN_ffff000090788724();
  pcVar5 = s_protect2_ffff0000907da4f1;
  if (iVar1 != 0) {
    pcVar5 = s_seccfg_ffff0000907d1c33;
  }
  FUN_ffff00009077de54
            (s___s__Lock__s_>_s_ffff0000907fa7e6,s_set_write_protect_ffff0000907d85f0,pcVar5,
             s_seccfg_ffff0000907d1c33);
  iVar1 = FUN_ffff0000907353a0(pcVar5,s_seccfg_ffff0000907d1c33,uVar6);
  if (iVar1 == 0) {
    return 0;
  }
  pcVar3 = s_seccfg_ffff0000907d1c33;
LAB_ffff0000907883f0:
  FUN_ffff00009077de54
            (s___s___Lock__s_>_s_failed__d_ffff0000907d1f57,s_set_write_protect_ffff0000907d85f0,
             pcVar5,pcVar3,iVar1);
  return iVar1;
}


