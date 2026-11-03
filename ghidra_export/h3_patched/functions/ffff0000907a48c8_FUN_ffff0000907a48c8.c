// ffff0000907a48c8  size=128  FUN_ffff0000907a48c8

long FUN_ffff0000907a48c8(undefined8 param_1,undefined8 param_2)

{
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  FUN_ffff00009071ea6c(0xc200010d,3,param_1,param_2,0,0,0,0,local_30);
  if (local_30[0] == 0) {
    local_30[0] = 0;
  }
  else {
    FUN_ffff00009077de54(s__s_failed__ffff0000907f7d8f,s_sha256_tfa_internal_ffff0000907f08a1);
  }
  return local_30[0];
}


