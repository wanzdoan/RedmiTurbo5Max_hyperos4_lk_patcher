// ffff0000907ce67c  size=216  FUN_ffff0000907ce67c

uint FUN_ffff0000907ce67c(void)

{
  uint uVar1;
  uint *puVar2;
  
  FUN_ffff00009077e11c(s__s_IN_ffff0000907dd40e,s_is_auth_token_used_ffff0000907fe876);
  puVar2 = (uint *)FUN_ffff00009077d514(0x101);
  if (puVar2 == (uint *)0x0) {
    FUN_ffff00009077e11c(s_Failed_to_malloc_auth_token_buff_ffff0000907dd841);
    uVar1 = 0xffffffff;
  }
  else {
    FUN_ffff00009077ee50(puVar2,0,0x101);
    FUN_ffff00009077e11c(s__s_IN_ffff0000907dd40e,s_get_auth_token_header_ffff0000907e8b5e);
    uVar1 = FUN_ffff0000907cd89c(0x1c,puVar2,0x100);
    if (uVar1 == 0) {
      uVar1 = -(*puVar2 >> 1 & 1) & 0xff000001;
      FUN_ffff00009077e11c(s__s_OUT_ffff0000907fa94e,s_is_auth_token_used_ffff0000907fe876);
    }
    else {
      FUN_ffff00009077e11c(s_Read_auth_token_Failed__ffff0000907f7927);
      FUN_ffff00009077e11c(s_get_auth_token_header_failed_ffff000090801cfa);
    }
    thunk_FUN_ffff0000907c314c(puVar2);
  }
  return uVar1;
}


