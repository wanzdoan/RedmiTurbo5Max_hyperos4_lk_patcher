// ffff0000907ce008  size=216  FUN_ffff0000907ce008

uint FUN_ffff0000907ce008(void)

{
  uint uVar1;
  uint *puVar2;
  
  FUN_ffff00009077de54(s__s_IN_ffff0000907dd338,s_is_auth_token_used_ffff0000907fe6ce);
  puVar2 = (uint *)FUN_ffff00009077d24c(0x101);
  if (puVar2 == (uint *)0x0) {
    FUN_ffff00009077de54(s_Failed_to_malloc_auth_token_buff_ffff0000907dd79e);
    uVar1 = 0xffffffff;
  }
  else {
    FUN_ffff00009077eb88(puVar2,0,0x101);
    FUN_ffff00009077de54(s__s_IN_ffff0000907dd338,s_get_auth_token_header_ffff0000907e8a1a);
    uVar1 = FUN_ffff0000907cd228(0x1c,puVar2,0x100);
    if (uVar1 == 0) {
      uVar1 = -(*puVar2 >> 1 & 1) & 0xff000001;
      FUN_ffff00009077de54(s__s_OUT_ffff0000907fa7b2,s_is_auth_token_used_ffff0000907fe6ce);
    }
    else {
      FUN_ffff00009077de54(s_Read_auth_token_Failed__ffff0000907f779d);
      FUN_ffff00009077de54(s_get_auth_token_header_failed_ffff000090801b1e);
    }
    thunk_FUN_ffff0000907c2ad8(puVar2);
  }
  return uVar1;
}


