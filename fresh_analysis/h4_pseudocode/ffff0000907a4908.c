
long FUN_ffff0000907a4908(undefined8 param_1,undefined8 param_2)

{
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  FUN_ffff00009071ebf4(0xc200010b,param_1,param_2,0,0,0,0,0,local_30);
  if (local_30[0] == 0) {
    local_30[0] = 0;
  }
  else {
    FUN_ffff00009077e11c
              (s__s_failed__ffff0000907f7f2b,s_crypto_hw_tfa_init_internal_ffff0000907e57f7);
  }
  return local_30[0];
}


