// ffff000090798c40  size=128  FUN_ffff000090798c40

long FUN_ffff000090798c40(undefined4 param_1)

{
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  FUN_ffff00009071ea6c(0xc2000133,param_1,0,0,0,0,0,0,local_30);
  if (local_30[0] == 0) {
    local_30[0] = 0;
  }
  else {
    FUN_ffff00009077de54
              (s__s_failed__ld___ffff0000907dbaa9,s_aes256_cbc_dec_fw_tfa_internal_ffff0000907fe39c)
    ;
  }
  return local_30[0];
}


