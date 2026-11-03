
long FUN_ffff0000b8063210(undefined8 param_1,undefined8 param_2)

{
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  FUN_ffff0000b800e6ac(0xc2000112,param_1,param_2,0,0,0,0,0,local_30);
  if (local_30[0] == 0) {
    local_30[0] = 0;
  }
  else {
    FUN_ffff0000b804c33c
              (s__s_failed__ffff0000b809bfd6,s_sha384_tfa_process_internal_ffff0000b8094fa5);
  }
  return local_30[0];
}


