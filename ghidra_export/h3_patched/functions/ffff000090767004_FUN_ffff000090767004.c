// ffff000090767004  size=44  FUN_ffff000090767004

void FUN_ffff000090767004(undefined4 param_1)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  long local_30;
  undefined4 local_28;
  
  if (DAT_ffff00009087bf14 == '\x01') {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1__no_db_encryp_ffff0000907d5007,
               s_set_blackbox_db_encrypt_config_ffff0000907d82fb,0xb82);
    return;
  }
  local_28 = 0;
  local_30 = 0;
  uVar1 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c04,&local_30,0xa400800,0xc);
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__d____s__get_db_encrypt_contr_ffff0000907e1d09;
    uVar3 = 0xb87;
  }
  else {
    if (local_30 != -0x718c2e0b64bd3a59) {
      FUN_ffff00009077de54
                (s__s__d____s__db_encrypt_control_z_ffff0000907fded1,
                 s_set_blackbox_db_encrypt_config_ffff0000907d82fb,0xb8b,s_BLACKBOX_ffff0000907d9988
                );
      local_30 = -0x718c2e0b64bd3a59;
      local_28 = 0;
      uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c04,&local_30,0xa400800,0xc);
      if ((uVar1 & 1) == 0) {
        pcVar2 = s__s__d____s__write_db_encrypt_con_ffff0000907e5210;
        uVar3 = 0xb90;
        goto LAB_ffff000090767154;
      }
    }
    local_28 = param_1;
    uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c04,&local_30,0xa400800,0xc);
    if ((uVar1 & 1) != 0) {
      return;
    }
    pcVar2 = s__s__d____s__write_db_encrypt_con_ffff0000907e5210;
    uVar3 = 0xb97;
  }
LAB_ffff000090767154:
  FUN_ffff00009077de54
            (pcVar2,s_set_blackbox_db_encrypt_config_ffff0000907d82fb,uVar3,
             s_BLACKBOX_ffff0000907d9988);
  return;
}


