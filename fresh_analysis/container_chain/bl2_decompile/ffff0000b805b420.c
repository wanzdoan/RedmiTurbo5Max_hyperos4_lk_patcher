
long FUN_ffff0000b805b420(undefined4 param_1)

{
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  FUN_ffff0000b800e6ac(0xc2000133,param_1,0,0,0,0,0,0,local_30);
  if (local_30[0] == 0) {
    local_30[0] = 0;
  }
  else {
    FUN_ffff0000b804c33c
              (s__s_failed__ld___ffff0000b808bfef,s_aes256_cbc_dec_fw_tfa_internal_ffff0000b809efd0)
    ;
  }
  return local_30[0];
}


