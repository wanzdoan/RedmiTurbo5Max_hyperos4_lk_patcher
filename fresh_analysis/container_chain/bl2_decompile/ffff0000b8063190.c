
long FUN_ffff0000b8063190(void)

{
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  FUN_ffff0000b800e6ac(0xc2000111,0,0,0,0,0,0,0,local_30);
  if (local_30[0] == 0) {
    local_30[0] = 0;
  }
  else {
    FUN_ffff0000b804c33c(s__s_failed__ffff0000b809bfd6,s_sha384_tfa_init_internal_ffff0000b8087b33);
  }
  return local_30[0];
}


