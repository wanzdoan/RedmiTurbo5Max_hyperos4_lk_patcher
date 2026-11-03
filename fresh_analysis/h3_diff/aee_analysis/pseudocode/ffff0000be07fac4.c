
long FUN_ffff0000be07fac4(undefined8 param_1,undefined8 param_2)

{
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  FUN_ffff0000be01d644(0xc200010e,param_1,param_2,0,0,0,0,0,local_30);
  if (local_30[0] == 0) {
    local_30[0] = 0;
  }
  else {
    FUN_ffff0000be05fd98
              (s__s_failed__ffff0000be0c0196,s_sha256_tfa_process_internal_ffff0000be0bef1f);
  }
  return local_30[0];
}


