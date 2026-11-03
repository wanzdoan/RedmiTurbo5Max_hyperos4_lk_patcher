
void FUN_ffff0000be050778(undefined4 param_1)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  long local_30;
  undefined4 local_28;
  
  if (DAT_ffff0000be1261e0 == '\x01') {
    FUN_ffff0000be05fd98
              (s__s__d__blackbox_v1__no_db_encryp_ffff0000be0a75fd,
               s_set_blackbox_db_encrypt_config_ffff0000be0a97e0,0xb82);
    return;
  }
  local_28 = 0;
  local_30 = 0;
  uVar1 = FUN_ffff0000be04cff4(s_blackbox_ffff0000be0bac45,&local_30,0xa400800,0xc);
  if ((uVar1 & 1) == 0) {
    pcVar2 = s__s__d____s__get_db_encrypt_contr_ffff0000be0b035c;
    uVar3 = 0xb87;
  }
  else {
    if (local_30 != -0x718c2e0b64bd3a59) {
      FUN_ffff0000be05fd98
                (s__s__d____s__db_encrypt_control_z_ffff0000be0c351f,
                 s_set_blackbox_db_encrypt_config_ffff0000be0a97e0,0xb8b,s_BLACKBOX_ffff0000be0aa9ee
                );
      local_30 = -0x718c2e0b64bd3a59;
      local_28 = 0;
      uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,&local_30,0xa400800,0xc);
      if ((uVar1 & 1) == 0) {
        pcVar2 = s__s__d____s__write_db_encrypt_con_ffff0000be0b2543;
        uVar3 = 0xb90;
        goto LAB_ffff0000be0508c8;
      }
    }
    local_28 = param_1;
    uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,&local_30,0xa400800,0xc);
    if ((uVar1 & 1) != 0) {
      return;
    }
    pcVar2 = s__s__d____s__write_db_encrypt_con_ffff0000be0b2543;
    uVar3 = 0xb97;
  }
LAB_ffff0000be0508c8:
  FUN_ffff0000be05fd98
            (pcVar2,s_set_blackbox_db_encrypt_config_ffff0000be0a97e0,uVar3,
             s_BLACKBOX_ffff0000be0aa9ee);
  return;
}


