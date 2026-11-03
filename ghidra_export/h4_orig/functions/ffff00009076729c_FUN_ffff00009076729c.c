// ffff00009076729c  size=44  FUN_ffff00009076729c

void FUN_ffff00009076729c(undefined4 param_1)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  long local_30;
  undefined4 local_28;
  
  if (DAT_ffff00009087bfac == '\x01') {
    FUN_ffff00009077e11c
              (s__s__d__blackbox_v1__no_db_encryp_ffff0000907d5044,
               s_set_blackbox_db_encrypt_config_ffff0000907d8391,0xb82);
    return;
  }
  local_28 = 0;
  local_30 = 0;
  uVar1 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&local_30,0xa400800,0xc);
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__d____s__get_db_encrypt_contr_ffff0000907e1df1;
    uVar3 = 0xb87;
  }
  else {
    if (local_30 != -0x718c2e0b64bd3a59) {
      FUN_ffff00009077e11c
                (s__s__d____s__db_encrypt_control_z_ffff0000907fe079,
                 s_set_blackbox_db_encrypt_config_ffff0000907d8391,0xb8b,s_BLACKBOX_ffff0000907d9a1f
                );
      local_30 = -0x718c2e0b64bd3a59;
      local_28 = 0;
      uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&local_30,0xa400800,0xc);
      if ((uVar1 & 1) == 0) {
        pcVar2 = s__s__d____s__write_db_encrypt_con_ffff0000907e532b;
        uVar3 = 0xb90;
        goto LAB_ffff0000907673ec;
      }
    }
    local_28 = param_1;
    uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&local_30,0xa400800,0xc);
    if ((uVar1 & 1) != 0) {
      return;
    }
    pcVar2 = s__s__d____s__write_db_encrypt_con_ffff0000907e532b;
    uVar3 = 0xb97;
  }
LAB_ffff0000907673ec:
  FUN_ffff00009077e11c
            (pcVar2,s_set_blackbox_db_encrypt_config_ffff0000907d8391,uVar3,
             s_BLACKBOX_ffff0000907d9a1f);
  return;
}


