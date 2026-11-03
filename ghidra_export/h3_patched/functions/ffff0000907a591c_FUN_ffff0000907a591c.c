// ffff0000907a591c  size=128  FUN_ffff0000907a591c

long FUN_ffff0000907a591c(undefined8 param_1,undefined8 param_2)

{
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  FUN_ffff00009071ea6c(0xc2000112,param_1,param_2,0,0,0,0,0,local_30);
  if (local_30[0] == 0) {
    local_30[0] = 0;
  }
  else {
    FUN_ffff00009077de54
              (s__s_failed__ffff0000907f7d8f,s_sha384_tfa_process_internal_ffff0000907ebad6);
  }
  return local_30[0];
}


