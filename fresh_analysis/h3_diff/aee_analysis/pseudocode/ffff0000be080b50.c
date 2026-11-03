
long FUN_ffff0000be080b50(void)

{
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  FUN_ffff0000be01d644(0xc2000111,0,0,0,0,0,0,0,local_30);
  if (local_30[0] == 0) {
    local_30[0] = 0;
  }
  else {
    FUN_ffff0000be05fd98(s__s_failed__ffff0000be0c0196,s_sha384_tfa_init_internal_ffff0000be0a6592);
  }
  return local_30[0];
}


