
long FUN_ffff0000907a4988(void)

{
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  FUN_ffff00009071ebf4(0xc200010c,0,0,0,0,0,0,0,local_30);
  if (local_30[0] == 0) {
    local_30[0] = 0;
  }
  else {
    FUN_ffff00009077e11c
              (s__s_failed__ffff0000907f7f2b,s_crypto_hw_tfa_disable_internal_ffff000090803424);
  }
  return local_30[0];
}


