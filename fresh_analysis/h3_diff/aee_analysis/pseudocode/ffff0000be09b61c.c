
uint FUN_ffff0000be09b61c(void)

{
  uint uVar1;
  uint *puVar2;
  
  FUN_ffff0000be05fd98(s__s_IN_ffff0000be0acfc9,s_is_auth_token_used_ffff0000be0c3a05);
  puVar2 = (uint *)FUN_ffff0000be05f1bc(0x101);
  if (puVar2 == (uint *)0x0) {
    FUN_ffff0000be05fd98(s_Failed_to_malloc_auth_token_buff_ffff0000be0ad1d7);
    uVar1 = 0xffffffff;
  }
  else {
    FUN_ffff0000be060ae0(puVar2,0,0x101);
    FUN_ffff0000be05fd98(s__s_IN_ffff0000be0acfc9,s_get_auth_token_header_ffff0000be0b4dce);
    uVar1 = FUN_ffff0000be0a254c(0x1c,puVar2,0x100);
    if (uVar1 == 0) {
      uVar1 = -(*puVar2 >> 1 & 1) & 0xff000001;
      FUN_ffff0000be05fd98(s__s_OUT_ffff0000be0c1274,s_is_auth_token_used_ffff0000be0c3a05);
    }
    else {
      FUN_ffff0000be05fd98(s_Read_auth_token_Failed__ffff0000be0bf09c);
      FUN_ffff0000be05fd98(s_get_auth_token_header_failed_ffff0000be0c5d43);
    }
    thunk_FUN_ffff0000be09937c(puVar2);
  }
  return uVar1;
}


