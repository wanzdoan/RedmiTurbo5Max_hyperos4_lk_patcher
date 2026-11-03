
int FUN_ffff000090788414(char param_1,int param_2)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  ulong uVar4;
  char *pcVar5;
  undefined4 uVar6;
  
  if (((param_2 == 2) || (param_1 == '\x04')) || (param_1 == '\x01')) {
    FUN_ffff00009077e11c(s__SEC__Bypass_Write_Protect__ffff0000907ffe61);
    return 0;
  }
  lVar2 = FUN_ffff00009071f790();
  iVar1 = FUN_ffff00009073527c();
  uVar6 = 1;
  if (iVar1 == 2) {
    uVar6 = 2;
  }
  FUN_ffff000090746154(1);
  iVar1 = FUN_ffff0000907889f4();
  if (iVar1 != 0) {
    pcVar3 = s___s__SIMLOCK_Data_Not_sync_0x_x__ffff0000907f0716;
LAB_ffff000090788514:
    FUN_ffff00009077e11c(pcVar3,s_set_write_protect_ffff0000907d8686,iVar1);
    return iVar1;
  }
  iVar1 = FUN_ffff000090794c6c(&DAT_ffff0000907ffe7e);
  if (iVar1 == 1) {
    iVar1 = FUN_ffff000090735638(&DAT_ffff0000907ffe7e,&DAT_ffff0000907ffe7e,0);
    FUN_ffff00009077e11c
              (s___s__Lock_otp_partition_____ffff0000907e55ab,s_set_write_protect_ffff0000907d8686);
    if (iVar1 != 0) {
      pcVar3 = s___s___Lock_otp_failed__d_ffff0000907f8edd;
      goto LAB_ffff000090788514;
    }
  }
  iVar1 = FUN_ffff000090735638(s_preloader_ffff0000907d7473,s_preloader_ffff0000907d7473,uVar6);
  if (iVar1 != 0) {
    pcVar3 = s___s__Lock_preloader_partition_fa_ffff0000907f73b4;
    goto LAB_ffff000090788514;
  }
  uVar4 = FUN_ffff00009071fb70();
  if ((uVar4 & 1) == 0) {
    lVar2 = FUN_ffff00009077c028(s_system_ffff0000907edad2);
    pcVar3 = s_super_ffff0000907e5b8c;
    if (lVar2 != 0) {
      pcVar3 = s_system_ffff0000907edad2;
    }
    FUN_ffff00009077e11c
              (s___s___Lock__s_>_s_ffff0000907dd470,s_set_write_protect_ffff0000907d8686,
               &DAT_ffff0000907e6dc8,pcVar3);
    iVar1 = FUN_ffff000090735638(&DAT_ffff0000907e6dc8,pcVar3,uVar6);
    if (iVar1 != 0) {
      pcVar5 = &DAT_ffff0000907e6dc8;
      goto LAB_ffff0000907886b0;
    }
  }
  else {
    if (*(char *)(lVar2 + 1) == 'a') {
      pcVar5 = s_modem_a_ffff000090803069;
      pcVar3 = s_dram_para_ffff0000907ddc72;
    }
    else {
      lVar2 = FUN_ffff00009077c028(s_system_b_ffff0000907e8735);
      pcVar5 = &DAT_ffff0000907e6dc8;
      pcVar3 = s_tee_b_ffff0000907ffe82;
      if (lVar2 != 0) {
        pcVar3 = s_system_b_ffff0000907e8735;
      }
    }
    FUN_ffff00009077e11c
              (s___s___Lock__s_>_s_ffff0000907dd470,s_set_write_protect_ffff0000907d8686,pcVar5,
               pcVar3);
    iVar1 = FUN_ffff000090735638(pcVar5,pcVar3,uVar6);
    if (iVar1 != 0) goto LAB_ffff0000907886b0;
  }
  iVar1 = FUN_ffff0000907889e4();
  pcVar5 = s_protect2_ffff0000907da588;
  if (iVar1 != 0) {
    pcVar5 = s_seccfg_ffff0000907d1c70;
  }
  FUN_ffff00009077e11c
            (s___s__Lock__s_>_s_ffff0000907fa982,s_set_write_protect_ffff0000907d8686,pcVar5,
             s_seccfg_ffff0000907d1c70);
  iVar1 = FUN_ffff000090735638(pcVar5,s_seccfg_ffff0000907d1c70,uVar6);
  if (iVar1 == 0) {
    return 0;
  }
  pcVar3 = s_seccfg_ffff0000907d1c70;
LAB_ffff0000907886b0:
  FUN_ffff00009077e11c
            (s___s___Lock__s_>_s_failed__d_ffff0000907d1f94,s_set_write_protect_ffff0000907d8686,
             pcVar5,pcVar3,iVar1);
  return iVar1;
}


